var angular = require('angular'),
    angularRoute = require('angular-route'),
    angularLoadingBar = require('angular-loading-bar');


var ngModule = module.exports = angular.module('app', [
  angularRoute.name,
  angularLoadingBar.name,
  require('./components/alerts').name,
  require('./components/autocomplete').name,
  require('./matches').name,
  require('./home').name
]);


var ApiService = require('./common/ApiService');

/**
 * Authentication / if needed
var AuthInterceptor = require('./common/AuthInterceptor');

ngModule.factory('authInterceptor', AuthInterceptor);
ngModule.config(['$httpProvider', function ($httpProvider) {
  $httpProvider.interceptors.push('authInterceptor');
}]);
*/

ngModule.service('api', ApiService);

ngModule.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
    redirectTo: '/'
  });
}]);