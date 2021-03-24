package org.techfrog.distributedlock.cassandra;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.techfrog.distributedlock.api.DistributedLock;

import java.util.concurrent.TimeUnit;

@Slf4j
class CassandraDistributedLock implements DistributedLock {

    private static final int WAIT_TIME = 100;

    private String lockName;
    private CassandraRepository repository;

    public CassandraDistributedLock(String lockName, CassandraRepository repository) {
        this.lockName = lockName;
        this.repository = repository;
    }

    @Override
    public void lock() {
        boolean aquired = false;
        while (!aquired) {
            aquired = repository.lock(lockName);
            if (!aquired) {
                try {
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void unlock() {
        repository.unlock(lockName);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new NotImplementedException("lockInterruptibly not implemented");
    }

    @Override
    public boolean tryLock() {
        return tryLock(2, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) {
        long waitUntil = System.nanoTime() + TimeUnit.NANOSECONDS.convert(l, timeUnit);

        boolean aquired = false;
        while (!aquired && waitUntil > System.nanoTime()) {
            aquired = repository.lock(lockName);
            if (!aquired) {
                try {
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    break;
                }
            }
        }

        return aquired;
    }
}
