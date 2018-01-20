/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
define(['jquery','app' ], function ($,app) {
	 //http://39.108.92.223:8081/search?fields=title,leader&condition=建设&filts=sector:民盟&sort.field=leader&page=1&rows=10
    app.factory('suggestMainService', ['$http','$q','$resource' , function($http,$q,$resource ) { 
    	
    	function  searchResult(content,field,page){
    		//alert("content="+content+",field="+field+",page="+page);
    		var url = _projPath+ 'microsuggest/search?';
			content = encodeURIComponent(content);
			url = url+"condition="+content;
			//url = encodeURIComponent(url);
			if(field!=undefined){
				url = url+"&fields="+field;
			}
			if(page!=undefined){
				url = url+"&page="+page;
			}  
			console.log("url="+url); 
			
			return $resource(url, {});
 		}
    	function  searchFacetResult(content,field){
    		//alert("content="+content+",field="+field+",page="+page);
    		var url = _projPath+ 'microsuggest/facet?';
			content = encodeURIComponent(content);
			url = url+"condition="+content;
			//url = encodeURIComponent(url);
			if(field!=undefined){
				url = url+"&fields="+field;
			} 
			console.log("url="+url); 
			
			return $resource(url, {});
 		}
    	function  getSearchHis(){
    		//alert("content="+content+",field="+field+",page="+page);
    		var url = _projPath+ 'microsuggest/searchhis?';
			return $resource(url, {});
 		}
    	function  keywords(id){
    		//alert("content="+content+",field="+field+",page="+page);
    		var url = _projPath+ 'keywords/'+id;
			return $resource(url, {});
 		}
    	
    	function  getorganizer(id){
    		//alert("content="+content+",field="+field+",page="+page);
    		var url = _projPath+ 'organizer/'+id;
			return $resource(url, {});
 		}
    	 return {
    		 searchResult,getSearchHis,searchFacetResult,keywords,getorganizer

    	  }
    }])

})
