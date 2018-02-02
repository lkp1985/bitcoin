package com.lkp.neo4j.respository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.lkp.neo4j.entity.BlockHeight;

@Component
public interface BlockHeightRepository extends MongoRepository<BlockHeight, Integer> {
    List<BlockHeight> findByHeight(int height);

}