function HomeController($scope, api) {

  api.matches().list(0, 5).then(function(matches) {
    $scope.matches = matches;
  });

  api.scores().list(0, 5).then(function(scores) {
    $scope.scores = scores;
  });
}

HomeController.$inject = [ '$scope', 'api' ];

module.exports = HomeController;