function HomeController($scope, api) {

  api.matches().list(0, 5).then(function(matches) {
    $scope.matches = matches;
  });

  api.scores().list(0, 5).then(function(scores) {
    $scope.scores = scores;
  });

  api.user().fetchStatus(function(status) {
    $scope.status = status;
  }).catch(function(e) {
    $alerts.error('Could not fetch status', 'Yup: ' + e.status);
  });
}

HomeController.$inject = [ '$scope', 'api' ];

module.exports = HomeController;