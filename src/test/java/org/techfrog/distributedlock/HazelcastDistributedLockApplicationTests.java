package org.techfrog.distributedlock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("hazelcast")
@SpringBootTest
public class HazelcastDistributedLockApplicationTests {

    //@Test
    void contextLoads() {
    }

}
