function NewTournamentController($scope, $location, $alerts, api) {

  var tournament = $scope.tournament = {
    invitations: []
  };

  $scope.completions = {
    players: [],
    player: ''
  };

  $scope.fetchPlayers = function(value) {
    return [
      { key: 'Nico Rehwaldt', value: { id: 'NICO', name: 'Nico Rehwaldt' } },
      { key: 'Class', value: { id: 'NICO1', name: 'Nico Rehwaldt 121^' } },
      { key: 'Micha Schö', value: { id: 'MICHA1', name: 'Micha Schöttes' } }
    ].filter(function(e) {
      return e.key.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    });
  };

  $scope.removePlayer = function(index) {
    tournament.invitations.splice(index, 1);
  };

  $scope.addPlayer = function(player) {
    console.log(player);

    tournament.invitations.push(player);
    $scope.completions.player = '';
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