var angular = require('angular');

var ngModule = module.exports = angular.module('autocomplete', []);

ngModule.directive('autocomplete', [ '$compile', '$q', function($compile, $q) {

  var template = '<div class="completions" ng-show="__completions.length">' +
                   '<ul><li ng-repeat="completion in __completions" ng-class="{ selected: $index == __idx }" ng-click="__select($index)">{{ completion.key }}</li></ul>' +
                 '</div>';

  return {
    require: '^ngModel',
    scope: {
      autocomplete: '&',
      onSelect: '&'
    },
    link: function(scope, element, attrs, ngModel) {
      var completionBox = $compile(angular.element(template).insertAfter(element))(scope);

      var fn = function(input) {
        return scope.autocomplete({ input: input });
      };

      scope.__completions = [];
      scope.__idx = -1;

      function autocomplete() {
        var value = element.val();

        $q.when(fn(value)).then(function(result) {
          scope.__completions = result;
        });
      }

      element.on('blur', function(event) {
        scope.$evalAsync(function() {
          scope.__completions = [];
        });
      });

      element.on('keydown', function(event) {

        if (event.which === 13) {
          event.preventDefault();
        }

        scope.$evalAsync(function() {

          switch (event.which) {
            case 40: // down
              scope.__idx = Math.min(scope.__idx + 1, scope.__completions.length - 1);
              break;
            case 38: // up
              scope.__idx = Math.max(scope.__idx - 1, -1);
              break;
            case 13: // enter
            case 39: // left
              scope.__select(scope.__idx || 0);
              break;
          }
        });

      });

      element.on('input', function() {
        scope.$evalAsync(autocomplete);
      });

      scope.__select = function(index) {

        var completion = scope.__completions[index];
        if (completion) {
          ngModel.$setViewValue(completion.key);
          ngModel.$render();

          scope.onSelect({ value: completion.value });
          scope.__completions = [];
        }
      };

      scope.$on('$destroy', function() {
        completionBox.remove();
      });
    }
  };

}]);
