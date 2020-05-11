package com.xyz.testbench.app.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xyz.testbench.app.modular.system.model.Log;

public interface LogMapper extends BaseMapper<Log> {

    int insertLog(Log log);

}
