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
        $rootScope.network = {
            pendingRequests: 0,
            lastRequestError: null,
            busy: false
        };
        function updateStatus(increment, error) {
            $rootScope.network.lastRequestError = error ? error : null;
            $rootScope.network.pendingRequests += increment;
            if ($rootScope.network.pendingRequests < 0) $rootScope.network.pendingRequests = 0;
            if (error) {
                console.warn("Server Error:", error);
            }
            $rootScope.network.busy = $rootScope.network.pendingRequests > 0;
        };
        return {
           'request': function (config) {
                updateStatus(1);
                return config || $q.when(config);
            },
            'requestError': function(rejection) {
                updateStatus(-1, rejection);
                return $q.reject(rejection);
            },
            'response': function(response) {
                updateStatus(-1);
                return response || $q.when(response);
            },
            'responseError': function(rejection) {
                updateStatus(-1, rejection);
                return $q.reject(rejection);
            }
        };
    }).
    directive('messages', function() {
        return {
            restrict: 'AC',
            templateUrl: 'directive/messages/messages.html'
        };
    });
            
angular.module('easyScrum', ['ngRoute', 'restangular', 'RequestInterceptor']).
    config(['$routeProvider', 'RestangularProvider', function($routeProvider, RestangularProvider) {
        RestangularProvider.setBaseUrl('service/');
        $routeProvider.when('/home', {templateUrl: 'views/home.html', controller: 'HomeCtrl'});
        $routeProvider.when('/day', {templateUrl: 'views/day.html', controller: 'DayCtrl', reloadOnSearch: false});
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
    }).
    directive('easyTooltip', function($timeout) {
        return {
            restrict: 'C',
            repalce: false,
            link: function (scope, element, attrs) {
                //console.log("tooltip attached to: ", element);
                $timeout(function() {element.tooltip(); }, 0);
            }
        };
    });