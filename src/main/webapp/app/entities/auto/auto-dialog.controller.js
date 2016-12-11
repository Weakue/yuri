(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('AutoDialogController', AutoDialogController);

    AutoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Auto', 'AutoPersonell'];

    function AutoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Auto, AutoPersonell) {
        var vm = this;

        vm.auto = entity;
        vm.clear = clear;
        vm.save = save;
        vm.autopersonells = AutoPersonell.query({filter: 'auto-is-null'});
        $q.all([vm.auto.$promise, vm.autopersonells.$promise]).then(function() {
            if (!vm.auto.autoPersonell || !vm.auto.autoPersonell.id) {
                return $q.reject();
            }
            return AutoPersonell.get({id : vm.auto.autoPersonell.id}).$promise;
        }).then(function(autoPersonell) {
            vm.autopersonells.push(autoPersonell);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auto.id !== null) {
                Auto.update(vm.auto, onSaveSuccess, onSaveError);
            } else {
                Auto.save(vm.auto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuriApp:autoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
