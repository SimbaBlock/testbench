package com.xyz.testbench.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyz.testbench.app.core.rpc.Api;
import com.xyz.testbench.app.core.rpc.CommonTxOputDto;
import com.xyz.testbench.app.core.rpc.TxInputDto;
import com.xyz.testbench.app.core.util.HttpUtil;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendTest {

    // 创建转账
    @Test
    public void createSend() throws Exception {

        List<TxInputDto> inputList = new ArrayList<>();
        List<CommonTxOputDto> output = new ArrayList<>();

        // 地址1
        String address1 = "13e3v3E2CYzaiFWhL3iKMpiuYPXiGNbpAc";

        // 地址2
        String Address2 = "16jJiqM1KgwxaDWXzLHrEA7uybgqNKm233";

        Map<String, String> query = new HashedMap();
        query.put("address", "1cf213345cf00bbe452da214d5e22d09fe895fc9");

        String reulst = HttpUtil.doPost("http://47.110.137.123:8433/rest/Api/getTokenUtxo", query);

        BigInteger toAmount = new BigInteger("5000");

        Map<String, String> tokenQuery = new HashedMap();
        tokenQuery.put("tokenId", "c588722dc910d42d39cb5c832e2bc56e1549469f83278f714e7ecc4bc40a7825");
        tokenQuery.put("address", address1);
        String tokenReulst = HttpUtil.doPost("http://47.110.137.123:8433/rest/Api/queryToken", tokenQuery);

        JSONObject tokenOB = (JSONObject) JSONObject.parse(tokenReulst);

        JSONObject token = tokenOB.getJSONObject("data");

        Integer precition = token.getInteger("precition");                      // 精度
        BigInteger tokenBalance = token.getBigInteger("token");                 // token余额
        BigInteger balance = new BigInteger("0");

        if (tokenBalance.compareTo(new BigInteger("0")) > 0) {
             balance = balance.add(tokenBalance);
        }

        BigInteger newAmout = balance.subtract(toAmount);


        JSONObject ob = (JSONObject) JSONObject.parse(reulst);

        JSONObject data = ob.getJSONObject("data");

        JSONArray utxos = data.getJSONArray("utxo");

        BigDecimal sumValue = new BigDecimal("0");

        for (Object o : utxos) {

            JSONObject utxo = (JSONObject) o;

            String txid = utxo.getString("txid");
            Integer n = utxo.getInteger("n");
            BigDecimal value = new BigDecimal(utxo.getString("value"));
            sumValue = sumValue.add(value);
            TxInputDto TxInputDto = new TxInputDto(txid, n, "");
            inputList.add(TxInputDto);

        }

        BigDecimal fee = new BigDecimal("0.000005");
        BigDecimal cost = new BigDecimal("0.00001");
        BigDecimal value = sumValue.subtract(fee).subtract(cost).subtract(cost);                                //newAmout.toString(16)
        CommonTxOputDto txOutputDto1 = new CommonTxOputDto(Address2, cost,"06534c502b2b000202010453454e4420c588722dc910d42d39cb5c832e2bc56e1549469f83278f714e7ecc4bc40a782508"+"0000000000001388",1);
        CommonTxOputDto txOutputDto2 = new CommonTxOputDto(address1, cost,"06534c502b2b000202010453454e4420c588722dc910d42d39cb5c832e2bc56e1549469f83278f714e7ecc4bc40a782508"+"00470de4df81ec78",1);        // token找零

        CommonTxOputDto txOutputDto3 = new CommonTxOputDto(address1, value,2); // 找零
        output.add(txOutputDto1);
        output.add(txOutputDto2);
        output.add(txOutputDto3);

        String hex = Api.CreateSlpppTransaction(inputList, output);

        String signHex = Api.SignRawTransaction(hex);

        String txid = Api.SendRawTransaction(signHex);

        System.out.println(txid);

    }

}
