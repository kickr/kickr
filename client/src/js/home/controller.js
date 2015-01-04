
function HomeController($scope, $alerts, api) {

  api.matches().list(0, 5).then(function(matches) {
    $scope.matches = matches;
  });

  api.scores().list(0, 5).then(function(scores) {
    $scope.scores = scores;
  });
}

HomeController.$inject = [ '$scope', '$alerts', 'api' ];

module.exports = HomeController;