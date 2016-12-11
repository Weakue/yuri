(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('AutoPersonellDeleteController',AutoPersonellDeleteController);

    AutoPersonellDeleteController.$inject = ['$uibModalInstance', 'entity', 'AutoPersonell'];

    function AutoPersonellDeleteController($uibModalInstance, entity, AutoPersonell) {
        var vm = this;

        vm.autoPersonell = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AutoPersonell.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
