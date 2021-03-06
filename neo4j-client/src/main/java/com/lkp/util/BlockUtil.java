package com.lkp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;

import com.lkp.neo4j.entity.BaseTransaction;
import com.lkp.neo4j.entity.InputTransaction;
import com.lkp.neo4j.entity.MyBlock;
import com.lkp.neo4j.entity.OutputTransaction;
import com.lkp.neo4j.entity.TransactionEntity;
import com.lkp.neo4j.entity.TxRelation;

/**
 * 
 * @author Administrator
 *
 */
public class BlockUtil {
	static Log logger = LogFactory.getLog(BlockUtil.class);
	@SuppressWarnings("deprecation")
	public static MyBlock blockTransfer(Block block){
		MyBlock myBlock = new MyBlock();
		
		String blockhash = block.getHashAsString();
		List<BaseTransaction> baseTxList = new ArrayList<BaseTransaction>();
		myBlock.setBlockhash(blockhash);
		myBlock.setBaseTxList(baseTxList);
		myBlock.setPreblockhash(block.getPrevBlockHash().toString());
		List<org.bitcoinj.core.Transaction> txList = block.getTransactions();
		for(org.bitcoinj.core.Transaction transaction : txList){
			BaseTransaction baseTransaction  = new BaseTransaction();
			baseTxList.add(baseTransaction);
			List<TransactionOutput> outputList = transaction.getOutputs();
			List<TransactionInput> inputList = transaction.getInputs();
			List<OutputTransaction> baseOutputList = new ArrayList<OutputTransaction>();
			List<InputTransaction> baseInputList = new ArrayList<InputTransaction>();
			baseTransaction.setInputList(baseInputList);
			baseTransaction.setOutputList(baseOutputList);
			baseTransaction.setTxid(transaction.getHashAsString());
			for(TransactionOutput output : outputList){
				OutputTransaction baseOutput = new OutputTransaction();
				
				try{ 
					String address =null;
					try{
						address = output.getScriptPubKey().getToAddress(output.getParams()).toString();
					}catch(Exception e){
						address  = output.getScriptPubKey().getFromAddress(output.getParams()).toString();
					} 
					if(address!=null){//这里可能要考虑隔离验证产生的output: SegWit commitment output
						long value = output.getValue().value;
						baseOutput.setMoney(value+"");
						baseOutput.setIndex(output.getIndex());
						baseOutput.setOutaddress(address);
						baseOutputList.add(baseOutput);
					}
				}catch(Exception e){ 
					logger.error("block:"+blockhash+" error:"+e.getMessage());
				}
			}
			for(TransactionInput input : inputList){
				InputTransaction baseInput = new InputTransaction();
				if(!input.isCoinBase()){//如果是coinbase,暂时不添加
					baseInput.setTxid(input.getOutpoint().getHash().toString());
					baseInput.setIndex((int)input.getOutpoint().getIndex());
					baseInputList.add(baseInput); 
				}else{
//					baseInput.setTxid(transaction.getHashAsString());//以当前交易ID为上个交易的txid,index=-1
//					baseInput.setIndex(-1);//或(int)input.getOutpoint().getIndex()
//					baseInputList.add(baseInput); 
				}
				 				
			}
		}
		return myBlock;
	}
	
	public static List<TransactionEntity> blockTransfer(MyBlock myBlock){
		List<TransactionEntity> entityList = new ArrayList<TransactionEntity>();
		for(BaseTransaction baseTransaction : myBlock.getBaseTxList()){
			//String txHash = baseTransaction.getTxid();
			TransactionEntity entity = new TransactionEntity();
			entityList.add(entity);
			entity.setHeight(myBlock.getHeight());
			entity.setBlockhash(myBlock.getBlockhash());
			List<TxRelation> inTxRelationList = new ArrayList<TxRelation>();
			List<TxRelation> outTxRelationList = new ArrayList<TxRelation>();
			entity.setInTxRelationList(inTxRelationList);
			entity.setOutTxRelationList(outTxRelationList);
			for(InputTransaction inputTx : baseTransaction.getInputList()){
				TxRelation txRelation = new TxRelation();
				int index = inputTx.getIndex();
				String inputTxId = inputTx.getTxid();
				if(index==-1 && inputTxId == null){//矿工coinbase 不作处理
					
//					txRelation.setInTx(baseTransaction.getTxid()); 
//					txRelation.setOutTx("coinbase_"+baseTransaction.getTxid()); //表示从coinbase 指向当前交易
//					txRelation.setTxIndex("coinbase_"+baseTransaction.getTxid());
//					inTxRelationList.add(txRelation);
				}else{
					txRelation.setInTx(baseTransaction.getTxid());//需要设置当前交易的txid，而不能设置inputTx的Txid
					txRelation.setTxIndex(inputTxId+"_"+index);
					inTxRelationList.add(txRelation);
				}
			}
			for(OutputTransaction outputTx : baseTransaction.getOutputList()){
				if(outputTx.getOutaddress()==null){
					continue;
				}
				TxRelation txRelation = new TxRelation();
				txRelation.setAddress(outputTx.getOutaddress());
				txRelation.setMoney(outputTx.getMoney());
				txRelation.setOutTx(baseTransaction.getTxid());
				txRelation.setTxIndex(baseTransaction.getTxid()+"_"+outputTx.getIndex());
				outTxRelationList.add(txRelation);
			}
		}
		return entityList;
	}
}
