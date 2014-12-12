function MatchesController($scope, $alerts, api) {

  $scope.page = 0;

  function reload() {
    api.matches().list($scope.page, 10).then(function(matches) {
      $scope.matches = matches;
    });
  }

  reload();
}

MatchesController.$inject = [ '$scope', '$alerts', 'api' ];

module.exports = MatchesController;