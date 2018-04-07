package com.lkp.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.lkp.btcdcli4j.client.BlockChainApi;

/**
 * 根据起始blockhash,依次将该blockhash以及后面的blockhash全部写入kafka
 * @author Administrator
 *
 */
@Component
public class BlockProducer {
	Log logger =  LogFactory.getLog(BlockProducer.class);
	@SuppressWarnings("rawtypes")
	@Autowired
    private KafkaTemplate kafkaTemplate;
	@Value("${spring.kafka.template.default-topic}")
	private String topic;
	//@Autowired
	private BlockChainApi blockApi;
	@SuppressWarnings("unchecked")
	public String sendMessage(String block){
		kafkaTemplate.send(topic, block);
		//logger.info(block +" produce success");
		String nextBlockHash = blockApi.nextBlockHash(block);
		return nextBlockHash; 
	}
}
