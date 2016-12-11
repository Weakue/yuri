(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('JournalDeleteController',JournalDeleteController);

    JournalDeleteController.$inject = ['$uibModalInstance', 'entity', 'Journal'];

    function JournalDeleteController($uibModalInstance, entity, Journal) {
        var vm = this;

        vm.journal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Journal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
