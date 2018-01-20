define(['app','socialMainService'], function(app,socialMainService){
      
   return app.controller('socialMainCtrl', ['$scope','$rootScope','$http','socialMainService', function ($scope,$rootScope,$http,socialMainService) {
	   $scope.$on('$viewContentLoaded', function() {
			$scope.searchResult("*");
			
//			$scope.timer = $interval(function() {
//				$scope.getLastInfo();
//			}, 2000);
			
		}); 
		
	    $scope.getHis = function(){
	    	socialMainService.getSearchHis().get({},function(resp){
	    		$scope.searchHis = resp.attribute.result;
	    		console.log("searchHis="+$scope.searchHis)	;
	    	})
	    }
		$scope.searchResult = function(content,field,num) {
			 if(content!="*"){
				 $scope.searhContext = content;
			 }
			 if(field=='tags'){
				 $scope.selectedField = "关注点";
			 }else if(field=='content'){
				 $scope.selectedField = "内容";
			 }else if(field=='relectUserName'){
				 $scope.selectedField = "委员";
			 }else if(field=='userinfo.deleName'){
				 $scope.selectedField = "界别";
			 }else if(field=='cppccLevel'){
				 $scope.selectedField = "机构";
			 }else{
				 $scope.selectedField = "默认";
			 }
			
			socialMainService.searchResult(content,field,num).get({},function(resp)
			{
				 console.log("resp="+JSON.stringify(resp));
				 $scope.getHis();
				if(resp.code == 1)
				{
					$scope.results = resp.attribute.result.docs;
					$scope.searchnum = resp.attribute.result.num;
					console.log("num="+$scope.searchnum);
					$scope.showtext = "全部展开";
				}else{
					$scope.showtext = "全部展开";
				}
			},function(){
				
			});
			
			
			socialMainService.searchFacetResult(content,field).get({},function(resp)
			{
				 console.log("facet resp="+JSON.stringify(resp));
				if(resp.code == 1)
				{ 
					$scope.facets = resp.attribute.result.facets;
					console.log("facets="+JSON.stringify($scope.facets)); 
					//获取提案类别
					angular.forEach($scope.facets, function(data,index,array){
						if(data.name=="所属机构"){
							
							$scope.suggestcppccLevels = data.value;
							$scope.showtext1 = ">>";
							$scope.num =4;
							$scope.danweishow=false;
							$scope.isshow1 = function() {
								if($scope.danweishow){
									$scope.danweishow=!$scope.danweishow;
									$scope.showtext1 = ">>";
									$scope.num = 4;
								}else{
									$scope.danweishow=!$scope.danweishow;
									$scope.showtext1 = "<<";
									$scope.num = $scope.suggestcppccLevels.length;
								}
							};
							console.log("所属机构="+JSON.stringify($scope.suggestcppccLevels));
						}
						if(data.name=="所属行业"){
							$scope.suggesttags = data.value;
							$scope.showtext2 = ">>";
							$scope.num1 =4;
							$scope.tagshow=false;
							$scope.isshow2 = function() {
								if($scope.tagshow){
									$scope.tagshow=!$scope.tagshow;
									$scope.showtext2 = ">>";
									$scope.num1 = 4;
								}else{
									$scope.tagshow=!$scope.tagshow;
									$scope.showtext2 = "<<";
									$scope.num1 = $scope.suggesttags.length;
								}
							};
							
							console.log("提案类别="+JSON.stringify($scope.suggesttags));
						}
						if(data.name=="界别"){
							$scope.suggestjiebie = data.value;
							$scope.showtext3 = ">>";
							$scope.num2 =4;
							$scope.jiebieshow=false;
							$scope.isshow3 = function() {
								if($scope.jiebieshow){
									$scope.jiebieshow=!$scope.jiebieshow;
									$scope.num2 = 4;
									$scope.showtext3 = ">>";
								}else{
									$scope.jiebieshow=!$scope.jiebieshow;
									$scope.num2 = $scope.suggestjiebie.length;
									$scope.showtext3 = "<<";
								}
							};
							console.log("界别="+JSON.stringify($scope.suggestjiebie));
						}
						if(data.name=="委员"){
							$scope.suggestmembers = data.value;
							$scope.showtext4 = ">>";
							$scope.num3 =4;
							$scope.membershow=false;
							$scope.isshow4 = function() {
								if($scope.membershow){
									$scope.membershow=!$scope.membershow;
									$scope.num3 = 4;
									$scope.showtext4 = ">>";
								}else{
									$scope.membershow=!$scope.membershow;
									$scope.num3 = $scope.suggestmembers.length;
									$scope.showtext4 = "<<";
								}
							};
							console.log("委员="+JSON.stringify($scope.suggestmembers));
						}
						
					})
					$scope.showtext = "全部展开";
				}else{
					$scope.showtext = "全部展开";
				}
			},function(){
				
			});
		
		} 
		
		
		
		$scope.selectedField = "标题/内容";
		$scope.selectClick = function($event){ 
			 $scope.selectedField = $event.target.text;
		}
		
		var keywordarr = new Array();
		$scope.getkeywords  =function(_result){
			console.log("id="+_result._id+",result="+JSON.stringify(_result));
			socialMainService.keywords(_result._id).get({},function(resp){
				//keywordarr = new Array();
				console.log("keywords resp="+JSON.stringify(resp));
				//$scope.keywords = resp.attribute.keywords;
				_result.keywords=resp.attribute.keywords
				//console.log("keywords="+JSON.stringify($scope.keywords));
				keywordarr.push(_result._id);
				
			});
		}
		
		$scope.hasKeywords = function(id){
			var flag = false;
			angular.forEach(keywordarr, function(data,index,array){
				if(data==id){
					console.log("true");
					flag = true;
				}
			})
			 
			return flag;
		}
		
		var organizerarr = new Array();
		$scope.getorganizer = function(_result){
			socialMainService.getorganizer(_result._id).get({},function(resp){
				console.log("organizer="+JSON.stringify(resp));
				_result.organizers=resp.attribute.organizer;
				organizerarr.push(_result._id);
			})
		}
		$scope.hasOrganizer = function(id){
			var flag = false;
			angular.forEach(organizerarr, function(data,index,array){
				if(data==id){
					console.log("true");
					flag = true;
				}
			})
			 
			return flag;
		}
		
		$scope.showtext = "全部展开";
		$scope.showAllSubject = function($event){
            var allOpen = "全部展开", allClose = "全部收起";
            var icon = "<i class=\"c-icon c-list-s\"></i>";
            var ul_zm =   jQuery("ul.zm");
            if ($scope.showtext  == allOpen) {
            	ul_zm.addClass(" current");
            	 jQuery($event.target).html(icon+allClose);
            	$scope.showtext = allClose;
            } else {
            	ul_zm.removeClass("current");
            	 jQuery($event.target).html(icon + allOpen);
            	 $scope.showtext = allOpen;
            }
		}
		
		$scope.enterEvent = function(e) {
	        var keycode = window.event?e.keyCode:e.which;
	        if(keycode==13){
	            $scope.searchClick();
	        }
	    }
		//点击搜索按钮事件
		$scope.searchClick = function($event){
			var content = $scope.searhContext;
			if(content==undefined || content.length==0){
				content= "";
			} 
			var fields = $scope.getField();
			$scope.searchResult(content,fields);
			
		}
		$scope.getField = function(){
			var fields = "";
			if($scope.selectedField == "默认"){
				
			}else if($scope.selectedField == "标题"){
				fields = "qunaziContent";
			}else if($scope.selectedField == "内容"){
				fields = "qunaziContent"	;
			}else if($scope.selectedField == "标题/内容"){
				fields = "qunaziContent"	;
			}else if($scope.selectedField == "关注点"){
				fields = "tags"	;
			}else if($scope.selectedField == "机构"){
				fields = "cppccLevel"	;
			}else if($scope.selectedField == "委员"){
				fields = "relectUserName";
			}else if($scope.selectedField == "界别"){
				fields = "userinfo.deleName"	;
			}
			return fields;
		}
		
		$scope.facetSearch = function(condition){
			var content = $scope.searhContext;
			if(content==undefined || content.length==0){
				$scope.searchResult(condition); 
			}else{
				condition = condition +" AND title:" + content;  
				$scope.searchResult(condition); 
			}
		}
		
		$scope.attentionSearch = function(condition){
			$scope.searhContext = condition;
			$scope.searchResult(condition); 
		}
		 
		
		$scope.page =function(num,$event){
			jQuery(".active").removeClass("active");
			var content = $scope.searhContext;
			jQuery($event.target).parent().addClass("active");
			var fields = $scope.getField();
			$scope.searchResult(content,fields,num);
		}
		
    }])

})