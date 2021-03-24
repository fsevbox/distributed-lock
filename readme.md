
**Install**
```
mvn clean package
```

**In memory lock accessed by multiple threads** 
```    
java -jar target/distributed-lock.jar
```

**Distributed lock on Hazelcast**  
Start multiple instances using a different port for each.
```
java -jar target/distributed-lock.jar --spring.profiles.active=hazelcast --server.port=${PORT}
```


**Distributed lock on Cassandra**  
Start multiple instances using a different port for each.
A Cassandra instance is expected to be provided externally.
```
java -jar target/distributed-lock.jar --spring.profiles.active=cassandra --server.port=${PORT}
```