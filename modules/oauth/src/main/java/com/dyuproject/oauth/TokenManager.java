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

package com.dyuproject.oauth;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the token; Provides the ability to get, save or invalidate the token.
 * 
 * @author David Yu
 * @created Jun 2, 2009
 */

public interface TokenManager
{
    
    /**
     * Initialize this manager based from the given properties.
     */
    public void init(Properties properties);
    
    /**
     * Gets the token.
     */
    public Token getToken(String consumerKey, HttpServletRequest request)
    throws IOException;
    
    /**
     * Saves the token.
     */
    public boolean saveToken(Token token, HttpServletRequest request, HttpServletResponse response)
    throws IOException;
    
    /**
     * Invalidates the token.
     */
    public boolean invalidate(String consumerKey, HttpServletRequest request, HttpServletResponse response) 
    throws IOException;

}
