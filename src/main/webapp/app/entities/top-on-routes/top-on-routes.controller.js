(function() {
    'use strict';

    angular
        .module('yuriApp')
        .controller('TopOnRoutesController', TopOnRoutesController);

    TopOnRoutesController.$inject = ['$scope', '$state', 'TopOnRoutes', 'ParseLinks', 'AlertService'];

    function TopOnRoutesController ($scope, $state, TopOnRoutes, ParseLinks, AlertService) {
        var vm = this;
        
        vm.topOnRoutes = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll () {
            TopOnRoutes.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.topOnRoutes.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.topOnRoutes = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
