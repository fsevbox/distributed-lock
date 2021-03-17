package org.techfrog.distributedlock.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.techfrog.distributedlock.HazelcastRunner;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

@Profile("hazelcast")
@Configuration
@EnableScheduling
public class HazelcastConfig {

    /**
     * to use unsafe mode set CP_MEMBER_COUNT = 0
     * in order to activate CP in safe mode set CP_MEMBER_COUNT to a value >= 3
     */
    public static final int CP_MEMBER_COUNT = 0;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.getCPSubsystemConfig().setCPMemberCount(CP_MEMBER_COUNT);
        config.getCPSubsystemConfig().setSessionHeartbeatIntervalSeconds(1);
        config.getCPSubsystemConfig().setSessionTimeToLiveSeconds(5);
        config.getCPSubsystemConfig().setMissingCPMemberAutoRemovalSeconds(10);
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public DistributedLockProvider distributedLockProvider() {
        return new HazelcastDistributedLockProvider(hazelcastInstance());
    }

    @Autowired
    Environment environment;

    @Bean
    public Runner processRunner() {
        return new HazelcastRunner(
                distributedLockProvider(), environment.getProperty("server.port"));
    }

}
