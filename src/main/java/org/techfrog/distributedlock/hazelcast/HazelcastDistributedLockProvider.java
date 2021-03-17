package org.techfrog.distributedlock.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.techfrog.distributedlock.api.DistributedLock;
import org.techfrog.distributedlock.api.DistributedLockProvider;

public class HazelcastDistributedLockProvider implements DistributedLockProvider {

    private HazelcastInstance hazelcastInstance;

    public HazelcastDistributedLockProvider(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public DistributedLock getLock(String lockName) {
        return new HazelcastDistributedLock(
                hazelcastInstance.getCPSubsystem().getLock(lockName));
    }
}
