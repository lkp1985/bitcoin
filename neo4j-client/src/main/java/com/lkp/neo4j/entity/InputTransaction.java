package com.lkp.neo4j.entity;

import lombok.Data;

@Data
public class InputTransaction{
	private String txid; 
	private int index;
}