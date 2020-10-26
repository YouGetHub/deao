package com.common;

import java.io.File;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 文件上传下载工具类
 */
public class AppFileUtils {

    //文件上传的保存路径
    public static  String UPLOAD_PATH="C:/upload/";//默认值

    /**
     * 根据文件老名字得到新名字
     * @param oldName
     * @return
     */
    public static String createNewFileName(String oldName) {
        String stuff=oldName.substring(oldName.lastIndexOf("."), oldName.length());
        return IdUtil.simpleUUID().toUpperCase()+stuff;
    }

    /**
     * 文件下载
     * @param path
     * @return
     */
    public static ResponseEntity<Object> createResponseEntity(String path) {
        //1,构造文件对象
        File file=new File(UPLOAD_PATH, path);
        if(file.exists()) {
            //将下载的文件，封装byte[]
            byte[] bytes=null;
            try {
                bytes =FileUtil.readBytes(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //创建封装响应头信息的对象
            HttpHeaders header=new HttpHeaders();
            //封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //设置下载的文件的名称
            //创建ResponseEntity对象
            ResponseEntity<Object> entity=
                    new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
            return entity;
        }
        return null;
    }

    /**
     * 根据路径删除图片
     * @param oldPath
     */
    public static void removeFileByPath(String oldPath) {
            File file=new File(UPLOAD_PATH, oldPath);
            if(file.exists()) {
                file.delete();
            }
    }
}