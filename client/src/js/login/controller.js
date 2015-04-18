function LoginController($scope, $alerts, $location, authentication) {

  $scope.login = function(name, password, rememberMe) {

    if ($scope.working) {
      return;
    }

    $scope.working = true;

    authentication.login(name, password, rememberMe).then(function(e) {
      $alerts.info({ title: 'Login successful', timeout: 8000, ttl: 1 });
      $location.path('/').replace();
    }).catch(function(e) {
      $scope.working = false;

      $alerts.error('Authentication failed');
    });
  };

  $scope.back = function() {
    $location.path('/').replace();
  };
}

LoginController.$inject = [ '$scope', '$alerts', '$location', 'authentication' ];

module.exports = LoginController;