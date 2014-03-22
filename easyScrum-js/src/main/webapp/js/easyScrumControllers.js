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

function TopNavCtrl($scope) {
}

function HomeCtrl($scope) {
}

function DayCtrl($scope, $filter, Restangular, $routeParams, $location) {
    var restTeams = Restangular.all('teams');
    $scope.sprints = [];
    $scope.days = [];
    $scope.showDayDialog = false;
    $scope.createNewDay = false;
    
    restTeams.getList().then(function(teams) {
       $scope.teams = teams; 
       if ($scope.teams && $scope.teams.length > 0) $scope.team = $scope.teams[0];
    });

    $scope.loadTeamSprints = function() {
        if ($scope.team && $scope.team.id) {
            $scope.team.all('sprints').getList().then(function(sprints) {
                $scope.sprints = sprints;
                if ($scope.sprints && $scope.sprints.length > 0) $scope.sprint = $scope.sprints[0]; // select first sprint
                else {
                    $scope.sprint = null;
                    $scope.days = [];
                }
            });
        } else {
            $scope.sprints = [];
        }
    };
    $scope.loadSprintDays = function() {
        if ($scope.sprint) $scope.days = $scope.sprint.all('days').getList().$object;
    };
    
    $scope.addDay = function() {
        $scope.day = {sprint: Restangular.stripRestangular($scope.sprint.clone()), day: $filter('date')(new Date(), 'yyyy-MM-dd'), burnDown: 0, upscaling: 0};
        $scope.dialogHeader = "New Sprint Day";
        $scope.createNewDay = true;
        $scope.showDayDialog = true;
    };
    $scope.editDay = function(day, index) {
        $scope.day = day;
        $scope.dialogHeader = "Edit Sprint Day: " + day.day;
        $scope.createNewDay = false;
        $scope.showDayDialog = true;
    };
    $scope.deleteDay = function(day, index) {
        day.remove().then($scope.loadSprintDays);
    };
    // add & edit
    $scope.submitDialog = function(action) {
        if (action === 'submit') {
            if ($scope.createNewDay) {
                $scope.days.post($scope.day).then(function(savedDay) {
                    $scope.loadSprintDays();
                    $scope.showDayDialog = false;
                });
            } else {
                $scope.day.put().then(function() {
                    $scope.loadSprintDays();
                    $scope.showDayDialog = false;
                });
            }
        } else {
            $scope.loadSprintDays();
            $scope.showDayDialog = false;
        }
    };

    $scope.$watch('team', $scope.loadTeamSprints);
    // load the days of the sprint if it changes
    $scope.$watch('sprint', $scope.loadSprintDays);
}

/**
 * Team Page Controller
 */
function TeamsCtrl($scope, $timeout, Restangular) {
    var restTeams = Restangular.all('teams');
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
        if ($scope.team && status === 'submit') {
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
}