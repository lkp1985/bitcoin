package com.lkp.neo4j.entity;

import lombok.Data;

@Data
public class OutTx {
	String address;
	String txId;
	String money;
	String type="rollout";
}
