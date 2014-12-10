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
}

AdminController.$inject = [ '$scope', '$http' ];

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/admin', {
    controller: AdminController,
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);