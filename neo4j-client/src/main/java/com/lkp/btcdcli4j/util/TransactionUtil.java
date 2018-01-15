package com.lkp.btcdcli4j.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lkp.neo4j.entity.Address;
import com.lkp.neo4j.entity.Transaction;
import com.lkp.neo4j.entity.TransactionEntity;
import com.lkp.neo4j.entity.TxRelation;

public class TransactionUtil {
	
	public static Transaction getTransaction(TransactionEntity txEntity){
		try{
			Transaction tr = new Transaction();
			tr.setBlockhash(txEntity.getBlockhash());
			tr.setHeight(txEntity.getHeight()+"");
			tr.setTxid(txEntity.getTxid());
			return tr;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param txEntity
	 * @return
	 */
	public static List<Address> getAddresses(TransactionEntity txEntity){
		try{
			List<Address> addressList  =new ArrayList<Address>();
			Set<String> addressSet  =new HashSet<String>();
			List<TxRelation> txRelationList  = txEntity.getOutTxRelationList();
			for(TxRelation txRela : txRelationList){
				addressSet.add(txRela.getAddress());
			}
			for(String addr :addressSet){
				Address address = new Address();
				address.setAddress(addr);
				addressList.add(address);
			}
			return addressList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<TxRelation> getInTxRelations(TransactionEntity txEntity){
		try{
			List<TxRelation> inTxRelationList = txEntity.getInTxRelationList();
			return inTxRelationList; 
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<TxRelation> getOutTxRelations(TransactionEntity txEntity){
		try{
			List<TxRelation> outTxRelationList = txEntity.getOutTxRelationList(); 
			return outTxRelationList; 
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
