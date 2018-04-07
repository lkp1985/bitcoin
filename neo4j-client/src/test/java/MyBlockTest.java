//import java.net.URL;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.arangodb.ArangoDB;
//import com.arangodb.ArangoDBException;
//import com.lkp.BlockApplication;
//import com.lkp.btcdcli4j.client.BlockChainApi;
//import com.lkp.neo4j.entity.BlockWithHeight;
//import com.lkp.neo4j.entity.MyBlock;
//import com.lkp.neo4j.entity.Task;
//import com.lkp.neo4j.entity.TransactionEntity;
//import com.lkp.schedule.FetchBlock;
//import com.lkp.util.BlockUtil;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = BlockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class MyBlockTest {
//
//	@LocalServerPort
//	private int port;
//
//	private URL base;
//
//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	@Autowired
//	FetchBlock fetchBlock;
//
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@Autowired
//	private BlockChainApi blockApi;
//
//	// @Before
//	public void setUp() throws Exception {
//		this.base = new URL("http://localhost:" + port + "/");
//	}
//
//	// @Test
//	public void findByTaskName() {
//		ResponseEntity<Task> test = this.restTemplate.getForEntity(this.base.toString() + "/测试任务", Task.class);
//		System.out.println(test.getBody());
//	}
//
//	@Test
//	public void saveTask() {
//		Task task = new Task();
//		task.setTaskName("测试任务");
//		ResponseEntity<Task> test = this.restTemplate.postForEntity(this.base.toString() + "/task", task, Task.class);
//		System.out.println(test.getBody());
//	}
//
//	@Test
//	public void saveToGraph() {
//		ArangoDB arangoDB = new ArangoDB.Builder().host("192.168.1.3").build();
//		 
//		String dbName = "mydb";
//		try {
//		  arangoDB.createDatabase(dbName);
//		  System.out.println("Database created: " + dbName);
//		} catch (ArangoDBException e) {
//		  System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
//		}
//	}
//	
//	@Test
//	public void testInsertMdb() {
//		try {
//			String blockhash = "12b5633bad1f9c167d523ad1aa1947b2732a865bf5414eab2f9e5ae5d5c191ba";
//			org.bitcoinj.core.Block block = fetchBlock.getBlock(blockhash);
//
//			MyBlock myBlock = BlockUtil.blockTransfer(block);
//
//			List<TransactionEntity> transList = BlockUtil.blockTransfer(myBlock);
//			System.out.println("myblock=" + myBlock);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 从本地bitcoin-qt下载block数据，存入 blockwithheight 表
//	 */
//	@Test
//	public void downloadBlockWithHeight() {
//		try {
//			String blockhash = "00000000839a8e6886ab5951d76f411475428afc90947ee320161bbf18eb6048";// 1
//			saveBLockWithHeight(blockhash);
//			blockhash = "000000000003ba27aa200b1cecaad478d2b00432346c3f1f3986da1afd33e506";// 100000
//			saveBLockWithHeight(blockhash);
//			blockhash = "000000000000034a7dedef4a161fa058a2d67a173a90155f3a2fe6fc132e0ebf";// 200000
//			saveBLockWithHeight(blockhash);
//			blockhash = "000000000000000082ccf8f1557c5d40b21edabb18d2d691cfbf87118bac7254";// 300000
//			saveBLockWithHeight(blockhash);
//			blockhash = "000000000000000004ec466ce4732fe6f1ed1cddc2ed4b328fff5224276e3f6f";// 400000
//			saveBLockWithHeight(blockhash);
//			blockhash = "00000000000000000024fb37364cbf81fd49cc2d51c09c75c35433c3a1945d04";// 500000
//			saveBLockWithHeight(blockhash);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Async
//	public void saveBLockWithHeight(String blockhash) {
//		while (true) {
//			com.neemre.btcdcli4j.core.domain.Block block = blockApi.getBlock(blockhash);
//			BlockWithHeight blockWithHeight = new BlockWithHeight();
//			blockWithHeight.setBlockhash(blockhash);
//			blockWithHeight.setHeight(block.getHeight());
//			blockWithHeight.setNextBlockHash(block.getNextBlockHash());
//			blockWithHeight.setPreBlockHash(block.getPreviousBlockHash());
//			mongoTemplate.save(blockWithHeight);
//			blockhash = block.getNextBlockHash();
//			System.out.println(block.getHeight() + " save success");
//		}
//
//	}
//}