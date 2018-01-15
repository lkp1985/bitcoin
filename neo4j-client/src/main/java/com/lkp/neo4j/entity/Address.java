package com.lkp.neo4j.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Address {
	@Id
	String address;
}
