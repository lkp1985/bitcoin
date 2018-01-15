//package com.lkp.neo4j.client;
//
//import java.io.File;
//
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;
//import org.neo4j.graphdb.Relationship;
//import org.neo4j.graphdb.Transaction;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.lkp.neo4j.entity.TransactionEntity;
//import com.lkp.neo4j.var.RelTypes;
//
//@Component
//public class GraphClient1   implements AutoCloseable
//{
//    private   GraphDatabaseService graphDb;
//
//    @Value("${spring.data.neo4j.url}")
//    String uri;
//    @Value("${spring.data.neo4j.username}")
//    String username;
//    @Value("${spring.data.neo4j.password}")
//    String password;
//    
//    @Value("${db.path}")
//    String path;
//    int sum  = 0;
////    public GraphClient()
////    {
////    	try{
////    		driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
////    		
////    	}catch(Exception e){
////    		
////    	}
////    }
////    public GraphClient(String uri,String username,String password  )
////    {
////        driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
////    }
//    
//    public GraphClient1(){
//    	try{
////    		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(path) );
////    		registerShutdownHook( graphDb );
//    		
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
//    private static void registerShutdownHook( final GraphDatabaseService graphDb )
//    {
//        // Registers a shutdown hook for the Neo4j instance so that it
//        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
//        // running application).
//        Runtime.getRuntime().addShutdownHook( new Thread()
//        {
//            @Override
//            public void run()
//            {
//            	System.out.println("graphDb will close");
//                graphDb.shutdown();
//            }
//        } );
//    }
//    
//    
//
//    public void init(){
//    	try{
//     		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(path) );
//    		registerShutdownHook( graphDb );
//    		
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
//    
//    public void run(){
//    	if(graphDb==null){
//    		init();
//    	}
//    	try ( Transaction tx = graphDb.beginTx() )
//    	{
//    		 
//    		Node firstNode = graphDb.createNode();
//    		firstNode.setProperty( "message", "Hello, " );
//    		Node secondNode = graphDb.createNode();
//    		secondNode.setProperty( "message", "World!" );
//
//    		Relationship relationship = firstNode.createRelationshipTo( secondNode, RelTypes.ROLLIN );
//    		relationship.setProperty( "message", "brave Neo4j " );
//    	    tx.success();
//    	}
//    }
//    public void saveTxRelation(TransactionEntity txEntity){
//    	if(graphDb==null){
//    		init();
//    	}
//    	try ( Transaction tx = graphDb.beginTx() )
//    	{
////    		String txid = txEntity.getTxid();
////    		AddressLabel address_label = new AddressLabel();
////    		TransactionLabel tx_label = new TransactionLabel();
////        	String blockhash = txEntity.getBlockhash();
////        	Node tx_node = graphDb.createNode(tx_label);
////        	tx_node.setProperty(BlockConst.TRANSACTION, txid);
////        	tx_node.setProperty(BlockConst.TRANSACTION_BLOCKHASH, blockhash);
////        
////        	Map<String, BigDecimal> output_Map = txEntity.getOutput_uxto(); 
////        	for(String out_address: output_Map.keySet()){
////        		 
////        		Node address_node  =  graphDb.createNode(address_label);
////        		address_node.setProperty("address", out_address);
////        		
////        		Relationship rollout = tx_node.createRelationshipTo( address_node, RelTypes.ROLLOUT );
////        		rollout.setProperty(BlockConst.ROLL_MONEY, output_Map.get(out_address)+"");
////        		 
////        	}
////        	if(txEntity.getIn_uxto()!=null && !txEntity.getIn_uxto().isEmpty()){
////        		
////        		Map<String, BigDecimal> input_Map = txEntity.getIn_uxto();
////        		for(String in_address: input_Map.keySet()){
////        			Node address_node  =  graphDb.createNode();
////        			address_node.addLabel(address_label);
////            		address_node.setProperty("address", in_address);
////            		
////            		Relationship rollout = address_node.createRelationshipTo( tx_node, RelTypes.ROLLIN );
////            		rollout.setProperty(BlockConst.ROLL_MONEY, output_Map.get(in_address)+"");
////        			 
////            	}
////        	}
////    	    tx.success();
//    	}
////    	if(driver==null){
////    		init();
////    	}
////    	try ( Session session = driver.session() )
////        {
////    		 
////            String result = session.writeTransaction( new TransactionWork<String>()
////            {
////                @Override
////                public String execute( Transaction tx )
////                {
////                	String txid = txEntity.getTxid();
////                	String blockhash = txEntity.getBlockhash();
////                	String process = "MERGE (t:"+BlockConst.TRANSACTION+"{"+
////                	BlockConst.TRANSACTION_TXID+":'"+txid+"',"+
////                			BlockConst.TRANSACTION_BLOCKHASH+":'"+blockhash+"'})";
////                	Map<String, BigDecimal> output_Map = txEntity.getOutput_uxto();
////                	int index  = 0;
////                	String out_a  ="out_a";
////                	String in_a = "in_a";
////                	for(String out_address: output_Map.keySet()){
////                		if(index > 0){
////                			out_a="out_a"+index;
////                		}
////                		process += " MERGE ("+out_a+":"+BlockConst.ADDRESS+"{"+BlockConst.ADDRESS_ID+":'"+out_address+"'})";
////                		process += " MERGE (t)-[:"+BlockConst.ROLLOUT +
////                				" {"+BlockConst.ROLL_MONEY+":"+output_Map.get(out_address)+"}]->("+out_a+")";
////                		index ++;
////                	}
////                	index  = 0;
////                	if(txEntity.getIn_uxto()!=null && !txEntity.getIn_uxto().isEmpty()){
////                		
////                		Map<String, BigDecimal> input_Map = txEntity.getIn_uxto();
////                		for(String in_address: input_Map.keySet()){
////                			if(index > 0){
////                    			in_a="in_a"+index;
////                    		}
////                    		process += " MERGE ("+in_a+":"+BlockConst.ADDRESS+"{"+BlockConst.ADDRESS_ID+":'"+in_address+"'})";
////                    		process += " MERGE ("+in_a+")-[:"+BlockConst.ROLLIN +
////                    				" {"+BlockConst.ROLL_MONEY+":"+input_Map.get(in_address)+"}]->(t)";
////                    		index++;
////                    	}
////                	}
////                	System.out.println("process="+process);
////                    tx.run( process, parameters() );
////                    return "success";
////                }
////            } );
////            System.out.println( result );
////        }
//    }
//    
//     
//
//    public static void main( String... args ) throws Exception
//    {
////        try  
////        {
////        	GraphClient greeter = new GraphClient() ;
////        	 
////            greeter.printGreeting( "hello, world" );
////            greeter.close();
////        }catch(Exception e){
////        	e.printStackTrace();
////        }
//    }
//	@Override
//	public void close() throws Exception {
//		// TODO Auto-generated method stub
//		System.out.println("will stop");
//		graphDb.shutdown();
//	}
//}