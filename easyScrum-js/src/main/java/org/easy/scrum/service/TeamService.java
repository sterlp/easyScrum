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

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.easy.scrum.model.TeamBE;
import org.easy.scrum.model.dao.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class TeamService {

    @Autowired
    private TeamDao teamDao;

    @RequestMapping(value = "/team", method = RequestMethod.GET)
    @ResponseBody
    public List<TeamBE> list() {
        List<TeamBE> result = new ArrayList<>();
        Iterable<TeamBE> teams = teamDao.findAll();
        for (TeamBE teamBE : teams) {
            result.add(teamBE);
        }
        return result;
    }

    @RequestMapping(value = "/team/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TeamBE getTeam(@PathVariable Long id) {   
        return teamDao.findOne(id);
    }
    
    @RequestMapping(value = "/team/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteTeam(@PathVariable Long id) {   
        teamDao.delete(id);
    }
    @RequestMapping(value = "/team", method = RequestMethod.POST)
    @ResponseBody
    public TeamBE save(@Valid @RequestBody TeamBE team) {
        return teamDao.save(team);
    }
    @RequestMapping(value = "/team/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public TeamBE update(@PathVariable Long id, @Valid @RequestBody TeamBE team) {
        team.setId(id);
        team = teamDao.save(team);
        return team;
    }
}
