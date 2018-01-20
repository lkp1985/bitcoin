/**
 * 取得实际位置 调用方式：geoFactory.getGeo();
 */
define([ 'jquery', 'app' ], function($, app) {
	// http://39.108.92.223:8081/search?fields=title,leader&condition=建设&filts=sector:民盟&sort.field=leader&page=1&rows=10
	app.factory('detailService', [ '$http', '$q', '$resource',
			function($http, $q, $resource) {
				function detail(id) {
					var url = _projPath + 'tian/' + id;
					return $resource(url, {});
				}
				return {
					detail : detail

				}
			} ])

})
