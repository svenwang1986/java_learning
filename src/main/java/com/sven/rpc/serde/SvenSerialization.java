package com.sven.rpc.serde;

public interface SvenSerialization {


    <T> byte[] serialize(T obj);

    <T> T deSerizlize(byte[] data, Class<T> clz);

}
