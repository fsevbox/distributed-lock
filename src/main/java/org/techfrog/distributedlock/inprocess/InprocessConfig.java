package org.techfrog.distributedlock.inprocess;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

import java.util.concurrent.Executors;

@Profile("inprocess")
@Configuration
public class InprocessConfig {

    @Bean
    public DistributedLockProvider distributedLockProvider() {
        return new InProcessDistriburedLockProvider();
    }

    @Bean
    public Runner processRunner() {
        return new InProcessRunner(
                distributedLockProvider(), Executors.newFixedThreadPool(5));
    }
}
