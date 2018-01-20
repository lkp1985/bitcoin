define(['app','reportService'], function(app,reportService){
      
   return app.controller('reportCtrl', ['$scope','$rootScope', '$routeParams','$q','$http','reportService', 
	   function ($scope,$rootScope, $routeParams,$q,$http,reportService) {
	   
	   $scope.cppccLevel = ['全部','湖南省政协','常德市政协','常德市津市政协','张家界市政协','益阳市政协','邵阳市政协','岳阳市政协','郴州市政协','郴州市临武县政协','郴州市北湖区政协'];
	   $scope.sexList = ['全部','男','女'];
	   $scope.ageList = [{'name':'全部','value':'all'},{'name':'0-20','value':'committeeMember.birthdate:[NOW-20YEAR TO NOW]'},
		   {'name':'20-30','value':'[NOW-30YEAR TO NOW-20YEAR]'},{'name':'30-40','value':'[NOW-40YEAR TO NOW-30YEAR]'},
		   {'name':'40-50','value':'[NOW-50YEAR TO NOW-40YEAR]'},{'name':'50-60','value':'[NOW-60YEAR TO NOW-50YEAR]'},
		   {'name':'60-70','value':'[NOW-70YEAR TO NOW-60YEAR]'},{'name':'70-80','value':'[NOW-80YEAR TO NOW-70YEAR]'},
		   {'name':'80-90','value':'[NOW-90YEAR TO NOW-80YEAR]'},{'name':'90-100','value':'[NOW-100YEAR TO NOW-90YEAR]'}];
	   $scope.$on('$viewContentLoaded', function() { 
		   
		   $scope.statistics();
//		   var defer1 = $q.defer();
//           var promise1 = defer1.promise;
		   
		});  
	   var canceler;
	 
	   $scope.areacode = "430000";
	   $scope.areaname="湖南省";
	   $scope.cppccLevel="";
	   $scope.type="提案";
	   $scope.currentMap = "430000";//当前选择的
	   
       $scope.statistics = function(){
    	   $scope.getCompareData();
    	   $scope.getCategoryData();
    	   $scope.getOrganizerData();
    	   $scope.getBrowserData();
//    	  
       }
     //获取本次及上次会议的数目：查询条件：届、次、类型（提案、微建议）、地区
       $scope.jie="十一";
       $scope.ci="四";
       
       
       $scope.getBrowserData = function(){
    	   reportService.getBrowserData( $scope.areacode,$scope.jie,$scope.ci,$scope.cppccLevel,
    			   $scope.selectCmsType).get({},function(resp){
    		   console.log("getBrowserData="+JSON.stringify(resp));
    		   if(resp.code==1){
    			   $scope.browserdata = resp.attribute.result;  
    			   console.log("browserdata="+JSON.stringify($scope.browserdata));
    		   }
    	   })
       }
       
       
       $scope.getOrganizerData = function(){
    	   reportService.getOrganizerData( $scope.areacode,$scope.jie,$scope.ci,$scope.cppccLevel,
    			   $scope.selectCmsType).get({},function(resp){
    		   console.log("getOrganizerData="+JSON.stringify(resp));
    		   if(resp.code==1){
    			   $scope.organizerdata = resp.attribute.result; 
    			   
    			   $scope.getorganizerdata($scope.organizerdata);
    		   }
    	   })
       }
       
       $scope.getCompareData = function(){
    	   reportService.getcomparenum( $scope.areacode,$scope.jie,$scope.ci,$scope.cppccLevel,
    			   $scope.selectCmsType).get({},function(resp){
    		   console.log("getcomparenum="+JSON.stringify(resp));
    		   if(resp.code==1){
    			   var compareData = resp.attribute.result.compare;
    			   $scope.sum=compareData.sum;
    			   $scope.lastsum=compareData.lastsum;
    			   $scope.jiti=compareData.jiti;
    			   $scope.geren=compareData.geren;
    			   $scope.secnum = compareData.secnum;
    			   $scope.maxSecName=compareData.maxSecName;
    			   $scope.secondSecName=compareData.secondSecName;
    			   $scope.memberNum=compareData.memberNum;
    			   $scope.maxSecNum=compareData.maxSecNum;
    			   $scope.secondSecNum=compareData.secondSecNum;
    			   $scope.female=compareData.female;
    			   $scope.male=compareData.male;
    			   if($scope.sum>=$scope.lastsum){
    				   $scope.gap=($scope.sum-$scope.lastsum);
    				   $scope.compare="增加";
    			   }else{
    				   $scope.gap=($scope.lastsum-$scope.sum);
    				   $scope.compare="减少";
    			   }
    			   
    			   
    			   var secData =  resp.attribute.result.sector;
    			   $scope.getcomparenum(compareData);
    			   $scope.getsectordata(secData);
    		   }
    	   })
       }
       $scope.getCategoryData = function(){
    	   reportService.getCategoryData( $scope.areacode,$scope.jie,$scope.ci,
    			   $scope.cppccLevel,$scope.selectCmsType).get({},function(resp){
    		   
    		   if(resp.code==1){
    			   var caretoryData = resp.attribute.result.category;
    			   console.log("caretoryData="+JSON.stringify(caretoryData));
    			   $scope.categoreContent = resp.attribute.result.categorycontent;
    			   $scope.tagscontent = resp.attribute.result.tagscontent;
    			   
    			   console.log("categoreContent="+$scope.categoreContent);
    			   $scope.result = resp.attribute.result
    			   var tagsData =  $scope.result.tags;
    			   $scope.getcategorydata(caretoryData);
    			   $scope.gettagsdata(tagsData);
    		   }
    	   })
       }
       
       $scope.getcomparenum = function(compareData){
    	   var title1 = compareData.title1;
    	   var title2 = compareData.title2;
    	   var value1 = compareData.value1;
    	   var value2 = compareData.value2;
    	   var name = compareData.name;
    	   option = {
    			   title : {
    			        text: '两次会议提案数' 
    			    },
    			    tooltip : {
    			        trigger: 'axis'
    			    },
    			  legend: {
    			        data:[title1,title2],
    			        x:'right'
    			    },
    			    /* 
    			    toolbox: {
    			        show : true,
    			        feature : {
    			            mark : {show: true},
    			            dataView : {show: true, readOnly: false},
    			            magicType : {show: true, type: ['line', 'bar']},
    			            restore : {show: true},
    			            saveAsImage : {show: true}
    			        }
    			    },*/
    			    calculable : true,
    			    xAxis : [
    			        {
    			            type : 'category',
    			            data : ['总数','集体','个人/联名','男委员','女委员']
    			        }
    			    ],
    			    yAxis : [
    			        {
    			            type : 'value'
    			        }
    			    ],
    			    series : [
    			        {
    			            name:title1,
    			            type:'bar',
    			            data:value1
    			        },
    			        {
    			            name:title2,
    			            type:'bar',
    			            data:value2
    			        }
    			    ]
    			};
    	   
    	   var myChart = echarts.init(document.getElementById('comparenum')); 
    	   myChart.setOption(option);
       }
       
       
       $scope.getsectordata = function(sectorData){
    		  
    	   option = {
           	    title: {
        	        text: "提交"+$scope.type+"的委员界别统计 ",
        	        x: "right",
        	        textStyle: {
        	            fontSize: 18,
        	            fontWeight: 'bolder',
        	            color: '#333'          // 主标题文字颜色
        	        },
        	    },
        	    tooltip: {
        	        trigger: "item",
        	        formatter: "{a} <br/>{b} : {c} ({d}%)"
        	    }, 
        	    toolbox: {
        	        show: false,
        	        feature: {
        	            mark: {
        	                show: true
        	            },
        	            dataView: {
        	                show: true,
        	                readOnly: true
        	            },
        	            restore: {
        	                show: true
        	            },
        	            saveAsImage: {
        	                show: true
        	            }
        	        }
        	    },
        	    calculable: true,
        	    series: [
        	        {
        	            name: "提交提案界别数",
        	            type: "pie",
        	            radius: "40%",
        	          	center: ["50%", "60%"],
        	            itemStyle: {
        	                normal: {
        	                    label: {
        	                        show: true,
        	                        formatter: "{b}: {c} ({d}%)"
        	                    }
        	                }
        	            },
        	            data: sectorData
        	        }
        	    ]
        	};
    	   
    	   var myChart = echarts.init(document.getElementById('sectorData')); 
    	   myChart.setOption(option);
       }
       
       $scope.getcategorydata = function(categoryData){
 		  
    	   option = {
           	    title: {
        	        text: $scope.type+" 类别统计",
        	        x: "center",
        	        textStyle: {
        	            fontSize: 18,
        	            fontWeight: 'bolder',
        	            color: '#333'          // 主标题文字颜色
        	        },
        	    },
        	    tooltip: {
        	        trigger: "item",
        	        formatter: "{a} <br/>{b} : {c} ({d}%)"
        	    }, 
        	    toolbox: {
        	        show: false,
        	        feature: {
        	            mark: {
        	                show: true
        	            },
        	            dataView: {
        	                show: true,
        	                readOnly: true
        	            },
        	            restore: {
        	                show: true
        	            },
        	            saveAsImage: {
        	                show: true
        	            }
        	        }
        	    },
        	    calculable: true,
        	    series: [
        	        {
        	            name: "类别",
        	            type: "pie",
        	            radius: "40%",
        	          	center: ["50%", "60%"],
        	            itemStyle: {
        	                normal: {
        	                    label: {
        	                        show: true,
        	                        formatter: "{b}: {c} ({d}%)"
        	                    }
        	                }
        	            },
        	            data: categoryData
        	        }
        	    ]
        	};
    	   
    	   var myChart = echarts.init(document.getElementById('categoryData')); 
    	   myChart.setOption(option);
       }
       $scope.gettagsdata = function(tagsData){
    	   option = {
           	    title: {
        	        text:  $scope.type+"关注点分布 ",
        	        x: "right",
        	        textStyle: {
        	            fontSize: 18,
        	            fontWeight: 'bolder',
        	            color: '#333'          // 主标题文字颜色
        	        },
        	    },
        	    tooltip: {
        	        trigger: "item",
        	        formatter: "{a} <br/>{b} : {c} ({d}%)"
        	    }, 
        	    toolbox: {
        	        show: false,
        	        feature: {
        	            mark: {
        	                show: true
        	            },
        	            dataView: {
        	                show: true,
        	                readOnly: true
        	            },
        	            restore: {
        	                show: true
        	            },
        	            saveAsImage: {
        	                show: true
        	            }
        	        }
        	    },
        	    calculable: true,
        	    series: [
        	        {
        	            name: "关注点",
        	            type: "pie",
        	            radius: "40%",
        	          	center: ["50%", "60%"],
        	            itemStyle: {
        	                normal: {
        	                    label: {
        	                        show: true,
        	                        formatter: "{b}: {c} ({d}%)"
        	                    }
        	                }
        	            },
        	            data: tagsData
        	        }
        	    ]
        	};
    	   
    	   var myChart = echarts.init(document.getElementById('tagsData')); 
    	   myChart.setOption(option);
       }
       
       
       
       $scope.getorganizerdata = function(organizerData){
  		  console.log("organizerData="+JSON.stringify(organizerData));
  		  var nameData = organizerData.name;
  		  var valueData = organizerData.value;
    	   option = {
	        		title: {
	        			text: "交办单位数统计 ",
	        			x: "left"
	        		},
	        		 tooltip: {
				        trigger: "axis"
				    },
				    legend: {
				    	x: "right",
				        data: [$scope.type+'数']
				    },
			        lable:{
				        normal:{
				            textStyle:{
				                fontsize:10
				            }
				        }
				    },
				    toolbox: {
				        show: false,
				        feature: {
				            mark: {
				                show: true
				            },
				            dataView: {
				                show: true,
				                readOnly: true
				            },
				            magicType: {
				                show: false,
				                type: ["line", "bar"]
				            },
				            restore: {
				                show: true
				            },
				            saveAsImage: {
				                show: true
				            }
				        }
				    },
				    calculable: true,
				    xAxis: [
				        {
				            type: "category",
				            data: nameData,
				            "axisLabel":{  
					       		interval: 0 ,
					       		rotate:35,//倾斜度 -90 至 90 默认为0 
					            textStyle:{
					                fontSize:12 
					            }
					        },
				        },
				    ],
				    yAxis: [
				        {
				            type: "value"
				        }
				    ],
				    series: [
				        {
				            name: $scope.type+"数",
				            type: "bar",
				            data: valueData,
				            barWidth: 10
				        },
				    ]
		        };
    	   
    	   var myChart = echarts.init(document.getElementById('organizerreport')); 
    	   myChart.setOption(option);
       }
    }])

    
})