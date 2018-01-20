package com.lkp.block.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

	@Autowired
	JdbcTemplate template;

	String GET_MOVIE_QUERY =
	  "MATCH (movie:Movie {title:{1}})" +
	  " OPTIONAL MATCH (movie)<-[r]-(person:Person)\n" +
	  " RETURN movie.title as title, collect({name: person.name, " +
	  " job: head(split(lower(type(r)),'_')), role: r.roles}) as cast LIMIT 1";

	String query = "MATCH (n) RETURN n LIMIT 1";
	@RequestMapping("/movie/{title}")
	public Map<String,Object> movie(@PathVariable("title") String title) {
	    return template.queryForMap(GET_MOVIE_QUERY, title);
	}
	
	@RequestMapping("/match")
	public Map<String,Object> movie2( ) {
		 
	    return template.queryForMap(query);
	}
}
 