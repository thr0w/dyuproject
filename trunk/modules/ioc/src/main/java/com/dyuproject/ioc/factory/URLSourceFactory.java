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

package com.dyuproject.ioc.factory;

import java.io.IOException;
import java.net.URL;

import org.mortbay.util.ajax.JSON.Source;

/**
 * @author David Yu
 * @created Feb 21, 2009
 */

public class URLSourceFactory extends AbstractSourceFactory
{
    
    private static final URLSourceFactory __instance = new URLSourceFactory();
    
    public static URLSourceFactory getInstance()
    {
        return __instance;
    }
    
    private URLSourceFactory()
    {
        
    }
    
    public Object getResource(String resource) throws IOException
    {
        return new URL(resource);
    }

    public Source getSource(String resource) throws IOException
    {        
        return getSource(new URL(resource));
    }
    
    public Source getSource(String resource, String metadata) throws IOException
    {
        return getSource(resource);
    }
    
    public Source getSource(URL resource) throws IOException
    {
        return getSource(resource.openStream());
    }

}
