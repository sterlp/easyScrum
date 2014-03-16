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

//http://www.jquery4u.com/tutorials/jqueryhtml5-input-focus-cursor-positions/
$.fn.setCursorEnd = function(pos) {
    this.each(function(index, elem) {
        var length = elem.value ? elem.value.length : 0; 
        if (elem.setSelectionRange && length > 0) {
            elem.setSelectionRange(length, length);
        } else if (elem.createTextRange && length) {
            var range = elem.createTextRange();
            range.collapse(true);
            range.moveEnd('character', length);
            range.moveStart('character', length);
            range.select();
        }
    });
    return this;
};

// http://stackoverflow.com/questions/17494732/how-to-make-a-loading-indicator-for-every-asynchronous-action-using-q-in-an-a
// or https://gist.github.com/maikeldaloo/5140733
angular.module('RequestInterceptor', [])
  .config(function ($httpProvider) {
    $httpProvider.interceptors.push('requestInterceptor');
  })
  .factory('requestInterceptor', function ($q, $rootScope) {
        $rootScope.pendingRequests = 0;
        $rootScope.lastRequestError = null;
        return {
           'request': function (config) {
                $rootScope.lastRequestError = null;
                $rootScope.pendingRequests++;
                return config || $q.when(config);
            },

            'requestError': function(rejection) {
                console.warn("requestError", rejection);
                $rootScope.lastRequestError = rejection;
                $rootScope.pendingRequests--;
                return $q.reject(rejection);
            },

            'response': function(response) {
                $rootScope.lastRequestError = null;
                $rootScope.pendingRequests--;
                return response || $q.when(response);
            },

            'responseError': function(rejection) {
                $rootScope.pendingRequests--;
                console.warn("responseError", rejection);
                $rootScope.lastRequestError = rejection;
                return $q.reject(rejection);
            }
        };
    });
            
angular.module('easyScrum', ['ngRoute', 'restangular', 'RequestInterceptor']).
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
        };
    }).
    directive('easyDialog', function($parse) {
        return {
            restrict: 'AC',
            repalce: true,
            transclude: true,
            templateUrl: 'directive/dialog/dialog.html',
            scope: {
                show: "=", // bidi binding on object
                header: "@" // string
            },
            link: function (scope, element, attrs) {
                var dialog = $(element.children()[0]);
                dialog.on('hidden.bs.modal', function () {
                    scope.$apply(function() {scope.show = false;});
                });
                dialog.on('shown.bs.modal', function () {
                    dialog.find(".modal-body :input:visible:enabled:first").focus().setCursorEnd();
                });
                scope.$watch("show", function(show) {
                    if (show === true) {
                        dialog.modal('show');
                    }
                    else dialog.modal('hide');
                });
                scope.close = function() {
                    scope.show = false;
                };
            }
        };
      }
    ).
    directive('easyDialogHeader', function() {
        return {
            restrict: 'AC',
            repalce: true,
            transclude: true,
            templateUrl: 'directive/dialog-head.html'
        };
      }
    );

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
        team.remove().then($scope.refresh);
    };
    $scope.edit = function(team) {
        $scope.editHeader = "Edit Team: '" + team.name + "'";
        $scope.team = team;
        $scope.showTeamDialog = true;
    };
    $scope.newTeam = function() {
        $scope.editHeader = "Create new Team";
        $scope.team = {__new: true};
        $scope.showTeamDialog = true;
    };
    $scope.dialogClose = function(status) {
        $scope.showTeamDialog = false;
        console.log("dialog close...", status);
        if ($scope.team && status === 'submit') {
            console.log("team: ", $scope.team);
            if ($scope.team.__new === true) {
                delete $scope.team.__new;
                restTeams.post($scope.team).then($scope.refresh); // create reload
            } else {
                $scope.team.put().then($scope.refresh); // "update" and reload
            }
        }
        $scope.team = null; // done
    };
    $scope.refresh(); // load the initial data
    // allow tooltips on this page
    $scope.layoutDone = function() { $timeout(function() {$('.easy-tooltip').tooltip(); }, 0) };
}