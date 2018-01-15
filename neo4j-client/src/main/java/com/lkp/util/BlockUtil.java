package com.lkp.util;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;

import com.lkp.neo4j.entity.BaseTransaction;
import com.lkp.neo4j.entity.InputTransaction;
import com.lkp.neo4j.entity.MyBlock;
import com.lkp.neo4j.entity.OutputTransaction;

/**
 * 
 * @author Administrator
 *
 */
public class BlockUtil {
	public static MyBlock BlockTransfer(Block block){
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
				baseOutputList.add(baseOutput);
				try{ 
				//	output.toString();
					if(output.getScriptPubKey().isSentToAddress()||output.getScriptPubKey().isPayToScriptHash()){
						String address = output.getScriptPubKey().getToAddress(output.getParams()).toString();
						
						baseOutput.setOutaddress(address);
						long value = output.getValue().value;
						baseOutput.setMoney(value+"");
						baseOutput.setIndex(output.getIndex());
					}
				}catch(Exception e){ 
					e.printStackTrace();
				}
				
				
			}
			
			for(TransactionInput input : inputList){
				InputTransaction baseInput = new InputTransaction();
				baseInputList.add(baseInput); 
				input.toString();
				if(!input.isCoinBase())
					
					baseInput.setTxid(input.getOutpoint().getHash().toString());
					baseInput.setIndex((int)input.getOutpoint().getIndex());
				} 
				 				
			}
		return myBlock;
	}
}
