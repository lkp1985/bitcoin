package com.lkp.neo4j.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;
/**
 * 
 * @author Administrator
 *
 */
@Data
public class BaseTransaction {
	@Id
	String txid; 
	List<OutputTransaction> outputList;
	List<InputTransaction> inputList;
}

