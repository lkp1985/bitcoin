define(['app','suggestDetailService'], function(app,suggestDetailService){
      
   return app.controller('suggestDetailCtrl', ['$scope','$rootScope', '$routeParams','$http','suggestDetailService', 
	   function ($scope,$rootScope, $routeParams,$http,suggestDetailService) {
	   $scope.$on('$viewContentLoaded', function() { 
		   $scope.getDetail($routeParams.id);
		   
		}); 
	   $scope.getDetail = function(id) {

		   suggestDetailService.detail(id).get({},function(resp){
			   if(resp.code == 1){
				   var result = resp.attribute.result;
				     $scope.tian = result.detail;
				     $scope.userinfo = $scope.tian.userinfo;
				     $scope.similarDoc = result.similarDoc;
				     $scope.title=$scope.tian.qunaziContent;
				     if($scope.tian.qunaziContent!=null && $scope.tian.qunaziContent.length>20){
				    	 $scope.title = $scope.tian.qunaziContent.substring(0,20)+"...";
				     }
				    
				   console.log("tian="+JSON.stringify($scope.tian));
				   console.log("similarDoc="+JSON.stringify($scope.similarDoc));
				   var content = $scope.tian.qunaziContent;
				   jQuery(".detail_content").html(content);
				  // console.log("title="+$scope.tian.title);
			   }
		   })
	   }
	   $scope.dateStamp2String =function(time){
	        var datetime = new Date();
	         datetime.setTime(time);
	         var year = datetime.getFullYear();
	         var month = datetime.getMonth() + 1;
	         var date = datetime.getDate();
	         var hour = datetime.getHours();
	         var minute = datetime.getMinutes();
	         var second = datetime.getSeconds();
	         var mseconds = datetime.getMilliseconds();
	         return year + "-" + month + "-" + date;
		}
    }])

})