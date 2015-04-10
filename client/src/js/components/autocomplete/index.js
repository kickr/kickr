var angular = require('angular');

var ngModule = module.exports = angular.module('autocomplete', []);

/**
 * A auto complete directive that can be attached to input elements.
 *
 * It fetches input suggestions whenever the user starts to type and
 * notifies other components if an autocompletion has been carried out.
 *
 * The auto completion allows the selection of complex values that
 * may have a string representation (displayed as the completion hint).
 *
 * @example
 *
 * <input type="text"
 *        autocomplete="findCities(input)"
 *        autocomplete-select="selectedCity=value"
 *        ng-model="cityName" />
 *
 * <script>
 *   function CityController($scope) {
 *     $scope.findCities = function(input) {
 *       // return promise, OK!
 *
 *       return [
 *         { key: 'Madrid', { name: 'Madrid', country: 'Spain' } },
 *         { key: 'Berlin', { name: 'Berlin', country: 'Germany' } }
 *       ]
 *     }
 *   }
 * </script>
 */
ngModule.directive('autocomplete', [ '$compile', '$q', '$timeout', function($compile, $q, $timeout) {

  var template = '<div class="completions" ng-show="__completions.length">' +
                   '<ul>' +
                     '<li ng-repeat="completion in __completions" ' +
                         'ng-class="{ selected: $index == __idx }" ' +
                         'ng-click="__select($index)">' +
                           '{{ completion.key }}' +
                     '</li>' +
                   '</ul>' +
                 '</div>';

  return {
    require: '^ngModel',
    scope: {
      autocomplete: '&',
      autocompleteSelect: '&'
    },
    link: function(scope, inputElement, attrs, ngModel) {

      // wrap input with autocomplete
      inputElement.wrap('<span class="autocomplete">');

      var completionBox = $compile(angular.element(template).insertAfter(inputElement))(scope);

      scope.__completions = [];
      scope.__idx = -1;

      function resetState(completions, idx) {
        scope.__completions = completions;
        scope.__idx = idx;
      }

      function updateView(index) {

        var completion = scope.__completions[index];

        if (completion) {
          ngModel.$setViewValue(completion.key);
          ngModel.$render();
        }

        scope.autocompleteSelect({ value: completion && completion.value });
      }

      function fetchCompletions(input) {
        return scope.autocomplete({ input: input });
      }

      function autocomplete() {
        var value = inputElement.val();

        $q.when(fetchCompletions(value)).then(function(result) {
          resetState(result, -1);
        });
      }

      inputElement.on('blur', function() {
        $timeout(function() {
          resetState([], -1);
        }, 100);
      });

      inputElement.on('keydown', function(event) {

        if (event.which === 13) {
          event.preventDefault();
        }

        if (event.ctrlKey || event.metaKey) {
          return;
        }

        scope.$evalAsync(function() {

          var completions = scope.__completions;

          switch (event.which) {
            case 9: // tab
            case 16: // shift
            case 37: // right
              return;
            case 13: // enter
            case 39: // left
              if (scope.__idx !== -1) {
                scope.__select(scope.__idx);
              }

              return;
            case 40: // down
              if (!completions.length) {
                return;
              }

              scope.__idx = Math.min(scope.__idx + 1, completions.length - 1);
              break;
            case 38: // up
              if (!completions.length) {
                return;
              }

              scope.__idx = Math.max(scope.__idx - 1, -1);
              break;
          }

          updateView(scope.__idx);
        });

      });

      inputElement.on('input', function() {
        scope.$evalAsync(autocomplete);
      });


      scope.__select = function(index) {
        console.log('Select', index);
        updateView(index);
        resetState([], -1);
      };

      scope.$on('$destroy', function() {
        completionBox.remove();
      });
    }
  };

}]);
