package org.techfrog.distributedlock.api;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Runner {

    public static final int LOCK_TRY_TIMEOUT = 1000;
    public static final int RUN_FOR_MILLIES = 900;

    protected DistributedLockProvider distributedLockProvider;

    public Runner(DistributedLockProvider distributedLockProvider) {
        this.distributedLockProvider = distributedLockProvider;
    }

    public abstract void execute() throws InterruptedException;

    protected void lockAndRun(String lockName) throws InterruptedException {
        DistributedLock lock = distributedLockProvider.getLock(lockName);
        if (lock.tryLock(LOCK_TRY_TIMEOUT, TimeUnit.MILLISECONDS)) {
            try {
                Thread.sleep(RUN_FOR_MILLIES);
                log.info(getIdentifier() + " locked on second " +
                        LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).getSecond());
            } finally {
                lock.unlock();
            }
        } else {
            log.info(getIdentifier() + " lock could not be aquired");
        }
    }

    protected abstract String getIdentifier();
}
