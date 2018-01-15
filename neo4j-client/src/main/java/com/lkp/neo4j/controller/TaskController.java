package com.lkp.neo4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lkp.neo4j.entity.Task;
import com.lkp.neo4j.respository.TaskRepository;

@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(value = "/task", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    public Task saveTask(@RequestBody Task taskInfo) {
        Task task = taskRepository.save(taskInfo);
        return task;
    }


    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Task create(@PathVariable String name) { 
    	Task task = taskRepository.findByTaskName(name); 
    	return task;
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() { 
    	return "test";
    }
}
 