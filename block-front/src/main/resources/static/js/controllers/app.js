/**
 * 建立angular.module
 */
define(['angular'], function (angular) {
    var app = angular.module('pinganApp', ['ngRoute','ngSanitize','ajaxLoading','ngResource'])
    .run(function ($rootScope,$location) {
    	 $rootScope.$on('$viewContentLoaded', function () { })
    	 $rootScope.topClick = function($event){
    	 }
    })
    
    return app;
   
});
