/*
 * Copyright 2014 sterlp.
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
import org.easy.scrum.model.GoalBE;
import org.easy.scrum.model.dao.GoalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/goals")
public class GoalService {

    @Autowired
    private GoalDao goalDao;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<GoalBE> findAllGoals() {
        Iterable<GoalBE> goals = goalDao.findAll();
        List<GoalBE> result = new ArrayList<>();
        for (GoalBE goal : goals) {
            result.add(goal);
        }
        return result;
    }
}
