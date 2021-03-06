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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;


/**
 * A resolver that resolves a resource by from the filesystem.
 * 
 * @author David Yu
 * @created Feb 23, 2009
 */

public class FileResolver extends AbstractResolver
{
    
    /**
     * The type of this resolver. ("file")
     */
    public static final String TYPE = generateTypeFromClass(FileResolver.class);
    
    public static final FileResolver DEFAULT = new FileResolver();
    
    /**
     * Gets the default instance.
     */
    public static FileResolver getDefault()
    {
        return DEFAULT;
    }
    
    public FileResolver()
    {
        
    }
    
    public String getType()
    {
        return TYPE;
    }
    
    public void resolve(Resource resource, Context context) throws IOException
    {
        File file = null;
        String path = resource.getPath();
        if(context==null || path.charAt(0)=='/' || (file=context.getResource().getFile())==null)
            file = new File(path);
        else
            file = new File(file.getParentFile(), path);
        
        resource.resolve(newReader(new FileInputStream(file)), getType(), file);
    }

    public Resource createResource(String path) throws IOException
    {
        return createResource(new File(path));
    }
    
    /**
     * Creates a resource from a given {@code file}.
     */
    public Resource createResource(File file) throws IOException
    {
        Reader reader = newReader(new FileInputStream(file));
        Resource resource = new Resource(file.getPath());
        resource.resolve(reader, getType(), file);
        return resource;
    }
    
}
