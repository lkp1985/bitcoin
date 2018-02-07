package com.lkp.block.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lkp.block.entity.Node;
import com.lkp.block.entity.Relation;
import com.lkp.block.entity.Relation2;

/**
 * 实体转换关系
 * @author Administrator
 *
 */
public class EntityTransferUtil {
	
	public final static String ADDRESS = "address";
	public final static String TRANSACTION = "transaction";
	public final static String ROLLIN = "rollin";
	public final static String ROLLOUT = "rollout";
	public final static String OTHER = "unknow";
	
	
	/**
	 * 返回某个结点的所有相关结点
	 * @return
	 */
	public static Map<String,Object> transferNodeRelation(List<Map<String,Object>> resultList,String nodeId){
		
		return null;
	}
	/**
	 * 通过neo4j返回的最短距离list数据转换成echart graph需要的对象格式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> transferShortPathRelation(List<Map<String,Object>> resultList){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Node> nodeList = new ArrayList<Node>();
		List<Relation> relationList = new ArrayList<Relation>();
		resultMap.put("nodes", nodeList);
		resultMap.put("links", relationList);
		Set<String> nodeSet = new HashSet<String>();
		for(Map<String,Object> map : resultList){
			for(Object value : map.values()){
				System.out.println("class="+value.getClass());
				List<Map<String,Object>> list = (List<Map<String,Object>>)value;
				for(int i=0;i<list.size()-2;i=i+2){
					Node node1 = getNode(list.get(i));
					Node node2 = getNode(list.get(i+2));
					 
					Relation relation = new Relation();
					
					String rollname = getRelationDesc(list.get(i+1));
					relation.setName(rollname);
					if(rollname.startsWith(ROLLIN)){//箭头要指向交易
						if(node1.getCategory().equals(ADDRESS)){
							relation.setSource(node1.getName());
							relation.setTarget(node2.getName());
						}else{
							relation.setSource(node2.getName());
							relation.setTarget(node1.getName());
						}
						
					}else{//rollout ,箭头指向address
						if(node1.getCategory().equals(ADDRESS)){
							relation.setSource(node2.getName());
							relation.setTarget(node1.getName());
						}else{
							relation.setSource(node1.getName());
							relation.setTarget(node2.getName());
						}
					}
					
					relationList.add(relation);
					if(!nodeSet.contains(node1.getName())){
						nodeList.add(node1);
						nodeSet.add(node1.getName());
					}
					if(!nodeSet.contains(node2.getName())){
						nodeList.add(node2);
						nodeSet.add(node2.getName());
					}
				}
//				for(Map<String,Object> valueMap : list){
//					System.out.println();
//					Node node = new Node();
//					
//					 
//					for(String key: valueMap.keySet()){
//						
//						 node.setName(type);
//						if(type.equals(ADDRESS)||type.equals(TRANSACTION)){
//							 node.setId(valueMap.get("id")+"");
//						}else if(type.equals(ROLLOUT)){
//							node.set
//						}
//					}
//				}
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 通过neo4j返回的最短距离list数据转换成d3需要的对象格式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> transferShortPathRelationD3(List<Map<String,Object>> resultList){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Node> nodeList = new ArrayList<Node>();
		List<Relation> relationList = new ArrayList<Relation>();
		List<Relation2> relation2List = new ArrayList<Relation2>();
		resultMap.put("nodes", nodeList);
		resultMap.put("links", relation2List);
		Set<String> nodeSet = new HashSet<String>();
		for(Map<String,Object> map : resultList){
			for(Object value : map.values()){
				System.out.println("class="+value.getClass());
				List<Map<String,Object>> list = (List<Map<String,Object>>)value;
				for(int i=0;i<list.size()-2;i=i+2){
					Node node1 = getNode(list.get(i));
					Node node2 = getNode(list.get(i+2));
					 
					Relation relation = new Relation();
					
					String rollname = getRelationDesc(list.get(i+1));
					relation.setName(rollname);
					if(rollname.startsWith(ROLLIN)){//箭头要指向交易
						if(node1.getCategory().equals(ADDRESS)){
							relation.setSource(node1.getName());
							relation.setTarget(node2.getName());
						}else{
							relation.setSource(node2.getName());
							relation.setTarget(node1.getName());
						}
						
					}else{//rollout ,箭头指向address
						if(node1.getCategory().equals(ADDRESS)){
							relation.setSource(node2.getName());
							relation.setTarget(node1.getName());
						}else{
							relation.setSource(node1.getName());
							relation.setTarget(node2.getName());
						}
					}
					
					relationList.add(relation);
					if(!nodeSet.contains(node1.getName())){
						nodeList.add(node1);
						nodeSet.add(node1.getName());
					}
					if(!nodeSet.contains(node2.getName())){
						nodeList.add(node2);
						nodeSet.add(node2.getName());
					}
				}
//				for(Map<String,Object> valueMap : list){
//					System.out.println();
//					Node node = new Node();
//					
//					 
//					for(String key: valueMap.keySet()){
//						
//						 node.setName(type);
//						if(type.equals(ADDRESS)||type.equals(TRANSACTION)){
//							 node.setId(valueMap.get("id")+"");
//						}else if(type.equals(ROLLOUT)){
//							node.set
//						}
//					}
//				}
			}
		}
		
		//修改links中的srouce和targe改为node中的index
		for(Relation relation : relationList){
			String target = relation.getTarget();
			String source = relation.getSource();
			Relation2 lind2 = new Relation2();
			lind2.setName(relation.getName());
			lind2.setType(relation.getType());
			relation2List.add(lind2);
			for(int i=0;i<nodeList.size();i++){
				if(target.equals(nodeList.get(i).getName())){
					relation.setTarget(i+"");
					lind2.setTarget(i);
				}
				if(source.equals(nodeList.get(i).getName())){
					relation.setSource(i+"");
					lind2.setSource(i);
				}
			}
			 
		}
		return resultMap;
	}
	public static String getRelationDesc(Map<String,Object> rollMap){
		if(rollMap.keySet().size()==0){
			return ROLLIN;
		}else if(rollMap.containsKey("money")){
			return ROLLOUT+" etc : "+ rollMap.get("money");
		}else{
			return rollMap.toString();
		}
	}
	
	public static Node getNode(Map<String,Object> map){
		Node node = new Node();
		if(map.containsKey("id") && map.get("id").toString().length()==64){
			node.setName(map.get("id")+"");
			node.setCategory(TRANSACTION);
		}else if(map.containsKey("id")){
			node.setName(map.get("id")+"");
			node.setCategory(ADDRESS);
		}else{
			node.setCategory(OTHER);
		}
		return node;
	}
	
	
	
	/**
	 * 根据ID来判断是什么类型，比如长度是64就是hashid,小于64是比特币钱包
	 * @param id
	 * @return
	 */
	public static String getEntityType(Map<String,Object> map){
		if(map.keySet().size()==0){
			return ROLLIN;
		}else if(map.containsKey("money")){
			return ROLLOUT;
		}else if(map.containsKey("id") && map.get("id").toString().length()==64){
			return TRANSACTION;
		}else if(map.containsKey("id")){
			return ADDRESS;
		}else{
			return OTHER;
		}
	}
}
