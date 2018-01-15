package com.lkp.btcdcli4j.client;

import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;

import com.lkp.btcdcli4j.util.BlockResourceUtils;
import com.neemre.btcdcli4j.core.client.BtcdClient;

/**A list of examples demonstrating the use of <i>bitcoind</i>'s deprecated RPCs (via the JSON-RPC 
 * API).*/
public class DeprecatedApi {

	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpProvider = BlockResourceUtils.getHttpProvider();
		Properties nodeConfig = BlockResourceUtils.getNodeConfig();
		BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
		
		client.getHashesPerSec();
	}
}
