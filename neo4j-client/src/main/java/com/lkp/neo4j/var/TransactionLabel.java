package com.lkp.neo4j.var;

import org.neo4j.graphdb.Label;

public class TransactionLabel implements Label  {

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Tx";
	}

}
