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
angular.module('easyScrum', ['ngRoute', 'restangular']).
    config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/home', {templateUrl: 'views/home.html', controller: 'HomeCtrl'});
        $routeProvider.when('/teams', {templateUrl: 'views/teams.html', controller: 'TeamsCtrl'});
        $routeProvider.otherwise({redirectTo: '/home'});
    }]).
    // http://stackoverflow.com/questions/13471129/angularjs-ng-repeat-finish-event
    directive('repeatDone', function() {
        return function(scope, element, attrs) {
            if (scope.$last) { // all are rendered
                console.log("repeatDone...");
                scope.$eval(attrs.repeatDone);
            }
        }
    });


function HomeCtrl($scope) {
}

function TeamsCtrl($scope, $timeout, Restangular) {
    Restangular.setBaseUrl('service/');
    var restTeams = Restangular.all('team');
    $scope.refresh = function () {
        restTeams.getList().then(function(teams) {
          $scope.teams = teams;
        });
    };
    $scope.delete = function (team, index) {
        //$scope.teams[index - 1].remove(); --> remove call
        if ($scope.teams.length > 1) {
            $scope.teams = $scope.teams.splice(index - 1, 1);
        } else {
            $scope.teams.pop(); // remove the last one
        }
        console.log($scope.teams);
    };
    // allow tooltips on this page
    $scope.layoutDone = function() { $timeout(function() {$('.easy-tooltip').tooltip(); }, 0) };
    $scope.refresh();
}