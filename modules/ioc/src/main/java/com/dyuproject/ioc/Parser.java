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

import com.dyuproject.ioc.Resource.Resolver;
import com.dyuproject.json.ConvertorCache;
import com.dyuproject.json.StandardJSON;


/**
 * Base class for a parser.
 * 
 * @author David Yu
 * @created Feb 21, 2009
 */

public abstract class Parser extends StandardJSON
{
    
    /**
     * The default instance. ({@link DefaultParser})
     */
    public static final Parser DEFAULT = new DefaultParser();
    
    /**
     * Gets the default instance.
     */
    public static Parser getDefault()
    {
        return DEFAULT;
    }

    protected final Resolver _resolver;
    
    protected Parser(ConvertorCache convertorCache, Resolver resolver)
    {
        super(convertorCache);
        _resolver = resolver;
    }

    /**
     * Gets the resolver.
     */
    public Resolver getResolver()
    {
        return _resolver;
    }
    
    /**
     * Parses the given resource and loads it into the {@link ApplicationContext}
     */
    public abstract void parse(Resource resource, ApplicationContext appContext);
    //protected abstract Object handleUnknown(Source source, char c);
    


}
