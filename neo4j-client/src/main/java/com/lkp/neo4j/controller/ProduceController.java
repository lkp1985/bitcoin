package com.lkp.neo4j.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lkp.kafka.BlockProducer;

/**
 * blockhash producer
 * @author Administrator
 *
 */
@RestController 
public class ProduceController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @SuppressWarnings("rawtypes")
	@Autowired
    private BlockProducer blockProducer;
    
    /**
     * 发送起始blockhash
     * @param blockhash
     */
    @RequestMapping(value = "/block/{blockhash}", method = RequestMethod.POST)
    public String sendKafka(@PathVariable("blockhash") String blockhash) {
        try {
        	String nextBlock = blockProducer.sendMessage(blockhash);
            logger.info("发送kafka成功."); 
            return nextBlock;
        } catch (Exception e) {
            logger.error("发送kafka失败", e); 
            return null;
        }
    }

}