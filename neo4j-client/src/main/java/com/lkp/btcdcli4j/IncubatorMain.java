package com.lkp.btcdcli4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.http.impl.client.CloseableHttpClient;

import com.lkp.btcdcli4j.util.BlockResourceUtils;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.neemre.btcdcli4j.daemon.BtcdDaemon;
import com.neemre.btcdcli4j.daemon.BtcdDaemonImpl;
import com.neemre.btcdcli4j.daemon.event.AlertListener;
import com.neemre.btcdcli4j.daemon.event.BlockListener;
import com.neemre.btcdcli4j.daemon.event.WalletListener;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.PeerNode;
import com.neemre.btcdcli4j.core.domain.Transaction;
import com.neemre.btcdcli4j.core.http.client.SimpleHttpClient;
import com.neemre.btcdcli4j.core.http.client.SimpleHttpClientImpl;
import com.neemre.btcdcli4j.core.jsonrpc.JsonPrimitiveParser;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClient;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClientImpl;
import com.neemre.btcdcli4j.core.util.CollectionUtils;
import com.neemre.btcdcli4j.core.util.StringUtils;

/**Please ignore this class - it's just a quick mock-up {@code main} for ironing out bugs/trying 
 * new features.*/
public class IncubatorMain {

	public static void main(String[] args) throws Exception {
		JsonPrimitiveParser parser = new JsonPrimitiveParser();
		System.out.println(parser.parseString("\"nul\"\""));

		String id = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.printf("Sample JSON-RPC request ID (unique): '%s'\n", id);

		List<String> listA = new ArrayList<String>();
		listA.add("listA: I like bitcoin");
		listA.add("listA: I like litecoin");
		listA.add("listA: I like dogecoin");
		List<String> listB = new ArrayList<String>();
		listB.add("listB: I like bitcoin");
		listB.add("listB: I like litecoin");
		listB.add("listB: I like dogecoin");
		List<String> listC = new ArrayList<String>();
		listC.add("listC: I like bitcoin");
		listC.add("listC: I like litecoin");
		listC.add("listC: I like dogecoin");
		List<String> listD = new ArrayList<String>();
		listD.add("listD: I like bitcoin");
		listD.add("listD: I like litecoin");
		listD.add("listD: I like dogecoin");

		System.out.printf("CollectionUtils.equalsSize(..) result is: '%s'\n", 
				CollectionUtils.equalsSize(listA, listB, listC, listD, null));
		System.out.printf("CollectionUtils.mergeInterlaced(..) result is: '%s'\n",
				CollectionUtils.mergeInterlaced(listA, listB, listC, listD));

		String[] arrayA = new String[3];
		arrayA[0] = "I (A)";
		arrayA[1] = "love (A)";
		arrayA[2] = "liquorice! (A)";
		String[] arrayB = new String[3];
		arrayB[0] = "I (B)";
		arrayB[1] = "love (B)";
		arrayB[2] = "liquorice! (B)";
		System.out.println(CollectionUtils.mergeInterlaced(Arrays.asList(arrayA), Arrays.asList(arrayB)));

		CloseableHttpClient httpProvider = BlockResourceUtils.getHttpProvider();
		Properties nodeConfig = BlockResourceUtils.getNodeConfig();
		JsonRpcClient rpcClient = new JsonRpcClientImpl(httpProvider, nodeConfig);
		String peerInfoJson = "[{\"id\":1093,\"addr\":\"194.71.109.94:8333\",\"addrlocal\":\"46.166." 
				+ "161.166:37578\",\"services\":\"0000000000000001\",\"lastsend\":1424883192,\"lastre"
				+ "cv\":1424883192,\"bytessent\":284293,\"bytesrecv\":10845237,\"conntime\":142487870"
				+ "6,\"pingtime\":0.24801400,\"version\":70001,\"subver\":\"/Satoshi:0.8.5/\",\"inbou"
				+ "nd\":false,\"startingheight\":345108,\"banscore\":0,\"synced_headers\":345116,\"sy"
				+ "nced_blocks\":345116,\"inflight\":[345112,345113,345114],\"whitelisted\":false},{"
				+ "\"id\":1094,\"addr\":\"128.199.254.244:8333\",\"addrlocal\":\"46.166.161.166:37662"
				+ "\",\"services\":\"0000000000000001\",\"lastsend\":1424883192,\"lastrecv\":14248831"
				+ "92,\"bytessent\":280158,\"bytesrecv\":10365354,\"conntime\":1424878774,\"pingtime"
				+ "\":0.58703400,\"version\":70002,\"subver\":\"/Satoshi:0.10.0/\",\"inbound\":false,"
				+ "\"startingheight\":345108,\"banscore\":0,\"synced_headers\":345114,\"synced_blocks"
				+ "\":345114,\"inflight\":[345109,345110,34511],\"whitelisted\":false}]";
		List<PeerNode> peerInfo = rpcClient.getMapper().mapToList(peerInfoJson, PeerNode.class);
		System.out.println("Sample 'peerInfo' as POJOs: " + peerInfo);

		//System.out.println("Testing 'CollectionUtils.asMap(..)' #1: " + CollectionUtils.asMap(
		//		"addressA", new BigDecimal("0.03"), "addressB", new BigDecimal("0.05"), "addressC"));
		System.out.println("Testing 'CollectionUtils.asMap(..)' #2: " + CollectionUtils.asMap(
				"addressA", new BigDecimal("0.03"), "addressB", new BigDecimal("0.05"), "addressC", 
				new BigDecimal("0.09")));

		SimpleHttpClient httpClient = new SimpleHttpClientImpl(httpProvider, nodeConfig);
		//httpClient.execute(HttpConstants.REQ_METHOD_GET, "This should throw an exception because "
		//		+ "it's a HTTP 'GET' request (currently not supported).");

		System.out.println(new BitcoindException(334, "'I am a Bitcoin-specific exception!'"));

		String nodeVersion = "60200"; //0.6.2
		String nodeVersion1 = "80400"; //0.8.4
		String nodeVersion2 = "100000"; //0.10.0
		String nodeVersion3 = "1080500"; //1.8.5

		System.out.printf("'StringBuilder#toString()' on empty 'StringBuilder' gives: '%s'\n", 
				new StringBuilder().toString());
		System.out.printf("'nodeVersion' was: '%s'\n", StringUtils.pad(nodeVersion, 8, '0', true));
		System.out.printf("'nodeVersion1' was: '%s'\n", StringUtils.pad(nodeVersion1, 8, '0', true));
		System.out.printf("'nodeVersion3' was: '%s'\n", StringUtils.pad(nodeVersion3, 8, '0', true));		
		System.out.printf("'Testing 'StringUtils.split(..)' #1: '%s'\n", 
				StringUtils.split("0123456789abcdef", 3));
		String decodedVersion = StringUtils.join(CollectionUtils.mergeInterlaced(
				StringUtils.split(StringUtils.pad(nodeVersion2, 8, '0', true), 2), 
				CollectionUtils.duplicate(".", 4)));
		System.out.printf("'decodedVersion' was: '%s'\n", decodedVersion);

		final BtcdClient client = new BtcdClientImpl(httpProvider, nodeConfig);
		final BtcdDaemon daemon = new BtcdDaemonImpl(client);
		daemon.addAlertListener(new AlertListener() {
			@Override
			public void alertReceived(String alert) {
				System.out.printf("New alert received! (Event details: '%s')\n", alert);
			}
		});
		daemon.addBlockListener(new BlockListener() {
			@Override
			public void blockDetected(Block block) {
				System.out.printf("New block detected! (Event details: '%s')\n", block);
			}
		});
		daemon.addWalletListener(new WalletListener() {
			@Override
			public void walletChanged(Transaction transaction) {
				System.out.printf("Wallet transaction changed! (Event details: '%s')\n", 
						transaction);
			}
		});
	}
}