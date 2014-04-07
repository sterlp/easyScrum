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
package org.easy.scrum.service;

import java.util.List;
import javax.validation.Valid;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.easy.scrum.model.dao.SprintDao;
import org.easy.scrum.model.dao.SprintDayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class SprintService {

    @Autowired
    private SprintDao sprintDao;
    
    @Autowired
    private SprintDayDao sprintDayDao;

    @RequestMapping(value = "/teams/{teamId}/sprints", method = RequestMethod.GET)
    @ResponseBody
    public List<SprintBE> teamSprints(@PathVariable Long teamId) throws Exception {
        return sprintDao.findBySprintId(teamId);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints/{sprintId}/days", method = RequestMethod.GET)
    @ResponseBody
    public List<SprintDayBE> sprintDays(
            @PathVariable Long teamId, 
            @PathVariable Long sprintId) throws Exception {
        return sprintDayDao.findDaysInSprint(sprintId);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints", method = RequestMethod.POST)
    @ResponseBody
    public SprintBE addSprint(
            @PathVariable Long teamId,
            @Valid @RequestBody SprintBE sprint) {
        sprint.setId(null);
        return sprintDao.save(sprint);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints/{sprintId}", method = RequestMethod.PUT)
    @ResponseBody
    public SprintBE updateSprint(
            @PathVariable Long teamId,
            @PathVariable Long sprintId,
            @Valid @RequestBody SprintBE sprint) {
        sprint.setId(sprintId);
        return sprintDao.save(sprint);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints/{sprintId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSprint(
            @PathVariable Long teamId,
            @PathVariable Long sprintId) {
        sprintDao.delete(sprintId);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints/{sprintId}/days", method = RequestMethod.POST)
    @ResponseBody
    public SprintDayBE addDay(
            @PathVariable Long teamId, 
            @PathVariable Long sprintId,
            @Valid @RequestBody SprintDayBE day) throws Exception {
        day.setId(null);
        return sprintDayDao.save(day);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints/{sprintId}/days/{dayId}", method = RequestMethod.PUT)
    @ResponseBody
    public SprintDayBE updateDay(
            @PathVariable Long teamId, 
            @PathVariable Long sprintId,
            @PathVariable Long dayId,
            @Valid @RequestBody SprintDayBE day) throws Exception {
        day.setId(dayId);
        return sprintDayDao.save(day);
    }
    
    @RequestMapping(value = "/teams/{teamId}/sprints/{sprintId}/days/{dayId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteDay(
            @PathVariable Long teamId, 
            @PathVariable Long sprintId,
            @PathVariable Long dayId) throws Exception {
        sprintDayDao.delete(dayId);
    }
}
