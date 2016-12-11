(function() {
    'use strict';
    angular
        .module('yuriApp')
        .factory('Journal', Journal);

    Journal.$inject = ['$resource', 'DateUtils'];

    function Journal ($resource, DateUtils) {
        var resourceUrl =  'api/journals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timeOut = DateUtils.convertLocalDateFromServer(data.timeOut);
                        data.timeIn = DateUtils.convertLocalDateFromServer(data.timeIn);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.timeOut = DateUtils.convertLocalDateToServer(copy.timeOut);
                    copy.timeIn = DateUtils.convertLocalDateToServer(copy.timeIn);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.timeOut = DateUtils.convertLocalDateToServer(copy.timeOut);
                    copy.timeIn = DateUtils.convertLocalDateToServer(copy.timeIn);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
