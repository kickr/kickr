var _ = require('lodash');

function HomeController($scope, $alerts, api) {

  api.matches().list(0, 5).then(function(matches) {
    $scope.matches = matches;
  });

  api.scores().get().then(function(leaderBoard) {
    $scope.leaderBoard = leaderBoard;
  });
}

HomeController.$inject = [ '$scope', '$alerts', 'api' ];

module.exports = HomeController;