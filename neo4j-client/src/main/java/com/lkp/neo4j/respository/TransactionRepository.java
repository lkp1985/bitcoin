package com.lkp.neo4j.respository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.lkp.neo4j.entity.Transaction;

@Component
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    

}