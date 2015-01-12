var _ = require('lodash');

function HomeController($scope, $alerts, api) {

  api.matches().list(0, 5).then(function(matches) {
    $scope.matches = matches;
  });

  api.scores().get().then(function(leaderBoard) {
    $scope.leaderBoard = leaderBoard;

    _.forEach(leaderBoard.performance, function(p) {
      p.average = Math.round((p.score + 0.0) / p.games);
      p.confidence = Math.round((1 - 1 / Math.sqrt(p.games * 2)) * 100) / 100;
    });

  });
}

HomeController.$inject = [ '$scope', '$alerts', 'api' ];

module.exports = HomeController;