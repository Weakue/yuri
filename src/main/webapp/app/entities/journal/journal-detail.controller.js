(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('JournalDetailController', JournalDetailController);

    JournalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Journal', 'Auto', 'Routes'];

    function JournalDetailController($scope, $rootScope, $stateParams, previousState, entity, Journal, Auto, Routes) {
        var vm = this;

        vm.journal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuriApp:journalUpdate', function(event, result) {
            vm.journal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
