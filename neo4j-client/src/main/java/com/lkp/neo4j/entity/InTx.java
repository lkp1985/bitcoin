package com.lkp.neo4j.entity;

import lombok.Data;

@Data
public class InTx {
	String address;
	String txId;
	String type="rollin";
}
