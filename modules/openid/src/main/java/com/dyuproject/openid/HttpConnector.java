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

package com.dyuproject.openid;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * HttpConnector - reads and writes bytes from urls.
 * 
 * @author David Yu
 * @created Sep 8, 2008
 */

public interface HttpConnector
{
    
    public static final String HEAD = "HEAD";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String X_WWW_FORM_URLENCODED = "x-www-form-urlencoded";
    
    public Response doHEAD(String url, OpenIdContext context) throws IOException;
    
    public Response doGET(String url, OpenIdContext context) throws IOException;
    
    public Response doGET(String url, Map<String,Object> parameters, OpenIdContext context) 
    throws IOException;
    
    public Response doPOST(String url, Map<String,Object> parameters, 
            OpenIdContext context) throws IOException;
    
    public Response doPOST(String url, String contentType, byte[] data, OpenIdContext context)
    throws IOException;
    
    public interface Response
    {
        
        public InputStream getInputStream() throws IOException;
        
        public String getHeader(String name);
        
        public void close() throws IOException;

    }

}