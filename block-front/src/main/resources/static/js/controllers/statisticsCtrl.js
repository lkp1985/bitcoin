define(['app','statisticsService'], function(app,statisticsService){
      
   return app.controller('statisticsCtrl', ['$scope','$rootScope', '$routeParams','$q','$http','statisticsService', 
	   function ($scope,$rootScope, $routeParams,$q,$http,statisticsService) {
	   
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
    	   $scope.getallnum();
    	   $scope.getmap();
    	   $scope.yearTrend();
	       $scope.sexTrend();
 	       $scope.sexDistribute();
 	       $scope.sectorDistribute();
 	       $scope.attentionDistribute();
	       $scope.ageDistribute();
	       $scope.educationDistribute();
	       $scope.organizerDistribute();
	       if($scope.type=="资讯阅读"){
	    	   $scope.getDateScope();
	    	   $scope.userReadTypeFacet();
	       }
       }
       
       $scope.getDateScope = function(){
    	   var condition = $scope.filtcondition();
    	   statisticsService.getDateFacet(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
    		   if(resp.code==1){
    			  
    			   var hourData = resp.attribute.result.hour;
    			   var monthData = resp.attribute.result.month;
    			   var yearData = resp.attribute.result.year;
    			   console.log("hourData data="+JSON.stringify(hourData));
    			   $scope.getYearScope(yearData);
    			   $scope.getDayScope(hourData);
    		   }
    		   
    	   });
       }
       $scope.getYearScope = function(yearData){
    	   option = {
    			    title : {
    			        text : '一年阅读量走势', 
    			    },
    			    tooltip : {
    			        trigger: 'item',
    			        formatter : function (params) {
    			            var date = new Date(params.value[0]);
    			            data = date.getFullYear() + '-'
    			                   + (date.getMonth() + 1) + '-'
    			                   + date.getDate() + ' ' ;
    			            return data + '<br/> 阅读量:'
    			                   + params.value[1]
    			        }
    			    },
    			    toolbox: {
    			        show : true,
    			        feature : {
    			            mark : {show: true},
    			            dataView : {show: true, readOnly: false},
    			            restore : {show: true},
    			            saveAsImage : {show: true}
    			        }
    			    },
    			    dataZoom: {
    			        show: true,
    			        start : 70
    			    },
    			    legend : {
    			        data : ['阅读量']
    			    },
    			    grid: {
    			        y2: 80
    			    },
    			    xAxis : [
    			        {
    			            type : 'time',
    			            splitNumber:10
    			        }
    			    ],
    			    yAxis : [
    			        {
    			            type : 'value'
    			        }
    			    ],
    			    series : [
    			        {
    			            name: '阅读量',
    			            type: 'line',
    			            showAllSymbol: true,
    			            symbolSize: function (value){
    			                return Math.round(value[2]/10) + 2;
    			            },
    			            data: $scope.getyeardata(yearData)
    			        }
    			    ]
    			};
    	   
    	   var myChart = echarts.init(document.getElementById('yearScope')); 
    	   myChart.setOption(option);
       }
       $scope.getDayScope = function(dayData){
    	   option = {
    			    title : {
    			        text : '阅读量时段分布', 
    			    },
    			    tooltip : {
    			        trigger: 'item',
    			        formatter : function (params) {
    			            var date = new Date(params.value[0]);
    			            data = date.getHours() + ':00:00';
    			            return data + '<br/> 阅读量:'
    			                   + params.value[1]
    			        }
    			    },
    			    toolbox: {
    			        show : true,
    			        feature : {
    			            mark : {show: true},
    			            dataView : {show: true, readOnly: false},
    			            restore : {show: true},
    			            saveAsImage : {show: true}
    			        }
    			    },
    			    dataZoom: {
    			        show: true,
    			        start : 70
    			    },
    			    legend : {
    			        data : ['阅读量']
    			    },
    			    grid: {
    			        y2: 80
    			    },
    			    xAxis : [
    			        {
    			            type : 'time',
    			            splitNumber:10
    			        }
    			    ],
    			    yAxis : [
    			        {
    			            type : 'value'
    			        }
    			    ],
    			    series : [
    			        {
    			            name: '阅读量',
    			            type: 'line',
    			            showAllSymbol: true,
    			            symbolSize: function (value){
    			                return Math.round(value[2]/10) + 2;
    			            },
    			            data: $scope.getdaydata(dayData)
    			        }
    			    ]
    			};
    	   
    	   var myChart = echarts.init(document.getElementById('hourScope')); 
    	   myChart.setOption(option);
       }
       $scope.getyeardata = function(valueData){
    	   var d = [];
           var len = 0;
           var now = new Date();
           var value;
           angular.forEach(valueData, function(value,key){
        	   d.push([
        		   key,
        		   value,
        		   value/1000
               ]);
           }) 
           console.log("d======"+JSON.stringify(d));
          // d = $scope.gettestdata();
           console.log("dd======"+JSON.stringify(d));
           return d;
       }
       $scope.getdaydata = function(valueData){
    	   var d = [];
           var len = 0;
           var now = new Date();
           var value;
           angular.forEach(valueData, function(value,key){
        	   d.push([
        		   key,
        		   value,
        		   value/10000
               ]);
           })  
           console.log("hour======"+JSON.stringify(d));
           return d;
       }
       $scope.gettestdata = function(){
    	   var d = [];
           var len = 0;
           var now = new Date();
           var value;
           while (len++ < 200) {
               d.push([
                   new Date(2014, 9, 1, 0, len * 10000),
                   (Math.random()*30).toFixed(2) - 0,
                   (Math.random()*100).toFixed(2) - 0
               ]);
           }
           console.log("testd======"+d);
           return d;
       }
       $scope.getallnum = function(){
    	   $scope.tiannum = 0;
    	   $scope.suggestnum = 0;
    	   $scope.socialnum = 0;
    	   $scope.cmsbrowsenum = 0;
    	   statisticsService.getallnum( $scope.areacode,$scope.startdate,$scope.enddate,$scope.cppccLevel,$scope.selectCmsType).get({},function(resp){
    		   console.log("getallnum="+JSON.stringify(resp));
    		   if(resp.code==1){
    			   $scope.tiannum = resp.attribute.result.tianNum;
    			   $scope.suggestnum = resp.attribute.result.suggestNum;
    			   $scope.socialnum = resp.attribute.result.socialNum;
    			   $scope.cmsbrowsenum = resp.attribute.result.cmsBrowseNum;
    		   }
    	   })
       }
       var chart = echarts.init(document.getElementById('tianMap'));  
       $scope.getmap = function(){ 
    	   
    	   $.get('js/mapjson/'+$scope.areacode+'.json', function (yCjson) {  
    		  // console.log("json="+JSON.stringify(yCjson));
    		   var condition= $scope.filtcondition();
    		   console.log("condition = " + condition);
    		   statisticsService.getTiAnMap(condition, $scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
    			   if(resp.code==1){
    				   var num = resp.attribute.num;
    				   
    				   if($scope.cppccLevel!=null && $scope.cppccLevel.length>0){
    					   if($scope.selectCmsType.length>0){ 
    			    		   title = $scope.cppccLevel+' ['+$scope.selectCmsType+"] 阅读量共有:"+num;
    			    	   }else{
    			    		   title = $scope.cppccLevel+' ['+$scope.type+"] 数共有:"+num;
    			    	   }
    				   }else{
    					   if($scope.selectCmsType.length>0){
    			    		   title = $scope.areaname+$scope.selectCmsType+"阅读量分布"
    			    	   }else{
    			    		   title = $scope.areaname+$scope.type+$scope.selectCmsType+"数分布"
    			    	   }
    				   } 
    				   
    				   var valueData = resp.attribute.result;
    				   var maxdata = 0;
    				   angular.forEach(valueData, function(data,index,array){
    					   if(index==0){//不取第一条，因为第一条都是省级或者市级政协
    						    
    					   }else{
    						   if( maxdata < data.value){
        						   maxdata = data.value;
        					   }
    					   }
    					   
    				   })
    				   console.log("mapdata="+JSON.stringify(valueData));
    				   console.log("maxdata="+maxdata);
    	               option = {  
    	                   title: {  
    	                       text: title,  
    	                       x:'center'  
    	                   },  
    	                   tooltip : {
    	                       trigger: 'item',
    	                       formatter: '{b}<br/>'+$scope.type+'数: {c}'
    	                   },
    	                   dataRange:{  
    	                       min:0,  
    	                       max:maxdata+1,  
    	                       text:['高','低'],  
    	                       realtime:true,  
    	                       calculable:true,  
    	                       color:['red','orange','yellow','#EEE'],
    	                   },  
    	                   series:[  
    	                       {  
    	                           name:'测试数量',  
    	                           type:'map',  
    	                           
    	                           map:$scope.areaname,  
    	                           mapLocation:{  
    	                               y:60  
    	                           },
    	                           label: {
    	                        	   normal: {
    	                        		   show: true
    	                    		   },
    	                    		   emphasis: {
    	                    			   show: true
    	                    		   }
    	                           },
    	                           itemStyle:{
    	                               normal:{label:{show:true}},
    	                               emphasis:{label:{show:true}}
    	                           },
    	                           data:valueData
    	                       }  
    	                   ],  
    	                     
    	               };  
    	               chart.setOption(option,true);
    				   
    			   }
    		   })
    		   
    		   echarts.registerMap($scope.areaname, yCjson);    
    	   });
       }
       
       $scope.tabClick = function(name,index){
       		var itemdata = $api.domAll('.item');
       		for(var i in itemdata){
       			$api.removeCls(itemdata[i],'selecton');
       		}
   			$api.addCls(itemdata[index],'selecton');
   			$api.removeCls($api.dom('.title-name'),'none');
	   		$api.addCls($api.dom('.item_check'),'none');
	   		$api.css($api.byId('myCanvas'),'margin-top:10px;');
	   		jQuery("#content_footer").removeClass("none");
	   		jQuery("#content_footer2").addClass("none");
    	   if(name=="社情民意"){
    		   //jQuery('#underleft').hide(); 
    		   jQuery('#go1').show();
    		   jQuery('#go2').hide();
    		   jQuery('#go4').hide();
    		   jQuery('#go3').hide();
    		   jQuery('#go5').show();
    		   jQuery('#go6').hide();
    		   jQuery('#go7').hide();
    		   jQuery('#go8').hide(); 
    		   jQuery('#go11').hide();
    		   jQuery('#go12').hide();
    		   
    	   }else if(name=='微建议'){
    		   jQuery('#go1').hide(); 
    		   jQuery('#go2').show();
    		   jQuery('#go3').show();
    		   jQuery('#go4').show();
    		   jQuery('#go5').show();
    		   jQuery('#go6').show();
    		   jQuery('#go7').hide();
    		   jQuery('#go8').show();
    		   jQuery('#go11').hide();
    		   jQuery('#go12').hide();
     		   jQuery('#go13').hide();
     		   jQuery('#go14').hide();
    	   }else if(name=='资讯阅读'){
    	   		$api.removeCls($api.dom('.item_check'),'none');
    	   		$api.addCls($api.dom('.title-name'),'none');
    	   		$api.css($api.byId('myCanvas'),'margin-top:40px');
//    	   		jQuery("#content_footer2").removeClass("none");
//    	   		jQuery("#content_footer").addClass("none");
    	   	   jQuery('#go1').hide();
     		   jQuery('#go2').hide();
     		   jQuery('#go4').hide();
     		   jQuery('#go3').hide();
     		   jQuery('#go5').hide();
     		   jQuery('#go6').hide();
     		   jQuery('#go7').hide();
     		   jQuery('#go8').hide(); 
     		   jQuery('#go11').show();
     		   jQuery('#go12').show();
     		   jQuery('#go13').show();
     		   jQuery('#go14').show();
    	   }else if(name=='提案'){
    		   jQuery('#go4').show();
    		   jQuery('#go1').show(); 
    		   jQuery('#go2').show();
    		   jQuery('#go3').show();
    		   jQuery('#go5').show();
    		   jQuery('#go6').show();
    		   jQuery('#go7').show();
    		   jQuery('#go8').show();
    		   jQuery('#go11').hide();
    		   jQuery('#go12').hide();
     		   jQuery('#go13').hide();
     		   jQuery('#go14').hide();
    	   } 
    	   $scope.type=name;
    	   $scope.selectCmsType = "";
    	   console.log("type="+$scope.type);
    	   $scope.statistics();
    	//   $scope.getDateScope();
       }
       
       var now=-1;
       $scope.selectCmsType="";
       $scope.itemClick = function(name,index){
       		console.log(now+'=='+index+",name="+name);
       		var itemdata = $api.domAll('.item_button_name');
       		if(now == index){
       			now=-1;
       			$api.removeCls(itemdata[index],'item_selecton');
       			$scope.selectCmsType = "";
       		}else{
	       		for(var i in itemdata){
	       			$api.removeCls(itemdata[i],'item_selecton');
	       		}
	   			$api.addCls(itemdata[index],'item_selecton');
	   			now = index;
	   			$scope.selectCmsType = name;
       		}
       		$scope.statistics();
       }
       
       
       //关注点热图
       $scope.getWordCloud = function(worddata,canvas,idname,color){  
    	   try {
    		   console.log("worddata="+JSON.stringify(worddata));
    		   jQuery('#'+idname).text("");
    		   angular.forEach(worddata, function(data,index,array){
    			   console.log("data.name="+data.name+",data.value="+data.value);
    			   var dataname = data.name;
    			   if(data.name.length>10){
    				   dataname  = data.name.substring(0,10)+"...";
    			   }
    			   var atext = jQuery("<a href='javascript:;'></a>").text(dataname+"("+data.value+")");
    			   console.log("atext="+atext.text());
    			   jQuery('#'+idname).append(atext);
    		   });
    			var i, et = jQuery('#'+idname).childNodes; 
    			TagCanvas.Start(canvas, idname, {
    				textColour: color,
    				textFont:'Helvetica',
    				outlineColour: '#f74444',
    				textHeight:15,
    				reverse: false,
    				depth: 0.8,
    				dragControl: true,
    				decel:0.95,
    				maxSpeed: 0.01,
    				initial: [-0.2, 0]
    			}); 
    			
    			 
    		} catch (e) {
    			// something went wrong, hide the canvas container
    			//document.getElementById('myCanvasContainer').style.display = 'none';
    		}
       }
       
        
       
       $scope.yearTrend = function(){ 
		   var condition = $scope.filtcondition();
		   if (canceler) canceler.resolve();
           canceler = $q.defer();
		   statisticsService.yearTrend(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   var nameData = resp.attribute.result.name;
				   var valueData = resp.attribute.result.value;
				   console.log("yearTrend resp="+JSON.stringify(resp));
				   var myChart = echarts.init(document.getElementById('tianTrend')); 
				   option = {
						    title: {text: $scope.type+"数走势"},
						    tooltip: {trigger: "axis"},
						    radius: "35%",
						    //legend: {data: ["数量"]},
						    toolbox: {show: false,
						        feature: {
						            mark: {show: true},
						            dataView: {show: true,readOnly: true},
						            magicType: {show: false,type: ["line", "bar"]},
						            restore: {show: true},
						            saveAsImage: {show: true}
						        }
						    },
						    calculable: true,
						    xAxis: [{type: "category",boundaryGap: true,data: nameData}],
						    yAxis: [{type: "value",name: "件"}],
						    series:[{name: "数量",type: "bar",data: valueData,barWidth: 15}]
						};

			        // 为echarts对象加载数据 
			        myChart.setOption(option); 
			   }
		   })
	   }
	   $scope.sexTrend = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.sexTrend(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   var NameData = resp.attribute.result.famale.name; 
				   var famaleValueData = resp.attribute.result.famale.value;
				   var menValueData = resp.attribute.result.men.value;
				   console.log("sexTrend resp="+JSON.stringify(resp));
				   var myChart_sex = echarts.init(document.getElementById('sexTrend')); 
			        option_sex = {
			        	    title: {
			        	        text: "男女委员"+$scope.type+"数走势"
			        	    },
			        	    tooltip: {
			        	        trigger: "axis"
			        	    },
			        	    legend: {
			        	    	x: "right",
			        	        data: ["男", "女"]
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
			        	            data: NameData
			        	        }
			        	    ],
			        	    yAxis: [
			        	        {
			        	            type: "value"
			        	        }
			        	    ],
			        	    series: [
			        	        {
			        	            name: "男",
			        	            type: "bar",
			        	            data: menValueData
			        	        },
			        	        {
			        	            name: "女",
			        	            type: "bar",
			        	            data: famaleValueData
			        	        }
			        	    ]
			        	};
			        
			        // 为echarts对象加载数据 
			        myChart_sex.setOption(option_sex); 
			   }
		   })
	   }
	   $scope.sexDistribute = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.sexDistribute(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   console.log("sexDistribute resp="+JSON.stringify(resp)); 
				   var valueData = resp.attribute.result;
				   var myChart_sexDistribute = echarts.init(document.getElementById('sexDistribute')); 
			        option_sexDistribute = {
			        	    title: {
			        	        text: "提交"+$scope.type+"的委员性别分布 ",
			        	        x: "left",
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
			        	    legend: {
			        	        orient: "horizontal",
			        	        x: "right",
			        	        data: ["男", "女"]
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
			        	            name: "委员性别",
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
			        	            data: valueData
			        	        }
			        	    ]
			        	};
			        
			        // 为echarts对象加载数据 
			        myChart_sexDistribute.setOption(option_sexDistribute); 
			   }
		   })
		   
	   }
	   $scope.sectorDistribute = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.sectorDistribute(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   console.log("sectorDistribute resp="+JSON.stringify(resp)); 
				   var Data = resp.attribute.result;
				   var nameData = [];
				   var valueData = [];
				   var length = Data.length>16?16:Data.length;
				   for(var i=0;i<length;i++){
				   		if(Data[i].name != '其他'){
					   		nameData.push(Data[i].name);
					   		valueData.push(Data[i].value);
				   		}
				   		
				   }
				   var myChart_sectorDistribute = echarts.init(document.getElementById('sectorDistribute')); 
			        option_sectorDistribute = {
		        		title: {
		        			text: "提交"+$scope.type+"的委员界别分布 ",
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
			        // 为echarts对象加载数据 
			        myChart_sectorDistribute.setOption(option_sectorDistribute); 
			   }
		   })
		   
	   }
	   $scope.educationDistribute = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.educationDistribute(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   var valueData = resp.attribute.result;
				   console.log("education value="+JSON.stringify(valueData));
				   var myChart_educationDistribute = echarts.init(document.getElementById('educationDistribute')); 
			        option_educationDistribute = {
			        		title: {text: "提交"+$scope.type+"委员学历分布 ",x: "left"},
			        		tooltip: {trigger: "item",formatter: "{a} <br/>{b} : {c} ({d}%)"},
			        	//	legend: {
//			        			orient: "vertical",
//			        			x: "left",
//			        			data: ["男", "女"]
//			        		},
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
			        				name: $scope.type+"数量",
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
			        				data: valueData
			        			}
			        			]
			        };

			        // 为echarts对象加载数据 
			        myChart_educationDistribute.setOption(option_educationDistribute); 
			   }
		   })
		  
	   }
	   $scope.attentionDistribute = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.attentionDistribute(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   console.log("attentionDistribute resp="+JSON.stringify(resp));
				   var Data = resp.attribute.result;
				   
				   var nameData = [];
				   var valueData = [];
				   var length = Data.length>16?16:Data.length;
				   for(var i=0;i<length;i++){
				   		if(Data[i].name != '其他'){
					   		nameData.push(Data[i].name);
					   		valueData.push(Data[i].value);
				   		}
				   }
				   console.log("attentionDistribute valueData="+JSON.stringify(valueData));
				   
				   var myChart_attentionDistribute = echarts.init(document.getElementById('attentionDistribute')); 
			        option_attentionDistribute = {
					    title: {
					        text: "委员关注点分布",
					        
					    },
					    tooltip: {
					        trigger: "axis"
					    },
					    legend: {
					    	x: "right",
					        data: ['关注点数']
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
						        }  
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
			        // 为echarts对象加载数据 
			        myChart_attentionDistribute.setOption(option_attentionDistribute); 
			        
			        //填充关注点词云图
			        if($scope.type=="资讯阅读"){
			        	var titleRead  = Data.titleRead;
			        	var userRead  = Data.userRead;
			        	console.log("titleRead="+JSON.stringify(titleRead));
			        	console.log("userRead="+JSON.stringify(userRead));
			        	
			        	$scope.getWordCloud(titleRead,'myCanvas','tags','#DB9B04');
			        //	$scope.getWordCloud(userRead,'myCanvas3','tags3','#33556');
			        	$scope.showUserRead(userRead);
			        }else{
			        	$scope.getWordCloud(Data,'myCanvas','tags','#DB9B04');
			        }
			   }
		   })
	   }
	   
	   
	  $scope.userReadTypeFacet = function(){
		  var condition = $scope.filtcondition();
		   statisticsService.userTypeDistribute(condition,$scope.startdate,$scope.enddate).get({},function(resp){
			   if(resp.code==1){
				   console.log("$scope.ageDistribute  resp="+JSON.stringify(resp)); 
				   var valueData = resp.attribute.result;
				   console.log(" valueData="+JSON.stringify(valueData));
				   var myChart_ageDistribute = echarts.init(document.getElementById('userType')); 
			       option_ageDistribute = {
			        		title: {
			        			text: "阅读用户类型统计 ",
			        			x: "left"
			        		},
			        		tooltip: {
			        			trigger: "item",
			        			formatter: "{a} <br/>{b} : {c} ({d}%)"
			        		},
			        	//	legend: {
//			        			orient: "vertical",
//			        			x: "left",
//			        			data: ["男", "女"]
//			        		},
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
			        				name: "阅读用户类型",
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
			        				data: valueData
			        			}
			        			]
			        };
			       
			       // 为echarts对象加载数据 
			       myChart_ageDistribute.setOption(option_ageDistribute); 
			   }
		   })
	  }
		   
	  $scope.showUserRead = function(Data){
		  var nameData = [];
		   var valueData = [];
		   var length = Data.length>16?16:Data.length;
		   for(var i=0;i<length;i++){
		   		if(Data[i].name != '其他'){
			   		nameData.push(Data[i].name);
			   		valueData.push(Data[i].value);
		   		}
		   }
		   console.log("attentionDistribute valueData="+JSON.stringify(valueData));
		   
		   var myChart_attentionDistribute = echarts.init(document.getElementById('userReadCount')); 
	        option_attentionDistribute = {
	        		title: {
	        			text: "用户阅读次数统计 ",
	        			x: "left"
	        		},
	        		tooltip: {
	        			trigger: "item",
	        			formatter: "{a} <br/>{b} : {c} ({d}%)"
	        		},
	        	//	legend: {
//	        			orient: "vertical",
//	        			x: "left",
//	        			data: ["男", "女"]
//	        		},
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
	        				name: "资讯阅读次数",
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
	        				data: Data
	        			}
	        			]
	        };
	        // 为echarts对象加载数据 
	        myChart_attentionDistribute.setOption(option_attentionDistribute); 
	  }
	   
	  
	  
	   $scope.ageDistribute = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.ageDistribute(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   console.log("$scope.ageDistribute  resp="+JSON.stringify(resp)); 
				   var valueData = resp.attribute.result;
				   console.log(" valueData="+JSON.stringify(valueData));
				   var myChart_ageDistribute = echarts.init(document.getElementById('ageDistribute')); 
			       option_ageDistribute = {
			        		title: {
			        			text: "提交"+$scope.type+"委员年龄分布 ",
			        			x: "left"
			        		},
			        		tooltip: {
			        			trigger: "item",
			        			formatter: "{a} <br/>{b} : {c} ({d}%)"
			        		},
			        	//	legend: {
//			        			orient: "vertical",
//			        			x: "left",
//			        			data: ["男", "女"]
//			        		},
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
			        				name: "委员人员数量",
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
			        				data: valueData
			        			}
			        			]
			        };
			       
			       // 为echarts对象加载数据 
			       myChart_ageDistribute.setOption(option_ageDistribute); 
			   }
		   })
	   } 
	  
	   
	   $scope.organizerDistribute = function(){
		   var condition = $scope.filtcondition();
		   statisticsService.organizerDistribute(condition,$scope.startdate,$scope.enddate,$scope.type).get({},function(resp){
			   if(resp.code==1){
				   console.log("$scope.organizerDistribute  resp="+JSON.stringify(resp)); 
				   var valueData = resp.attribute.result;
				   console.log("organizerDistribute valueData="+JSON.stringify(valueData));
				   var myChart_organizerDistribute = echarts.init(document.getElementById('organizerDistribute')); 
			       option_organizerDistribute = {
			        		title: {
			        			text: "主办单位统计 ",
			        			x: "left"
			        		},
			        		tooltip: {
			        			trigger: "item",
			        			formatter: "{a} <br/>{b} : {c} ({d}%)"
			        		},
			        	//	legend: {
//			        			orient: "vertical",
//			        			x: "left",
//			        			data: ["男", "女"]
//			        		},
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
			        				name: "提案数量",
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
			        				data: valueData
			        			}
			        			]
			        };
			       
			       // 为echarts对象加载数据 
			       myChart_organizerDistribute.setOption(option_organizerDistribute); 
			   }
		   })
	   } 
	   $scope.filtcondition = function(){
		   if($scope.startdate!=null&&$scope.enddate!=null && $scope.startdate!=undefined && $scope.enddate!=undefined){
			    
		   }
		   var condition = ""	;
		   
		   if($scope.cppccLevel!=null && $scope.cppccLevel!=undefined && $scope.cppccLevel.length>0){
			   condition +="cppccLevel:"+$scope.cppccLevel;
		   }else{
			   condition += "areacode:"+$scope.areacode;
		   }
		   if($scope.selectCmsType.length>0){
			   condition +=" groupName:"+$scope.selectCmsType;
		   }
		   return condition;
	   }
       
       
       
       
       
    // 新版智能分析页面
	   var bg1=true
	   $scope.isshowshi =function (type,areacode,areaname,cppccLevel){
			$scope.collapse(type);
			$scope.areacode = areacode;
			$scope.areaname=areaname;
			$scope.cppccLevel = cppccLevel;
			//$scope.getmap( areaname+"提案热力图",'areacode:'+areacode);
			$scope.statistics();
		}
		
	   var bgvalue =[false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false];
	   $scope.collapse = function(index){
		   console.log("index="+index+",value="+bgvalue[index]);
		   for(var i=1; i<=14;i++){
			   console.log("index="+index+",value="+bgvalue[index]);
			   if(index==0){//不折叠
				   
			   }else{
				   for(var i=1; i<=14;i++){
					   var bg = "bg"+i;
					  console.log("bg="+bg);
					   if(index==i){
						  
						   if(bgvalue[index]){
								$api.addCls(document.getElementById(bg),'none');
								$api.domAll('.shi img')[i-1].src='views/wepPage/img/images_bg/sanjiao_close.png';
								bgvalue[index] = !bgvalue[index];
							}else{
								$api.removeCls(document.getElementById(bg),'none');
								$api.domAll('.shi img')[i-1].src='views/wepPage/img/images_bg/sanjiao_open.png';
								bgvalue[index] = !bgvalue[index];
							}
						   
					   }else{//其他的都折叠
						   bgvalue[i]=false;
						   $api.addCls(document.getElementById(bg),'none');
						   $api.domAll('.shi img')[i-1].src='views/wepPage/img/images_bg/sanjiao_close.png';
					   }
				   }
			   }
		   }
		   
	   }
	   
	   $scope.startdate = null;
	   $scope.enddate = null;
		lay('#version').html('-v'+ laydate.v);

		//执行一个laydate实例
		laydate.render({
		  elem: '#inputdate1', //指定元素
		  calendar: true,
		  ready: function(date){
		    console.log(date); //得到初始的日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
		  },
		  done: function(value, date, endDate){
			  $scope.startdate = value;
		  }
		});
		laydate.render({
		  elem: '#inputdate2', //指定元素
		  calendar: true,
		  ready: function(date){
		    console.log(date); //得到初始的日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
		  },
		  done: function(value, date, endDate){
			  $scope.enddate = value;
			  $scope.statistics();
		  }
		});     
       
       
    }])

})