var angular = require('angular'),
    angularRoute = require('angular-route'),
    angularCookies = require('angular-cookies'),
    angularLoadingBar = require('angular-loading-bar');


var ngModule = module.exports = angular.module('app', [
  angularRoute.name,
  angularCookies.name,
  angularLoadingBar.name,
  require('./common/auth').name,
  require('./components/alerts').name,
  require('./components/autocomplete').name,
  require('./navbar').name,
  require('./admin').name,
  require('./tournaments').name,
  require('./login').name,
  require('./tournaments').name,
  require('./matches').name,
  require('./home').name
]);


var Api = require('./common/api');

ngModule.service('api', Api);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
    redirectTo: '/'
  });
}]);