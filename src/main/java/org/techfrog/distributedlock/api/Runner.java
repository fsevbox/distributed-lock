package org.techfrog.distributedlock.api;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Runner {

    public static final int LOCK_TRY_TIMEOUT = 1000;
    public static final int RUN_FOR_MILLIES = 990;
    public static final String LOCKER_NAME = "aaa-lock";

    protected DistributedLockProvider distributedLockProvider;

    public Runner(DistributedLockProvider distributedLockProvider) {
        this.distributedLockProvider = distributedLockProvider;
    }

    public abstract void execute() throws InterruptedException;

    protected void lockAndRun() throws InterruptedException {
        DistributedLock lock = distributedLockProvider.getLock(LOCKER_NAME);
        if (lock.tryLock(LOCK_TRY_TIMEOUT, TimeUnit.MILLISECONDS)) {
            try {
                Thread.sleep(RUN_FOR_MILLIES);
                log.info(getIdentifier() + " locked on " +
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
