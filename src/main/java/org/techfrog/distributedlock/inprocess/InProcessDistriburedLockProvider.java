package org.techfrog.distributedlock.inprocess;

import org.techfrog.distributedlock.api.DistributedLock;
import org.techfrog.distributedlock.api.DistributedLockProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class InProcessDistriburedLockProvider implements DistributedLockProvider {

    private Map<String, ReentrantLock> locksMap;

    public InProcessDistriburedLockProvider() {
        this.locksMap = new ConcurrentHashMap<>();
    }

    @Override
    public DistributedLock getLock(String lockName) {
        ReentrantLock lock = locksMap.computeIfAbsent(lockName, k -> new ReentrantLock());
        return new InProcessDistributedLock(lock);
    }

}
