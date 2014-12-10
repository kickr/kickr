var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('tournaments', [
  require('./new').name
]);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/tournaments', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);