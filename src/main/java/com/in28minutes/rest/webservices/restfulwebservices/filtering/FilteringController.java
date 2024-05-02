package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {
	
	@GetMapping("/filtering")
	public MappingJacksonValue filtering() {
		SomeBean someBean = new SomeBean("value1", "value2", "value3", "value4");
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
		createFilter(mappingJacksonValue, "field1", "field3");
		
		return mappingJacksonValue;
	}
	
	@GetMapping("/filtering-list")
	public MappingJacksonValue filteringList() {
		List<SomeBean> someBeans = Arrays.asList(new SomeBean("value1", "value2", "value3", "value4"), new SomeBean("value5", "value6", "value7", "value8"));
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBeans);
		createFilter(mappingJacksonValue, "field2", "field3");
		
		return mappingJacksonValue;
	}
	
	private void createFilter(MappingJacksonValue mappingJacksonValue, String value1, String value2) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(value1, value2);
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
		mappingJacksonValue.setFilters(filters);
	}

}
