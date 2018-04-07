package com.lkp.neo4j.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.lkp.btcdcli4j.util.TransactionUtil;
import com.lkp.neo4j.entity.Address;
import com.lkp.neo4j.entity.TransactionEntity;
import com.lkp.neo4j.entity.TxRelation;

@Component
public class GraphClient   implements AutoCloseable
{
	Log logger = LogFactory.getLog(GraphClient.class);
//	@Autowired
//	AddressRepository addressReop;
//	@Autowired
//	TxRelationRepository relationReop;
//	@Autowired
//	TransactionRepository txRepo;
	
//	@Autowired
//    private MongoTemplate mongoTemplate;
	
    private   GraphDatabaseService graphDb;

    @Value("${spring.data.neo4j.url}")
    String uri;
    @Value("${spring.data.neo4j.username}")
    String username;
    @Value("${spring.data.neo4j.password}")
    String password;
    
    @Value("${db.path}")
    String path;
    int sum  = 0;
//    public GraphClient()
//    {
//    	try{
//    		driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
//    		
//    	}catch(Exception e){
//    		
//    	}
//    }
//    public GraphClient(String uri,String username,String password  )
//    {
//        driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
//    }
    
    public GraphClient(){
    	try{
//    		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(path) );
//    		registerShutdownHook( graphDb );
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
            	System.out.println("graphDb will close");
                graphDb.shutdown();
            }
        } );
    }
    
    

    public void init(){
    	try{
//     		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(path) );
//    		registerShutdownHook( graphDb );
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
     
    /**
     * 将节点及关系保存到mongodb txRelation表
     * @param txEntity
     */
    public void saveTransactionInfo( MongoTemplate mongoTemplate,TransactionEntity txEntity){
    	try{
    		//long start =  System.currentTimeMillis();
    		List<Address> addressList = TransactionUtil.getAddresses(txEntity);
        	com.lkp.neo4j.entity.Transaction transaction = TransactionUtil.getTransaction(txEntity);
        	List<TxRelation> outTxRelationList = TransactionUtil.getOutTxRelations(txEntity);
        	List<TxRelation> inTxRelationList = TransactionUtil.getInTxRelations(txEntity);
        	int size = 0;
        	if(outTxRelationList!=null){
        		size+=outTxRelationList.size();
        	}
        	if(inTxRelationList!=null){
        		size+=inTxRelationList.size();
        	}
        	long end = System.currentTimeMillis();
        	//logger.info("get txRelationList:"+size+" ,spend "+(end-start));
//        	try{
//        		mongoTemplate.save(transaction);
//        		
//        	}catch(Exception e){
//        		logger.error("save mongodb error:"+e.getMessage(),e);
//        	}
//        	
//        	for(Address address :addressList){
//        		try{
//        			mongoTemplate.save(address);
//        			
//        		}catch(Exception e){
//        			logger.error("save mongodb error:"+e.getMessage(),e);
//        		}
//        	}
        	for(TxRelation outTxRelation : outTxRelationList){
        		try{
        			Query query = Query.query(Criteria.where("_id").is(outTxRelation.getTxIndex()));
            		Update update = new Update().set("address", outTxRelation.getAddress());
            		update.set("money", outTxRelation.getMoney());
            		update.set("outTx", outTxRelation.getOutTx());
            		try{
            			mongoTemplate.upsert(query, update, TxRelation.class); 
            			
            		}catch(Exception e){//可能会报唯一性错误 ，报错后再更新一次，
            			mongoTemplate.upsert(query, update, TxRelation.class); 
            		}
        		}catch(Exception e){
        			logger.error("save mongodb error:"+outTxRelation+",\n"+ e.getMessage(),e);
        		}
        		
        	}
        	
        //	mongoTemplate.insert(inTxRelationList, TxRelation.class);
        	for(TxRelation inTxRelation : inTxRelationList){
        		try{
        			Query query = Query.query(Criteria.where("_id").is(inTxRelation.getTxIndex()));
            		Update update = new Update().set("inTx", inTxRelation.getInTx());
                    try{
                    	mongoTemplate.upsert(query, update, TxRelation.class); 
                    }catch(Exception e){//可能会报唯一性错误 ，报错后再更新一次，
                    	mongoTemplate.upsert(query, update, TxRelation.class);
                    }
        		}catch(Exception e){
        			logger.error("save mongodb error:"+e.getMessage(),e);
        		}
        		
        	}
        	//logger.info("in blockheight:"+txEntity.getHeight()+" ,save  relation:"+size+", spend "+(System.currentTimeMillis() - end));
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    	
    	
    }
    
    
    public void saveTxRelation(TransactionEntity txEntity){
    	if(graphDb==null){
    		init();
    	}
    	try ( Transaction tx = graphDb.beginTx() )
    	{
    		Node result = null;
    		ResourceIterator<Node> resultIterator = null;
    		 String queryString = "MERGE (n:User {name: $name}) RETURN n";
    		    Map<String, Object> parameters = new HashMap<>();
    		    parameters.put( "name", username );
    		    resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );
    		    result = resultIterator.next();
    		    tx.success(); 
    		 
    		/*String txid = txEntity.getTxid();
        	String blockhash = txEntity.getBlockhash();
        	String process = "MERGE (t:"+BlockConst.TRANSACTION+"{"+
        	BlockConst.TRANSACTION_TXID+":'"+txid+"',"+
        			BlockConst.TRANSACTION_BLOCKHASH+":'"+blockhash+"'})";
        	Map<String, BigDecimal> output_Map = txEntity.getOutput_uxto();
        	int index  = 0;
        	String out_a  ="out_a";
        	String in_a = "in_a";
        	for(String out_address: output_Map.keySet()){
        		if(index > 0){
        			out_a="out_a"+index;
        		}
        		process += " MERGE ("+out_a+":"+BlockConst.ADDRESS+"{"+BlockConst.ADDRESS_ID+":'"+out_address+"'})";
        		process += " MERGE (t)-[:"+BlockConst.ROLLOUT +
        				" {"+BlockConst.ROLL_MONEY+":"+output_Map.get(out_address)+"}]->("+out_a+")";
        		index ++;
        	}
        	index  = 0;
        	if(txEntity.getIn_uxto()!=null && !txEntity.getIn_uxto().isEmpty()){
        		
        		Map<String, BigDecimal> input_Map = txEntity.getIn_uxto();
        		for(String in_address: input_Map.keySet()){
        			if(index > 0){
            			in_a="in_a"+index;
            		}
            		process += " MERGE ("+in_a+":"+BlockConst.ADDRESS+"{"+BlockConst.ADDRESS_ID+":'"+in_address+"'})";
            		process += " MERGE ("+in_a+")-[:"+BlockConst.ROLLIN +
            				" {"+BlockConst.ROLL_MONEY+":"+input_Map.get(in_address)+"}]->(t)";
            		index++;
            	}
        	}
        	System.out.println("process="+process);
        	 
    		graphDb.execute(process);
    		 
    	    tx.success();*/
    	}
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
    }
    
     

    public static void main( String... args ) throws Exception
    {
//        try  
//        {
//        	GraphClient greeter = new GraphClient() ;
//        	 
//            greeter.printGreeting( "hello, world" );
//            greeter.close();
//        }catch(Exception e){
//        	e.printStackTrace();
//        }
    }
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("will stop");
		graphDb.shutdown();
	}
}