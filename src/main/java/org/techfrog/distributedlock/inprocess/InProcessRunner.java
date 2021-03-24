package org.techfrog.distributedlock.inprocess;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

import java.util.concurrent.ExecutorService;

@Slf4j
public class InProcessRunner extends Runner implements CommandLineRunner {

    public static final String LOCK_NAME = "aaa-lock";
    private ExecutorService executorService;

    public InProcessRunner(DistributedLockProvider distributedLockProvider, ExecutorService executorService) {
        super(distributedLockProvider);
        this.executorService = executorService;
    }

    @Override
    public void run(String... args) {
        execute();
    }

    public void execute() {
        Runnable runnable = () -> {
            try {
                lockAndRun(LOCK_NAME);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        };

        while (true) {
            executorService.execute(runnable);
            //avoid filling up the thread pool queue
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    protected String getIdentifier() {
        return Thread.currentThread().getName();
    }

}
