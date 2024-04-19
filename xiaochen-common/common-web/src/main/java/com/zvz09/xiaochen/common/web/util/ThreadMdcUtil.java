package com.zvz09.xiaochen.common.web.util;

import com.zvz09.xiaochen.common.core.util.Snowflake;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.zvz09.xiaochen.common.core.constant.CommonConstant.TRACE_ID;

public class ThreadMdcUtil {
    public static void setTraceIdIfAbsent(){
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, Snowflake.getSnowflakeId());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
