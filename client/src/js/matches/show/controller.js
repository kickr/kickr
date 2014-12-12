function MatchController($scope, $alerts, $routeParams, $location, $timeout, api) {

  function load() {
    api.matches().get($routeParams.id).then(function(match) {
      $scope.match = match;
    });
  }

  function remove() {

    var match = $scope.match;

    api.matches().remove(match).then(function() {
      $timeout(function() {
        $alerts.info('Match removed', 'Match with id ' + match.id + ' was removed');
      }, 500);

      $location.path('/matches');
    });
  }

  $scope.remove = remove;

  load();
}

MatchController.$inject = [ '$scope', '$alerts', '$routeParams', '$location', '$timeout', 'api' ];

module.exports = MatchController;