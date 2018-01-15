package com.lkp.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BlockConsumer {
	Log logger =  LogFactory.getLog(BlockConsumer.class);
	@Autowired
	AsyncSaveGraphTask asyncTask;
	
	@KafkaListener(topics = { "${spring.kafka.template.default-topic}" })
	public void processMessage(String content) {
		try {
			logger.info("in consume,"+content+" cunsume begin");
			//asyncTask.saveBlockTransaction(content);
			 
//			logger.warn("in consume,"+content+" cunsume begin");
//			asyncTask.test(content);
			//logger.error("in consume,"+content+" cunsume end");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
}