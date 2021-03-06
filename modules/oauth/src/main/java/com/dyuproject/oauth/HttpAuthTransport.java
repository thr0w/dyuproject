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

package com.dyuproject.oauth;

import java.io.IOException;

import com.dyuproject.util.http.HttpConnector;
import com.dyuproject.util.http.UrlEncodedParameterMap;
import com.dyuproject.util.http.HttpConnector.Parameter;
import com.dyuproject.util.http.HttpConnector.Response;

/**
 * Sends the oauth request parameters via the HTTP Authorization header.
 * 
 * @author David Yu
 * @created Jun 1, 2009
 */

public final class HttpAuthTransport extends Transport
{
    
    /**
     * "Authorization"
     */
    public static final String NAME = "Authorization";
    
    /**
     * "WWW-Authenticate"
     */
    public static final String WWW_AUTHENTICATE= "WWW-Authenticate";
    
    /**
     * The default instance.
     */
    public static final HttpAuthTransport DEFAULT = new HttpAuthTransport();
    
    /**
     * Gets the default instance.
     */
    public static HttpAuthTransport getDefault()
    {
        return DEFAULT;
    }
    
    static final Signature.Listener __authHeaderListener = new Signature.Listener()
    {
        public void handleOAuthParameter(String key, String value, StringBuilder oauthBuffer)
        {
            oauthBuffer.append(',').append(key).append('=').append('"').append(value).append('"');
        }
        public void handleRequestParameter(String key, String value, StringBuilder requestBuffer)
        {
            
        }        
    };
    
    public String getName()
    {
        return NAME;
    }    
    
    public String getMethod()
    {
        return HttpConnector.GET;
    }
    
    public void handleOAuthParameter(String key, String value, StringBuilder buffer)
    {
        buffer.append(',').append(key).append('=').append('"').append(value).append('"');
    }

    public Response send(UrlEncodedParameterMap params, Endpoint ep, Token token,
            TokenExchange exchange, NonceAndTimestamp nts, Signature signature, 
            HttpConnector connector) throws IOException
    {
        StringBuilder oauthBuffer = new StringBuilder();
        StringBuilder requestBuffer = new StringBuilder();
        
        putDefaults(params, ep, token, exchange, nts, signature, oauthBuffer, requestBuffer);
        
        oauthBuffer.setCharAt(0, ' ');
        oauthBuffer.insert(0, "OAuth");
        
        String url = params.getUrl();
        
        if(requestBuffer.length()!=0)
        {
            requestBuffer.setCharAt(0, '?');
            requestBuffer.insert(0, url);
            url = requestBuffer.toString();
        }

        return parse(connector.doGET(url, new Parameter(NAME, oauthBuffer.toString())), token);
    }
    
    /**
     * Gets the computed value for the "Authorization" header.
     */
    public static String getAuthHeaderValue(UrlEncodedParameterMap params, Endpoint ep, 
            Token token, NonceAndTimestamp nts, Signature signature)
    {
        StringBuilder oauthBuffer = new StringBuilder();
        params.put(Constants.OAUTH_CONSUMER_KEY, ep.getConsumerKey());
        params.put(Constants.OAUTH_TOKEN, token.getKey());
        nts.put(params, ep.getConsumerKey());
        signature.generate(params, ep.getConsumerSecret(), token, DEFAULT.getMethod(), 
                __authHeaderListener, oauthBuffer, null);
        
        oauthBuffer.setCharAt(0, ' ');
        oauthBuffer.insert(0, "OAuth");
        
        return oauthBuffer.toString();
    }

}
