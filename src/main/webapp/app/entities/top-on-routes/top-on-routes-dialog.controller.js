(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('TopOnRoutesDialogController', TopOnRoutesDialogController);

    TopOnRoutesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TopOnRoutes'];

    function TopOnRoutesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TopOnRoutes) {
        var vm = this;

        vm.topOnRoutes = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.topOnRoutes.id !== null) {
                TopOnRoutes.update(vm.topOnRoutes, onSaveSuccess, onSaveError);
            } else {
                TopOnRoutes.save(vm.topOnRoutes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuriApp:topOnRoutesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
