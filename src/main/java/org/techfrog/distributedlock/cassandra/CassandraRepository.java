package org.techfrog.distributedlock.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CassandraRepository {

    private CqlSession cqlSession;
    private PreparedStatement preparedLock;
    private PreparedStatement preparedUnlock;

    public CassandraRepository(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
        this.preparedLock = cqlSession.prepare(
                "insert into locker (name, owner) values (?, ?) if not exists");
        this.preparedUnlock = cqlSession.prepare(
                "delete from locker where name = ? if owner = ?");
    }

    public boolean lock(String lockName) {
        boolean locked = false;
        BoundStatement bound = preparedLock.bind(lockName, Thread.currentThread().getName());
        try {
            ResultSet result = cqlSession.execute(bound);
            locked = result.wasApplied();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return locked;
    }

    public boolean unlock(String lockName) {
        boolean unlocked = false;
        BoundStatement bound = preparedUnlock.bind(lockName, Thread.currentThread().getName());
        try {
            ResultSet result = cqlSession.execute(bound);
            unlocked = result.wasApplied();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return unlocked;
    }
}
