package com.di.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * node存储的信息
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String name;
}
