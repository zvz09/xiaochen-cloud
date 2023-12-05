package com.zvz09.xiaochen.lock.function;

/**
 * @author zvz09
 */
@FunctionalInterface
public interface LockingFunction {
    void apply() throws InterruptedException;
}
