package com.xyz.testbench.app.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xyz.testbench.app.modular.system.model.Access;

public interface AccessService extends IService<Access> {

    Access findByAccessKey(String accesskey);

}
