package org.techfrog.distributedlock.hazelcast;

import org.techfrog.distributedlock.api.DistributedLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class HazelcastDistributedLock implements DistributedLock {

    private Lock lock;

    public HazelcastDistributedLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return lock.tryLock(l, timeUnit);
    }

}
