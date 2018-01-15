//import java.net.URL;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.lkp.ServerApplication;
//import com.lkp.neo4j.entity.Task;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class TaskTest {
//
//    @LocalServerPort
//     private int port;
//
//    private URL base;
//    
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Before
//    public void setUp() throws Exception {
//        this.base = new URL("http://localhost:" + port + "/");
//    }
//
//    @Test
//    public void findByTaskName() {
//        ResponseEntity<Task> test =         this.restTemplate.getForEntity(
//                this.base.toString() + "/测试任务", Task.class);
//        System.out.println( test.getBody());
//    }
//
//
// @Test
// public void saveTask() {
//	 Task task = new Task();
//	 task.setTaskName("测试任务");
//        ResponseEntity<Task> test =         this.restTemplate.postForEntity(
//                this.base.toString() + "/task",task, Task.class);
//        System.out.println( test.getBody());
//    }
//} 