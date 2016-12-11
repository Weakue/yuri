(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('AutoDetailController', AutoDetailController);

    AutoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Auto', 'AutoPersonell'];

    function AutoDetailController($scope, $rootScope, $stateParams, previousState, entity, Auto, AutoPersonell) {
        var vm = this;

        vm.auto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuriApp:autoUpdate', function(event, result) {
            vm.auto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
