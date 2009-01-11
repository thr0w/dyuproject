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

package com.dyuproject.web.rest.service;

import com.dyuproject.web.rest.WebContext;

/**
 * A bean which contain annotated methods w/c will be a REST resource.
 * 
 * @author David Yu
 * @created Dec 4, 2008
 */

public interface Service
{
    
    public void init(WebContext context);
    public void destroy(WebContext context);

}