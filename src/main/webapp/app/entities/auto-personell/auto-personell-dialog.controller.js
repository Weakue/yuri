(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('AutoPersonellDialogController', AutoPersonellDialogController);

    AutoPersonellDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AutoPersonell'];

    function AutoPersonellDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AutoPersonell) {
        var vm = this;

        vm.autoPersonell = entity;
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
            if (vm.autoPersonell.id !== null) {
                AutoPersonell.update(vm.autoPersonell, onSaveSuccess, onSaveError);
            } else {
                AutoPersonell.save(vm.autoPersonell, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuriApp:autoPersonellUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
