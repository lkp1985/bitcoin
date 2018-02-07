package com.lkp.block.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lkp.block.util.EntityTransferUtil;

@RestController
public class TaskController {

	@Autowired
	JdbcTemplate template;

	String GET_MOVIE_QUERY =
	  "MATCH (movie:Movie {title:{1}})" +
	  " OPTIONAL MATCH (movie)<-[r]-(person:Person)\n" +
	  " RETURN movie.title as title, collect({name: person.name, " +
	  " job: head(split(lower(type(r)),'_')), role: r.roles}) as cast LIMIT 1";

	String queryForMap = "MATCH (n) return n limit 1";
	String matchN = "MATCH (n)-[m*1]-(p) where n.id='nodeId' RETURN m,n,p";
	//String queryForList = "MATCH p=()-[r:ROLLOUT]->() RETURN p LIMIT 25";
	String queryForList = "match (a1)-[a2]->(a3)-[a4]->(a5)-[a6]->(a7) where a1.id='0d1104b887bc4d7abb21e41215b476413d94701271a7faaa224fc6ddab50e854' return a1,a2,a3,a4,a5,a6,a7";
	 
	String shortpath = "MATCH p=allShortestPaths(  (bacon {id:'startId'})-[*]-(meg {id:'endId'}))RETURN p limit limitNum";
		 
	@RequestMapping("/movie/{title}")
	public Map<String,Object> movie(@PathVariable("title") String title) {
	    return template.queryForMap(GET_MOVIE_QUERY, title);
	}
	
	@RequestMapping("/map")
	public Map<String,Object> forMap( ) {
	      return template.queryForMap(queryForMap);
	}
	 
	
	@RequestMapping("/matchn")
	public  Map<String, Object>  matchN(String nodeId) {
		String tempMatchN = matchN.replace("nodeId", nodeId);
		List<Map<String, Object>> map = template.queryForList(tempMatchN);
		Map<String,Object> resultMap = EntityTransferUtil.transferNodeRelation(map,nodeId);
		System.out.println("class="+map.get(0).getClass());
	    return resultMap;
	}
	
	
	@RequestMapping("/path")
	public Map<String, Object> shortPath(String startId,String endId,String limit) {
		System.out.println("startId="+startId+",endId="+endId+",limit="+limit);
		String tempQuery = shortpath;
		tempQuery = tempQuery.replace("startId", startId);
		tempQuery = tempQuery.replace("endId", endId);
		if(limit==null || limit.equals("undefined")){
			limit = "1";
		}
		tempQuery = tempQuery.replace("limitNum", limit);
		System.out.println("shortPaht="+tempQuery);
		List<Map<String, Object>> map = template.queryForList(tempQuery);
		Map<String,Object> resultMap = EntityTransferUtil.transferShortPathRelation(map);
	    return resultMap;
	}
}
 