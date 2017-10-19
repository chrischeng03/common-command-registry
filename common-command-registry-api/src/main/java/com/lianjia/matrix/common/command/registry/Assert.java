package com.lianjia.matrix.common.command.registry;

/**
 * @author 程天亮
 * @Created
 */
public class Assert {

    public static void checkNonNull(Object obj) {
        if (obj == null) throw new NullPointerException("Object is Null");
    }
}
