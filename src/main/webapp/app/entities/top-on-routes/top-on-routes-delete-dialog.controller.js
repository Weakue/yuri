(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('TopOnRoutesDeleteController',TopOnRoutesDeleteController);

    TopOnRoutesDeleteController.$inject = ['$uibModalInstance', 'entity', 'TopOnRoutes'];

    function TopOnRoutesDeleteController($uibModalInstance, entity, TopOnRoutes) {
        var vm = this;

        vm.topOnRoutes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TopOnRoutes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
