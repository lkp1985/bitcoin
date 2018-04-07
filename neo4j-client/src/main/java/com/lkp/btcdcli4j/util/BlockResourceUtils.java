package com.lkp.btcdcli4j.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class BlockResourceUtils { 
//	node.bitcoind.rpc.protocol = http
//			node.bitcoind.rpc.host = 192.168.1.3
//			node.bitcoind.rpc.port = 8332
//			node.bitcoind.rpc.user = bitcoinrpc
//			node.bitcoind.rpc.password = 111111
//			node.bitcoind.http.auth_scheme = Basic
//
//			# Configuration parameters for the 'bitcoind' notification daemon ('BtcdDaemon')
//			node.bitcoind.notification.alert.port = 15158
//			node.bitcoind.notification.block.port = 15159
//			node.bitcoind.notification.wallet.port = 15160
//	@Value("${node.bitcoind.rpc.protocol}")
//	private String rpc_protocol;
//	@Value("${node.bitcoind.rpc.host}")
//	private String rpc_host;
//	@Value("${node.bitcoind.rpc.port}")
//	private String rpc_port;
//	@Value("${node.bitcoind.rpc.user}")
//	private String rpc_user;
//	@Value("${node.bitcoind.rpc.password}")
//	private String rpc_password;
//	@Value("${node.bitcoind.http.auth_scheme}")
//	private String auth_schema;
//	@Value("${node.bitcoind.notification.alert.port}")
//	private String alert_port;
//	@Value("${node.bitcoind.notification.block.port}")
//	private String block_port;
//	@Value("${node.bitcoind.notification.wallet.port}")
//	private String wallet_port;
	
	public static CloseableHttpClient getHttpProvider() {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(connManager)
				.build();
		return httpProvider;
	}
	
	public static BtcdClient getBtcdProvider() throws BitcoindException, CommunicationException, 
			IOException {
		return new BtcdClientImpl(getHttpProvider(), getNodeConfig());
	}
	
	public static Properties getNodeConfig() throws IOException {
		Properties nodeConfig = new Properties();
//		nodeConfig.put("node.bitcoind.rpc.protocol", rpc_protocol);
//		nodeConfig.put("node.bitcoind.rpc.host", rpc_host);
//		nodeConfig.put("node.bitcoind.rpc.port", rpc_port);
//		nodeConfig.put("node.bitcoind.rpc.user", rpc_user);
//		nodeConfig.put("node.bitcoind.rpc.password", rpc_password);
//		nodeConfig.put("node.bitcoind.http.auth_scheme", auth_schema);
//		nodeConfig.put("node.bitcoind.notification.alert.port", alert_port);
//		nodeConfig.put("node.bitcoind.notification.block.port", block_port);
//		nodeConfig.put("node.bitcoind.notification.wallet.port", wallet_port);
		String file = "/home/workspace/neo4j_home/node_config.properties";
		File f = new File(file);
		if(!f.exists()){
		//	f = new File("src/main/resources/node_config.properties");
			f = new File("D:\\workspace_lkp\\neo4j-client\\src\\main\\resources\\node_config.properties");
		}
		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				f));
		nodeConfig.load(inputStream);
		inputStream.close();
		return nodeConfig;
	}
}