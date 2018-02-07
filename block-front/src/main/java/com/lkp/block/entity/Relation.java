package com.lkp.block.entity;

import lombok.Data;

@Data
public class Relation{
	String source;
	String target;
	String name;//格式: rollin/rollout + " etc:"+money ,如  转出etc:0.00001 ,对于rolin不需要添加money
	String type;//rollin or rollout
}