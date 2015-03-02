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
            try { elem.setSelectionRange(length, length);} catch (e) {/** ignore */};
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
// http://stackoverflow.com/questions/563406/add-days-to-datetime
/**
 * Adds days to the given date.
 * @param {type} days how many days to add
 * @param {type} skipWeekend if true skips sun and saturday
 * @returns {Date|Date.prototype.addDays.dat}
 */
Date.prototype.addDays = function(days, skipWeekend) {
    var dat = new Date(this.valueOf());
    dat.setDate(dat.getDate() + days);
    if (skipWeekend === true && dat.isWeekend()) {
        dat.setDate(dat.getDate() + 1);
        if (dat.isWeekend()) dat.setDate(dat.getDate() + 1);
    }
    return dat;
};
/**
 * Checks of the given date is a weekend
 * @returns {Boolean} true if Sunday or Saturday
 */
Date.prototype.isWeekend = function() {
    return this.getDay() === 6 || this.getDay() === 0;
};


// TODO replace most of the own directives with either:
// http://mgcrea.github.io/angular-strap/
// http://angular-ui.github.io/


// http://stackoverflow.com/questions/17494732/how-to-make-a-loading-indicator-for-every-asynchronous-action-using-q-in-an-a
// or https://gist.github.com/maikeldaloo/5140733
angular.module('easyComponents', [])
  .config(function ($httpProvider) {
    $httpProvider.interceptors.push('requestInterceptor');
  }).
  factory('requestInterceptor', function ($q, $rootScope) {
        $rootScope.network = {
            pendingRequests: 0,
            lastRequestError: null,
            busy: false
        };
        function updateStatus(increment, error) {
            $rootScope.network.lastRequestError = error ? error : null;
            $rootScope.network.errorType = null;
            $rootScope.network.fieldErrors = {}; // reset field errors
            $rootScope.network.pendingRequests += increment;
            if ($rootScope.network.pendingRequests < 0) $rootScope.network.pendingRequests = 0;
            // check for error
            if (error && error.data && error.data.fieldErrors) {
                $rootScope.network.errorType = 'validation';
                console.info("User Input Error (validation):", error.data.fieldErrors);
                angular.forEach(error.data.fieldErrors, function(fieldError) {
                    $rootScope.network.fieldErrors[fieldError.field] = fieldError;
                });
            } else if (error) {
                $rootScope.network.errorType = 'unknown';
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
    directive('easyMessages', function() {
        return {
            restrict: 'AC',
            templateUrl: 'directive/messages/messages.html'
        };
    }).
    directive('easyMessage', function() {
        function getBindValue(attrs) {
            if (attrs.easyMessage) return attrs.easyMessage;
            if (attrs.ngModel) {
                var dot = attrs.ngModel.indexOf('.');
                if(dot < 0) return attrs.ngModel;
                return attrs.ngModel.substring(1 + dot, attrs.ngModel.length);
            }
            return null; // nothing to bind too!
        }
        function link (scope, element, attrs) {
            var bindTo = getBindValue(attrs); 
            if (bindTo) {
                bindTo = 'network.fieldErrors.' + bindTo;
                var formGroup = element.closest('.form-group');
                scope.$watch(bindTo, function(newValue) {
                    if (newValue) formGroup.addClass('has-error');
                    else formGroup.removeClass('has-error');
                });
            }
        }
        return {
            restrict: 'AC',
            compile: function (element, attrs, transclude) {
                var bindTo = getBindValue(attrs); 
                if (bindTo) {
                    bindTo = 'network.fieldErrors.' + bindTo;
                    element.parent().append('<span class="help-block" ng-show="' + bindTo + '.defaultMessage">{{ ' + bindTo + '.defaultMessage }}</span>');
                } else {
                    console.error("Message directive needs a value to bind to the validation error data or ngModel.");
                }
                return link;
            }
        };
    }).
    // http://stackoverflow.com/questions/13471129/angularjs-ng-repeat-finish-event
    directive('repeatDone', function() {
        return function(scope, element, attrs) {
            if (scope.$last) { // all are rendered
                console.log("repeatDone...");
                scope.$eval(attrs.repeatDone);
            }
        };
    }).
    directive('easyDialog', function() {
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
                var dialog = null;
                addEventHandler();
                if (element.children().length > 0) {
                    dialog = $(element.children()[0]);
                }
                scope.$watch("show", function(show) {
                    if (show === true) {
                        $(element.children()[0]).modal('show');
                    } else {
                        dialog && dialog.modal('hide');
                    }
                });
                scope.close = function() {
                    scope.show = false;
                };
                function addEventHandler() {
                    element.on('hidden.bs.modal', function () {
                        scope.$apply(function() {scope.show = false;});
                    });
                    element.on('shown.bs.modal', function () {
                        //dialog.find(".modal-body :input:visible:enabled:first").focus().setCursorEnd();
                        dialog && dialog.find(".modal-body .dialog-focus").focus().setCursorEnd();
                    });
                    //init = true;
                }
            }
        };
    }).
    /**
     * Directive which can be used instead of
     * requires: http://bootboxjs.com/ & bootstrap
     * @param {function} confirm-okay
     */
    directive('easyConfirmClick', function() {
        return {
            restrict: 'A',
            repalce: false,
            transclude: false,
            link: function (scope, element, attrs) {
                element.on('click', function(e) {
                    bootbox.confirm(attrs.easyConfirmClick, function(result) {
                        if (result) {
                            scope.$apply(attrs.confirmOkay);
                        }
                    });
                });
            }
        };
    }).
    directive('easyTooltip', function($timeout) {
        return {
            restrict: 'C',
            repalce: false,
            link: function (scope, element, attrs) {
                //console.log("tooltip attached to: ", element);
                $timeout(function() {element.tooltip(); }, 0, false);
            }
        };
    }).
    // requeries: http://eternicode.github.io/bootstrap-datepicker
    directive('easyDate', function() {
        return {
            restrict: 'AC',
            repalce: false,
            require: '?ngModel',
            scope: {
                startDate: "@", // string
                endDate: "@" // string
            },
                link: function(scope, element, attrs, ngModel) {
                //console.log(attrs);
                var datePicker = element.datepicker({
                    format: 'yyyy-mm-dd',
                    startDate: scope.startDate,
                    endDate: scope.endDate,
                        autoclose: true,
                        todayHighlight: true
                    });

                    if (ngModel) {
                        ngModel.$render = function() {
                            //console.log('update of the date ', attrs.ngModel, ' value: ', ngModel.$viewValue);
                            datePicker.datepicker('update', ngModel.$viewValue);
                        };
                    }
                
                scope.$watch('startDate', function(newVal){ 
                    datePicker.datepicker('setStartDate', newVal ? new Date(newVal) : null); 
                });
                scope.$watch('endDate', function(newVal){ 
                    datePicker.datepicker('setEndDate', newVal ? new Date(newVal) : null); 
                });
            }
        };
    }).
    filter('percent', function($filter) {
        return function(input) {
                return $filter('number')(parseFloat(input) * 100, 2) + "%";
        };
    }).
    directive('easyPanel', function() {
        return {
            restrict: 'EAC',
            transclude: true,
            templateUrl: 'directive/panel.html'
        };
    });
    
    

angular.module('simpleBurndownChart', []).
    // requeries: https://github.com/adangel/simple-burndown-chart
    // see also: http://docs.angularjs.org/api/ng/type/ngModel.NgModelController
    directive('easyBurndown', function($filter) {
        var burndowns = 0;
        return {
            restrict: 'E',
            repalce: false,
            require: '?ngModel',
            scope: {
                showGrid: "@", // string
                showComments: "@" // string
            },
            link: function (scope, element, attrs, ngModel) {
                if(!ngModel) {
                    log.warn('easyBurndown requires a ngModel to render.');
                    return; // do nothing if no ng-model
                }
                var id = "easyBurndown_" + (++burndowns);
                ngModel.$render = render;
                $(window).bind("resize", render);

                var rendering = null;
                function render() {
                    element.empty();
                    if (ngModel.$viewValue) {
                        if (rendering) clearTimeout(rendering);
                        rendering = setTimeout(function() {
                            element.html('<div style="easyBurndown" id="' + id + '"></div>');
                            // copy is needed as d3.js does change the data inside
                            var data = angular.copy(ngModel.$viewValue);
                            if (!data.timeDomain) { // no timedomain, lets create one
                                var start = new Date(data.start),
                                    end = new Date(data.end);
                                data.timeDomain = [];
                                while(start <= end) {
                                    data.timeDomain.push($filter('date')(start, 'yyyy-MM-dd'));
                                    start.setDate(start.getDate() + 1); // next day
                                    if (start.getDay() === 6) start.setDate(start.getDate() + 2); // skip weekend
                                }
                            }
                            // TODO add this code to the simple burndown
                            if (data.burndowns.length > 0) { // check if the first points to the first day
                                    if (data.burndowns[0].date !== data.start
                                            || data.burndowns[0].hours !== data.plannedHours) {
                                        // add the start dot into the chart
                                    data.burndowns.unshift({
                                        date: data.start,
                                        hours: data.plannedHours});
                                }
                            }
                                
                            //console.log('render burndown for:', ngModel.$viewValue, ' full data: ', data);
                            SBD.render(data, {
                                chartNodeSelector: '#' + id, 
                                showGrid: scope.showGrid || true, 
                                showComments: scope.showComments || true,
                                dateFormat: "%Y-%m-%d",
                                width: $(element).parent().width()
                                });
                            }, 200);
                    }
                }
            }
        };
    });