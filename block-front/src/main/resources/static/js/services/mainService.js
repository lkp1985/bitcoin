/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
define(['jquery','app' ], function ($,app) {
	 //http://39.108.92.223:8081/search?fields=title,leader&condition=建设&filts=sector:民盟&sort.field=leader&page=1&rows=10
    app.factory('mainService', ['$http','$q','$resource' , function($http,$q,$resource ) { 
    	
    	function  findPath(from,to,limit){
    		//alert("content="+content+",field="+field+",page="+page);
    		var url = _projPath+ 'path?startId='+from+'&endId='+to+"&limit="+limit;
			console.log("url="+url); 
			return $resource(url, {});
 		}
    	 
    	 return {
    		 findPath 

    	  }
    }])

})
