'use strict';

describe('Controller Tests', function() {

    describe('Journal Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockJournal, MockAuto, MockRoutes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockJournal = jasmine.createSpy('MockJournal');
            MockAuto = jasmine.createSpy('MockAuto');
            MockRoutes = jasmine.createSpy('MockRoutes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Journal': MockJournal,
                'Auto': MockAuto,
                'Routes': MockRoutes
            };
            createController = function() {
                $injector.get('$controller')("JournalDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yuriApp:journalUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
