package com.lkp.btcdcli4j.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import com.lkp.btcdcli4j.util.BlockResourceUtils;
import com.lkp.neo4j.entity.TransactionEntity;
import com.lkp.neo4j.entity.TxRelation;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.RawInput;
import com.neemre.btcdcli4j.core.domain.RawOutput;
import com.neemre.btcdcli4j.core.domain.RawTransaction;

/**
 * A list of examples demonstrating the use of <i>bitcoind</i>'s block chain
 * RPCs (via the JSON-RPC API).
 */
@Component
public class BlockChainApi {

	public static LinkedBlockingQueue<Block> blockQueue = new LinkedBlockingQueue<Block>(20);
	public static LinkedBlockingQueue<BtcdClient> clientQueue = new LinkedBlockingQueue<BtcdClient>(100);
	public static int blockNum = 0;
	public static int txNum = 0;
	BtcdClient client ;
	
	public BlockChainApi(){
		//init();
	}
	
	/**
	 * 根据交易ID获取交易对象
	 * @param blockid
	 * @return
	 */
	public   TransactionEntity getTx(String txid){
		try{
			long start = System.currentTimeMillis();
			if(client==null){
				init();
			}
			//txid = "b41744c1ddce2f3daa1a0c2882bd417a0170c3d6e16d61355852266536c1d2b0";
			RawTransaction trax = (RawTransaction) client
					.getRawTransaction(txid, 1);
			//System.out.println("trax="+trax);
			TransactionEntity entity = getTransEntity(trax);
		 
			long end = System.currentTimeMillis();
			System.out.println("txid:"+txid+" spend "+(end-start));
			return entity;
		}catch(Exception e){
			e.printStackTrace( );
			return null;
		}
		
	}
	
	public Set<String> getOutAddress(String txid){
		try{
			Set<String> addressSet = new HashSet<String>();
			if(client==null){
				init();
			}
			//txid = "b41744c1ddce2f3daa1a0c2882bd417a0170c3d6e16d61355852266536c1d2b0";
			RawTransaction trax = (RawTransaction) client
					.getRawTransaction(txid, 1);
			
			List<RawOutput> outputList = trax.getVOut();
			Map<String,BigDecimal> outputMap = new LinkedHashMap<String,BigDecimal>(); 
			 
			for(RawOutput output : outputList){ 
				try{
					addressSet.addAll(output.getScriptPubKey().getAddresses());
				}catch(Exception e){
					
				} 
				 
			}
			
			return addressSet;
		}catch(Exception e){
			e.printStackTrace( );
			return null;
		}
	}
	private   TransactionEntity getTransEntity(RawTransaction trax){
		TransactionEntity entity = new TransactionEntity();
		String txid = trax.getTxId();
		entity.setTxid(txid);
		entity.setBlockhash(trax.getBlockHash());
		 
		List<RawInput> inputlist  = trax.getVIn();
		List<RawOutput> outputList = trax.getVOut();
//		Map<String,BigDecimal> outputMap = new LinkedHashMap<String,BigDecimal>();
//		Map<String,BigDecimal> inputMap  = new LinkedHashMap<String,BigDecimal>();
//		entity.setOutput_uxto(outputMap);
//		entity.setIn_uxto(inputMap);
		
		List<TxRelation> outTxRelationList = new ArrayList<TxRelation>();
		entity.setOutTxRelationList(outTxRelationList);
		for(RawOutput output : outputList){
			int n = output.getN();
			BigDecimal value = output.getValue();
			List<String> addressList = output.getScriptPubKey().getAddresses();
			if(addressList!=null && !addressList.isEmpty()){
				String address  = addressList.get(0);
				TxRelation txRelation = new TxRelation();
				txRelation.setTxIndex(txid+"_"+output.getN());
				txRelation.setAddress(address);
				txRelation.setMoney(value+"");
				txRelation.setOutTx(txid);
				outTxRelationList.add(txRelation);
			} 
		}
		List<TxRelation> inTxRelationList = new ArrayList<TxRelation>();
		entity.setInTxRelationList(inTxRelationList);
		
		for(RawInput input : inputlist){
			String pretxid = input.getTxId();
			if(pretxid==null){//base block ,属于矿工挖矿的交易 ,没有Input
				 continue;
			}else{
				int index = input.getVOut();
				TxRelation inTxRelation = new TxRelation();
				inTxRelation.setTxIndex(pretxid+"_"+index);
				inTxRelation.setInTx(txid); 
				inTxRelationList.add(inTxRelation);
//				try {
//					RawTransaction pre_trax = (RawTransaction) client
//							.getRawTransaction(pretxid, 1);
//					RawOutput pre_output = pre_trax.getVOut().get(index);
//					BigDecimal pre_value = pre_output.getValue();
//					List<String> pre_addressList = pre_output.getScriptPubKey().getAddresses();
//					if(pre_addressList!=null && !pre_addressList.isEmpty()){
//						String pre_address  = pre_addressList.get(0);
//						inputMap.put(pre_address, pre_value);
//					}
//				} catch (BitcoindException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (CommunicationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}catch(Exception ex){
//					ex.printStackTrace();
//				}
			}
			
			
		}
		return entity;
	}
	public   void init(){
		try{
			CloseableHttpClient httpProvider = BlockResourceUtils.getHttpProvider();
			Properties nodeConfig = BlockResourceUtils.getNodeConfig();
			client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<String> getTxList(String blockHash){
		Block block = null;
		try {
			block = client.getBlock(blockHash);
		} catch (BitcoindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return block.getTx();
	}
	
	public String  nextBlockHash(String blockhash){
		try {
			Block block  = client.getBlock(blockhash);
			return block.getNextBlockHash();
		} catch (BitcoindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public Block  getBlock(String blockhash){
		try {
			Block block  = client.getBlock(blockhash);
			return block;
		} catch (BitcoindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	public Block  getNextBlock(String blockhash){
		try {
			Block block  = client.getBlock(blockhash);
			return block;
		} catch (BitcoindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	public String  preBlockHash(String blockhash){
		try {
			Block block  = client.getBlock(blockhash);
			return block.getPreviousBlockHash();
		} catch (BitcoindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	public static void main(String[] args) throws Exception { 
		BlockChainApi api = new BlockChainApi();
		if(api.client == null){
			api.init();
		}
		Block block = api.client.getBlock("000000000000000001676e89df804a1d12413cd97466e97e5cf6926c12f626a2");
		System.out.println("block=" + block);
		System.out.println("tx.size=" + block.getTx().size());
		 
		for (String tx : block.getTx()) {
			try {
				api.getTx(tx);
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		// RawTransaction trax =
		// (RawTransaction)client.getRawTransaction("a9535a86e93ed545d9f94ac293af064cfc683e9a195bcc4c41595b537fc9272a",1);
		// Transaction tran =
		// client.getTransaction("a9535a86e93ed545d9f94ac293af064cfc683e9a195bcc4c41595b537fc9272a");
		// System.out.println("trax="+trax);
		// System.out.println("trax="+tran);
		// String[] blocks =
		// {"0000000000000289f27f014e541289911f3594860345264ff857672698d6ce22","0000000000000289f27f014e541289911f3594860345264ff857672698d6ce22"};
		// for(String blockstr : blocks){
		// Block block = client.getBlock(blockstr);
		// BlockChainInfo info = client.getBlockChainInfo();
		//
		// Transaction tran =
		// client.getTransaction("15a59cdc11e69b9c43dd8923ad7ff9436df1b0ec96336580c6feb4bacd80394b");
		// System.out.println("tx.size="+block.getTx().size()+",tran="+tran.getAmount());
		//// for(int i=0;i<block.getTx().size();i++){
		//// try{
		////
		//// Transaction tran = client.getTransaction(block.getTx().get(i));
		////
		//// System.out.println(tran.getFee()+","+tran.getBlockIndex()+","+
		//// tran.getAmount()+","+tran.getTo());
		//// System.out.println("details :");
		//// for(PaymentOverview po: tran.getDetails()){
		//// System.out.println(po.getAccount()+","+po.getAddress()+","+po.getVOut()+","+po.getAmount()+","+
		//// po.getFee());
		//// }
		//// }catch(Exception e){
		//// e.printStackTrace();
		//// }
		////
		////
		//// }
		// }

		// client.getBlockCount();
		// client.getBlockHash(345168);
		// client.getChainTips();
		// client.getDifficulty();
		// client.getMemPoolInfo();
		// client.getRawMemPool();
		// client.getRawMemPool(true);
		// client.getTxOutSetInfo();
		// client.verifyChain();
		// client.verifyChain(CheckLevels.LEVEL_4.getIdentifier());
		// client.verifyChain(CheckLevels.LEVEL_4.getIdentifier(), 1000);
	}

	public static BtcdClient getClient() {
		try {
			CloseableHttpClient httpProvider = BlockResourceUtils.getHttpProvider();
			Properties nodeConfig = BlockResourceUtils.getNodeConfig();
			BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
			return client;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void getBlockInfo(BtcdClient client, String blockstr) {
		try {
			int blockNum = 0;
			int txNum = 0;
			while (true) {
				System.out.println("blockNum=" + blockNum++);
				Block block = client.getBlock(blockstr);

				for (String txid : block.getTx()) {
					getTrancation(client, txid, blockNum, txNum++);
				}
				System.out.println("transctions num=" + txNum);
				// txNum += block.getTx().size();
				blockstr = block.getPreviousBlockHash();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getTrancation(BtcdClient client, String txid, int blocknum, int txnum) {
		try {
			RawTransaction trax = (RawTransaction) client.getRawTransaction(txid, 1);
			for (RawOutput out : trax.getVOut()) {
				System.out.println("blocknum=" + blocknum + ",txnum=" + (txnum) + "," + out.getValue() + ","
						+ out.getScriptPubKey().getAddresses() + "," + out.getN());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class pushBLockThread implements Runnable {
	String blockstr = "0000000000000289f27f014e541289911f3594860345264ff857672698d6ce22";
	LinkedBlockingQueue<Block> blockQueue;

	public pushBLockThread(LinkedBlockingQueue<Block> blockQueue) {
		// TODO Auto-generated constructor stub
		this.blockQueue = blockQueue;
	}

	@Override
	public void run() {
		try {
			System.out.println("push thread start");
			CloseableHttpClient httpProvider = BlockResourceUtils.getHttpProvider();
			Properties nodeConfig = BlockResourceUtils.getNodeConfig();
			BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
			while (true) {
				Block block = client.getBlock(blockstr);
				while (!blockQueue.offer(block)) {
					Thread.sleep(3000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class pullBlockThread implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("pull thread start");
				Block block = BlockChainApi.blockQueue.take();
				new Thread(new RunTranctionThread(block)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}

class RunTranctionThread implements Runnable {
	BtcdClient client;
	Block block;

	public RunTranctionThread(Block block) {
		try {

			// TODO Auto-generated constructor stub
			CloseableHttpClient httpProvider = BlockResourceUtils.getHttpProvider();
			Properties nodeConfig = BlockResourceUtils.getNodeConfig();
			client = BlockChainApi.clientQueue.take();
			this.block = block;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("run thread start");
		getBlockInfo();
	}

	public void getBlockInfo() {
		try {
			System.out.println("blockNum=" + BlockChainApi.blockNum++);

			for (String txid : block.getTx()) {
				getTrancation(client, txid, BlockChainApi.blockNum, BlockChainApi.txNum++);
			}
			System.out.println("transctions num=" + BlockChainApi.txNum);
			BlockChainApi.clientQueue.offer(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getTrancation(BtcdClient client, String txid, int blocknum, int txnum) {
		try {
			RawTransaction trax = (RawTransaction) client.getRawTransaction(txid, 1);
			for (RawOutput out : trax.getVOut()) {
				System.out.println("blocknum=" + blocknum + ",txnum=" + (txnum) + "," + out.getValue() + ","
						+ out.getScriptPubKey().getAddresses() + "," + out.getN());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
