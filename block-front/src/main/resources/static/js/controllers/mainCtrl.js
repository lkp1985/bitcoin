define(['app','mainService'], function(app,mainService){
      
   return app.controller('mainCtrl', ['$scope','$rootScope','$http','mainService', function ($scope,$rootScope,$http,mainService) {
	   $scope.$on('$viewContentLoaded', function() {
		   $scope.showGraph();
		}); 
	   
	   $scope.showGraph = function(){
		   var myChart = echarts.init(document.getElementById("graphchart"), "green");
		   option = {
				    title: {
				        text: '斯塔克家族关系图谱'
				    },
				    color:['#48cda6','#fd87ab' ],
				    tooltip: {
				        formatter: function(x) {
				            return x.data.des;
				        }
				    },
				    legend: [{ 
				        width:140,      //图行例组件的宽度,默认自适应
				        x : 'right',   //图例显示在右边 
				        tooltip: {
				            show: true
				        }, 
				        selectedMode: 'false',
				        data: [{name:'凯瑟琳.斯塔克'},'艾德.斯塔克' ] 
				    	
				    }], 
				    series: [{
				       type: 'graph',
				       layout: 'force',
				       symbol: 'path://M19.300,3.300 L253.300,3.300 C262.136,3.300 269.300,10.463 269.300,19.300 L269.300,21.300 C269.300,30.137 262.136,37.300 253.300,37.300 L19.300,37.300 C10.463,37.300 3.300,30.137 3.300,21.300 L3.300,19.300 C3.300,10.463 10.463,3.300 19.300,3.300 Z',
				       symbolSize: [60,20],
				       edgeSymbol: ['circle', 'arrow'],
				       symbolRotate:0,
				       roam: true, 
				       hoverAnimation:true,
				       focusNodeAdjacency:true, 
				       edgeSymbolSize: [10, 10],
				        
				        force: {
				            repulsion: 2500,
				            edgeLength: [10, 50]
				        },
				        draggable: true,//如果不设置该值，则拖动图时是整体跟着一起动，而不是改变某一个节点的位置,可以单独设置某个结点的  draggable: true
				        itemStyle: {
				            normal: {
				                color: '#4b565b'
				            }
				        },
				        lineStyle: {
				            normal: {
				                width: 1,
				                color: '#4b565b'

				            }
				        },
				        edgeLabel: {
				            normal: {
				                show: true,
				                formatter: function(x) {
				                    return x.data.name;
				                }
				            }
				        },
				        label: {
				            normal: {
				                show: true,
				                textStyle: {},
				                formatter: function(x) {
				                    return x.data.name;
				                }
				            }
				        },
				        data: [{
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
				        }, {
				            name: '凯瑟琳.斯塔克',
				            "category": "凯瑟琳.斯塔克",
				            des: '临冬城夫人',
				        }, {
				            name: '罗伯.斯塔克',
				            des: '临冬城继承人',
				            itemStyle: {
				                normal: {
				                    color: 'red'
				                }
				            }
				        }, {
				            name: '琼恩.雪诺',
				            itemStyle: {
				                normal: {
				                    color: 'red'
				                }
				            }
				        }, {
				            name: '三傻.斯塔克',
				        }, {
				            name: '布兰.斯塔克',
				            "category": "布兰.斯塔克",
				            itemStyle: {
				                normal: {
				                    color: 'red'
				                }
				            }
				        }, {
				            name: '艾利亚.斯塔克',
				        }, ],
				        links: [{
				            source: '艾德.斯塔克',
				            target: '班扬.斯塔克',
				            name: '兄弟',
				            des: '班杨是艾德等弟弟'
				        }, {
				            source: '艾德.斯塔克',
				            target: '凯瑟琳.斯塔克',
				            name: '夫妻'
				        }, {
				            source: '艾德.斯塔克',
				            target: '罗伯.斯塔克',
				            name: "父子"
				        }, {
				            source: '艾德.斯塔克',
				            target: '琼恩.雪诺',
				            name: "私生子"
				        }, {
				            source: '艾德.斯塔克',
				            target: '布兰.斯塔克',
				            name: "父子"
				        }, {
				            source: '艾德.斯塔克',
				            target: '三傻.斯塔克',
				            name: "父女"
				        }, {
				            source: '艾德.斯塔克',
				            target: '艾利亚.斯塔克',
				            name: "父女"
				        }, {
				            source: '凯瑟琳.斯塔克',
				            target: '艾利亚.斯塔克',
				            name: "母女"
				        }, {
				            source: '凯瑟琳.斯塔克',
				            target: '布兰.斯塔克',
				            name: "母子"
				        }, {
				            source: '凯瑟琳.斯塔克',
				            target: '三傻.斯塔克',
				            name: "母女"
				        }, {
				            source: '凯瑟琳.斯塔克',
				            target: '琼恩.雪诺',
				            name: "母子"
				        }, {
				            source: '凯瑟琳.斯塔克',
				            target: '罗伯.斯塔克',
				            name: "母子"
				        } ],
				        categories: [{
				            'name': '凯瑟琳.斯塔克'
				        }, {
				            'name': '布兰.斯塔克'
				        }, {
				            'name': '艾德.斯塔克'
				        }, {
				            'name': '关羽948'
				        }, {
				            'name': '张飞408'
				        }, {
				            'name': '赵云393'
				        }, {
				            'name': '孙权390'
				        }, {
				            'name': '吕布384'
				        }, {
				            'name': '周瑜328'
				        }, {
				            'name': '魏延327'
				        }, {

				        }],
				        lineStyle: {
				            normal: {
				                color: 'source',
				                curveness: 0,
				                type: "solid"
				            }
				        }
				    }]
				};
		   
		   myChart.setOption(option);
	   }
    }])

})