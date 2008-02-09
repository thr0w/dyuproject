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

package com.dyuproject.web.ws;

import com.dyuproject.util.FormatConverter.Builder;

/**
 * @author David Yu
 */

public abstract class AbstractWebServiceError implements WebServiceResponse
{

    public final void convert(Builder builder, String format) 
    {
        builder.put("error", true);
        builder.put("cause", getClass().getSimpleName());
        builder.put("message", getMessage());
    }
    
    protected String getMessage()
    {
        return getClass().getSimpleName();
    }
}
