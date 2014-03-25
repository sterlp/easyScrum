'use strict';
/* 
 * Copyright 2014 Paul.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * easyScrum Routes
 */            
angular.module('easyScrum', ['ngRoute', 'restangular', 'easyComponents', 'simpleBurndownChart']).
    config(['$routeProvider', 'RestangularProvider', function($routeProvider, RestangularProvider) {
        RestangularProvider.setBaseUrl('service/');
        $routeProvider.when('/home', {templateUrl: 'views/home.html', controller: 'HomeCtrl'});
        $routeProvider.when('/day', {templateUrl: 'views/day.html', controller: 'DayCtrl', reloadOnSearch: false});
        $routeProvider.when('/sprints', {templateUrl: 'views/sprints.html', controller: 'SprintsCtrl'});
        $routeProvider.when('/teams', {templateUrl: 'views/teams.html', controller: 'TeamsCtrl'});
        $routeProvider.otherwise({redirectTo: '/home'});
    }]);