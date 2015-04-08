function NewTournamentController($scope, $location, $alerts, api) {

  var tournament = $scope.tournament = {
    invitations: []
  };

  $scope.completions = {
    players: [],
    player: ''
  };

  $scope.findPlayers = function(value) {
    var match = /([\w]+)\s*<([^>]+)>/.exec(value);

    if (match) {
      return [
        {
          key: match[0],
          value: { alias: match[1], email: match[2] }
        }
      ];
    }

    return api.players().find(value).then(function(results) {
      return results.map(function(r) {
        return { key: r.alias + (r.name ? '(' + r.name + ')' : ''), value: r };
      });
    });
  };

  $scope.removePlayer = function(index) {
    tournament.invitations.splice(index, 1);
  };

  $scope.addPlayer = function(player) {
    console.log(player);

    tournament.invitations.push(player);
    $scope.completions.player = '';

    $scope.newPlayer = null;
  };

  $scope.save = function() {
    api.tournaments().create(tournament).then(function(tournament) {
      $location.path('/tournament/' + tournament.id);
    }).catch(function(e) {
      $alerts.error('Failed to save', 'Could not create tournament: ' + e.status);
    });
  };
}

NewTournamentController.$inject = [ '$scope', '$location', '$alerts', 'api' ];

module.exports = NewTournamentController;