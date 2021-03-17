package org.techfrog.distributedlock.api;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    void lock();

    void unlock();

    void lockInterruptibly() throws InterruptedException;

    boolean tryLock();

    boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException;
}
