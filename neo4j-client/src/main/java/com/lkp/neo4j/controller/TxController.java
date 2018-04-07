package com.lkp.neo4j.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lkp.btcdcli4j.client.BlockChainApi;
import com.lkp.neo4j.client.GraphClient;
import com.lkp.neo4j.entity.TransactionEntity;

/**
 * 与交易有关的接口
 * @author Administrator
 *
 */
@RestController 
public class TxController {

	@Autowired
	GraphClient  graphClient;
	
	//@Autowired
	BlockChainApi blockApi;
	/**
	 * 根据区块ID，保存该block的所有交易关系
	 * @param blockid
	 */
    @RequestMapping(value = "/txrelation/block/{blockid}", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    public void saveTxRelation(@PathVariable("blockid") String blockid) {
    	List<String> txList = blockApi.getTxList(blockid);
    	for(String txid : txList){
    		//txid="669e18d6a4e33566601ddcec0dce8a5da627c01e28dac831ab607ae151607aaf";
    		TransactionEntity txEntity = blockApi.getTx(txid);
    		graphClient.saveTxRelation(txEntity);
    	}
    }

}
 