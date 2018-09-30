package com.skyguard.zmq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    private static final String DATA_PATH = "/opt/zmq/data";

    public static int getPort(String path) throws IOException {

        if(Files.notExists(Paths.get(DATA_PATH))){
            Files.createDirectories(Paths.get(DATA_PATH));
        }

        String fileName = DATA_PATH+"/"+path;
        if(Files.notExists(Paths.get(fileName))){
            Files.createFile(Paths.get(fileName));
        }

        FileInputStream inputStream = null;
        FileChannel channel = null;

        try {
            inputStream = new FileInputStream(fileName);
            channel = inputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)Files.size(Paths.get(fileName)));
            channel.read(byteBuffer);
            String str = new String(byteBuffer.array(),"utf-8");
            Long port = Long.parseLong(str);
            int serverPort = port.intValue();
            serverPort++;
            setPort(serverPort,fileName);
            return serverPort;
        }catch (Exception e){
            LOG.error("get file error",e);
        }finally {
            if(inputStream!=null){
                inputStream.close();
            }
            channel.close();
        }

        return 0;
    }

    public static void setPort(int port,String path) throws IOException {

        if(port>0){

            if(Files.notExists(Paths.get(DATA_PATH))){
                Files.createDirectories(Paths.get(DATA_PATH));
            }

            if(Files.notExists(Paths.get(path))){
                Files.createFile(Paths.get(path));
            }

            FileOutputStream outputStream = null;
            FileChannel channel = null;

            try {
                outputStream = new FileOutputStream(path);
                channel = outputStream.getChannel();
                String str = String.valueOf(port);
                channel.write(ByteBuffer.wrap(str.getBytes()));
            }catch (Exception e){
                LOG.error("get file error",e);
            }finally {
                if(outputStream!=null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {

                    }
                }
                try {
                    channel.close();
                } catch (IOException e) {

                }
            }


        }

    }







}
