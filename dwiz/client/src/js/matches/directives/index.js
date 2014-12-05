var fs = require('fs');
var angular = require('angular');

var ngModule = module.exports = angular.module('games.directives', []);

ngModule.directive('matchSummary', function() {
  return {
    restrict: 'E',
    scope: {
      match: '='
    },
    replace: true,
    template: fs.readFileSync(__dirname + '/match-summary.html', 'utf8')
  };
});

ngModule.directive('score', function() {
  return {
    restrict: 'E',
    scope: {
      score: '='
    },
    replace: true,
    template: fs.readFileSync(__dirname + '/score.html', 'utf8')
  };
});


ngModule.directive('playerSummary', function() {
  return {
    restrict: 'E',
    scope: {
      player: '='
    },
    replace: true,
    template: fs.readFileSync(__dirname + '/player-summary.html', 'utf8')
  };
});