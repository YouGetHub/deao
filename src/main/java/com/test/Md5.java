package com.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.common.AppFileUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Shiro 加密工具类
 */
public class Md5 {

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123", "1123", 2);

        String s = md5Hash.toHex();
        System.out.println(s);
    }
    private String salt;
    private String pwd;

    public Md5(String pwd) {
        this.pwd = pwd;
    }

    public Md5(String salt, String pwd) {
        this.salt = salt;
        this.pwd = pwd;
    }

    public Md5() {
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    // 加密
    public Md5 setMd5(String md5,String pwd,int hashIterations){
        SimpleHash simpleHash = new SimpleHash(md5,pwd,IdUtil.simpleUUID().toUpperCase(),hashIterations);
        return new Md5(IdUtil.simpleUUID().toUpperCase(),simpleHash.toString());
    }

    // 加密
    public Md5 setMd5(String md5,String pwd,String salt,int hashIterations){
        SimpleHash simpleHash = new SimpleHash(md5,pwd,salt,hashIterations);
        return new Md5(simpleHash.toString());
    }

}
