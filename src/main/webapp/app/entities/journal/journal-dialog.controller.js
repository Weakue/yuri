(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('JournalDialogController', JournalDialogController);

    JournalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Journal', 'Auto', 'Routes'];

    function JournalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Journal, Auto, Routes) {
        var vm = this;

        vm.journal = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.autos = Auto.query();
        vm.routes = Routes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.journal.id !== null) {
                Journal.update(vm.journal, onSaveSuccess, onSaveError);
            } else {
                Journal.save(vm.journal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuriApp:journalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.timeOut = false;
        vm.datePickerOpenStatus.timeIn = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
