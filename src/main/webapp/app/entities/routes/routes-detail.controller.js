(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('RoutesDetailController', RoutesDetailController);

    RoutesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Routes', 'Journal'];

    function RoutesDetailController($scope, $rootScope, $stateParams, previousState, entity, Routes, Journal) {
        var vm = this;

        vm.routes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuriApp:routesUpdate', function(event, result) {
            vm.routes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
