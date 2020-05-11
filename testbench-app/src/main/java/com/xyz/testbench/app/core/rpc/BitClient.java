package com.xyz.testbench.app.core.rpc;

import com.alibaba.fastjson.JSONObject;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRpcException;
import wf.bitcoin.krotjson.Base64Coder;
import wf.bitcoin.krotjson.JSON;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;


public class BitClient {

	private URL noAuthURL;

	private HostnameVerifier hostnameVerifier = null;

	private SSLSocketFactory sslSocketFactory = null;

	private String authStr;

	public final URL rpcURL;

	public static final Charset QUERY_CHARSET = Charset.forName("ISO8859-1");

	public BitClient(URL rpc) {
		this.rpcURL = rpc;
		try {
			noAuthURL = new URI(rpc.getProtocol(), null, rpc.getHost(), rpc.getPort(), rpc.getPath(), rpc.getQuery(),
					null).toURL();
		} catch (MalformedURLException | URISyntaxException ex) {
			throw new IllegalArgumentException(rpc.toString(), ex);
		}
		authStr = rpc.getUserInfo() == null ? null
				: String.valueOf(Base64Coder.encode(rpc.getUserInfo().getBytes(Charset.forName("ISO8859-1"))));
	}

	public Object query(String method, Object... o) {
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) noAuthURL.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);

			if (conn instanceof HttpsURLConnection) {
				if (hostnameVerifier != null)
					((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
				if (sslSocketFactory != null)
					((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
			}

			((HttpURLConnection) conn).setRequestProperty("Authorization", "Basic " + authStr);
			byte[] r = prepareRequest(method, o);
			conn.getOutputStream().write(r);
			conn.getOutputStream().close();
			int responseCode = conn.getResponseCode();
			if (responseCode == 500) {
//				throw new BitcoinRPCException(method, Arrays.deepToString(o), responseCode, conn.getResponseMessage(),
//						new String(loadStream(conn.getErrorStream(), true)));
				String a = new String(loadStream(conn.getErrorStream(), true));
				JSONObject error = (JSONObject) JSONObject.parse(a);
				JSONObject data = error.getJSONObject("error");
				Integer code = data.getInteger("code");
				String message = data.getString("message");

			}
			return loadResponse(conn.getInputStream(), "1", true);
		} catch (IOException ex) {
			throw new BitcoinRPCException(method, Arrays.deepToString(o), ex);
		}
	}

	public byte[] prepareRequest(final String method, final Object... params) {
		return JSON.stringify(new LinkedHashMap() {
			{
				put("method", method);
				put("params", params);
				put("id", "1");
			}
		}).getBytes(QUERY_CHARSET);
	}

	public Object loadResponse(InputStream in, Object expectedID, boolean close)
			throws IOException, BitcoinRpcException {
		try {
			String r = new String(loadStream(in, close), QUERY_CHARSET);
			try {
				Map response = (Map) JSON.parse(r);

				if (!expectedID.equals(response.get("id")))
					throw new BitcoinRPCException("Wrong response ID (expected: " + String.valueOf(expectedID)
							+ ", response: " + response.get("id") + ")");

				if (response.get("error") != null)
					throw new BitcoinRpcException(JSON.stringify(response.get("error")));

				return response.get("result");
			} catch (ClassCastException ex) {
				throw new BitcoinRPCException("Invalid server response format (data: \"" + r + "\")");
			}
		} finally {
			if (close)
				in.close();
		}
	}

	private static byte[] loadStream(InputStream in, boolean close) throws IOException {
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (;;) {
			int nr = in.read(buffer);

			if (nr == -1)
				break;
			if (nr == 0)
				throw new IOException("Read timed out");

			o.write(buffer, 0, nr);
		}
		return o.toByteArray();
	}

	public String createRawTransaction(List<TxInputDto> inputs, List<TxOutputDto> outputs) throws BitcoinRpcException {
		List<Map> pInputs = new ArrayList<>();

		for (final TxInputDto txInput : inputs) {
			pInputs.add(new LinkedHashMap() {
				{
					put("txid", txInput.txid());
					put("vout", txInput.vout());
				}
			});
		}

		Map<String, Object> pOutputs = new LinkedHashMap();

//		Double oldValue;
		for (TxOutputDto txOutput : outputs) {
			if (txOutput.getAddress() != null)
				pOutputs.put(txOutput.getAddress(), txOutput.getAmount());
            else if ("data".equals(txOutput.data())) {
				pOutputs.put(txOutput.data(), (String) txOutput.content());
			} else if (txOutput.getOpreturn() != null){
				pOutputs.put("data", txOutput.getOpreturn());
			}
//            else {
//				pOutputs.put(txOutput.data(), BitcoinUtil.normalizeAmount((Double) txOutput.content()));
//			}
		}

//		for (TxOutputDto txOutput : outputs) {
//			if ("data".equals(txOutput.data())) {
//				pOutputs.put(txOutput.data(), (String) txOutput.content());
//			} else {
//				pOutputs.put(txOutput.data(), BitcoinUtil.normalizeAmount((Double) txOutput.content()));
//			}
//		}
//		createslprawtransaction
		return (String) query("createrawtransaction", pInputs, pOutputs);

	}




	public String CreateContractTransaction(List<TxInputDto> inputs, List<CommonTxOputDto> outputs) throws BitcoinRpcException {
		List<Map> pInputs = new ArrayList<>();

		for (final TxInputDto txInput : inputs) {
			pInputs.add(new LinkedHashMap() {
				{
					put("txid", txInput.txid());
					put("vout", txInput.vout());
				}
			});
		}

		Map<String, Object> pOutputs = new LinkedHashMap();

		for (CommonTxOputDto contractTxOputDto : outputs) {
			if (contractTxOputDto.getType() == 1) {
				pOutputs.put("address", contractTxOputDto.getAddress());
				pOutputs.put("amount", contractTxOputDto.getAmount());
				pOutputs.put("script", contractTxOputDto.getScript());
			} else if (contractTxOputDto.getType() == 2) {
				pOutputs.put("address", contractTxOputDto.getAddress());
				pOutputs.put("amount", contractTxOputDto.getAmount());
			} else if (contractTxOputDto.getType() == 3){
				pOutputs.put("data", contractTxOputDto.getData());
			}
		}


		return (String) query("createcontracttransaction", pInputs, pOutputs);

	}


	public String CreateSlpppTransaction(List<TxInputDto> inputs, List<CommonTxOputDto> outputs) throws BitcoinRpcException {
		List<Map> pInputs = new ArrayList<>();

		for (final TxInputDto txInput : inputs) {
			pInputs.add(new LinkedHashMap() {
				{
					put("txid", txInput.txid());
					put("vout", txInput.vout());
				}
			});
		}

		List<Map> pOutputs = new ArrayList<>();

		for (CommonTxOputDto contractTxOputDto : outputs) {
			pOutputs.add(new LinkedHashMap() {
				{
					if(contractTxOputDto.getType()==1) {
						put("address", contractTxOputDto.getAddress());
						put("amount", contractTxOputDto.getAmount());
						put("script", contractTxOputDto.getScript());
					} else if(contractTxOputDto.getType()==2) {
						put("address", contractTxOputDto.getAddress());
						put("amount", contractTxOputDto.getAmount());
					} else if(contractTxOputDto.getType()==3){
						put("data", contractTxOputDto.getData());
					}
				}
			});
		}


		return (String) query("createslppptransaction", pInputs, pOutputs);

	}


	// 签名认证
	public String signRawTransaction(String hex) throws BitcoinRpcException{
		return signRawTransaction1(hex);
	}

	public String signRawTransaction1(String hex) {
//		signrawtransaction
		Map result = (Map) query("signslppptransaction", hex); // if sigHashType is
																// null it will return
																// the default "ALL"
		//(Boolean) result.get("complete")
//		if ((Boolean) result.get("complete"))
			return (String) result.get("hex");
//		else
//			throw new BitcoinRpcException("Incomplete");
	}

	public String sendRawTransaction(String hex) throws BitcoinRpcException {
		return (String) query("sendrawtransaction", hex);
	}

	public String getRawTransactionHex(String txid, Boolean format) throws BitcoinRpcException {
		return JSON.stringify(query("getrawtransaction", txid, format));
	}

	public String decodeRawTransaction(String hex) throws BitcoinRpcException {
		return JSON.stringify(query("decoderawtransaction", hex));
	}

	public String getNewAddress() throws BitcoinRpcException {
		return (String) query("getnewaddress");
	}

	public String signMessage(String bitcoinAdress, String message) throws BitcoinRpcException {
	    return (String) query("signmessage", bitcoinAdress, message);
	}

	public boolean verifyMessage(String bitcoinAddress, String signature, String message) throws BitcoinRpcException {
	    return (boolean) query("verifymessage", bitcoinAddress, signature, message);
	}

	public String getBlock(String blockHash) {
		return JSON.stringify(query("getblock", blockHash));
	}

	public String validateAddress(String address) {
		 return JSON.stringify(query("validateaddress", address));
	}

}
