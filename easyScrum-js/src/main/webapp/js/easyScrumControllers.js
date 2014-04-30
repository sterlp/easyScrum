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

function GoalCtrl($scope, Restangular) {
    $scope.restGoals = Restangular.all('goals');
    $scope.goals = $scope.restGoals.getList().$object;
}

/**
 * Team Page Controller
 */
function TeamsCtrl($scope, Restangular) {
    $scope.restTeams = Restangular.all('teams');
    $scope.addNewTeam = false;
    $scope.showTeamDialog = false;
    $scope.reloadTeams = function() {
        $scope.teams = $scope.restTeams.getList().$object;
    };
    $scope.deleteTeam = function(team, index) {
        team.remove().then($scope.reloadTeams);
    };
    $scope.editTeam = function(team) {
        $scope.addNewTeam = false;
        $scope.editTeamHeader = "Edit Team: '" + team.name + "'";
        $scope.team = team;
        $scope.showTeamDialog = true;
    };
    $scope.createTeam = function() {
        $scope.addNewTeam = true;
        $scope.editTeamHeader = "Create new Team";
        $scope.team = {};
        $scope.showTeamDialog = true;
    };
    $scope.closeTeamDialog = function(status) {
        $scope.showTeamDialog = false;
        if ($scope.team && status === 'submit') {
            if ($scope.addNewTeam) {
                $scope.restTeams.post($scope.team).then($scope.reloadTeams); // create reload
            } else {
                $scope.team.put().then($scope.reloadTeams); // "update" and reload
            }
        }
        $scope.team = null; // done
    };
    $scope.reloadTeams(); // load the initial data
}

function SprintsCtrl($scope, $filter, Restangular, $routeParams, $location) {
    $scope.restTeams = Restangular.all('teams');
    $scope.team = null; // selected team
    $scope.teams = []; // all teams
    $scope.sprint = null; // selected sprint
    $scope.sprints = []; // team sprints
    $scope.showSprintDialog = false;
    $scope.sprintDialogHeader = "";
    $scope.createNewSprint = false;
    $scope.today = new Date().setHours(0,0,0,0); // date of today
    
    // read the teams
    $scope.restTeams.getList().then(function(teams) {
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
    $scope.$watch('team', $scope.loadTeamSprints);
    
    $scope.addSprint = function() {
        var now = new Date();
        $scope.sprint = {
            team: Restangular.stripRestangular($scope.team.clone()), 
            start: $filter('date')(now, 'yyyy-MM-dd'), 
            end: $filter('date')(new Date().setDate(now.getDate() + 14), 'yyyy-MM-dd'),  
            availableHours: 100, plannedHours: 100, storyPoints: 0
        };
        $scope.sprintDialogHeader = "Create new Sprint for Team " + $scope.team.name;
        $scope.createNewSprint = true;
        $scope.showSprintDialog = true;
    };
    $scope.editSprint = function(sprint) {
        $scope.sprint = angular.copy(sprint);
        $scope.sprintDialogHeader = "Edit Sprint: " + sprint.name;
        $scope.createNewSprint = false;
        $scope.showSprintDialog = true;
    };
    $scope.deleteSprint = function(sprint) {
        sprint.remove().then($scope.loadTeamSprints);
    };
    $scope.submitSprintDialog = function(action) {
        if (action === 'submit') {
            if ($scope.createNewSprint) { // add
                $scope.sprints.post($scope.sprint).then(function(savedSprint) {
                    $scope.loadTeamSprints();
                    $scope.showSprintDialog = false;
                });
            } else { // update
                $scope.sprint.put().then(function() {
                    $scope.loadTeamSprints();
                    $scope.showSprintDialog = false;
                });
            }
        } else {
            $scope.showSprintDialog = false;
        }
    };
}

function DayCtrl($scope, $filter, Restangular, $routeParams, $location) {
    // http://digital-drive.com/?p=188
    angular.extend(this, new SprintsCtrl($scope, $filter, Restangular, $routeParams, $location)); // extend the sprint controller
    $scope.days = [];
    $scope.showDayDialog = false;
    $scope.createNewDay = false;
    
    // load the days of the sprint if it changes
    $scope.loadSprintDays = function() {
        $scope.burnDownData = null;
        if ($scope.sprint) $scope.sprint.all('days').getList().then(function(days) {
            $scope.days = days;
            // build burndown
            if (days) {
                var sprint = $scope.sprint,
                    day = null,
                    hoursRemaining = sprint.plannedHours,
                    burnDownData = {
                        start: sprint.start,
                        end: sprint.end,
                        plannedHours: sprint.plannedHours,
                        burndowns: [],
                        totalDone: 0,
                        totalAdded: 0,
                        storyPoints: sprint.storyPoints,
                        workDays: 0,
                        totalDays: 0,
                        hoursRemaining: hoursRemaining,
                        passedDays: 0,
                        sprint: $scope.sprint
                    };

                for (var i = days.length - 1; i >= 0; --i) {
                    day = days[i];
                    burnDownData.totalDone += day.burnDown;
                    burnDownData.burndowns.push({
                        date: day.day,
                        hours: hoursRemaining = hoursRemaining - day.burnDown,
                        comment: day.comment
                    });
                    if (day.upscaling > 0) {
                        burnDownData.totalAdded += day.upscaling;
                        burnDownData.burndowns.push({
                            date: day.day,
                            hours: hoursRemaining = hoursRemaining + day.upscaling,
                            comment: day.reasonForUpscaling
                        });
                    }
                }
                burnDownData.hoursRemaining = hoursRemaining;
                var start = new Date(sprint.start),
                    end = new Date(sprint.end),
                    now = new Date().setHours(0,0,0,0);
                burnDownData.timeDomain = [];
                //burnDownData.remainingDays = end - start;
                while(start <= end) {
                    ++burnDownData.totalDays;
                    ++burnDownData.workDays;
                    if (start < now) ++burnDownData.passedDays;
                    burnDownData.timeDomain.push($filter('date')(start, 'yyyy-MM-dd'));
                    start.setDate(start.getDate() + 1); // next day
                    if (start.getDay() === 6) {
                        start.setDate(start.getDate() + 2); // skip weekend
                        burnDownData.totalDays += 2;
                    } 
                }
                burnDownData.remainingDays = burnDownData.workDays - burnDownData.passedDays;
                burnDownData.sprintProgressInPercent = 100 - (burnDownData.remainingDays / burnDownData.workDays * 100)
                //if (burnDownData.remainingDays > 0) burnDownData.timeProgress = 100 - ()
                $scope.burnDownData = burnDownData;
            }
        });
    };
    $scope.$watch('sprint', $scope.loadSprintDays);
    
    $scope.addDay = function() {
        var newDate = new Date();
        if ($scope.days && $scope.days.length > 0) newDate = new Date($scope.days[0].day).addDays(1, true);
        $scope.day = {sprint: Restangular.stripRestangular($scope.sprint.clone()), 
            day: $filter('date')(newDate, 'yyyy-MM-dd'), burnDown: 0, upscaling: 0};
        $scope.dialogHeader = "Add Day to Sprint: " + $scope.sprint.name;
        $scope.createNewDay = true;
        $scope.showDayDialog = true;
    };
    $scope.editDay = function(day) {
        $scope.day = angular.copy(day);
        $scope.dialogHeader = "Edit Sprint Day: " + day.day;
        $scope.createNewDay = false;
        $scope.showDayDialog = true;
    };
    $scope.deleteDay = function(day) {
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
            $scope.showDayDialog = false;
        }
    };
}