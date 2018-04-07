package com.lkp.schedule;

import java.io.BufferedReader;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.lkp.neo4j.entity.InTx;
import com.lkp.neo4j.entity.OutTx;

/**
 * 将txRelation的inTx和outTx分别导出后再重新插入mdb，以去重以免去掉空值，以免在后面导入neo4j时报错的问题
 * @author Administrator
 *
 */
public class InsertInputOutput {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@Value("${inputFilePath}")
	String inputFile;

	@Value("${outputFilePath}")
	String outputFile;
	@Scheduled(cron = "0/1 * * * * ?")
	public void toChong() { 
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = null;
			while((line=br.readLine())!=null){
				try{
					String[] lines = line.split(",");
					if(lines.length==2&&lines[0].trim().length()>10&&lines[1].trim().length()>10){
						InTx inTx = new InTx();
						inTx.setAddress(lines[0]);
						inTx.setTxId(lines[1]);
						mongoTemplate.save(inTx);
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			br.close();
			br = new BufferedReader(new FileReader(outputFile));
			line = null;
			while((line=br.readLine())!=null){
				try{
					String[] lines = line.split(",");
					if(lines.length==3&&lines[0].trim().length()>10&&lines[1].trim().length()>10&&lines[1].trim().length()>0){
						OutTx outTx = new OutTx();
						outTx.setAddress(lines[1]);
						outTx.setTxId(lines[0]);
						outTx.setMoney(lines[2]);
						mongoTemplate.save(outTx);
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
