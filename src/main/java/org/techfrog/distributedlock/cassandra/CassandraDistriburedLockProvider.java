package org.techfrog.distributedlock.cassandra;

import org.techfrog.distributedlock.api.DistributedLock;
import org.techfrog.distributedlock.api.DistributedLockProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CassandraDistriburedLockProvider implements DistributedLockProvider {

    private Map<String, DistributedLock> locks;
    private CassandraRepository repository;

    public CassandraDistriburedLockProvider(CassandraRepository repository) {
        this.repository = repository;
        locks = new ConcurrentHashMap<>();
    }

    @Override
    public DistributedLock getLock(String lockName) {
        return locks.computeIfAbsent(lockName, (k) -> new CassandraDistributedLock(lockName, repository));
    }
}
