function NavbarController(scope, location) {
  scope.currentLocation = '';

  scope.$on('$routeChangeStart', function() {
    scope.currentLocation = location.path();
  });

  scope.goto = function (newLocation) {
    location.path(newLocation);
  };

  scope.activeClass = function (locationName) {
    return scope.currentLocation.indexOf(locationName) != -1 ? ['active'] : [];
  };
}

NavbarController.$inject = [ '$scope', '$location' ];

module.exports = NavbarController;