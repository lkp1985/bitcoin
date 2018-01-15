package com.lkp.neo4j.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * 区别于 bitcoinj 的block
 * @author Administrator
 *
 */
@Data
public class MyBlock {
	@Id
	private String blockhash;
	private int height;
	private String preblockhash;
	private List<BaseTransaction> baseTxList;
}
