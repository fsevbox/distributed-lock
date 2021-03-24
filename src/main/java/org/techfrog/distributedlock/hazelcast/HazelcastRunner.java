package org.techfrog.distributedlock.hazelcast;

import org.springframework.scheduling.annotation.Scheduled;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

public class HazelcastRunner extends Runner {

    public static final String LOCK_NAME = "aaa-lock";
    private String port;

    public HazelcastRunner(DistributedLockProvider distributedLockProvider, String port) {
        super(distributedLockProvider);
        this.port = port;
    }

    // run every second
    @Scheduled(cron = "* * * * * *")
    public void execute() throws InterruptedException {
        lockAndRun(LOCK_NAME);
    }

    @Override
    protected String getIdentifier() {
        return port;
    }
}
