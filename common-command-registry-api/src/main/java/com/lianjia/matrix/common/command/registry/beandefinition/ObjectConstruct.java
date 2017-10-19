package com.lianjia.matrix.common.command.registry.beandefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public class ObjectConstruct {
    private List<Object> args = new ArrayList<>();

    public ObjectConstruct(Object... objs) {
        if (null != objs) {
            for (Object obj : objs) {
                args.add(obj);
            }
        }
    }

    public List<Object> getArgs() {
        return Collections.unmodifiableList(args);
    }
}
