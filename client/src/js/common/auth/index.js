var angular = require('angular');

var ngModule = module.exports = angular.module('common.auth', []);

var Authentication = require('./authentication');

/**
 * Authentication / if needed
var AuthInterceptor = require('./common/AuthInterceptor');

ngModule.factory('authInterceptor', AuthInterceptor);
ngModule.config(['$httpProvider', function ($httpProvider) {
  $httpProvider.interceptors.push('authInterceptor');
}]);
*/

ngModule.service('authentication', Authentication);


//// application bootstrap ///////////////////

// init authentication data
ngModule.run([ 'authentication', function(authentication) {
  authentication.load();
}]);


ngModule.run([ '$rootScope', function($rootScope) {

  $rootScope.$on('$routeChangeStart', function(e, nextRoute) {

    var resolve,
        requiredPermissions = nextRoute.requiredPermissions;

    if (requiredPermissions) {
      resolve = nextRoute.resolve;

      if (!resolve) {
        resolve = nextRoute.resolve = {};
      }

      if (!resolve.$currentUser) {
        resolve.$currentUser = [
          'authentication', '$q', '$location', '$alerts',
          function(authentication, $q, $location, $alerts) {

          function checkPermissions(currentUser) {

            if ((currentUser.permissions & requiredPermissions) !== requiredPermissions) {

              var message = 'Insufficient permissions to access ' + $location.path();

              if (!currentUser.name) {
                message += ' (try logging in)';
              }

              $alerts.error({
                title: 'Unauthorized',
                message: message
              });

              return $q.reject(new Error('insufficient permissions'));
            }

            return currentUser;
          }

          function rejectUnauthorized() {
            $alerts.error({
              title: 'Unauthorized',
              message: 'You need to be logged in to access ' + $location.path()
            });

            return $q.reject(new Error('unauthorized'));
          }

          return authentication.load().then(checkPermissions, rejectUnauthorized);
        }];

      }
    }
  });

}]);