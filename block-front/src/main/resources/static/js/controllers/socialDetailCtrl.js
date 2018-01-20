define(['app','socialDetailService'], function(app,detailService){
      
   return app.controller('socialDetailCtrl', ['$scope','$rootScope', '$routeParams','$http','socialDetailService', 
	   function ($scope,$rootScope, $routeParams,$http,socialDetailService) {
	   $scope.$on('$viewContentLoaded', function() { 
		   $scope.getDetail($routeParams.id);
		   
		}); 
	   $scope.getDetail = function(id) {
		   socialDetailService.detail(id).get({},function(resp){
			   if(resp.code == 1){
				   var result = resp.attribute.result;
				     $scope.tian = result.detail;
				     $scope.similarDoc = result.similarDoc;
				   console.log("tian="+JSON.stringify($scope.tian));
				   console.log("similarDoc="+JSON.stringify($scope.similarDoc));
				   var content = $scope.tian.socialContent;
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