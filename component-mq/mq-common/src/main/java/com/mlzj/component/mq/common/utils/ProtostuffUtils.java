package com.mlzj.component.mq.common.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author yhl
 * @date 2020/5/15
 */
public class ProtostuffUtils {


    /**
     * buffer空间大小
     */
    private static final Integer BUFFER_SIZE = 1024;
    /**
     * linkedBuffer
     */
    private static final ThreadLocal<LinkedBuffer> BUFFER = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(BUFFER_SIZE));

    private static LinkedBuffer getBuffer() {
        return BUFFER.get().clear();
    }

    /**
     * 序列化方法，把指定对象序列化成字节数组
     *
     * @param obj 对象
     * @param <T> 对象的泛型
     * @return 字节数组
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        byte[] data;
        data = ProtostuffIOUtil.toByteArray(obj, schema, getBuffer());
        return data;
    }

    /**
     * 反序列化方法，将字节数组反序列化成指定Class类型
     *
     * @param data  字节数组
     * @param clazz 类
     * @param <T>   泛型
     * @return 解析好的对象
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }

    private ProtostuffUtils() {
    }
}
