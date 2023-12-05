package com.zvz09.xiaochen.lock;

import com.zvz09.xiaochen.lock.function.LockingFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zvz09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private final RedissonClient redissonClient;

    private static final int DEFAULT_LOCK_WAIT_TIME = 20;
    private static final int DEFAULT_LOCK_LEASE_TIME = 10;
    private static final TimeUnit DEFAULT_TIME_UNIT =  TimeUnit.SECONDS;

    private void withTryLock(String lockKey, LockingFunction lockingFunction){
        this.withTryLock(lockKey,DEFAULT_LOCK_WAIT_TIME,DEFAULT_LOCK_LEASE_TIME,DEFAULT_TIME_UNIT,lockingFunction);
    }

    private void withTryLock(String lockKey,long waitTime, long leaseTime, TimeUnit timeUnit, LockingFunction lockingFunction) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
                lockingFunction.apply();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Operation failed", e);
        } finally {
            lock.unlock();
        }
    }

    private void withLock(String lockKey, LockingFunction lockingFunction){
        this.withLock(lockKey,DEFAULT_LOCK_LEASE_TIME,DEFAULT_TIME_UNIT,lockingFunction);
    }

    private void withLock(String lockKey,long leaseTime, TimeUnit timeUnit, LockingFunction lockingFunction) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, timeUnit);
        try {
            lockingFunction.apply();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Operation failed", e);
        } finally {
            lock.unlock();
        }
    }
}
