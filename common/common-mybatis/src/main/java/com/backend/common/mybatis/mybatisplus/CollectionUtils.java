package com.backend.common.mybatis.mybatisplus;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isNotEmpty(Map<?,?> map) {
        return map != null && !map.isEmpty();
    }
}
