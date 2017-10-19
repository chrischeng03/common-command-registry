package com.lianjia.matrix.common.command.registry.entity;

import com.alibaba.fastjson.JSON;
import com.lianjia.matrix.common.command.registry.Assert;

import java.util.HashMap;
import java.util.Map;

public abstract class Instruction {
    /**
     * 指令值
     *
     * @return
     */
    public abstract int value();

    /**
     * 转为byte数组
     *
     * @return
     */
    public abstract byte[] toBytes();


    public static Instruction from(byte[] source) {
        Map<String, Object> map = JSON.parseObject(new String(source));
        return from(map);
    }

    public static Instruction from(Map<String, Object> map) {
        Integer value = (Integer) map.get("value");
        if (null == value) {
            throw new ExceptionInInitializerError("Value Not Exist");
        }
        return new DefaultInstruction(value, map);
    }
}

class DefaultInstruction extends Instruction {

    private int value;

    private Map<String, Object> source;

    DefaultInstruction(int value, Map<String, Object> source) {
        Assert.checkNonNull(source);
        this.value = value;
        this.source = new HashMap<>(source);
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public byte[] toBytes() {
        if (source == null || source.size() == 0) return new byte[0];
        return JSON.toJSONBytes(source);
    }

    @Override
    public String toString() {
        return "DefaultInstruction{" +
                "value=" + value +
                ", source=" + source +
                '}';
    }
}

