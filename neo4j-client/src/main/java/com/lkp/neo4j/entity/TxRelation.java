package com.lkp.neo4j.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;
/**
 * 交易关系
 * @author Administrator
 *
 */
@Data
public class TxRelation {
	@Id
	String txIndex;
	String outTx;
	String address;
	String inTx;
	String money;//转账金额
}
