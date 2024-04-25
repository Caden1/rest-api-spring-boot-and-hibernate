package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDaoService {
	
	private static List<User> users = new ArrayList<User>();
	
	static {
		users.add(new User(1, "John", LocalDate.now().minusYears(30)));
		users.add(new User(2, "Jane", LocalDate.now().minusYears(25)));
		users.add(new User(3, "Caden", LocalDate.now().minusYears(20)));
	}
	
	public List<User> findAll() {
		return users;
	}

}
