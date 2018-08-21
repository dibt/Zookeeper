package com.di.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 工作服务器信息
 */
@Data
public class RunningData implements Serializable{
    private static final long serialVersionUID = 4260577459043203630L;
    private Long cid;
    private String name;
}
