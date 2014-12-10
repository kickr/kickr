var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('matches', [
  require('./directives').name,
  require('./new').name
]);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/matches', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);