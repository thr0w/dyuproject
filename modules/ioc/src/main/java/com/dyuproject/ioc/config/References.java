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

import java.util.HashMap;
import java.util.Map;

import org.mortbay.util.ajax.JSON.Convertible;
import org.mortbay.util.ajax.JSON.Output;

import com.dyuproject.ioc.Context;

/**
 * A collection of multiple {@link Reference} objects.
 * 
 * @author David Yu
 * @created Feb 20, 2009
 */

public class References implements Convertible
{
    
    
    /**
     * Looks up the {@link References} starting from the last.
     */
    public static References getLast(References refs)
    {
        return refs._refs==null ? refs : getLast(refs._refs);
    }
    
    /**
     * Wraps the {@code refs} into the {@code wrapper}.
     */
    public static void wrapRefs(References refs, References wrapper)
    {
        if(wrapper._refs==null)
            wrapper._refs = refs;
        else
            wrapRefs(refs, wrapper._refs);          
    }
    
    /**
     * Gets a referenced object from the provided {@code refs}.
     */
    public static Object getRef(String key, References refs)
    {
        if(refs._map==null)
            return refs._refs==null ? null : getRef(key, refs._refs);
        
        Object value = refs._map.get(key);        
        return value!=null ? value : (refs._refs==null ? null : getRef(key, refs._refs));
    }

    References _refs;
    Map<String,Object> _map;
    
    public References()
    {
        Context context = Context.getCurrent();
        if(context!=null)
            context.getAppContext().addRefs(this);
    }
    
    public References(Map<String,Object> map)
    {
        _map = map;
    }
    
    /**
     * Add {@code refs} to this {@link References} object.
     */
    public final void addRefs(References refs)
    {
        wrapRefs(refs, this);
    }
    
    /**
     * Puts a referenced object with the given {@code key}.
     */
    public final Object put(String key, Object value)
    {
        if(_map==null)
            _map = new HashMap<String,Object>();
        
        return _map.put(key, value);
    }
    
    /**
     * Gets a referenced object with the given {@code key}.
     */
    public final Object get(String key)
    {
        return key==null ? null : getRef(key, this);
    }
    
    /**
     * Puts a collection of referenced objects.
     */
    public final void putAll(Map<String,Object> map)
    {
        if(_map==null)
            _map = map;
        else
            _map.putAll(map);
    }
    
    @SuppressWarnings("unchecked")
    public final void fromJSON(Map map)
    {
        map.remove("class");
        if(_map==null)
            _map = (Map<String,Object>)map;
        else
            _map.putAll((Map<String,Object>)map);
    }

    public final void toJSON(Output out)
    {
        if(_map==null || _map.isEmpty())
            return;
        
        for(Map.Entry<String, Object> entry: _map.entrySet())
            out.add(entry.getKey(), entry.getValue());
    }
    
    /**
     * Destroy this object and all its references to other objects.
     */
    public final void destroy()
    {
        if(_refs!=null)
            _refs.destroy();
        _refs = null;
        
        if(_map!=null)
            _map.clear();
        _map = null;
    }

}
