package org.techfrog.distributedlock.cassandra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

@Slf4j
public class CassandraRunner extends Runner {

    public static final String LOCK_NAME = "aaa-lock";
    private String port;

    public CassandraRunner(DistributedLockProvider distributedLockProvider, String port) {
        super(distributedLockProvider);
        this.port = port;
    }

    // run every second
    @Scheduled(cron = "* * * * * *")
    public void execute() {
        try {
            lockAndRun(LOCK_NAME);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected String getIdentifier() {
        return port;
    }
}
