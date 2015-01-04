var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('matches.new', []);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/matches/new', {
    requiredPermissions: 2,
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);