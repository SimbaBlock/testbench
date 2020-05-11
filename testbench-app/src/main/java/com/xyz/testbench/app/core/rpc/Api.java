package com.xyz.testbench.app.core.rpc;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.Unspent;

import java.net.URL;
import java.util.List;

@Component
public class Api {

	private static String user = "dev";

	private static String password = "a";

	private static String host = "47.110.137.123";

	private static String port = "8332";


	/**
	 * 返回钱包内的UTXO
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static JSONObject ListUnspent(String address) throws Exception {

		JSONObject json = new JSONObject();
		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitcoinJSONRPCClient bitcoinClient = new BitcoinJSONRPCClient(url);
		List<Unspent> list = bitcoinClient.listUnspent(0, 9999, address);
		for (Unspent unspent : list) {
			json.put("txid", unspent.txid());
			json.put("account", unspent.amount());
			json.put("address", unspent.address());
			json.put("vout", unspent.vout());
		}
		return json;

	}

	/**
	 * 返回一个新的地址用于接收支付
	 * @return
	 * @throws Exception
	 */
	public static String GetNewAddress() throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient BitClient = new BitClient(url);
		String address = BitClient.getNewAddress();
		return address;

	}

	/**
	 * 创建未签名的序列化交易
	 * @param input
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public static String CreateRawTransaction(List<TxInputDto> input, List<TxOutputDto> output) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String relust = bitClient.createRawTransaction(input, output);
		return relust;

	}

	/**
	 * 签名裸交易
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static String SignRawTransaction(String hex) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String relust = bitClient.signRawTransaction(hex);
		return relust;

	}

	/**
	 * 验证并发送裸交易到P2P网络
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static String SendRawTransaction(String hex) throws  Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String relust = bitClient.sendRawTransaction(hex);
		return relust;

	}

	/**
	 * 返回指定的裸交易
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static JSONObject GetRawTransaction(String hex) throws Exception {

		JSONObject json = new JSONObject();
		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		json = (JSONObject) JSONObject.parse(bitClient.getRawTransactionHex(hex, true));
		return json;

	}


	/**
	 * 解析裸交易
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static JSONObject DecodeRawTransaction(String hex) throws Exception {

		JSONObject json = new JSONObject();
		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String a = bitClient.decodeRawTransaction(hex);
		json = (JSONObject) JSONObject.parse(a);
		return json;

	}

	/**
	 * 签名消息
	 * @param address
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String SignMessage(String address, String message) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String relust = bitClient.signMessage(address, message);
		return relust;

	}

	/**
	 * 验证签名的消息
	 * @param address
	 * @param sign
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static Boolean VerifyMessage(String address, String sign, String message) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		Boolean flag = bitClient.verifyMessage(address, sign, message);
		return flag;

	}

//	/**
//	 * 获取指定哈希的区块
//	 * @param
//	 * @return
//	 * @throws Exception
//	 */
//	public static wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.Block GetBlock(String blockHash) throws Exception {
//
//		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
//		BitcoinJSONRPCClient bitcoinClient = new BitcoinJSONRPCClient(url);
//		Block JSON = bitcoinClient.getBlock(blockHash);
//		return JSON;
//
//	}

	/**
	 * 获取指定哈希的区块
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static JSONObject GetBlock(String blockHash) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		JSONObject relust = (JSONObject)JSONObject.parse(bitClient.getBlock(blockHash));
		return relust;

	}

	/**
	 * 获取指定高度区块的哈希
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public static String GetBlockHash(Integer height) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitcoinJSONRPCClient bitcoinClient = new BitcoinJSONRPCClient(url);
		String blockHash = bitcoinClient.getBlockHash(height);
		return blockHash;

	}

	/**
	 * 获取区块数量
	 * @return
	 * @throws Exception
	 */
	public static int GetBlockCount() throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitcoinJSONRPCClient bitcoinClient = new BitcoinJSONRPCClient(url);
		int blockCount = bitcoinClient.getBlockCount();
		return blockCount;

	}

	/**
	 * 获取交易池详情
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static List<String> GetRawMemPool() throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitcoinJSONRPCClient bitcoinClient = new BitcoinJSONRPCClient(url);
		List<String>  blockHash = bitcoinClient.getRawMemPool();
		return blockHash;

	}

	/**
	 * 地址hash
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static JSONObject ValidateAddress(String address) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		JSONObject relust = (JSONObject)JSONObject.parse(bitClient.validateAddress(address));
		return relust;

	}

	/**
	 * 创建两个地址都可以花费的交易
	 * @param input
	 * @param output
	 * @return 		createcontracttransaction
	 * @throws Exception
	 */
	public static String CreateContractTransaction(List<TxInputDto> input, List<CommonTxOputDto> output) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String relust = bitClient.CreateContractTransaction(input, output);
		return relust;

	}

	/**
	 * 创建单地址可以花费的交易
	 * @param input
	 * @param output
	 * @return 		createslppptransaction
	 * @throws Exception
	 */
	public static String CreateSlpppTransaction(List<TxInputDto> input, List<CommonTxOputDto> output) throws Exception {

		URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
		BitClient bitClient = new BitClient(url);
		String relust = bitClient.CreateSlpppTransaction(input, output);
		return relust;

	}

}
