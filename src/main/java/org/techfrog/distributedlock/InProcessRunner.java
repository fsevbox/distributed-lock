package org.techfrog.distributedlock;

import org.springframework.boot.CommandLineRunner;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

import java.util.concurrent.ExecutorService;

public class InProcessRunner extends Runner implements CommandLineRunner {

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
                lockAndRun();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        while(true) {
            executorService.execute(runnable);
        }
    }

    @Override
    protected String getIdentifier() {
        return Thread.currentThread().getName();
    }

}
