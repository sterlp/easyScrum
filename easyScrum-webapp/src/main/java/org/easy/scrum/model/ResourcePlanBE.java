/*
 * Copyright 2013 Paul Sterl.
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
package org.easy.scrum.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <pre>
 * |--------------------------------------------------
 * | Days  -16-  | Days  | Off | Hours | -60%-
 * |--------------------------------------------------
 * | Paul        | 16    | -0- |  128  |  76,8
 * | Peter       | 15    | -1- |  120  |  72,0
 * | Hans        | 10    | -6- |   80  |  48,0
 * | -------------------------------------------------
 * | 3 Team Mem  | 41    |  7  |  328  | 196,8
 * |
 * </pre>
 */
//@Entity
//@Table(name = "RESOURCE_PLAN")
public class ResourcePlanBE extends AbstractEntity {
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sprint_id")
    @NotNull
    private SprintBE sprint;
    
    /**
     * Percent value between 1 and 100 which defines how much other work the
     * team has to do, e.g. emails and stuff.
     */
    @Max(100) @Min(1) @Digits(integer = 3, fraction = 2)
    private double focusFactor = 60.0;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="RESOURCE_PLAN_TO_MEMBER",
        joinColumns={@JoinColumn(name="RESOURCE_PLAN_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="TEAM_MEMBER_ID", referencedColumnName="ID")})
    private Set<TeamMemberBE> members = new LinkedHashSet<>();

    @Override
    public String getName() {
        return null;
    }
}
