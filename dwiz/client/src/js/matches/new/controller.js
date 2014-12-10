function NewMatchController($scope, $location, $alerts, api) {

  var match = $scope.match = {
    teams: {
      team1: {},
      team2: {}
    },
    games: []
  };

  // fetch table
  api.tables().list().then(function(tables) {
    match.table = tables[0];
  });

  function getScore(games) {
    var score = {
      team1: 0,
      team2: 0
    };

    games.forEach(function(g) {
      if (g.team1 > g.team2) {
        score.team1++;
      }

      if (g.team2 > g.team1) {
        score.team2++;
      }
    });

    return score;
  }

  function parseGames(values) {
    return values.split(/\s+/).map(function(str) {
      return str.split(/:/);
    }).map(function(results) {
      var left = parseInt(results[0]),
          right = parseInt(results[1]);

      if (isNaN(left) || isNaN(right)) {
        throw new Error('parse error: ' + results);
      }

      return {
        team1: left,
        team2: right
      };
    });
  }

  $scope.$watch('games', function(newValue) {
    try {
      match.games = parseGames(newValue);
      $scope.score = getScore(match.games);
    } catch (e) {
      // todo display error to user
      match.games = [];
      $scope.score = null;
    }

    $scope.form.games.$setValidity('format', !!$scope.score);
  });

  $scope.save = function() {

    if (!$scope.form.$valid) {
      return;
    }

    api.matches().create(match).then(function(match) {
      $location.path('/matches/' + match.id);
    }).catch(function(e) {
      $alerts.error('Failed to save', 'Could not create match: ' + e.status);
    });
  };
}

NewMatchController.$inject = [ '$scope', '$location', '$alerts', 'api' ];

module.exports = NewMatchController;