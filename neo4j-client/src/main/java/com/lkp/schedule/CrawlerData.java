package com.lkp.schedule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lkp.neo4j.entity.AddressBBS;

/**
 * 定时抓取数据，主要是btc.com  查看有话题的btc 地址
 * 
 * @author lkp
 *
 */
@Component
public class CrawlerData {
	private static final Logger logger = LoggerFactory.getLogger(CrawlerData.class);
  
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${crawlerBlock}")
	private String crawlerBlock;
	 
	private String preblock = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f";
	/**
	 *  从btc.com上抓取数据
	 */
	//@Scheduled(cron = "0/1 * * * * ?")
	public void crawlerBtccomData() { 
		while(true){
			try{
				List<AddressBBS> addressList = new ArrayList<AddressBBS>();
				Document doc = Jsoup.parse(new URL("http://btc.com/"+crawlerBlock), 5000);
				List<AddressBBS> addresses = parseAddressBBS(doc);
				if(addresses!=null){
					addressList.addAll(addresses);
				}
				int pageCount = getPageNum(doc);
				
				if(pageCount>1){
					for(int i=2;i<=pageCount;i++){
						try{
							Document tdoc = Jsoup.parse(new URL("http://btc.com/"+crawlerBlock+"?page="+i+"&order_by=tx_block_idx&asc=1"), 10000);
							addresses = parseAddressBBS(tdoc);
							if(addresses!=null){
								addressList.addAll(addresses);
							}
						}catch(Exception e){
							logger.error(e.getMessage(),e);
						}
						
					}
				}
				if(addressList.size()>0){
					logger.info("find bbs address:"+addresses+" in block:"+crawlerBlock);
					for(AddressBBS bbs : addressList){
						mongoTemplate.save(bbs);
					}
				}
				String temp = crawlerBlock;
				crawlerBlock = getNextBlock(doc);
				preblock = temp;
				//Thread.sleep(1000);
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
		}
	}

	public static List<AddressBBS> parseAddressBBS(Document doc){
		try{
			List<AddressBBS> addressList = new ArrayList<AddressBBS>();
			
			Elements eles =  doc.getElementsByTag("li");
			for(Element ele : eles){
				 
				Elements liEles = ele.getElementsByClass("glyphicon-new-window");
				if(liEles!=null && liEles.size()==1){
					AddressBBS bbs = new AddressBBS();
					addressList.add(bbs);
					Elements aeles = ele.getElementsByTag("a");//https://btc.com/
					String addressurl = aeles.get(0).attr("href");
					String address = addressurl.substring(16);
					String addressbbs = aeles.get(1).attr("href")	;
					bbs.setAddress(address);
					bbs.setUrl(addressbbs);
					logger.info("addressurl="+addressurl+",addressbbs="+addressbbs);
				}
			}
			return addressList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static int getPageNum(Document doc){
		try{
			Elements eles = doc.getElementsByTag("script");
			for(Element ele : eles){
				 
				String globals = ele.html();
				if(globals.startsWith("var globals")){
					int index = globals.indexOf("page_count: '");
					int index2 = globals.indexOf("enable_order_by:");
					String pagecount = globals.substring(index+13, index2-11);
					System.out.println("pagecount="+pagecount);
					return Integer.parseInt(pagecount);
				}
			}
			return 0;
//			int index = body.indexOf("page_count: '");
//			String temp = body.substring(index+1);
//			int index2 = temp.indexOf("'");
//			String pageNum = body.substring(index, index2);
			
		}catch(Exception e	){
			return 0;
		}
	}
	
	public String getNextBlock(Document doc){
		Elements eles = doc.getElementsByAttribute("ga-target");
		if(eles!=null && eles.size()>0){
			return eles.get(2).text();
//			for(Element ele : eles){
//				String text = ele.text();
//				if(!text.equals(crawlerBlock) && !text.equals(preblock)){
//					return text;
//				}
//				System.out.println("text="+text);
//			}
		}
		return "";
	}
	public static void main(String[] args) {
		try {
			Document doc = Jsoup.parse(new URL("https://btc.com/000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"), 5000);
			parseAddressBBS(doc);
			int pageCount = getPageNum(doc);
			if(pageCount>1){
				for(int i=2;i<=pageCount;i++){
					doc = Jsoup.parse(new URL("http://btc.com/00000000000000000024fb37364cbf81fd49cc2d51c09c75c35433c3a1945d04?page="+i+"&order_by=tx_block_idx&asc=1"), 5000);
					parseAddressBBS(doc);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}