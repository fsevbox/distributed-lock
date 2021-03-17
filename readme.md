
**Install**
```
mvn clean package
```

**In memory lock accessed by multiple threads** 
```    
java -jar target/distributed-lock.jar --spring.profiles.active=inprocess
```

**Distributed lock on Hazelcast**  
```
java -jar target/distributed-lock.jar --spring.profiles.active=hazelcast --server.port=${PORT}
```