package org.techfrog.distributedlock.api;

public interface DistributedLockProvider {

    DistributedLock getLock(String lock);
}
