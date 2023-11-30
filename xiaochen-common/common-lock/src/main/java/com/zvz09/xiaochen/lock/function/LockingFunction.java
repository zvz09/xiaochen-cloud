package com.zvz09.xiaochen.lock.function;

/**
 * @author lizili-YF0033
 */
@FunctionalInterface
public interface LockingFunction {
    void apply() throws InterruptedException;
}
