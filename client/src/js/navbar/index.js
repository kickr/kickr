var angular = require('angular');
var NavbarController = require('./controller');

var ngModule = module.exports = angular.module('navbar', [
  require('./directives').name
]);

ngModule.controller('NavbarController', NavbarController);
