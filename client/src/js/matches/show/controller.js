function MatchController($scope, $alerts, $routeParams, $location, $timeout, api) {

  function load() {
    api.matches().get($routeParams.id).then(function(match) {
      $scope.match = match;
    });
  }

  function remove() {

    var match = $scope.match;

    api.matches().remove(match).then(function() {
      $alerts.info({
        title: 'Match removed',
        message: 'Match with id ' + match.id + ' was removed',
        ttl: 1
      });

      $location.path('/matches');
    });
  }

  $scope.remove = remove;

  load();
}

MatchController.$inject = [ '$scope', '$alerts', '$routeParams', '$location', '$timeout', 'api' ];

module.exports = MatchController;