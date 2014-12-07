var angular = require('angular');

var ngModule = module.exports = angular.module('alerts', []);

ngModule.service('$alerts', ['$rootScope', '$q', function($rootScope, $q) {
  var self = this;

  function broadcast(topic, clazz, data) {
    var d = $q.defer();

    $rootScope.$broadcast(topic, angular.extend({ clazz: clazz, deferred: d }, data));
    return d.promise;
  }

  function createFunc(type) {
    return function(title, message, topic) {
      return broadcast(topic || 'alerts', 'alert-' + type, { title: title, message: message });
    };
  }

  [ 'info', 'warn', 'error', 'success' ].forEach(function(type) {
    self[type] = createFunc(type);
  });
}]);

ngModule.directive('alerts', ['$rootScope', '$timeout', function($rootScope, $timeout) {

  return {
    restrict: 'E',
    scope: true,
    template:
      '<div ng-repeat="alert in _alerts" class="alert alert-block transition {{ alert.clazz }}">' +
        '<a class="close" href ng-click="_close($index)">&times;</a>' +
        '<h4>{{ alert.title }}</h4>' +
        '{{ alert.message }}' +
      '</div>',
    link: function(scope, el, attrs) {
      var alertChannel = attrs.topic || 'alerts';

      scope._alerts = [];

      scope._close = function(index){
        scope._alerts[index].deferred.resolve();
        scope._alerts.splice(index, 1);
      };

      $rootScope.$on(alertChannel, function(event, data) {
        var timeout = data.timeout || attrs.timeout;
        scope._alerts.push(data);

        if (timeout) {
          $timeout(function() {
            scope._close(scope._alerts.indexOf(data));
          }, timeout);
        }
      });
    }
  };
}]);
