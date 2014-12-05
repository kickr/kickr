function MatchesController($scope, api) {

  api.matches().list().then(function(matches) {
    $scope.matches = matches;
  });
}

MatchesController.$inject = [ '$scope', 'api' ];

module.exports = MatchesController;