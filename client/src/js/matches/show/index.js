var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('matches.show', []);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/matches/:id', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);