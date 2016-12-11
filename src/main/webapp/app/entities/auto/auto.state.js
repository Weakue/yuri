(function() {
    'use strict';

    angular
        .module('yuriApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('auto', {
            parent: 'entity',
            url: '/auto',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Autos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auto/autos.html',
                    controller: 'AutoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('auto-detail', {
            parent: 'entity',
            url: '/auto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Auto'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auto/auto-detail.html',
                    controller: 'AutoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Auto', function($stateParams, Auto) {
                    return Auto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'auto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('auto-detail.edit', {
            parent: 'auto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto/auto-dialog.html',
                    controller: 'AutoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Auto', function(Auto) {
                            return Auto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auto.new', {
            parent: 'auto',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto/auto-dialog.html',
                    controller: 'AutoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                num: null,
                                color: null,
                                mark: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('auto', null, { reload: 'auto' });
                }, function() {
                    $state.go('auto');
                });
            }]
        })
        .state('auto.edit', {
            parent: 'auto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto/auto-dialog.html',
                    controller: 'AutoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Auto', function(Auto) {
                            return Auto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auto', null, { reload: 'auto' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auto.delete', {
            parent: 'auto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auto/auto-delete-dialog.html',
                    controller: 'AutoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Auto', function(Auto) {
                            return Auto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auto', null, { reload: 'auto' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
