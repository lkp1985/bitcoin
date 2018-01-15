package com.lkp.kafka;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lkp.btcdcli4j.client.BlockChainApi;
import com.lkp.neo4j.client.GraphClient;
import com.lkp.neo4j.entity.MyBlock;
import com.lkp.neo4j.entity.TransactionEntity;
import com.lkp.schedule.FetchBlock;
import com.lkp.util.BlockUtil;
import com.neemre.btcdcli4j.core.domain.Block;
/**
 * 异步存储区块信息到图数据库
 * @author Administrator
 *
 */
@Component  
public class AsyncSaveGraphTask {  
	Log logger =  LogFactory.getLog(AsyncSaveGraphTask.class);
	@Autowired
	private GraphClient graphClient;
	
	@Autowired
	private BlockChainApi blockApi;
	
	 @Autowired
	 private MongoTemplate mongoTemplate;
	 
	 
	 
	@Autowired
    FetchBlock fetchBlock;
	@Async  
    public void test(String blockhash) throws InterruptedException{  
		logger.info("AsyncSaveGraphTask begin consume:"+blockhash);
		Thread.sleep(600000);
		logger.info("AsyncSaveGraphTask end  consume:"+blockhash);
	}
	
	@Async  
    public void saveBlock(String blockhash,int height) throws InterruptedException, ExecutionException{  
		org.bitcoinj.core.Block block = fetchBlock.getBlock(blockhash);
		
		MyBlock myBlock = BlockUtil.BlockTransfer(block);
		myBlock.setHeight(height);
		mongoTemplate.save(myBlock);
		
	}
	@Async  
    public void saveBlockTransaction(String blockhash) throws InterruptedException{  
    	logger.info("begin save block:"+blockhash+" transaction relation");
    	long start = System.currentTimeMillis();
    	Block block = blockApi.getBlock(blockhash);
    	List<String> txList = block.getTx();
    	logger.info("block "+ blockhash+" total have transaction "+txList.size());
    	for(String txid : txList){
    		try{
    			TransactionEntity txEntity = blockApi.getTx(txid);
        		txEntity.setHeight(block.getHeight());
        		graphClient.saveTransactionInfo(txEntity);
    		}catch(Exception e){
    			logger.error("save tx "+ txid+" error:"+e.getMessage(),e);
    		}
    		
    	}
    	logger.info("save block :"+blockhash+" success,total have transaction "+
    				txList.size()+", spend:"+(System.currentTimeMillis() - start));
    	//logger.info(" save block "+blockhash+" transaction relation success , total "+txList.size() +" transaction");
    }  
}  