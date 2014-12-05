function HomeController($scope, api) {

  api.matches().list().then(function(matches) {
    $scope.matches = matches;
  });
}

HomeController.$inject = [ '$scope', 'api' ];

module.exports = HomeController;