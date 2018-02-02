package com.lkp.schedule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lkp.btcdcli4j.client.BlockChainApi;
import com.lkp.neo4j.entity.BlockHeight;
import com.lkp.neo4j.entity.BlockWithHeight;
import com.lkp.neo4j.entity.LastBlock;
import com.lkp.neo4j.entity.TxRelation;
import com.lkp.neo4j.respository.BlockHeightRepository;
import com.lkp.neo4j.respository.LastBlockRepository;
import com.lkp.redis.RedisService;
import com.lkp.util.BlockQueue;
import com.neemre.btcdcli4j.core.domain.Block;

/**
 * 定时发送最近一个blockhash
 * 
 * @author lkp
 *
 */
@Component
public class TimeSendBlock {
	private static final Logger logger = LoggerFactory.getLogger(TimeSendBlock.class);

	// @Autowired
	// private BlockProducer producer;

	@Autowired
	private BlockChainApi blockApi;

//	@Autowired
//	private TxRelationRepository txRelationRepo;
//
//	@Autowired
//	private AddressRepository repo;
	
	@Autowired
	private BlockHeightRepository heightRepo;

 	@Autowired
 	private MongoTemplate mongoTemplate;
//
//	@Autowired
//	private GraphClient graphClient;

	@Autowired
	private LastBlockRepository lastBlockRepo;

	@Value("${timeBlockNum}")
	int timeBlockNum;// 每次produce block的数目

	@Value("${produce_on}")
	int produce_on;
	String lastblockhash;// 完整抓取题案开关
	LastBlock lastBlock;
	int total = 0;

	@Autowired
	RedisService redisService;

	//@Autowired
	FetchBlock fetchBlock;
	int sum = 1;

	@Value("${height}")
	int height;

	@Value("${topic}")//从mdb读取myblock解析后并重新写入txRelation的topic
	String topic;
	
	@Value("${parsetopic}")
	String parsetopic;
	
	@Value("${downtopic}")//下载block的topic
	String downtopic;
	
	@Value("${maxThread}")
	int maxThread;
	// @Scheduled(cron="0/1 * * * * ?")
	public void saveBlock() {
		if (lastblockhash == null || lastblockhash.trim().length() == 0) {
			lastBlock = lastBlockRepo.findOne("1");
			height = Integer.parseInt(lastBlock.getHeight());
			lastblockhash = lastBlock.getBlockhash();
		}
		// while(lastblockhash!=null){
		// org.bitcoinj.core.Block block = null;
		// try {
		// block = fetchBlock.getBlock(lastblockhash);
		// lastblockhash = block.getPrevBlockHash().toString();
		// MyBlock myBlock = BlockUtil.BlockTransfer(block);
		// myBlock.setHeight(height--);
		// mongoTemplate.save(myBlock);
		// System.out.println("total save block " + sum++);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// logger.error("block height :"+height +" save error");
		// e.printStackTrace();
		// }
		// }

	}
	
	
	/**
	 *  发送blockhash供bitcoinj下载block数据
	 */
	@Scheduled(cron = "0/1 * * * * ?")
	public void sendRedisDownBlock() {
		if(BlockQueue.heightSet.size()>maxThread){
			//logger.info("queue is full,return");
			return;
		}
		List<BlockHeight> heightList = heightRepo.findByHeight(height);
		while(heightList!=null && heightList.size()>0){
			logger.warn("height:"+height+" block has consumed");
			height--;
			heightList = heightRepo.findByHeight(height);
		}
//		Query query = new Query();
//	    Criteria criteria = Criteria.where("height").is(height);
//	    query.addCriteria(criteria);
//		List<BlockWithHeight> blockWithHeihtList = mongoTemplate.find(query, BlockWithHeight.class);
//		if(blockWithHeihtList==null || blockWithHeihtList.isEmpty()){
//			return;
//		}
		BlockWithHeight blockWithHeight = new BlockWithHeight();
		blockWithHeight.setBlockhash("000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f");
		blockWithHeight.setHeight(0);
		//BlockWithHeight blockWithHeight = blockWithHeihtList.get(0);
		redisService.sendChannelMess(downtopic, blockWithHeight.getBlockhash()+","+(height--));
		
		
		
//		redisService.sendChannelMess(topic, block.getHash()+","+block.getHeight());
//		lastblockhash = block.getPreviousBlockHash();
//		lastBlock.setBlockhash(block.getHash());
//		lastBlock.setHeight(block.getHeight() + "");
		//lastBlockRepo.save(lastBlock);
	}

	
	/**
	 *  发送blockhash以供消费者从服务器下载mongodb中的交易数据，解析交易格式再次写入mongodb
	 */
	//@Scheduled(cron = "* 0/1 * * * ?")
	public void sendRedisParseBlock() {
		//if(height==500000)
		if(BlockQueue.heightSet.size()>=maxThread){
			//logger.info("queue is full,return");
			return;
		}
		List<BlockHeight> heightList = heightRepo.findByHeight(height);
		while(heightList!=null && heightList.size()>0){
			logger.warn("height:"+height+" block has consumed");
			height++;
			heightList = heightRepo.findByHeight(height);
		}
		while(BlockQueue.heightSet.size()<maxThread){
			logger.info("queue is empty:"+BlockQueue.heightSet.size()+" , will fill block:"+height);
			redisService.sendChannelMess(parsetopic, (height++)+"");
		}
		
	}
	
	
	// @Scheduled(cron="0/1 * * * * ?")
	public void testRedis() {
		redisService.sendChannelMess("test", "hello" + (total++));
		try {
			if (produce_on == 0) {
				return;
			}
			if (lastblockhash == null || lastblockhash.trim().length() == 0) {
				lastBlock = lastBlockRepo.findOne("1");
				lastblockhash = lastBlock.getBlockhash();
			}
			if (lastblockhash == null) {
				logger.error("lastblock get null");
				return;
			}

			for (int i = 0; i < timeBlockNum; i++) {// 一次取10个block
				// producer.sendMessage(lastblockhash);
				redisService.sendChannelMess("block", lastblockhash);

				logger.info("send block:" + lastblockhash);
				lastblockhash = blockApi.nextBlockHash(lastblockhash);
				// producer.sendMessage(lastblock);
			}
			Block block = blockApi.getBlock(lastblockhash);

			lastBlock.setBlockhash(block.getHash());
			lastBlock.setHeight(block.getHeight() + "");
			lastBlockRepo.save(lastBlock);
			// logger.info("current
			// block:"+lastblock+",height="+block.getHeight());
			// save to lastBlock to mongodb

			// List<String> txList = blockApi.getTxList(lastblock);
			// for(String tx : txList){
			// TransactionEntity entity = blockApi.getTx(tx);
			// if(graphClient==null){
			// graphClient.init();
			// }
			// graphClient.saveTxRelation(entity);
			// }
			// Set<String> addressSet =new HashSet<String>();
			// List<Address> addressList = new ArrayList<Address>();
			// for(String tx : txList){
			// addressSet.addAll(blockApi.getOutAddress(tx));
			// }
			// for(String add : addressSet){
			// Address address = new Address();
			// address.setAddress(add);
			// addressList.add(address);
			// }
			// repo.save(addressList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Scheduled(cron="0/1 * * * * ?")
	public void executeGetArticleTask() {
		try {
			int index = 0;
			while (index++ < 7) {
				// lastblockhash = producer.sendMessage(lastblockhash);
			}
			logger.info("lastblock=" + lastblockhash);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron="0/1 * * * * ?")
	public void test() {
		TxRelation txRe = new TxRelation();
		txRe.setTxIndex("11");
		txRe.setAddress("abc");
		txRe.setMoney("2.5");

		List<TxRelation> txRelationList = new ArrayList<TxRelation>();
		txRelationList.add(txRe);
		txRe = new TxRelation();
		txRe.setTxIndex("22");
		txRe.setAddress("abc");
		txRe.setMoney("2.5");
		txRelationList.add(txRe);

		// mongoTemplate.insert(txRelationList, TxRelation.class);
		// mongoTemplate.save(txRelationList, "txRelation");
		// txRelationRepo.save(txRe);
		//
		//// txRe = new TxRelation();
		//// txRe.set_id("11");
		//// txRe.setTxid("txabc");
		//// txRe.setType("rollin");
		//// txRelationRepo.save(txRe);
		////
		//
		// Query query = Query.query(Criteria.where("_id").is("33"));
		//
		// //addToset：数组不存在则创建，同时不会加入重复的数据
		// Update update = new Update().set("txid", "txabc");
		// update.set("type", "rollin");
		// mongoTemplate.upsert(query, update, TxRelation.class);

	}

	// @Scheduled(cron="0/1 * * * * ?")
	public void saveAddress() {
		try {
			// if(total++>=1){
			// return;
			// }
			if (produce_on == 0) {
				return;
			}
			if (lastblockhash == null || lastblockhash.trim().length() == 0) {
				lastBlock = lastBlockRepo.findOne("1");
				lastblockhash = lastBlock.getBlockhash();
			}
			if (lastblockhash == null) {
				logger.error("lastblock get null");
				return;
			}

			for (int i = 0; i < timeBlockNum; i++) {// 一次取10个block
				// producer.sendMessage(lastblockhash);
				logger.info("send block:" + lastblockhash);
				lastblockhash = blockApi.nextBlockHash(lastblockhash);
				// producer.sendMessage(lastblock);
			}
			Block block = blockApi.getBlock(lastblockhash);

			lastBlock.setBlockhash(block.getHash());
			lastBlock.setHeight(block.getHeight() + "");
			lastBlockRepo.save(lastBlock);
			// logger.info("current
			// block:"+lastblock+",height="+block.getHeight());
			// save to lastBlock to mongodb

			// List<String> txList = blockApi.getTxList(lastblock);
			// for(String tx : txList){
			// TransactionEntity entity = blockApi.getTx(tx);
			// if(graphClient==null){
			// graphClient.init();
			// }
			// graphClient.saveTxRelation(entity);
			// }
			// Set<String> addressSet =new HashSet<String>();
			// List<Address> addressList = new ArrayList<Address>();
			// for(String tx : txList){
			// addressSet.addAll(blockApi.getOutAddress(tx));
			// }
			// for(String add : addressSet){
			// Address address = new Address();
			// address.setAddress(add);
			// addressList.add(address);
			// }
			// repo.save(addressList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}