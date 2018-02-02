package com.lkp.neo4j.entity;

import lombok.Data;

/**
 * 与钱包地址有关的话题网站，主要从btc.com网站上抓取，一般有重点话题的该网站都会有超链接
 * @author Administrator
 *
 */
@Data
public class AddressBBS {
	private String address;
	private String url;
	 
}
