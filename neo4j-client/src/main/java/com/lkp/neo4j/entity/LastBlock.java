package com.lkp.neo4j.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;
/**
 * produce 生产的最近一个Block,用于调度,只保留一个值，所以ID默认为1
 * @author Administrator
 *
 */
@Data
public class LastBlock {
	@Id
	String id;
	String blockhash;
	String height;
	
}
