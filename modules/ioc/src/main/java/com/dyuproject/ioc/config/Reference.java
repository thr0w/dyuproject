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

package com.dyuproject.ioc.config;

import java.util.Map;

import org.mortbay.log.Log;
import org.mortbay.util.ajax.JSONPojoConvertor;
import org.mortbay.util.ajax.JSON.Convertor;

import com.dyuproject.ioc.Context;

/**
 * A configuration component from a json context file which allows a pojo to 
 * be referenced by an id {@link String}.
 * 
 * @author David Yu
 * @created Feb 20, 2009
 */

public final class Reference
{
    
    static boolean setProps(Object ref, Map<String,Object> props)
    {
        Context context = Context.getCurrent();
        if(context==null)
        {
            Log.warn("No current context for Reference");
            return false;
        }
        Convertor c = context.getParser().getConvertorCache().getConvertor(ref.getClass(), true);
        return c instanceof JSONPojoConvertor && ((JSONPojoConvertor)c).setProps(ref, props)!=0;
    }
    
    protected Object _ref;
    protected Map<String,Object> _props;
    
    public Reference()
    {
        
    }
    
    public Reference(Object ref)
    {
        _ref = ref;
    }
    
    /**
     * Gets the object being referenced.
     */
    public Object getRef()
    {
        return _ref;
    }
    
    /**
     * Sets the object to reference.
     */
    public void setRef(Object ref)
    {
        if(ref!=null)
        {
            _ref = ref;
            if(_props!=null)
                setProps(_ref, _props);
        }        
    }
    
    /**
     * Gets the properties of a referenced object to be able to set or 
     * override existing properties.
     */
    public Map<String,Object> getProps()
    {
        return _props;
    }
    
    /**
     * Sets the properties of a reference object which can set or override existing properties.
     */
    public void setProps(Map<String,Object> props)
    {
        if(props!=null)
        {
            _props = props;
            if(_ref!=null)
                setProps(_ref, _props);
        }
    }

}
