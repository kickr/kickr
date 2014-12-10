function TournamentsController($scope, api) {

  api.tournaments().list().then(function(tournaments) {
    $scope.tournaments = tournaments;
  });
}

TournamentsController.$inject = [ '$scope', 'api' ];

module.exports = TournamentsController;