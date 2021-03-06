//========================================================================
//Copyright 2007-2008 David Yu dyuproject@gmail.com
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

package com.dyuproject.web.rest;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.util.Delim;


/**
 * Dispatches to the container's default servlet
 * 
 * @author David Yu
 * @created May 16, 2008
 */

public final class DefaultDispatcher extends AbstractLifeCycle implements ViewDispatcher
{
    
    static final String[] NAMES = Delim.COMMA.split(System.getProperty("web.default_dispatcher_name", "default,_ah_default"));
    
    private static final Logger log = LoggerFactory.getLogger(DefaultDispatcher.class);
    
    RequestDispatcher _default;
    
    protected void init()
    {
        for(String s : NAMES)
        {
            if((_default=getWebContext().getServletContext().getNamedDispatcher(s))!=null)
            {
                log.info("dispatcher name: {}", s);
                break;
            }
        }
        
        if(_default==null)
            log.warn("default dispatcher not resolved");
        
        log.info("initialized.");
    }

    public void dispatch(String uri, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        _default.forward(request, response);        
    }

}
