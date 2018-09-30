package com.skyguard.zmq.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoUtil {

    private static KryoPool kryoPool = new KryoPool.Builder(()->{
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setDefaultSerializer(JavaSerializer.class);
        kryo.setRegistrationRequired(false);
        return kryo;
    }).build();


    public static byte[] serialize(Object obj){

        return kryoPool.run((kryo)->{
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Output output = new Output(bos);
                kryo.writeClassAndObject(output, obj);
                return bos.toByteArray();
            }catch(Exception e){
                return null;
            }
        });

    }

    public static Object deserialize(byte[] bytes){


        Object result = kryoPool.run((kryo)->{
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                kryo = kryoPool.borrow();
                Input input = new Input(bis);
                return kryo.readClassAndObject(input);
            }catch(Exception e){
                return null;
            }
        });

        return result;
    }




}
