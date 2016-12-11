(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('RoutesDialogController', RoutesDialogController);

    RoutesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Routes', 'Journal'];

    function RoutesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Routes, Journal) {
        var vm = this;

        vm.routes = entity;
        vm.clear = clear;
        vm.save = save;
        vm.journals = Journal.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.routes.id !== null) {
                Routes.update(vm.routes, onSaveSuccess, onSaveError);
            } else {
                Routes.save(vm.routes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuriApp:routesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
