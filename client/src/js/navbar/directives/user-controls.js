
function UserControlsController($scope, $window, $alerts, $location, authentication) {

  $scope.logout = function() {
    authentication.logout().then(function() {
      $alerts.info({ title: 'Logout successful', ttl: 1, timeout: 8000 });
      $location.path('/');
    });
  };

  $scope.login = function() {

    if ($window.localStorage.username && $window.localStorage.password) {
      authentication.login($window.localStorage.username, $window.localStorage.password);
    } else {
      $location.path('/login');
    }
  };
}


UserControlsController.$inject = [ '$scope', '$window', '$alerts', '$location', 'authentication' ];

module.exports = UserControlsController;