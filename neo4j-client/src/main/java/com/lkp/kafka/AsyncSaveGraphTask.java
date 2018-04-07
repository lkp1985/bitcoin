package com.lkp.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lkp.btcdcli4j.client.BlockChainApi;
import com.lkp.btcdcli4j.util.TransactionUtil;
import com.lkp.neo4j.client.GraphClient;
import com.lkp.neo4j.entity.BlockHeight;
import com.lkp.neo4j.entity.BlockWithHeight;
import com.lkp.neo4j.entity.MyBlock;
import com.lkp.neo4j.entity.TransactionEntity;
import com.lkp.neo4j.entity.TxRelation;
import com.lkp.schedule.FetchBlock;
import com.lkp.util.BlockQueue;
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
	
	//@Autowired
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
	
	
	/**
	 * 从myblock中取出数据并解析成TransactionEntity重新存储到mdb
	 * @param height
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Async  
    public void saveBlockTx(String height) throws InterruptedException, ExecutionException{  
		BlockQueue.heightSet.add(Integer.parseInt(height));
		
		Query query = new Query();
	     Criteria criteria = Criteria.where("height").is(Integer.parseInt(height));
	     query.addCriteria(criteria);
	     long start = System.currentTimeMillis();
	     MyBlock block = mongoTemplate.findOne(query, MyBlock.class);
	     if(block!=null){
	    	 try{
	    		 List<TransactionEntity> entityList = BlockUtil.blockTransfer(block);
		    	// List<TxRelation> inTxRelationList = new ArrayList<TxRelation>();
		    	 
		    	 
			     for(TransactionEntity entity : entityList){
			    	 graphClient.saveTransactionInfo(mongoTemplate,entity);
			    	 //inTxRelationList.addAll(TransactionUtil.getInTxRelations(entity));
			     }
			    // mongoTemplate.insert(inTxRelationList, TxRelation.class);
				 logger.info("block="+block.getHeight()+" save "+block.getBaseTxList().size()+" success,spend:"+(System.currentTimeMillis()-start));
				 BlockQueue.heightSet.remove(Integer.parseInt(height));
				 BlockHeight blockHeight = new BlockHeight();
				 blockHeight.setHeight(Integer.parseInt(height));
				 mongoTemplate.save(blockHeight);
	    	 }catch(Exception e){
	    		 BlockQueue.heightSet.remove(Integer.parseInt(height));
	    	 }
	    	 
	     }else{
	    	 System.out.println("block :"+height+" is null");
	     }
	    
		
	}
	/**
	 * 将mdb的myblock数据解析交易关系后保存到图数据库：arangodb或neo4j或其他图数据库
	 * @param height
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Async  
    public void saveTxToGraph(String height) throws InterruptedException, ExecutionException{  
		
	}
	
	/**
	 * 直接从区块链网上下载数据，并转成MyBlock对象
	 * @param blockhash
	 * @param height
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Async  
    public void saveMyBlock(String blockhash,int height) throws InterruptedException, ExecutionException{  
		org.bitcoinj.core.Block block = fetchBlock.getBlock(blockhash);
		MyBlock myBlock = BlockUtil.blockTransfer(block);
		myBlock.setHeight(height);
		mongoTemplate.save(myBlock);
		
	}
	
	@Async  
    public void saveBlockWithHeight(String blockhash) throws InterruptedException, ExecutionException{ 
		while(true){
			com.neemre.btcdcli4j.core.domain.Block block = blockApi.getBlock(blockhash);
			BlockWithHeight blockWithHeight = new BlockWithHeight();
			blockWithHeight.setBlockhash(blockhash);
			blockWithHeight.setHeight(block.getHeight());
			blockWithHeight.setNextBlockHash(block.getNextBlockHash());
			blockWithHeight.setPreBlockHash(block.getPreviousBlockHash());
			mongoTemplate.save(blockWithHeight);
			blockhash = block.getNextBlockHash();
			logger.info("save blockwithheight "+block.getHeight()+" success");
		}
		
		
	}
	
	/**
	 * 直接从区块链网上下载数据，并转成txRelation对象
	 * @param blockhash
	 * @param height
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Async  
    public void saveBlockTransaction(String blockhash,int height)  {  
		try{
			BlockQueue.heightSet.add(height);
			org.bitcoinj.core.Block block = fetchBlock.getBlock(blockhash);
			logger.info("fetch block:"+blockhash+" success"); 
			 
			
			MyBlock myBlock = BlockUtil.blockTransfer(block);
		//	List<TransactionEntity> entityList = BlockUtil.blockTransfer(myBlock);
//		     for(TransactionEntity entity : entityList){
//		    	 graphClient.saveTransactionInfo(mongoTemplate,entity);
//		     }
		     List<TransactionEntity> entityList = BlockUtil.blockTransfer(myBlock);
	    	 List<TxRelation> inTxRelationList = new ArrayList<TxRelation>();
	    	 
	    	 
		     for(TransactionEntity entity : entityList){
		    	// graphClient.saveTransactionInfo(mongoTemplate,entity);
		    	 inTxRelationList.addAll(TransactionUtil.getInTxRelations(entity));
		     }
		     mongoTemplate.insert(inTxRelationList, TxRelation.class);
		     logger.info("block "+height+" ,total insert inTxRelationList:"+inTxRelationList.size()+" success");
		     BlockHeight blockHeight = new BlockHeight();
			 blockHeight.setHeight(height);
			 
			 mongoTemplate.save(blockHeight);
			 BlockQueue.heightSet.remove(height);
		}catch(Exception e){
			e.printStackTrace();
			BlockQueue.heightSet.remove(height);
		}
		
		//myBlock.setHeight(height);
		//mongoTemplate.save(myBlock);
		
	}
	
	
	@Async  
    public void saveBlockTransaction(String blockhash) throws InterruptedException{  
    	logger.info("begin save block:"+blockhash+" transaction relation");
    	long start = System.currentTimeMillis();
    	Block block = blockApi.getBlock(blockhash);
    	 
    	List<String> txList = block.getTx();
    	if(!txList.isEmpty()){
    		String txid = txList.get(0);
    		try{
    			TransactionEntity txEntity = blockApi.getTx(txid);
    			String address = txEntity.getOutTxRelationList().get(0).getAddress();
    			String money = txEntity.getOutTxRelationList().get(0).getMoney();
    			String index = txEntity.getOutTxRelationList().get(0).getInTx();
    			String outTx = txEntity.getOutTxRelationList().get(0).getOutTx();
    			
    			TxRelation relation = new TxRelation();
    			relation.setAddress(address);
    			relation.setMoney(money);
    			relation.setOutTx(outTx);
    			//relation.set
    			System.out.println(txEntity.getOutTxRelationList().get(0).getAddress());
        		txEntity.setHeight(block.getHeight());
        		graphClient.saveTransactionInfo(mongoTemplate,txEntity);
    		}catch(Exception e){
    			logger.error("save tx "+ txid+" error:"+e.getMessage(),e);
    		}
    	}
//    	logger.info("block "+ blockhash+" total have transaction "+txList.size());
//    	for(String txid : txList){
//    		try{
//    			TransactionEntity txEntity = blockApi.getTx(txid);
//    			System.out.println(txEntity.getOutTxRelationList().get(0).getAddress());
//        		txEntity.setHeight(block.getHeight());
//        		graphClient.saveTransactionInfo(mongoTemplate,txEntity);
//    		}catch(Exception e){
//    			logger.error("save tx "+ txid+" error:"+e.getMessage(),e);
//    		}
//    		
//    	}
    	logger.info("save block :"+blockhash+" success,total have transaction "+
    				txList.size()+", spend:"+(System.currentTimeMillis() - start));
    	//logger.info(" save block "+blockhash+" transaction relation success , total "+txList.size() +" transaction");
    }  
}  