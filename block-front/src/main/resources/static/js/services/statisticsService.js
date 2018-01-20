/**
 * 取得实际位置 调用方式：geoFactory.getGeo();
 */
define([ 'jquery', 'app' ], function($, app) {
	// http://39.108.92.223:8081/search?fields=title,leader&condition=建设&filts=sector:民盟&sort.field=leader&page=1&rows=10
	app.factory('statisticsService', [ '$http', '$q', '$resource',
			function($http, $q, $resource) {
			
			
				function tianStatistics() {
					var url = _projPath + 'tian/' + id;
					return $resource(url, {});
				}
				function yearTrend(condition,startDate,endDate,type){
					
					
					
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
					
					url += 'yearFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					var obj = $resource(url, {});
					return obj;
				}
				
				function sexTrend(condition,startDate,endDate, type){
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
					
					url += 'sexYearFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				function sexDistribute(condition,startDate,endDate,type){
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
					
					url +=  'sexFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				function sectorDistribute(condition,startDate,endDate ,type){
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
					
					url +=  'sectorFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				function attentionDistribute(condition,startDate,endDate,type){
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
					
					url += 'tagsFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					
					console.log("attenurl="+url);
					return $resource(url, {});
				}
				
				
				
				
				function getDateFacet(condition,startDate,endDate ,type){
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
					url  += 'dateFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				function organizerDistribute(condition,startDate,endDate ,type){
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
					
					url  += 'organizerFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				
				 
				function ageDistribute(condition,startDate,endDate ,type){
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
					
					url +='ageFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				
				function userTypeDistribute(condition,startDate,endDate){
					url = _projPath +"cmsinfobrowser/";
					url +='usertype?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				
				 
				function educationDistribute(condition,startDate,endDate ,type){
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
					
					url +=  'educationFacet?condition='+condition+"&startDate="+startDate+"&endDate="+endDate;
					return $resource(url, {});
				}
				function getallnum(areacode,startDate,endDate,cppccLevel,cmstype){
					var url = _projPath + 'allnum?areacode='+areacode+'&startDate='+startDate+"&endDate="+endDate+"&cppccLevel="+cppccLevel+"&cmstype="+cmstype;
					return $resource(url, {});
				}
				function getTiAnMap(condition,startDate,endDate,type){
					var url ="";
					if(type=='微建议'){
						  url = _projPath +"microsuggest/cppccLevelFacet";
					}else if(type=='社情民意'){
						  url = _projPath +"social/cppccLevelFacet";
					}else if(type=='资讯阅读'){
						  url = _projPath +"cmsinfobrowser/cppccLevelFacet";
					}
					else{
						  url = _projPath+"cppccLevelFacet" ;
					}
					
					if(condition!=null && condition.length>0){
						var conditions = condition.split(" ");
						var filt = "";
						if(conditions.length>1){
							condition = conditions[0];
							filt = conditions[1];
							url+='?filts='+condition+"&filts="+filt;
						}else{
							url+='?filts='+condition
						}
						//condition = encodeURIComponent(condition);
						//url+='?filts='+condition;
						
						url += "&startDate="+startDate+"&endDate="+endDate;
					}else{
						url += "?startDate="+startDate+"&endDate="+endDate;
					}
					
					
					console.log("cppcclevelcondition="+url);
					return $resource(url, {});
				}
				
				return {
					tianStatistics,yearTrend,sexTrend,sexDistribute,sectorDistribute,getTiAnMap,getDateFacet,
					userTypeDistribute,
					attentionDistribute,ageDistribute,educationDistribute,organizerDistribute,getallnum
				}
			} ])

})
