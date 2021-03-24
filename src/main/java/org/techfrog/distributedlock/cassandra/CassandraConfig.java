package org.techfrog.distributedlock.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.techfrog.distributedlock.api.DistributedLockProvider;
import org.techfrog.distributedlock.api.Runner;

import java.net.InetSocketAddress;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

@Slf4j
@Profile("cassandra")
@Configuration
@EnableScheduling
class CassandraConfig {

    @Bean
    public CqlSession session(@Value("${cassandra.host:127.0.0.1}") String host,
                              @Value("${cassandra.port:9042}") int port,
                              @Value("${cassandra.datacenter:cluster}") String datacenter,
                              @Value("${cassandra.keyspace}") String keyspace) {

        CqlSession cqlSession = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(host, port))
                .withLocalDatacenter(datacenter)
                .build();
        log.info("[OK] Connected to Keyspace {} on node {}", keyspace, host);

        setupKeyspace(cqlSession, keyspace);
        createTables(cqlSession);
        return cqlSession;
    }

    private void setupKeyspace(CqlSession cqlSession, String keyspace) {
        cqlSession.execute(createKeyspaceSimpleStrategy(keyspace, 1));
        cqlSession.execute("USE " + keyspace);
        log.info("Keyspace '{}' created (if needed).", keyspace);
    }

    private SimpleStatement createKeyspaceSimpleStrategy(String keyspaceName, int replicationFactor) {
        return SchemaBuilder.createKeyspace(keyspaceName)
                .ifNotExists()
                .withSimpleStrategy(replicationFactor)
                .withDurableWrites(true)
                .build();
    }

    private void createTables(CqlSession cqlSession) {
        cqlSession.execute(createTable("locker")
                .ifNotExists()
                .withPartitionKey("name", DataTypes.TEXT)
                .withColumn("owner", DataTypes.TEXT)
                .withColumn("value", DataTypes.TEXT)
                .withDefaultTimeToLiveSeconds(2)
                .build());
        log.info("Table 'locker' created (if needed).");
    }

    @Bean
    public CassandraRepository cassandraRepository(CqlSession session) {
        return new CassandraRepository(session);
    }

    @Bean
    public DistributedLockProvider distributedLockProvider(CassandraRepository repository) {
        return new CassandraDistriburedLockProvider(repository);
    }

    @Autowired
    Environment environment;

    @Bean
    public Runner processRunner(DistributedLockProvider distributedLockProvider) {
        return new CassandraRunner(distributedLockProvider, environment.getProperty("server.port"));
    }


}
