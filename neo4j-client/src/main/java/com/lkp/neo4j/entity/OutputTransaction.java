package com.lkp.neo4j.entity;

import lombok.Data;

@Data
public class OutputTransaction{
	private String outaddress;
	private String money;
	private int index;
}

