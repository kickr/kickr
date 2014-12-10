var angular = require('angular');
var NavbarController = require('./Controller');

var ngModule = module.exports = angular.module('navbar', []);

ngModule.controller('NavbarController', NavbarController);
