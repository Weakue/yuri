(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('AutoPersonellDetailController', AutoPersonellDetailController);

    AutoPersonellDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AutoPersonell'];

    function AutoPersonellDetailController($scope, $rootScope, $stateParams, previousState, entity, AutoPersonell) {
        var vm = this;

        vm.autoPersonell = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuriApp:autoPersonellUpdate', function(event, result) {
            vm.autoPersonell = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
