(function() {
    'use strict';
    angular
        .module('yuriApp')
        .factory('TopOnRoutes', TopOnRoutes);

    TopOnRoutes.$inject = ['$resource'];

    function TopOnRoutes ($resource) {
        var resourceUrl =  'api/top-on-routes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
