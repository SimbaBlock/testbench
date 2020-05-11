package com.xyz.testbench.app.modular.system.service;

import com.xyz.testbench.app.modular.system.model.PayLog;


public interface PayLogService {

    PayLog findByUidAndCode(String uid, String code);

    int inserPayLog(PayLog payLog);

}
