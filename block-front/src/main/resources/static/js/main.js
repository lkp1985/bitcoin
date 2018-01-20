/**
 * 入口文件
 * 2014-11-30 mon
 */
require.config({
    baseUrl: "js/",
    paths: {
      "jquery": "libs/jquery203",
      "bootstrap": "libs/bootstrap.min",
      "fastclick" : "libs/fastclick",
      "blocksit" : "libs/blocksit",
      "idTabs" : "libs/idTabs",
      "stickUp" : "libs/stickUp",
      "angular" : "libs/angular.min",
      "angular-route" : "libs/angular-route.min",
      "angular-sanitize" : "libs/angular-sanitize.min",
      "angular-resource" : "libs/angular-resource.min", 
//      "laydate": "libs/laydate",
//      "api":"libs/api",
      
      
      "telBox" : "directives/telBox",
      "fav" : "directives/fav",
      "imgList" : "directives/imgList", 
      "backButton" : "directives/backButton",
      "fliterBox" : "directives/fliterBox",
      "jrTab" : "directives/jrTab",
      "fixBar" : "directives/fixBar",
      
      
      "app" : "controllers/app",
      "loadingInterceptor" : "controllers/loadingInterceptor", 
      "const":"util/const",
      
       
      "mainService" : "services/mainService",
      "suggestMainService" : "services/suggestMainService",
      "socialMainService" : "services/socialMainService",
      "detailService" : "services/detailService",
      "socialDetailService" : "services/socialDetailService",
      "suggestDetailService" : "services/suggestDetailService",
      "statisticsService" : "services/statisticsService",
      "reportService" : "services/reportService",

      
      //定义与业务相关的控制
      "mainCtrl" : "controllers/mainCtrl",
      "suggestMainCtrl":"controllers/suggestMainCtrl",
      "socialMainCtrl":"controllers/socialMainCtrl",
      "detailCtrl" : "controllers/detailCtrl",
      "suggestDetailCtrl" : "controllers/suggestDetailCtrl",
      "socialDetailCtrl" : "controllers/socialDetailCtrl",
      "statisticsCtrl" : "controllers/statisticsCtrl",
      "reportCtrl" : "controllers/reportCtrl",
//      "echarts":"libs/echarts.common.min",
      "route" : "routes/appRoute" 
    },
    shim: {
       'angular': {
          exports: 'angular'
       },
       'angular-route':{
          deps: ["angular"],
          exports: 'angular-route'
       },
       'angular-resource':{
           deps: ["angular"],
           exports: 'angular-resource'
        },
       
       'angular-sanitize':{
          deps: ["angular"],
          exports: 'angular-sanitize'
       },
       'blocksit':{
          deps: ["jquery","angular"],
          exports: 'blocksit'
       },
       'idTabs': {
          deps: ['jquery'],
          exports: 'idTabs'
       },
       'stickUp': {
          deps: ['jquery'],
          exports: 'stickUp'
       }
    },packages: [
//    	 {
//             name: 'echarts',
//             location: "libs/echarts.min",
//             main: 'echarts'
//         },
//         {
//             name: 'zrender',
//             location: "libs/zrender", // zrender与echarts在同一级目录
//             main: 'zrender'
//         }
    ],
});

//将需要引用的模块加入项目. 
require(['jquery','angular','angular-route','angular-resource','angular-sanitize','bootstrap', 
	'app','loadingInterceptor','route','telBox','fav','backButton','fliterBox','idTabs','jrTab','stickUp','fixBar','const',
	'mainCtrl','suggestMainCtrl','socialMainCtrl','detailCtrl','suggestDetailCtrl',
	'socialDetailCtrl','statisticsCtrl','reportCtrl'],function ($,angular){

      $(function () {

            angular.bootstrap(document,["pinganApp"]);

      })

});