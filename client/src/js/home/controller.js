function HomeController($scope, api) {

  api.matches().list(0, 5).then(function(matches) {
    $scope.matches = matches;
  });
}

HomeController.$inject = [ '$scope', 'api' ];

module.exports = HomeController;