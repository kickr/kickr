var fs = require('fs');

var angular = require('angular');

var ngModule = module.exports = angular.module('login', []);

ngModule.config([ '$routeProvider', function($routeProvider) {

  $routeProvider.when('/login', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}])