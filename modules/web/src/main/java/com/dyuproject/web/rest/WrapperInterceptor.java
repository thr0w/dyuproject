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

import javax.servlet.ServletException;

/**
 * @author David Yu
 * @created Dec 4, 2008
 */

public class WrapperInterceptor extends AbstractInterceptor
{
    
    private Interceptor _interceptor;
    
    public void setInterceptor(Interceptor interceptor)
    {
        _interceptor = interceptor;
    }
    
    public Interceptor getInterceptor()
    {
        return _interceptor;
    }

    @Override
    protected void init()
    {
        // TODO Auto-generated method stubs
        
    }

    public void postHandle(boolean handled, RequestContext requestContext)
            throws ServletException, IOException
    {
        _interceptor.postHandle(handled, requestContext);        
    }

    public boolean preHandle(RequestContext requestContext)
            throws ServletException, IOException
    {        
        return _interceptor.preHandle(requestContext);
    }

    
    

}