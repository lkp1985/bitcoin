package com.lkp.neo4j.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * 记录每个区块的高度与blockhash
 * @author Administrator
 *
 */
@Data
public class BlockWithHeight {
	@Id
	int height; ; 
	private String blockhash;
	private String nextBlockHash;
	private String preBlockHash;
}
