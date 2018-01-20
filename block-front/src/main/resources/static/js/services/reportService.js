/**
 * 取得实际位置 调用方式：geoFactory.getGeo();
 */
define([ 'jquery', 'app' ], function($, app) {
	// http://39.108.92.223:8081/search?fields=title,leader&condition=建设&filts=sector:民盟&sort.field=leader&page=1&rows=10
	app.factory('reportService', [ '$http', '$q', '$resource',
			function($http, $q, $resource) {
			
			
				function tianStatistics() {
					var url = _projPath + 'tian/' + id;
					return $resource(url, {});
				}
				function getcomparenum(areacode,jie,ci,cppccLevel,type){
					
					
					var url;
					if(type=='微建议'){
						  url = _projPath +"microsuggest/";
					}else if(type=='社情民意'){
						  url = _projPath +"social/";
					}else if(type=='资讯阅读'){
						  url = _projPath +"cmsinfobrowser/";
					}
					else{
						  url = _projPath ;
					}
					
					url += 'report/comparenum?areacode='+areacode+"&jie="+jie+"&ci="+ci;
					var obj = $resource(url, {});
					return obj;
				}
				function getCategoryData(areacode,jie,ci,cppccLevel,type){
					
					
					var url;
					if(type=='微建议'){
						  url = _projPath +"microsuggest/";
					}else if(type=='社情民意'){
						  url = _projPath +"social/";
					}else if(type=='资讯阅读'){
						  url = _projPath +"cmsinfobrowser/";
					}
					else{
						  url = _projPath ;
					}
					
					url += 'report/categoryFacet?areacode='+areacode+"&jie="+jie+"&ci="+ci;
					var obj = $resource(url, {});
					return obj;
				}
				
				function getOrganizerData(areacode,jie,ci,cppccLevel,type){
					
					var url;
					if(type=='微建议'){
						  url = _projPath +"microsuggest/";
					}else if(type=='社情民意'){
						  url = _projPath +"social/";
					}else if(type=='资讯阅读'){
						  url = _projPath +"cmsinfobrowser/";
					}
					else{
						  url = _projPath ;
					}
					
					url += 'report/organizerFacet?areacode='+areacode+"&jie="+jie+"&ci="+ci;
					var obj = $resource(url, {});
					return obj;
				}
				
				function getBrowserData(areacode,jie,ci,cppccLevel,type){
					
					var url;
					if(type=='微建议'){
						  url = _projPath +"microsuggest/";
					}else if(type=='社情民意'){
						  url = _projPath +"social/";
					}else if(type=='资讯阅读'){
						  url = _projPath +"cmsinfobrowser/";
					}
					else{
						  url = _projPath ;
					}
					
					url += 'report/browserFacet?areacode='+areacode+"&jie="+jie+"&ci="+ci;
					var obj = $resource(url, {});
					return obj;
				}
				
				return {
					getCategoryData,getcomparenum,getOrganizerData,getBrowserData
				}
			} 
	])

})
