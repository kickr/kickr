var fs = require('fs');

var angular = require('angular');

var ngModule = module.exports = angular.module('navbar.directives', []);

var UserControlsController = require('./user-controls');

ngModule.directive('userControls', [ '$rootScope', 'authentication', function($rootScope, authentication) {

  return {
    template: fs.readFileSync(__dirname + '/user-controls.html', 'utf8'),
    restrict: 'A',
    scope: true,
    controller: UserControlsController,
    link: function(scope, element, attrs) {

      scope.$watch(authentication.userChange, function(user) {
        scope.currentUser = user;
      });
    }
  }
}]);

ngModule.directive('navbar', function() {
  return {
    replace: true,
    restrict: 'A',
    template: fs.readFileSync(__dirname + '/navbar.html', 'utf8')
  };
});