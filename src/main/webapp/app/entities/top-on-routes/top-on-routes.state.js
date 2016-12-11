(function() {
    'use strict';

    angular
        .module('yuriApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('top-on-routes', {
            parent: 'entity',
            url: '/top-on-routes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TopOnRoutes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/top-on-routes/top-on-routes.html',
                    controller: 'TopOnRoutesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('top-on-routes-detail', {
            parent: 'entity',
            url: '/top-on-routes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TopOnRoutes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/top-on-routes/top-on-routes-detail.html',
                    controller: 'TopOnRoutesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TopOnRoutes', function($stateParams, TopOnRoutes) {
                    return TopOnRoutes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'top-on-routes',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('top-on-routes-detail.edit', {
            parent: 'top-on-routes-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/top-on-routes/top-on-routes-dialog.html',
                    controller: 'TopOnRoutesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TopOnRoutes', function(TopOnRoutes) {
                            return TopOnRoutes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('top-on-routes.new', {
            parent: 'top-on-routes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/top-on-routes/top-on-routes-dialog.html',
                    controller: 'TopOnRoutesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                routeName: null,
                                countOnRoute: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('top-on-routes', null, { reload: 'top-on-routes' });
                }, function() {
                    $state.go('top-on-routes');
                });
            }]
        })
        .state('top-on-routes.edit', {
            parent: 'top-on-routes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/top-on-routes/top-on-routes-dialog.html',
                    controller: 'TopOnRoutesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TopOnRoutes', function(TopOnRoutes) {
                            return TopOnRoutes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('top-on-routes', null, { reload: 'top-on-routes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('top-on-routes.delete', {
            parent: 'top-on-routes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/top-on-routes/top-on-routes-delete-dialog.html',
                    controller: 'TopOnRoutesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TopOnRoutes', function(TopOnRoutes) {
                            return TopOnRoutes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('top-on-routes', null, { reload: 'top-on-routes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
