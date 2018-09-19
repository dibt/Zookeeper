package com.di.enums;

import lombok.Getter;


@Getter
public enum LockStatus {
    LOCK(0,"获取锁"),
    UNLOCK(1,"释放锁"),
    TRYLOCK(2,"尝试获取锁");

    private Integer code;

    private String message;

     LockStatus(Integer code,String message){
         this.code = code;
         this.message = message;
     }



}
