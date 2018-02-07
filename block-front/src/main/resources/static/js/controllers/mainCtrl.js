define(['app','mainService'], function(app,mainService){
      
   return app.controller('mainCtrl', ['$scope','$rootScope','$http','mainService', function ($scope,$rootScope,$http,mainService) {
	   $scope.$on('$viewContentLoaded', function() {
		    $scope.showpath();
		//   $scope.showpath();
	//	   $scope.showGraph();
		  // 	$scope.showD3();
		}); 
	   
	   
	   $scope.showpath = function(){

		   mainService.findPath($rootScope.from,$rootScope.to,$rootScope.limit).get({},function(resp){
	    		console.log("resp="+JSON.stringify(resp));

    			var links = resp.links;
    			var nodes = resp.nodes;
    			 
    		
    			//$scope.drawForce(nodes,links);
    			$scope.showD3(nodes,links);
	    	})
	   
	   }
	    
	   
	   $scope.drawForce = function(nodes,links){
		   var color = d3.scaleOrdinal(d3.schemeCategory20);
		//   var canvas =   document.querySelector("#chart");
		   var canvas=document.getElementById('chart');
		   alert("canvas="+canvas);
		   var svg = d3.select("svg");
		  var context = canvas.getContext("2d"),
		    width = canvas.width,
		    height = canvas.height;

		     
		    
		    
			var simulation = d3.forceSimulation()
			    .force("link", d3.forceLink().id(function(d) { return d.name; }))
			    .force("charge", d3.forceManyBody())
			    .force("center", d3.forceCenter(width / 2, height / 2));
	  
			  simulation
			      .nodes(nodes)
			      .on("tick", ticked);
	
			  
			  simulation.force("link")
			      .links(links);
	
			  d3.select(canvas)
			      .call(d3.drag()
			          .container(canvas)
			          .subject(dragsubject)
			          .on("start", dragstarted)
			          .on("drag", dragged)
			          .on("end", dragended));
	
			  
			  // 绘制
	            var svg_links = svg.selectAll("line")
	                .data(links)
	                .enter()
	                .append("line")
	                .style("stroke","#ccc")
	                .style("stroke-width",3);
	    
	            var svg_nodes = svg.selectAll("circle")
	                .data(nodes)
	                .enter()
	                .append("circle")
	                .attr("cx",function(d){return d.cx;})
	                .attr("cy",function(d){return d.cy;})
	                .attr("r",30)
	                .style("fill",function(d,i){
	                    return color(i);
	                });
 
	            var svg_text = svg.selectAll("text")
	                .data(nodes)
	                .enter()
	                .append("text")
	                .style("fill","#000")
	                .attr("dx",20)
	                .attr("dy",10)
	                .text(function(d){return d.name;});
			  
			  
	            console.log("转换后的nodes links数据:");
	            console.log(nodes);
	            console.log(links);
			  
			  
			  
			  
			  function ticked() {
			    context.clearRect(0, 0, width, height);
	
			    context.beginPath();
			    links.forEach(drawLink);
			    context.strokeStyle = "#aaa";
			    context.stroke();
	
			    context.beginPath();
			    nodes.forEach(drawNode);
			    context.fill();
			    context.strokeStyle = "#fff";
			    context.stroke();
			  }
	
			  function dragsubject() {
			    return simulation.find(d3.event.x, d3.event.y);
			  }
			
			 
	
			function dragstarted() {
			  if (!d3.event.active) simulation.alphaTarget(0.3).restart();
			  d3.event.subject.fx = d3.event.subject.x;
			  d3.event.subject.fy = d3.event.subject.y;
			}
	
			function dragged() {
			  d3.event.subject.fx = d3.event.x;
			  d3.event.subject.fy = d3.event.y;
			}
	
			function dragended() {
			  if (!d3.event.active) simulation.alphaTarget(0);
			  d3.event.subject.fx = null;
			  d3.event.subject.fy = null;
			}
	
			function drawLink(d) {
			  context.moveTo(d.source.x, d.source.y);
			  context.lineTo(d.target.x, d.target.y);
			}
	
			function drawNode(d) {
			  context.moveTo(d.x + 3, d.y);
			  context.arc(d.x, d.y, 3, 0, 2 * Math.PI);
			}
	   }
	   $scope.showD3 = function(nodes_data,edges_data){ 
		   edges_data.forEach(function (e) {
			    var sourceNode = nodes_data.filter(function (n) {
			                return n.name === e.source;
			            })[0],
			            targetNode = nodes_data.filter(function (n) {
			                return n.name === e.target;
			            })[0];
			    e.source = sourceNode
			    e.target = targetNode
			});
		   
			 var  width = 900,
			    height = 400;
			 
			  

		        var color = d3.scale.category20();
		        var edgeWidth = 2;
		        var r = 18;
		        
		        d3.select("#chart").selectAll("*").remove();//直接jquery的remove()就行
		        
		        
		        var svg = d3.select("#chart").append("svg")
		                .attr("width", width)
		                .attr("height", height);

		        
		        var defs = svg.append("defs");  
		        
		        var arrowMarker = defs.append("marker")  
		                                .attr("id","arrow")  
		                                .attr("markerUnits","strokeWidth")  
		                                .attr("markerWidth","7")  
		                                .attr("markerHeight","8")  
		                                .attr("viewBox","0 0 12 12")   
		                                .attr("refX","10")  
		                                .attr("refY","6")  
		                                .attr("orient","auto");  
		          
		        var arrow_path = "M2,2 L10,6 L2,10 L6,6 L2,2";  
		        arrowMarker.append("path")  
	            .attr("d",arrow_path)  
	            .attr("fill","#aaa");  
		        
		        var force = d3.layout.force()
		                .nodes(nodes_data)
		                .links(edges_data)
		                .size([width, height])
		                .linkDistance(100)
		                .friction(0.8)
		                .charge(-500)
		                .start();

		        //边
		        var links = svg.selectAll("line")
		                .data(edges_data)
		                .enter()
		                .append("line")
		                .attr("marker-end", "url(#arrow)")
		                .style("stroke", "#aaa")
		                .style("stroke-width", 2);
		        
		        
		        links.append("text").text("abc");
		        
			//节点
		        var nodes = svg.selectAll("circle")
		                .data(nodes_data)
		                .enter()
		                .append("circle")
		                .attr("r", r)
		                .style("fill", function (d, i) {
		                	if(d.category=='address'){
		                		return "#22ff00";
		                	}else{
		                		return "#00aadd";
		                	}
		                  //  return color(i);
		                })
//		                .on("click", function (d, i) {
//		                    if (i == 0) {
//		                        update();
//		                    }
//		                })
		                .call(force.drag);
			//标签
		        var nodes_labels = svg.selectAll(".nodetext")
		                .data(nodes_data)
		                .enter()
		                .append("text")
		                .attr("class","nodetext")  
		                .attr("dx", function (d, i) {
		                	return -10;
		                	//return -16 * (nodes_data[i].name.length);
		                })
 		                .attr("dy", -20)
		                .attr("fill", "#000000")
		                .style("font-size", 5)
		                .text(function (d ) {
		                	console.log("d.name="+d.name);
		                    return d.name.substring(0,5)+"...";
		                });
		        var edges_labels = svg.selectAll(".linktext")
                .data(edges_data)
                .enter()
                .append("text")
                .attr("class","linktext")  
                .attr("dx", function (d, i) {
                	 return 0;
                })
	                .attr("dy", 0)
                .attr("fill", function (d, i) {
                	console.log("d.name="+d.name);
                	if(d.name=="rollin"){
                		return "#22ff00";
                	}else{
                		return "#00aadd";
                	}
                })
                .style("font-size", 5)
                .text(function (d ) {
                	console.log("d.source.name="+d.name);
                    return d.name.substring(0,10);
                });
//		       添加提示
		        svg.append("title")  
	            .text(function(d)  
	            {  
	                return "";  
	            });  
	              
	          
	        //------------------------------------2.div提示框,通过设置透明度（opacity属性）实现 显示和隐藏     
	            //添加提示框的div  
	            var tooltip =  d3.select("#chart").append("div")  
	                        .attr("class","tooltip") //用于css设置类样式  
	                        .attr("opacity",0.0);  
	              
	            //响应事件  
	                //-鼠标移入事件  
//	            nodes.on("mouseover",function(d)  
//	            {     
//	                //设置tooltip文字  
////	                tooltip.html(d.name)  
////	                //设置tooltip的位置(left,top 相对于页面的距离)   
////	                        .attr("x",d.x)  
////	                        .attr("y",d.y-20)  
////	                        .style("opacity",1.0);  
//	            })  
//	            //--鼠标移出事件  
//	            .on("mouseout",function(d)  
//	            {  
//	                tooltip.style("opacity",0.0);  
//	            });   
	              
	            //-----------------------------------3.svg中的text标签提示框  
	              
	            nodes.on("mouseover",function(d)  
	            {  
	            	d3.select("#tooltip").remove();  
	                //var x = parseFloat(d3.select(this).attr("x")); 这个我的无法获得他们的值  
	                //var y =parseFloat(d3.select(this).attr("y")+20);  
	                  
	                var x =width/2-d.name.length;
	                var y =10;  
	                //添加标签  
	                svg.append("text")  
	                    .attr("id","tooltip")                     
	                    .attr("x",x)  
	                    .attr("y",y)   
	                    .attr("text-anchor","middle")    
	                    .attr("font-family","sans-setif")    
	                    .attr("font-size","11px")    
	                    .attr("font-weight","bold")    
	                    .attr("fill","black")    
	                    //文本内容  
	                    .text(d.name);                            
	              
	            })  
	            //鼠标移出时通过ID移除标签  
//	            .on("mouseout",function(d)  
//	            {  
//	                d3.select("#tooltip").remove();  
//	            });    
		        
	            
	            links.on("mouseover",function(d)  
	    	            {  
	    	                //var x = parseFloat(d3.select(this).attr("x")); 这个我的无法获得他们的值  
	    	                //var y =parseFloat(d3.select(this).attr("y")+20);  
	    	                  
	    	                var x =width/2-d.name.length;
	    	                var y =10;  
	    	                //添加标签  
	    	                svg.append("text")  
	    	                    .attr("id","tooltip")                     
	    	                    .attr("x",x)  
	    	                    .attr("y",y)   
	    	                    .attr("text-anchor","middle")    
	    	                    .attr("font-family","sans-setif")    
	    	                    .attr("font-size","11px")    
	    	                    .attr("font-weight","bold")    
	    	                    .attr("fill","black")    
	    	                    //文本内容  
	    	                    .text(d.name);                            
	    	              
	    	            })  
	    	            //鼠标移出时通过ID移除标签  
	    	            .on("mouseout",function(d)  
	    	            {  
	    	                d3.select("#tooltip").remove();  
	    	            });    

			//运动刷新
		        force.on("tick", function (d) {
		            links.attr("x1", function (d) {
		                var distance = Math.sqrt((d.target.y - d.source.y) * (d.target.y - d.source.y) +
		                        (d.target.x - d.source.x) * (d.target.x - d.source.x));
		                var x_distance = (d.target.x - d.source.x) / distance * r;
		                return d.source.x + x_distance;
		            }).attr("y1", function (d) {
		                var distance = Math.sqrt((d.target.y - d.source.y) * (d.target.y - d.source.y) +
		                        (d.target.x - d.source.x) * (d.target.x - d.source.x));
		                var y_distance = (d.target.y - d.source.y) / distance * r;
		                return d.source.y + y_distance;
		            }).attr("x2", function (d) {
		                var distance = Math.sqrt((d.target.y - d.source.y) * (d.target.y - d.source.y) +
		                        (d.target.x - d.source.x) * (d.target.x - d.source.x));
		                var x_distance = (d.target.x - d.source.x) / distance * r;
		                return d.target.x - x_distance;
		            }).attr("y2", function (d) {
		                var distance = Math.sqrt((d.target.y - d.source.y) * (d.target.y - d.source.y) +
		                        (d.target.x - d.source.x) * (d.target.x - d.source.x));
		                var y_distance = (d.target.y - d.source.y) / distance * r;
		                return d.target.y - y_distance;
		            });


		            nodes.attr("cx", function (d) {//圆心x坐标
		                return d.x;
		            }).attr("cy", function (d) {
		                return d.y;
		            });

		            nodes_labels.attr("x", function (d) { 
		            	//console.log("d.source.x="+d.source.x);
		                return d.x;
		            });
		            nodes_labels.attr("y", function (d) {
		                return d.y;
		            });
		            edges_labels.attr("x", function (d) { 
		            	//console.log("d.source.x="+d.source.x);
		                return (d.source.x+d.target.x)/2;
		            });
		            edges_labels.attr("y", function (d) {
		                return (d.source.y+d.target.y)/2;
		            });
//		            links_labels.attr("x", function (d) {
//		            	console.log("link.x="+d.target.x);
//		                return d.target.x;
//		            });
//		            links_labels.attr("y", function (d) {
//		            	console.log("link.y="+d.target.y);
//		                return d.target.y;
//		            });
		        });

			//用于产生不同颜色的节点
		        var colorIndex = 8;

		        //添加节点更新
		        function update() {
		            nodes_data.push({'name': 'xxx'});
		            edges_data.push({'source': 0, 'target': nodes_data.length - 1});

		            links = links.data(force.links());

		            links.enter()
		                    .append("line")
		                    .style("stroke", "#ccc")
		                    .style("stroke-width", 2);

		            links.exit().remove();

		            nodes = nodes.data(force.nodes());
		            nodes.enter().append("circle")
		                    .attr("r", r)
		                    .style("fill", color(colorIndex++))
		                    .call(force.drag);

		            nodes.exit().remove();

		            force.start();
		        }

		        //回车事件
		        $(document).keydown(function(e) {
		            if(e.which == 13) {
		               update();
		            }
		        });
		        
//			 var svg_edges = svg.selectAll("line")
//		     .data(edges)
//		     .enter()
//		     .append("line")
//		     .style("stroke","#ccc")
//		     .style("stroke-width",1);
//		 
//		 var color = d3.scale.category20();
//		 
//		//画线  
//		 var svg_edges = svg.selectAll("line")  
//		 .data(edges)  
//		 .enter()  
//		 .append("line")  
//		 .style("stroke","#ccc")  
//		 .style("stroke-width",1);  
//		   
//		 var color = d3.scale.category20();  
//		 //画点  
//		 var svg_nodes = svg.selectAll("circle")  
//		 .data(nodes)  
//		 .enter()  
//		 .append("circle")  
//		 .attr("r",10)  
//		 .style("fill",function(d,i){  
//		     return color(i);  
//		 })  
//		 .call(force.drag);  
//		 
//		 force.on("tick", function () {  
//			    svg_edges.attr("x1",function(d){return d.source.x;});  
//			    svg_edges.attr("y1", function (d){return d.source.y;});  
//			    svg_edges.attr("x2",function(d){return d.target.x;});  
//			    svg_edges.attr("y2",function(d){return d.target.y});  
//			  
//			    svg_nodes.attr("cx",function(d){return d.x;});  
//			    svg_nodes.attr("cy",function(d){return d.y});  
//			});  
	   }
	   
	   
	   
	   
	   $rootScope.from = "1FTyQf5AbmsbdbonvAGQit9b8gcoscg15";
	   $rootScope.to = "1J4A9otDipuF6DyFPGFKK2cesZVph8bEjW";
	//   $rootScope.limit = 1;
	   $rootScope.searchClick = function(){
		   	$scope.showpath();
  	 }	
	   
	   
	   
	   $scope.findPath = function(from,to,limit){
		   mainService.findPath(from,to,limit).get({},function(resp){
	    		console.log("resp="+JSON.stringify(resp));
	    		var links = resp.links;
	    		var nodes = resp.nodes;
	    		var myChart = echarts.init(document.getElementById("graphchart"), "green");
	    		myChart.on('click', function (params) {
	    		    console.log("click="+params.name);
	    		});
	 		   option = {
	 				  color: ['#8fc31f','#f35833','#00ccff','#ffcc00'],
	 				   title: {
	 				        text: '交易图谱',
	 				        x:'left'
	 				    },
	 				  
	 				    tooltip: {
	 				        formatter: function(x) {
	 				            return x.data.name;
	 				        }
	 				    },
	 				    legend: [{ 
	 				        width:140,      //图行例组件的宽度,默认自适应
	 				        x : 'right',   //图例显示在右边 
	 				        tooltip: {
	 				            show: true
	 				        }, 
	 				        selectedMode: 'false',
	 				        data: [ 'address','transaction' ] 
	 				    	
	 				    }], 
	 				    series: [{
	 				       type: 'graph',
	 				       layout: 'force',
	 				       symbol: 'path://M19.300,3.300 L253.300,3.300 C262.136,3.300 269.300,10.463 269.300,19.300 L269.300,21.300 C269.300,30.137 262.136,37.300 253.300,37.300 L19.300,37.300 C10.463,37.300 3.300,30.137 3.300,21.300 L3.300,19.300 C3.300,10.463 10.463,3.300 19.300,3.300 Z',
	 				       symbolSize: [160,40],
	 				       edgeSymbol: ['circle', 'arrow'],
	 				       symbolRotate:0,
	 				       roam: true, 
	 				       hoverAnimation:true,
	 				       focusNodeAdjacency:true, 
	 				       edgeSymbolSize: [10, 10],
	 				        
	 				        force: {
	 				            repulsion: 2500,
	 				            edgeLength: [30, 90]
	 				        },
	 				        draggable: true,//如果不设置该值，则拖动图时是整体跟着一起动，而不是改变某一个节点的位置,可以单独设置某个结点的  draggable: true
	 				        
	 				        lineStyle: {
	 				            normal: {
	 				                width: 1,
	 				                color: '#ff565b',
	 				                image:''
	 				            }
	 				        },
	 				        edgeLabel: {
	 				            normal: {
	 				            	//show: true, //隐藏边的描述
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
	 				                	if(x.data.name.length>36){
	 				                		
	 				                		return x.data.name.substring(0,18)+"\n"+x.data.name.substring(18,36)+"...";
	 				                	}else{
	 				                		return x.data.name.substring(0,18)+"\n"+x.data.name.substring(18);
	 				                	}
	 				                }
	 				            }
	 				        },
	 				    //   symbol: 'image://https://goss4.vcg.com/creative/vcg/800/version23/VCG41172668056.jpg',
	 				       
	 				        data: nodes,
	 				        links: links,
	 				        categories: [{
	 				            'name': 'transaction'
	 				        }, {
	 				            'name': 'address'
	 				        } ],
	 				        lineStyle: {
	 				            normal: {
	 				                color: 'source',
	 				                curveness: 0,
	 				                type: "solid"
	 				            }
	 				        }
	 				    }]
	 				};
	 		   console.log("option="+JSON.stringify(option));
	 		   myChart.setOption(option);
	    	})
	   }
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
				        data: [{name:'凯瑟琳.斯塔克'},'艾德.斯塔克','罗伯.斯塔克' ] 
				    	
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
				            	 
				            }
				        },
				        lineStyle: {
				            normal: {
				                width: 1,
				                color: '#4b565b',
				                show:true

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
				            "category": "罗伯.斯塔克",
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
				            'name': '罗伯.斯塔克'
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
		   console.log("option2="+JSON.stringify(option));
		   myChart.setOption(option);
	   }
    }])

})