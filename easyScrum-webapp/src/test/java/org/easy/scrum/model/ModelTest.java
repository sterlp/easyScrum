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

import org.junit.Test;
import static org.junit.Assert.*;

public class ModelTest {
    
    @Test
    public void testEquals() {
        TeamBE t1 = new TeamBE();
        TeamBE t2 = new TeamBE();
        SprintBE s = new SprintBE();
        
        assertFalse("Null should not be equal", t1.equals(null));
        assertFalse("Different Objects should not be equal", t1.equals(t2));
        assertFalse("Different Objects should not be equal", t1.equals(s));
        
        t1.setId(1L);
        t2.setId(1L);
        s.setId(1L);
        assertTrue("Same Objects same Id should be equal", t1.equals(t2));
        assertFalse("Different Objects should not be equal", t1.equals(s));
        
        t1.setId(1L);
        t2.setId(2L);
        assertFalse("Same Objects different Id should not be equal", t1.equals(t2));
    }
}