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

package com.dyuproject.ioc;

import java.io.IOException;
import java.net.URL;


/**
 *  A resolver that resolves a resource by opening the stream to the {@link URL}.
 * 
 * @author David Yu
 * @created Feb 23, 2009
 */

public final class URLResolver extends AbstractResolver
{
    
    /**
     * The type of this resolver. ("url")
     */
    public static final String TYPE = generateTypeFromClass(URLResolver.class);
    
    /**
     * The default instance.
     */
    public static final URLResolver DEFAULT = new URLResolver();
    
    /**
     * Gets the default instance.
     */
    public static URLResolver getDefault()
    {
        return DEFAULT;
    }
    
    public URLResolver()
    {
        
    }
    
    public String getType()
    {
        return TYPE;
    }    

    public void resolve(Resource resource, Context context) throws IOException
    {
        resource.resolve(newReader(new URL(resource.getPath()).openStream()), getType());
    }

    public Resource createResource(String path) throws IOException
    {
        return new Resource(path, getType(), newReader(new URL(path).openStream()));
    }
    
    /**
     * Creates a resource from a given {@code url}.
     */
    public Resource createResource(URL url) throws IOException
    {
        return new Resource(url.toString(), getType(), newReader(url.openStream()));
    }
    
    
}
