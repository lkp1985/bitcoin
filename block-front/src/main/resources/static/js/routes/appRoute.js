/**
 * 路由
 */
define([ 'app' ], function(app) {

	return app.config([ '$routeProvider', function($routeProvider) {
		var system ={};  
	    var p = navigator.platform;       
	    system.win = p.indexOf("Win") == 0;  
	    system.mac = p.indexOf("Mac") == 0;  
	    system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);     
	    if(system.win||system.mac||system.xll){//如果是电脑跳转
			$routeProvider.when('/', {
				templateUrl : 'views/main.html',
				controller : 'mainCtrl'
			}).when('/detail/tian/:id', {
				templateUrl : 'views/tianDetail.html',
				controller : 'detailCtrl'
			}).when('/detail/suggest/:id', {
				templateUrl : 'views/suggestDetail.html',
				controller : 'suggestDetailCtrl'
			}).when('/detail/social/:id', {
				templateUrl : 'views/socialDetail.html',
				controller : 'socialDetailCtrl'
			}).when('/statistics', {
				//templateUrl : 'views/statistics.html',
				templateUrl : 'views/wepPage/statistics.html',
				controller : 'statisticsCtrl'
			}).when('/report', {
				//templateUrl : 'views/statistics.html',
				templateUrl : 'views/wepPage/report.html',
				controller : 'reportCtrl'
			}).when('/suggest', {//微建议
				templateUrl : 'views/suggestMain.html',
				controller : 'suggestMainCtrl'
			}).when('/suggest/:id', {//微建议详情
				templateUrl : 'views/suggestDetail.html',
				controller : 'suggestCtrl'
			}).when('/social', {//社情民意
				templateUrl : 'views/socialMain.html',
				controller : 'socialMainCtrl'
			}).when('/social/:id', {//社情民意详情
				templateUrl : 'views/socialDetail.html',
				controller : 'suggestCtrl'
			})
			
			
			.otherwise({
				redirectTo : '/'
			});
	    }else{  //如果是手机
	       $routeProvider.when('/', {
				templateUrl : 'views/main.html',
				controller : 'mainCtrl'
			}).when('/detail/tian/:id', {
				templateUrl : 'views/tianDetail.html',
				controller : 'detailCtrl'
			}).when('/detail/suggest/:id', {
				templateUrl : 'views/suggestDetail.html',
				controller : 'suggestDetailCtrl'
			}).when('/detail/social/:id', {
				templateUrl : 'views/socialDetail.html',
				controller : 'socialDetailCtrl'
			}).when('/statistics', {
				//templateUrl : 'views/statistics.html',
				templateUrl : 'views/wepPage/statistics.html',
				controller : 'statisticsCtrl'
			}).when('/suggest', {//微建议
				templateUrl : 'views/suggestMain.html',
				controller : 'suggestMainCtrl'
			}).when('/suggest/:id', {//微建议详情
				templateUrl : 'views/suggestDetail.html',
				controller : 'suggestCtrl'
			}).when('/social', {//社情民意
				templateUrl : 'views/socialMain.html',
				controller : 'socialMainCtrl'
			}).when('/social/:id', {//社情民意详情
				templateUrl : 'views/socialDetail.html',
				controller : 'suggestCtrl'
			})
			
			
			.otherwise({
				redirectTo : '/'
			});
	    }

	} ])

})