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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MintTest {

    // 创建增发
    @Test
    public void createMint() throws Exception {

        List<TxInputDto> inputList = new ArrayList<>();
        List<CommonTxOputDto> output = new ArrayList<>();

        String address = "13e3v3E2CYzaiFWhL3iKMpiuYPXiGNbpAc";

        Map<String, String> query = new HashedMap();
        query.put("address", address);

        String reulst = HttpUtil.doPost("http://47.110.137.123:8433/rest/Api/getUtxo", query);

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
        BigDecimal cost = new BigDecimal("0.00002");
        BigDecimal value = sumValue.subtract(fee).subtract(cost);
        CommonTxOputDto txOutputDto1 = new CommonTxOputDto(address, cost,"06534c502b2b00020201044d494e5420c588722dc910d42d39cb5c832e2bc56e1549469f83278f714e7ecc4bc40a7825010108002386f26fc10000",1);
        CommonTxOputDto txOutputDto2 = new CommonTxOputDto(address, value,2); // 找零
        output.add(txOutputDto1);
        output.add(txOutputDto2);

        String hex = Api.CreateSlpppTransaction(inputList, output);
        String signHex = Api.SignRawTransaction(hex);
        String txid = Api.SendRawTransaction(signHex);

        System.out.println(txid);

    }



    // 创建非权限地址增发测试
    @Test
    public void createNotMintAuthority() throws Exception {

        List<TxInputDto> inputList = new ArrayList<>();
        List<CommonTxOputDto> output = new ArrayList<>();

        String address = "16nsgp2UFViMa3JQfpFfAjuWvRwXjt4Dws";

        Map<String, String> query = new HashedMap();
        query.put("address", address);

        String reulst = HttpUtil.doPost("http://47.110.137.123:8433/rest/Api/getUtxo", query);

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
        BigDecimal cost = new BigDecimal("0.00002");
        BigDecimal value = sumValue.subtract(fee).subtract(cost);
        CommonTxOputDto txOutputDto1 = new CommonTxOputDto(address, cost,"06534c502b2b00020201044d494e5420c588722dc910d42d39cb5c832e2bc56e1549469f83278f714e7ecc4bc40a7825010108002386f26fc10000",1);
        CommonTxOputDto txOutputDto2 = new CommonTxOputDto(address, value,2); // 找零
        output.add(txOutputDto1);
        output.add(txOutputDto2);

        String hex = Api.CreateSlpppTransaction(inputList, output);
        String signHex = Api.SignRawTransaction(hex);
        String txid = Api.SendRawTransaction(signHex);

        System.out.println(txid);

    }



}
