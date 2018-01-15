package com.lkp.neo4j.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;
/**
 * 
 * @author Administrator
 *
 */
@Data
public class Transaction {
	@Id
	String txid;
	String blockhash;
	String height;
}
