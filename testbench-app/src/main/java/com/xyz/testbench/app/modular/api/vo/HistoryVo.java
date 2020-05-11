package com.xyz.testbench.app.modular.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HistoryVo implements Serializable {

    private Long time;

    private String business;

    private String change;

    private String balance;


}
