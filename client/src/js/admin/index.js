var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('admin', [ ]);

function AdminController($scope, $http, $alerts) {

  $scope.createDemoData = function() {
    $http.post('/api/admin/demo').then(function() {
      $alerts.info('Demo data created', 'YUP');
    }).catch(function(e) {
      $alerts.info('Ainnnt gonna create demo data', 'Problems ' + e.status);
    });
  };

  $scope.updateRatings = function() {
    $http.post('/api/admin/scoreboard/update').then(function() {
      $alerts.info('Ratings updated', 'YUP');
    }).catch(function(e) {
      $alerts.info('Ainnnt gonna update that stuff', 'Problems ' + e.status);
    });
  };

  $scope.resetRatings = function() {
    $http.post('/api/admin/scoreboard/reset').then(function() {
      $alerts.info('Ratings reset', 'YUP');
    }).catch(function(e) {
      $alerts.info('Ainnnt gonna reset that stuff', 'Problems ' + e.status);
    });
  };
}

AdminController.$inject = [ '$scope', '$http', '$alerts' ];

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/admin', {
    requiredPermissions: 6,
    controller: AdminController,
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);