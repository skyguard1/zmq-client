package com.skyguard.zmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ZmqConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ZmqConfig.class);

    private static Properties properties = new Properties();



    public static String getValue(String key){

        URL url = ZmqConfig.class.getResource("/");
        String filePath = url.getPath();



        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath+"/zmq-config.properties"));
            properties.load(inputStream);
            String value = properties.getProperty(key);
            return value;
        }catch(Exception e){
            LOG.error("get file error",e);
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                }catch(Exception e){

                }
            }
        }

        return null;
    }




}
