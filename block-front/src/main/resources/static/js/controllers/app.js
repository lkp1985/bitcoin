/**
 * 建立angular.module
 */
define(['angular'], function (angular) {
    var app = angular.module('pinganApp', ['ngRoute','ngSanitize','ajaxLoading','ngResource'])
    .run(function ($rootScope,$location) {
    	 $rootScope.$on('$viewContentLoaded', function () {
    	 		//alert($location.path());
    		 var ids = ['tian','social','suggest','statistics'];
    		 var path = $location.path().substring(1);
    		 
    		 if(path==""){
    			 path="tian";
    		 }
    		 for(var i=0;i<ids.length;i++){
    			 if(ids[i]==path){
    				 document.getElementById(path).className='current select';
    			 }else{
    				 document.getElementById(ids[i]).className='';
    			 }
    		 } 
    	 })
    	 $rootScope.topClick = function($event){
//    		/ alert(jQuery($event.target).parent().parent().find("li").length);
    		 jQuery($event.target).parent().parent().find("li").removeClass("current select");
    		 jQuery($event.target).parent().addClass("current select");  
    	 }
    })
    
    return app;
   
});
