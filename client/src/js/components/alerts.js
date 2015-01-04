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

      var alert;

      if (angular.isObject(title)) {
        alert = title;
        topic = message;
      } else {
        alert = { title: title, message: message };
      }

      return broadcast(topic || 'alerts', 'alert-' + type, alert);
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

      scope._close = function(index) {
        scope._alerts[index].deferred.resolve();
        scope._alerts.splice(index, 1);
      };

      scope.$on('$routeChangeStart', function() {

        console.log('$routeChangeStart', arguments);

        var outdated = [];

        angular.forEach(scope._alerts, function(alert, index) {
          if (alert.ttl !== undefined) {
            alert.ttl--;
          }

          if ((alert.ttl === undefined && !alert.timeout) || alert.ttl === -1) {
            outdated.push(alert);
          }
        });

        angular.forEach(outdated, function(alert) {
          scope._close(scope._alerts.indexOf(alert));
        });
      });

      scope.$on(alertChannel, function(event, data) {
        var timeout = data.timeout || attrs.timeout;
        scope._alerts.push(data);

        if (timeout) {
          $timeout(function() {
            var idx = scope._alerts.indexOf(data);
            if (idx !== -1) {
              scope._close(idx);
            }
          }, timeout);
        }
      });
    }
  };
}]);
