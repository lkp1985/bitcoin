package com.lkp.block.entity;

import java.util.List;

/**
 * echarts graph 
 * @author Administrator
 *
 */
public class GraphEntity {
	/**
	 * data: [{
				            name: '班扬.斯塔克',
				            des: '守夜人首席游骑兵',
				           
				            itemStyle: {
				                normal: {
				                    color: 'red'
				                }
				            }
				        }, {
				            name: '艾德.斯塔克',
				            des: '临冬城公爵，国王之手',
				            "category": "艾德.斯塔克",
				            draggable: false,
				            itemStyle: {
				                normal: {
				                    color: 'red'
				                }
				            }
				        }],
				        links: [{
				            source: '艾德.斯塔克',
				            target: '班扬.斯塔克',
				            name: '兄弟',
				            des: '班杨是艾德等弟弟'
				        }, {
				            source: '艾德.斯塔克',
				            target: '凯瑟琳.斯塔克',
				            name: '夫妻'
				        }],
	
	 */
	List<Node> nodeList;
	List<Relation> links;
	 
}

