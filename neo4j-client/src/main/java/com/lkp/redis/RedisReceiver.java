package com.lkp.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lkp.kafka.AsyncSaveGraphTask;

@Service
public class RedisReceiver {
    @Autowired
    RedisService redisService;
    @Autowired
    AsyncSaveGraphTask asyncTask;
    
    Log logger =  LogFactory.getLog(RedisReceiver.class);
    public void receiveMessage(String message) {
    	try {
			asyncTask.saveBlockTx(message); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void receiveMessage2(String message) {
    	try {
		//	logger.info("in consume,"+message+" cunsume begin");
			String blockhash = message.split(",")[0];
			String height = message.split(",")[1];
			asyncTask.saveBlock(blockhash,Integer.parseInt(height)); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
