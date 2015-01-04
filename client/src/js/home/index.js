var fs = require('fs');
var angular = require('angular');

var EndPoint = require('../common/api/end-point');

var ngModule = module.exports = angular.module('home', [ ]);

ngModule.run([ 'api', function(api) {

  function HomeApi(http, uri) {
    EndPoint.call(this, http, uri);
  }

  var home = api.create(HomeApi, 'home');

  api.home = function() {
    return home;
  };
}]);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {
    controller: require('./controller'),
    template: fs.readFileSync(__dirname + '/index.html', 'utf8')
  });
}]);