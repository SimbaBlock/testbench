package com.xyz.testbench.app.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xyz.testbench.app.modular.system.model.Log;

public interface LogService extends IService<Log> {

    int insertLog(Log log);

}
