import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.lkp.BlockApplication;
import com.lkp.neo4j.entity.Task;
import com.lkp.neo4j.entity.TxRelation;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskTest {

    @LocalServerPort
     private int port;

    private URL base;
    
    @Autowired
    private TestRestTemplate restTemplate;


	@Autowired
    private MongoTemplate mongoTemplate;
   // @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

   // @Test
    public void findByTaskName() {
        ResponseEntity<Task> test =         this.restTemplate.getForEntity(
                this.base.toString() + "/测试任务", Task.class);
        System.out.println( test.getBody());
    }


 //@Test
 public void saveTask() {
	 Task task = new Task();
	 task.setTaskName("测试任务");
        ResponseEntity<Task> test =         this.restTemplate.postForEntity(
                this.base.toString() + "/task",task, Task.class);
        System.out.println( test.getBody());
    }
 
 	@Test
 	public void testInsertMdb(){
 		TxRelation inTxRelation = new TxRelation();
 		inTxRelation.setTxIndex("75c079d0f167b8a1ab4171306f98c9e16318bf052d660c2b313913d8d3e0857a_1");
 		inTxRelation.setInTx("6940b9c9ab027a08c8ac56aacba0579c98590cb63541a00988ab8e29a27231c9");
 		Query query = Query.query(Criteria.where(""
 				+ "_id").is(inTxRelation.getTxIndex()));
		Update update = new Update().set("inTx", inTxRelation.getInTx());
        mongoTemplate.upsert(query, update, TxRelation.class); 
        System.out.println("success");
 	}
} 