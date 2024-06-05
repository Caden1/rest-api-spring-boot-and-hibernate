This repository contains my practice and notes from section 8 of the Udemy course [Master Spring Boot 3 & Spring Framework 6 with Java](https://www.udemy.com/course/spring-boot-and-spring-framework-tutorial-for-beginners), created by Ranga Karanam, the founder of in28minutes.

# Section 8: Creating a Java REST API with Spring Boot, Spring Framework and Hibernate

Creating a REST API with Spring Boot - An Overview:
* 1. We will build 3 simple Hello World REST APIs
    * Understand @RestController, @RequestMapping, @PathVariable, Java Bean to JSON Conversion
* 2. Build a REST API for a Social Media Application
    * Design and Build a Great REST API
        * Choose right URI for your resources (/users, /users/{id}, /users/{id}/posts)
        * Choose right request method for actions (GET, POST, PUT, DELETE, …)
        * Designing the right Request and Response Structures
        * Implementing Security, Validation, and Exception Handling
    * Build Advanced REST API Features
        * Internationalization, HATEOAS, Versioning, Documentation, Content Negotiation, …
* 3. Connect your REST API to a Database
    * JPA and Hibernate
    * Will use H2 and MySQL

Initializing a REST API Project with Spring Boot:
* Using Spring Initializr to create project
* Group: com.in28minutes.rest.webservices
* Artifact: restful-web-services
* Dependencies:
    * Spring Web
    * Spring Data JPA
    * H2 Database
    * Spring Boot DevTools

Creating a Hello World REST API with Spring Boot:
* “@RestController” annotation makes the controller a rest controller
* “@RequestMapping” annotation allows you to specify the Request Method and Path
* “@GetMapping” annotation can be used instead of “@RequestMapping” with “method=RequestMethod.GET”

Enhancing the Hello World REST API to return a Bean:
* We created a class (or Bean) “HelloWorldBean” that gets auto wired using constructor injection
* Our new controller method creates an instance of “HelloWorldBean” and returns it
* Going to this path “/hello-world-bean” in a web browser returns the bean as a JSON response:
{"message":"Hello World"}

What's happening in the background? Spring Boot Starters & Autoconfig:
* 1. How are requests being handled?
    * Dispatcher Servlet - Front Controller Pattern
        * The first thing all requests go to is the Dispatcher Servlet. This is because it’s mapped to the root URL of ‘/’
            * Mapping servlets: dispatherServlet urls=[ / ]
                * You should see the above in your logs if you put them on debug level
        * When a request goes to the Dispatcher Servlet, the Dispatcher Servlet then looks at the methods in your Rest Controller to see the path to execute next
        * The Dispatcher Servlet is configured by a Spring Boot feature called Auto Configuration (DispatcherServletAutoConfiguration)
            * Based on classes available in the class path, Spring Boot automatically detects what we are building (web app, rest api, …) and auto configures a Dispatcher Servlet 
            * If you search for “DispatcherServletAutoConfiguration” in the Debug level logs, you can see the bean being created
* 2. How does HelloWorldBean Object get converted to JSON?
    * @ResponseBody and JacksonHttpMessageConverters
        * The annotation we used “@RestController” includes the annotation “@ResponseBody”
        * The default conversion auto setup by Spring Boot Configuration uses JacksonHttpMessageConverters, which is also part of Auto Configuration (JacksonHttpMessageConvertersConfiguration)
            * If you search for “JacksonHttpMessageConvertersConfiguration” in the Debug level logs, you can see the bean being created
* 3. Who is configuring error mapping:
    * Auto Configuration (ErrorMvcAutoConfiguration)
* 4. How are all the jars available (Spring, Spring MVC, Jackson, Tomcat)?
    * Starter Projects - Spring Boot Starter Web (spring-webmvc, spring-web, spring-boot-starter-tomcat, spring-boot-starter-json)

Enhancing the Hello World REST API with a Path Variable:
* Example of Path Parameter (or Path Variable):
/users/{id}/todos/{id}

* First id might be for the user, second id might be for the specific todo you want. Example:
/users/2/todos/100

* Path variables should be in curly braces. Example:
@GetMapping(path="/hello-world/path-variable/{name}")

* To capture the path variable, you use the annotation “@PathVariable” as a parameter in the method using it. Example:
public HelloWorldBean helloWorldPathVariable(@PathVariable String name)

* The variable name in the method parameter needs to match the variable name in the path:
@GetMapping(path="/hello-world/path-variable/{name}")
public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
	return new HelloWorldBean(String.format("Hello World, %s", name));
}

* Visiting “http://localhost:8080/hello-world/path-variable/caden” produces:
{"message":"Hello World, caden"}

Designing the REST API for Social Media Application:
* Social Media App Key Resources:
    * Users
    * Posts
* Key Details:
    * User: id, name, birthDate
    * Post: id, description
* Request Methods for REST API
    * GET - Retrieve details of a resource
    * POST - Create a new resource
    * PUT - Update an existing resource
    * PATCH - Update part of a resource (example: only updating the birthDate of a user)
    * DELETE - Delete a resource

* Users REST API:
    * Retrieve all Users
        * GET /users
    * Create a User
        * POST /users
    * Retrieve one User
        * GET /users/{id}
    * Delete a User
        * DELETE /users/{id}
    * Posts REST API (The Users Posts to the Social Media App):
        * Retrieve all posts for a User
            * GET /users/{id}/posts
        * Create a post for a User
            * POST /users/{id}/posts
        * Retrieve details of a post
            * GET /users/{id}/posts/{post_id}

* He recommends using Plurals for resources
    * Example, use /users instead of /user
        * When getting all users, it doesn’t make sense to use /user

Creating User Bean and UserDaoService:
* Data Access Object (DAO)

Implementing GET Methods for User Resource:
* Added ability to retrieve one user based on its “id” using a path variable

Implementing POST Method to create User Resource:
* Typically when creating a user, the “id” creation should be handled by Spring
* @RequestBody allows you to pass in a body with your request
* You can’t execute POST requests directly from the browser
    * Using Talend API Tester chrome plugin

Enhancing POST Method to return correct HTTP Status Code and Location:
* Important Response Statuses:
    * 200 - Success
    * 201 - Created
    * 204 - No Content
    * 401 - Unauthorized
    * 400 - Bad Request
    * 404 - Resource Not Found
    * 500 - Server Error
* Using Spring Framework class “ResponseEntity”
* Using ServletUriComponentsBuilder to respond with URI Location
    * This adds a “location” to the response headers. Example:
POST http://localhost:8080/users
Body:
{
    "name": "Andrew",
    "birthDate": "2004-04-25"
}

Response:
201
Headers:
Location: http://localhost:8080/users/4
Content-Length: 0
Date: Thu, 25 Apr 2024 17:58:49 GMT
Keep-Alive: timeout=60
Connection: keep-alive

Implementing Exception Handling - 404 Resource Not Found:
* Creating UserNotFoundException class that extends java.lang.RuntimeException
* Changed “return users.stream().filter(predicate).findFirst().get();” to “return users.stream().filter(predicate).findFirst().orElse(null);” so it will return null if the user is not found. This allows us to handle the null case in the “UserResource” class and throw the exception.
* When a resource is not found we want to throw a 404. Do this with “@ResponseStatus(code = HttpStatus.NOT_FOUND)”
    * This still shows the complete stack trace due to using Spring Boot DevTools. We don’t want this in production.
    * Java JAR files are what’s run in Production, and when you run a Java JAR file Spring Boot DevTools is disabled, so this is not an issue

Implementing Generic Exception Handling for all Resources:
* The ResponseEntityExceptionHandler class is provided by Spring
    * This class Handles all Spring MVC raised exceptions by returning a ResponseEntity with formatted error details in the body
* By default the ResponseEntityExceptionHandler classes “handleException” method handles all exceptions
* By extending ResponseEntityExceptionHandler you can replace the “handleException” methods functionality by creating your own method with the “@ExceptionHandler” annotation.
    * A good way to do this is by going to the ResponseEntityExceptionHandler class and copying the method signature for the “handleException” class and pasting it into the class that extends ResponseEntityExceptionHandler. Change the name of the method to something else, like “handleAllExceptions”. You then add the “@ExceptionHandler(Exception.class)” annotation above the method. “Exception.class” in the parameter means it will perform this functionality for all exceptions.
    * REMEMBER to add @ControllerAdvice above the class; it’s similar to @Component but for classes that declare “@ExceptionHandler”
* You can customize the response for specific exceptions by specifying the exception in the “@ExceptionHandler()” parameter. Example:
    * @ExceptionHandler(UserNotFoundException.class)

Implementing DELETE Method to delete a User Resource:
* Used “@DeleteMapping” annotation

Implementing Validations for REST API:
* What happens now if we create a user with a blank name and a birthdate in the future?
    * It’s still created successfully, we need to fix this.
* Add “spring-boot-starter-validation” dependency
* Use @Valid annotation before the variable in the methods parameter. The validations that are defined on your object are automatically invoked. From package “jakarta.validation.Valid”
* Then add constraints, such as @Size and @Past, to the fields of the Bean that needs validation
* We’re using the “@Override” annotation on the “handleHandlerMethodValidationException” method, but I’m not sure why

Understanding Open API Specification and Swagger:
* Consumers of your REST API need to understand:
    * Resources
    * Actions
    * Request/Response Structure (Constraints/Validations)
* You can generate documentation form your code
* Swagger introduced in 2011
* Open API introduced in 2016 and is based on the Swagger specification
* Swagger UI:
    * Visualize and interact with your REST API
    * Can be used by Swagger and Open API

Configuring Auto Generation of Swagger Documentation:
* “springdoc-openapi” java library helps to automate the generation of API documentation for spring boot projects
* SpringDoc OpenAPI
* GitHub page - SpringDoc OpenAPI
* After adding the following dependency and restarting the server, go to http://localhost:8080/swagger-ui.html to see the specification
<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
	<version>2.3.0</version>
</dependency>

* On the swagger page, clicking on “/v3/api-docs” opens the JSON file for the API. This shows all your api paths, but also shows the openapi version, server info, components, etc.
    * The components section shows things like the User object and its properties (fields) and what validations they have on them. Example:
"components": {
    "schemas": {
        "User": {
            "type": "object",
            "properties": {
                "id": {
                    "type": "integer",
                    "format": "int32"
                  },
                "name": {
                    "maxLength": 2147483647,
                    "minLength": 2,
                    "type": "string"
                  },
                "birthDate": {
                    "type": "string",
                    "format": "date"
                  }
              }
          }
    }
}

Exploring Content Negotiation - Implementing Support for XML:
* Visits to the same resource might have different response format, or language, needs. Example:
    * Going to localhost:8080/users returns a JSON in English, but the consumer might need XML in Dutch
* Through Content Negotiation, the consumer can tell the REST API provider what they want
    * Examples:
        * Accept header (MIME types - application/xml, application/json, ..)
        * Accept-Language header (en, nl, fr, ..)
    * This is done through request headers
* First add the dependency:
<dependency>
	<groupId>com.fasterxml.jackson.dataformat</groupId>
	<artifactId>jackson-dataformat-xml</artifactId>
</dependency>

* Restart the server
* Now, you can send a GET Request to “http://localhost:8080/users” and add the header key: “Accept” value: “application/xml”. The response will be in XML
* For JSON, add the header key: “Accept” value: “application/json”

Exploring Internationalization for REST API:
* Internationalization abbreviation is i18n
* Allows consumer to choose language
* Use the “Accept-Language” Header with values for the language
* Example languages:
    * en - English
    * nl - Dutch
    * fr - French
* To allow this to work:
    * In the same folder as “appplication.properties” create a file called “messages.properties”
    * In the controller add a field that uses the “MessageSource” spring class and create a constructor for it for constructor injection
    * Create a “messages” file for every language that defines what’s returned:
        * Whatever is in “messages.properties” will be the default
        * messages_nl.properties
            * good.morning.message=Goedemorgen
        * messages_fr.properties
            * good.morning.message=Bonjour
        * messages_de.properties
            * good.morning.message=Guten Morgen
        * messages_en.properties
            * good.morning.message=Good Morning
    * GET Request to “http://localhost:8080/hello-world-internationalized” with header key: “Accept-Language” value: “nl” returns “Goedemorgen”

Versioning REST API - URI Versioning:
* Let’s say you have a “person” endpoint that returns:
{
	“name”: “Bob Charlie”
}

* You want to change this to return:
{
	“name”: {
		“firstName”: “Bob”
		“lastName”: “Charlie”
	}
}

* The problem is you can’t just make the change and break all your consumers code that are expecting the first one
* This is why we use versioning
* The first one is Version 1 (v1), and the second one is Version 2 (v2)
* Options for versioning:
    * Add “v1” or “v2” to the URL
        * localhost:8080/v1/person
        * localhost:8080/v2/person
    * Request Parameter
    * Header
    * Media Type

Versioning REST API - Request Param, Header and Content Negotiation:
* Request Parameter:
    * Examples:
        * localhost:8080/person?version=1
        * localhost:8080/person?version=2
    * You can specify a specific request parameter in the @GetMapping annotation using the “params” keyword:
        * @GetMapping(path="/person", params="version=1")
            * In this, the method will only be called if “version=1” appears as a request parameter
* (Custom) Headers Versioning:
    * Examples:
        * SAME-URL headers=[X-API-VERSION=1]
        * SAME-URL headers=[X-API-VERSION=2]
    * You can specify a specific header in the @GetMapping annotation using the “headers” keyword:
        * @GetMapping(path="/person/header", headers="X-API-VERSION=1")
            * In this, the method will only be called if the request header has a key of “X-API-VERSION” and a value of “1”
* Media type versioning (a.k.a. “content negotiation” or “accept header”):
    * Examples:
        * SAME-URL produces=application/vnd.company.app-v1+json
        * SAME-URL produces=application/vnd.company.app-v2+json
    * You can specify a specific accept header in the @GetMapping annotation using the “produces” keyword:
        * @GetMapping(path="/person/accept", produces="application/vnd.company.app-v1+json")
            * In this, the method will only be called if the request header has a key of “Accept” and a value of “application/vnd.company.app-v1+json”
* Which one is best to use?
    * It doesn’t really matter too much as long as you stay consistent
    * It seems like URI versioning would be the overall easiest to use

Implementing HATEOAS for REST API:
* Hypermedia as the Engine of Application State (HATEOAS)
* HATEOAS allows you to enhance your REST API to tell consumers how to perform subsequent actions
* An example is the “_links” section below:
{
	“name”: “Adam”,
	“_links”: {
		“all-users”: {
			“href”: “http://localhost:8080/users”
		}
	}
}

* Implement using HAL (JSON Hypertext Application Language)
* HAL is a simple format that gives a consistent and easy way to hyperlink between resources in your API
    * The HAL standard is the key “_links” with links to resources below
* Make use of Spring HATEOAS to implement it
* Add “spring-boot-starter-hateoas” dependency
* To use it, the method needs to return an EntityModel<T>. Example:
public EntityModel<User> retrieveUser(@PathVariable int id)

* Essentially we’re wrapping the user class in an EntityModel
* Use the WebMvcLinkBuilder class to add links to the EntityModel
* Example of adding a link to the method “retrieveAllUsers()” in the same class this code is in:
EntityModel<User> entityModel = EntityModel.of(user);
WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
entityModel.add(link.withRel("all-users"));

Implementing Static Filtering for REST API:
* Customizing REST API Responses
* Serialization: Process of converting an object to a stream (example: JSON)
    * This happens with REST API responses; they get converted to JSON
* To change the name of the field returned in a JSON response you can use the “@JSONProperty” annotation
* You can filter out specific fields you don’t want returned. 2 types:
    * Static
        * Filter for all requests (@JsonIgnoreProperties, @JsonIgnore)
    * Dynamic
        * Filter only for specific requests (@JsonFilter with FilterProvider)
* Unlike @JsonIgnore which is above a field, @JsonIgnoreProperties is above the class name and allows you to specify the fields you want ignored
* I prefer @JsonIgnore because it seems more clear what’s being filtered out and if the field name changes, you only have to change it once

Implementing Dynamic Filtering for REST API:
* For “/filtering” return:
    * field1 and field3
* For “/filtering-list” return:
    * field2 and field3 in the first set
    * field5 and field6 in the second set
* Unlike static filtering, which happens in the Bean, Dynamic filtering happens in the Controller
* The “MappingJacksonValue”, “SimpleBeanPropertyFilter”, “FilterProvider”, and “SimpleFilterProvider” classes allow you to define how to do the filtering by passing serialization instructions (write the filtering logic)
* Remember to add the “@JsonFilter()” annotation to the bean

Monitoring APIs with Spring Boot Actuator:
* Spring Boot Actuator: Provides Spring Boot’s production-ready features
    * Monitor and manage your app in production
* Spring Boot Starter Actuator: Starter to add Spring Boot Actuator to your app
* Provides some endpoint:
    * Beans - Complete list of Spring beans in your app
    * Health - App health info
    * Metrics - App metrics
    * Mappings - Details about Request Mappings
    * And a lot more …
* Add “spring-boot-starter-actuator” to POM
* Then go to “http://localhost:8080/actuator”
    * By default this only exposes the health endpoint
* To expose more, go the “application.properties” and add:
management.endpoints.web.exposure.include=*

* The above exposes all the endpoints
* For metrics “http://localhost:8080/actuator/metrics” it pulls up a list of strings that need to be appended to the end of the URL to see its metrics. Example:
    * http://localhost:8080/actuator/metrics/http.server.requests

Exploring APIs with Spring Boot HAL Explorer:
* JSON Hypertext Application Language (HAL)
    * We discussed HAL in the “Implementing HATEOAS for REST API” section above
* HAL Explorer
    * An API explorer for RESTful Hypermedia APIs using HAL
    * This enables non-technical teams to play with APIs
* Spring Boot HAL Explorer
    * Auto-configures it for Spring Boot projects
* Add to POM file:
<dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-rest-hal-explorer</artifactId>
</dependency>

* Notice the above has groupId “org.springframework.data” not “org.springframework.boot”
* Simply going to “http://localhost:8080” will bring up HAL Explorer
    * This is similar to using API Tester or PostMan, but any HAL links being returned are easier to use by simply clicking a button
    * Can type in endpoints, such as “/actuator” to bring up links to mess around with
    * Provides buttons for GET, POST, etc

Additional Setting For Next Step - H2-CONSOLE:
* Add this to application.properties:
    * spring.h2.console.enabled=true

Creating User Entity and some test data:
* Creating an entity named User from the user class causes the error:
    * Syntax error in SQL statement "create table [*]user (birth_date date, id integer not null, name varchar(255), primary key (id))"; expected "identifier"; SQL statement: create table user (birth_date date, id integer not null, name varchar(255), primary key (id))
* This is because “user” is a keyword in H2
* Rename the table to “user_details” like this:
@Entity(name="user_details")
public class User {}

* Create “data.sql” and add the needed properties
* Go to http://localhost:8080/h2-console
* JDBC URL: jdbc:h2:mem:testdb

Enhancing REST API to connect to H2 using JPA and Hibernate:
* We got the error “No default constructor for entity 'com.in28minutes.rest.webservices.restfulwebservices.user.User”
* In order to use the following interface we created:
public interface UserRepository extends JpaRepository<User, Integer> {
}

* We also need the User class to have a default constructor; create an empty constructor in User class:
protected User() { }

* Remember, interfaces that extend JpaRepository have some built in methods you can use such as “findAll()” and “findById()”

Creating Post Entity with Many to One Relationship with User Entity:
* The @OneToMany annotation is used with the @ManyToOne annotation
    * In our example we have Many Posts to One User, and One User to Many Posts
* @ManyToOne(fetch = FetchType.LAZY)
    * LAZY: When used in the Post class, it will retrieve the Post details only
    * EAGER: When used in the Post class, it will retrieve the details of the Post and User in the same query
* These entities (tables) are created and in the the “post” table there’s a field called “user_id”; this is because the @OneToMany(mappedBy = "user") and @ManyToOne(fetch = FetchType.LAZY) annotations tells Spring that there are many posts for each user; it needs to relate the “post” table with the “user” table with the “user_id”

Implementing a GET API to retrieve all Posts of a User:
* Added @GetMapping("/jpa/users/{id}/posts")
