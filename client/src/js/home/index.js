var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('home', [ ]);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);