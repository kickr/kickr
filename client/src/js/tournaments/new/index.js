var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('tournaments.new', []);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/tournaments/new', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);