(function() {
    'use strict';

    angular
        .module('yuriApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('auto-personell', {
            parent: 'entity',
            url: '/auto-personell',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AutoPersonells'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auto-personell/auto-personells.html',
                    controller: 'AutoPersonellController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('auto-personell-detail', {
            parent: 'entity',
            url: '/auto-personell/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AutoPersonell'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auto-personell/auto-personell-detail.html',
                    controller: 'AutoPersonellDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AutoPersonell', function($stateParams, AutoPersonell) {
                    return AutoPersonell.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'auto-personell',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('auto-personell-detail.edit', {
            parent: 'auto-personell-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto-personell/auto-personell-dialog.html',
                    controller: 'AutoPersonellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AutoPersonell', function(AutoPersonell) {
                            return AutoPersonell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auto-personell.new', {
            parent: 'auto-personell',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto-personell/auto-personell-dialog.html',
                    controller: 'AutoPersonellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                fatherName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('auto-personell', null, { reload: 'auto-personell' });
                }, function() {
                    $state.go('auto-personell');
                });
            }]
        })
        .state('auto-personell.edit', {
            parent: 'auto-personell',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto-personell/auto-personell-dialog.html',
                    controller: 'AutoPersonellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AutoPersonell', function(AutoPersonell) {
                            return AutoPersonell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auto-personell', null, { reload: 'auto-personell' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auto-personell.delete', {
            parent: 'auto-personell',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto-personell/auto-personell-delete-dialog.html',
                    controller: 'AutoPersonellDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AutoPersonell', function(AutoPersonell) {
                            return AutoPersonell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auto-personell', null, { reload: 'auto-personell' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
