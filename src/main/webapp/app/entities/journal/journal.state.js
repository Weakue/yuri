(function() {
    'use strict';

    angular
        .module('yuriApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('journal', {
            parent: 'entity',
            url: '/journal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Journals'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/journal/journals.html',
                    controller: 'JournalController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('journal-detail', {
            parent: 'entity',
            url: '/journal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Journal'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/journal/journal-detail.html',
                    controller: 'JournalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Journal', function($stateParams, Journal) {
                    return Journal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'journal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('journal-detail.edit', {
            parent: 'journal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/journal/journal-dialog.html',
                    controller: 'JournalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Journal', function(Journal) {
                            return Journal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('journal.new', {
            parent: 'journal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/journal/journal-dialog.html',
                    controller: 'JournalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                timeOut: null,
                                timeIn: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('journal', null, { reload: 'journal' });
                }, function() {
                    $state.go('journal');
                });
            }]
        })
        .state('journal.edit', {
            parent: 'journal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/journal/journal-dialog.html',
                    controller: 'JournalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Journal', function(Journal) {
                            return Journal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('journal', null, { reload: 'journal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('journal.delete', {
            parent: 'journal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/journal/journal-delete-dialog.html',
                    controller: 'JournalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Journal', function(Journal) {
                            return Journal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('journal', null, { reload: 'journal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
