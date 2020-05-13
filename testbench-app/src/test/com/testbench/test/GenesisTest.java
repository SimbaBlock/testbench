package com.testbench.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.testbench.app.core.rpc.Api;
import com.testbench.app.core.rpc.CommonTxOputDto;
import com.testbench.app.core.rpc.TxInputDto;
import com.testbench.app.core.util.HttpUtil;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GenesisTest {

    // 创建genesis
    @Test
    public void createGenesis() throws Exception {

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
        CommonTxOputDto txOutputDto1 = new CommonTxOputDto(address, cost,"06534c502b2b000202010747454e455349530341424323546574686572204c74642e20555320646f6c6c6172206261636b656420746f6b656e734168747470733a2f2f7465746865722e746f2f77702d636f6e74656e742f75706c6f6164732f323031362f30362f546574686572576869746550617065722e70646620db4451f11eda33950670aaf59e704da90117ff7057283b032cfaec77793139160108010108002386f26fc10000",1);
        CommonTxOputDto txOutputDto2 = new CommonTxOputDto(address, value,2); // 找零
        output.add(txOutputDto1);
        output.add(txOutputDto2);

        String hex = Api.CreateSlpppTransaction(inputList, output);

        String signHex = Api.SignRawTransaction(hex);

        String txid = Api.SendRawTransaction(signHex);

        System.out.println(txid);

    }



}
