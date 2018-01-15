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
import com.lkp.config.RedisService;
import com.lkp.neo4j.client.GraphClient;
import com.lkp.neo4j.entity.LastBlock;
import com.lkp.neo4j.entity.TxRelation;
import com.lkp.neo4j.respository.AddressRepository;
import com.lkp.neo4j.respository.LastBlockRepository;
import com.lkp.neo4j.respository.TxRelationRepository;
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

	@Autowired
	private TxRelationRepository txRelationRepo;

	@Autowired
	private AddressRepository repo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GraphClient graphClient;

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

	@Autowired
	FetchBlock fetchBlock;
	int sum = 1;

	// @Value("${height}")
	int height;

	@Value("${topic}")
	String topic;
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

	//@Scheduled(cron = "0/1 * * * * ?")
	public void sendRedisBlock() {
		if (lastblockhash == null || lastblockhash.trim().length() == 0) {
			lastBlock = lastBlockRepo.findOne("1");
			lastblockhash = lastBlock.getBlockhash();
		}
		if (lastblockhash == null) {
			logger.error("lastblock get null");
			return;
		}
		Block block = blockApi.getBlock(lastblockhash);

		redisService.sendChannelMess(topic, block.getHash()+","+block.getHeight());
		lastblockhash = block.getPreviousBlockHash();
//		lastBlock.setBlockhash(block.getHash());
//		lastBlock.setHeight(block.getHeight() + "");
		//lastBlockRepo.save(lastBlock);
	}

	// @Scheduled(cron="0/1 * * * * ?")
	public void testRedis() {
		redisService.sendChannelMess("test", "hello" + (total++));

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

	 @Scheduled(cron="0/1 * * * * ?")
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