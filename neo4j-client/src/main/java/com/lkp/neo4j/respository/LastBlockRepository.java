package com.lkp.neo4j.respository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.lkp.neo4j.entity.LastBlock;

@Component
public interface LastBlockRepository extends MongoRepository<LastBlock, String> {
    

}