package com.lkp.neo4j.entity;

import java.util.List;

import lombok.Data;

/**
 * 区块链交易对象
 * @author Administrator
 *
 */
@Data
public class TransactionEntity { 
	List<TxRelation> inTxRelationList;
	List<TxRelation> outTxRelationList;
	String txid; 
	int height;//交易所在的块的高度
	String blockhash;//交易所在块的hash
}
