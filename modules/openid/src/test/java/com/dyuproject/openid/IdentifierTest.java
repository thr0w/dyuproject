//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package com.dyuproject.openid;

import junit.framework.TestCase;

/**
 * @author David Yu
 * @created Jan 10, 2009
 */

public class IdentifierTest extends TestCase
{
    
    public void testNormalization()
    {
        // from http://openid.net/specs/openid-authentication-2_0.html#normalization_example        
        assertEquals(Identifier.getIdentifier("example.com", null, null).getUrl(), "http://example.com/");
        assertEquals(Identifier.getIdentifier("http://example.com", null, null).getUrl(), "http://example.com/");
        assertEquals(Identifier.getIdentifier("http://example.com/user", null, null).getUrl(), "http://example.com/user");
        assertEquals(Identifier.getIdentifier("http://example.com/user/", null, null).getUrl(), "http://example.com/user/");
        assertEquals(Identifier.getIdentifier("http://example.com/user", null, null).getUrl(), "http://example.com/user");
        assertEquals(Identifier.getIdentifier("http://example.com/", null, null).getUrl(), "http://example.com/");       

    }    

}
