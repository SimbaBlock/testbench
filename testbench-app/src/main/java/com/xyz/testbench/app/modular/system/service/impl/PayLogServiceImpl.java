package com.xyz.testbench.app.modular.system.service.impl;

import com.xyz.testbench.app.modular.system.dao.PayLogMapper;
import com.xyz.testbench.app.modular.system.model.PayLog;
import com.xyz.testbench.app.modular.system.service.PayLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class PayLogServiceImpl implements PayLogService {

    @Resource
    private PayLogMapper payLogMapper;

    @Override
    public PayLog findByUidAndCode(String uid, String code) {
        return payLogMapper.findByUidAndCode(uid, code);
    }

    @Override
    public int inserPayLog(PayLog payLog) {
        return payLogMapper.inserPayLog(payLog);
    }

}
