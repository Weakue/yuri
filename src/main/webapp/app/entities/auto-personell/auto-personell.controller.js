(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('AutoPersonellController', AutoPersonellController);

    AutoPersonellController.$inject = ['$scope', '$state', 'AutoPersonell'];

    function AutoPersonellController ($scope, $state, AutoPersonell) {
        var vm = this;
        
        vm.autoPersonells = [];

        loadAll();

        function loadAll() {
            AutoPersonell.query(function(result) {
                vm.autoPersonells = result;
            });
        }
    }
})();
