//package com.lkp.neo4j.client;
//
//import static org.neo4j.driver.v1.Values.parameters;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
//import org.neo4j.driver.v1.AuthTokens;
//import org.neo4j.driver.v1.Driver;
//import org.neo4j.driver.v1.GraphDatabase;
//import org.neo4j.driver.v1.Session;
//import org.neo4j.driver.v1.StatementResult;
//import org.neo4j.driver.v1.Transaction;
//import org.neo4j.driver.v1.TransactionWork;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.lkp.neo4j.entity.TransactionEntity;
//import com.lkp.neo4j.var.BlockConst;
//
//@Component
//public class GraphClient2 implements AutoCloseable
//{
//    private   Driver driver;
//
//    @Value("${spring.data.neo4j.url}")
//    String uri;
//    @Value("${spring.data.neo4j.username}")
//    String username;
//    @Value("${spring.data.neo4j.password}")
//    String password;
//    int sum  =0;
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
//    @Override
//    public void close() throws Exception
//    {
//        driver.close();
//    }
//
//    public void init(){
//    	try{
//    		driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
//    		
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
//    public void saveTxRelation(TransactionEntity txEntity){
//    	if(driver==null){
//    		init();
//    	}
//    	try ( Session session = driver.session() )
//        {
//    		 
//            String result = session.writeTransaction( new TransactionWork<String>()
//            {
//                @Override
//                public String execute( Transaction tx )
//                {
//                	String txid = txEntity.getTxid();
//                	String blockhash = txEntity.getBlockhash();
//                	String process = "MERGE (t:"+BlockConst.TRANSACTION+"{"+
//                	BlockConst.TRANSACTION_TXID+":'"+txid+"',"+
//                			BlockConst.TRANSACTION_BLOCKHASH+":'"+blockhash+"'})";
//                	Map<String, BigDecimal> output_Map = txEntity.getOutput_uxto();
//                	int index  = 0;
//                	String out_a  ="out_a";
//                	String in_a = "in_a";
//                	for(String out_address: output_Map.keySet()){
//                		if(index > 0){
//                			out_a="out_a"+index;
//                		}
//                		process += " MERGE ("+out_a+":"+BlockConst.ADDRESS+"{"+BlockConst.ADDRESS_ID+":'"+out_address+"'})";
//                		process += " MERGE (t)-[:"+BlockConst.ROLLOUT +
//                				" {"+BlockConst.ROLL_MONEY+":"+output_Map.get(out_address)+"}]->("+out_a+")";
//                		index ++;
//                	}
//                	index  = 0;
//                	if(txEntity.getIn_uxto()!=null && !txEntity.getIn_uxto().isEmpty()){
//                		
//                		Map<String, BigDecimal> input_Map = txEntity.getIn_uxto();
//                		for(String in_address: input_Map.keySet()){
//                			if(index > 0){
//                    			in_a="in_a"+index;
//                    		}
//                    		process += " MERGE ("+in_a+":"+BlockConst.ADDRESS+"{"+BlockConst.ADDRESS_ID+":'"+in_address+"'})";
//                    		process += " MERGE ("+in_a+")-[:"+BlockConst.ROLLIN +
//                    				" {"+BlockConst.ROLL_MONEY+":"+input_Map.get(in_address)+"}]->(t)";
//                    		index++;
//                    	}
//                	}
//                	System.out.println("process="+process);
//                    tx.run( process, parameters() );
//                    return "success";
//                }
//            } );
//            System.out.println( result );
//        }
//    }
//    
//    public void printGreeting( final String message )
//    {
//        try ( Session session = driver.session() )
//        {
//        	 
//            String greeting = session.writeTransaction( new TransactionWork<String>()
//            {
//                @Override
//                public String execute( Transaction tx )
//                {
//                	String synax = "CREATE (a:Greeting) " +
//                            "SET a.message = $message " +
//                            "RETURN a.message + ', from node ' + id(a)";
//                	System.out.println("synamx="+synax);
//                    StatementResult result = tx.run(synax,
//                            parameters( "message", message ) );
//                    return result.single().get( 0 ).asString();
//                }
//            } );
//            System.out.println( greeting );
//        }
//    }
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
//}