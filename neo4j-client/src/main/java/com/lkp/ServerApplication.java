package com.lkp;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ServerApplication   {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ServerApplication.class, args);
    }
    
    
    
    /**
     * 自定义异步线程池
     * @return
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() { 
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(); 
       executor.setThreadNamePrefix("BlockConsum_");
      executor.setMaxPoolSize(48); 
      executor.setQueueCapacity(5);  
      executor.setCorePoolSize(24);
      executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  
      executor.initialize();  
      // 设置拒绝策略
//      executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//        @Override
//        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//          // .....
//        }
//      });
      // 使用预定义的异常处理类
      // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
   
      return executor; 
    } 
}
 