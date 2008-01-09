/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.scxml;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.TransitionTarget;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BuiltinTest extends TestCase {

    public BuiltinTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(BuiltinTest.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { BuiltinTest.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }
    
    public void testIsMemberEmptySet() {
        Set<TransitionTarget> set = new HashSet<TransitionTarget>();
        
        assertFalse(Builtin.isMember(set, "on"));
    }
    
    public void testIsMemberFalse() {
        TransitionTarget state = new State();
        state.setId("off");
        
        Set<TransitionTarget> set = new HashSet<TransitionTarget>();
        set.add(state);
        
        assertFalse(Builtin.isMember(set, "on"));
    }
    
    public void testIsMemberTrue() {
        TransitionTarget state = new State();
        state.setId("on");
        
        Set<TransitionTarget> set = new HashSet<TransitionTarget>();
        set.add(state);
        
        assertTrue(Builtin.isMember(set, "on"));
    }

}
