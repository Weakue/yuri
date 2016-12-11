(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('TopOnRoutesDetailController', TopOnRoutesDetailController);

    TopOnRoutesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TopOnRoutes'];

    function TopOnRoutesDetailController($scope, $rootScope, $stateParams, previousState, entity, TopOnRoutes) {
        var vm = this;

        vm.topOnRoutes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuriApp:topOnRoutesUpdate', function(event, result) {
            vm.topOnRoutes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
