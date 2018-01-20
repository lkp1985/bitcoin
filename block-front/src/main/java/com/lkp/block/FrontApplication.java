package com.lkp.block;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@RestController
public class FrontApplication {  

    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    } 
    
    @Bean
    public DataSource dataSource() {
        String NEO4J_URL = System.getenv("NEO4J_URL");
        if (NEO4J_URL==null) NEO4J_URL=System.getProperty("NEO4J_URL","jdbc:neo4j:http://localhost:7474");
        return new DriverManagerDataSource(NEO4J_URL,"neo4j","111111");
    }
}


