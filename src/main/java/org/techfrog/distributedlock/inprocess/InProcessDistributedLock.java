package org.techfrog.distributedlock.inprocess;

import org.techfrog.distributedlock.api.DistributedLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class InProcessDistributedLock implements DistributedLock {

    private ReentrantLock reentrantLock;

    public InProcessDistributedLock(ReentrantLock reentrantLock) {
        this.reentrantLock = reentrantLock;
    }

    @Override
    public void lock() {
        reentrantLock.lock();
    }

    @Override
    public void unlock() {
        reentrantLock.unlock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        reentrantLock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return reentrantLock.tryLock();
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return reentrantLock.tryLock(l, timeUnit);
    }
}
