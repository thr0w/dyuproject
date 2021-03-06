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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.dyuproject.util.ClassLoaderUtil;
import com.dyuproject.util.Delim;
import com.dyuproject.util.http.HttpConnector;
import com.dyuproject.util.http.UrlEncodedParameterMap;
import com.dyuproject.util.http.HttpConnector.Response;

/**
 * The scheme/transport which determines how the oauth parameters are sent over http. 
 * 
 * @author David Yu
 * @created Jun 1, 2009
 */

public abstract class Transport implements Signature.Listener
{
    
    private static final Map<String,Transport> __defaults = new HashMap<String,Transport>(5);
    private static final int __inputBuffer = Integer.getInteger("tranport.input_buffer", 128);
    
    /**
     * Registers a custom transport.
     */
    public static void register(Transport transport)
    {
        __defaults.put(transport.getName(), transport);
    }
    
    /**
     * Gets a transport based from the given name.
     */
    public static Transport get(String name)
    {
        return __defaults.get(name);
    }
    
    static
    {
        register(HttpAuthTransport.DEFAULT);
        register(HttpPostTransport.DEFAULT);
        register(HttpGetTransport.DEFAULT);
        String ext = System.getProperty("oauth.transports.ext");
        if(ext!=null)
        {
            try
            {
                StringTokenizer tokenizer = new StringTokenizer(ext, ",;");
                while(tokenizer.hasMoreTokens())
                {
                    register((Transport)ClassLoaderUtil.newInstance(tokenizer.nextToken().trim(), 
                            Signature.class));
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Appends the key and the value; Encodes the value before it is appended. 
     */
    public static void appendToUrl(String key, String value, StringBuilder urlBuffer)
    {
        urlBuffer.append('&').append(key).append('=').append(Signature.encode(value));
    }
    
    /**
     * Builds the auth url as {@link StringBuilder} to redirect the user to, based from 
     * the given token.
     */
    public static StringBuilder buildAuthUrl(String authUrl, Token token)
    {
        char separator = authUrl.indexOf('?')==-1 ? '?' : '&';
        return new StringBuilder()
            .append(authUrl)
            .append(separator)
            .append(Constants.OAUTH_TOKEN)
            .append('=')
            .append(Signature.encode(token.getKey()));
    }
    
    /**
     * Builds the auth url as {@link StringBuilder} to redirect the user to, based from 
     * the given token and callback url.
     */
    public static StringBuilder buildAuthUrl(String authUrl, Token token, String callbackUrl)
    {
        char separator = authUrl.indexOf('?')==-1 ? '?' : '&';
        return new StringBuilder()
            .append(authUrl)
            .append(separator)
            .append(Constants.OAUTH_TOKEN)
            .append('=')
            .append(Signature.encode(token.getKey()))
            .append('&')
            .append(Constants.OAUTH_CALLBACK)
            .append('=')
            .append(callbackUrl==null ? Constants.OOB : Signature.encode(callbackUrl));
    }
    
    /**
     * Gets the auth url based from the given token.
     */
    public static String getAuthUrl(String authUrl, Token token)
    {
        return buildAuthUrl(authUrl, token).toString();
    }
    
    /**
     * Gets the auth url based from the given token and callback url.
     */
    public static String getAuthUrl(String authUrl, Token token, String callbackUrl)
    {
        return buildAuthUrl(authUrl, token, callbackUrl).toString();
    }
    
    static Response parse(Response response, Token token) throws IOException
    {
        if(response.getStatus()==200)
            parse(response.getInputStream(), token);
        return response;
    }
    
    static String getMessage(InputStream in) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(in, Constants.ENCODING);
        char[] cbuf = new char[__inputBuffer];
        StringBuilder buffer = new StringBuilder(__inputBuffer);
        for(int len=0; (len = reader.read(cbuf))!=-1; )
            buffer.append(cbuf, 0, len);
        return buffer.toString();
    }
    
    static void parse(InputStream in, Token token) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(in, Constants.ENCODING);
        char[] cbuf = new char[__inputBuffer];
        StringBuilder buffer = new StringBuilder(__inputBuffer);
        for(int len=0; (len = reader.read(cbuf))!=-1; )
            buffer.append(cbuf, 0, len);
        
        String[] pairs = Delim.AMPER.split(buffer);
        String key=null, secret=null;
        for(String pair : pairs)
        {
            int idx = pair.indexOf('=');
            if(idx==-1)
                continue;
            String k = pair.substring(0, idx);
            String v = Signature.decode(pair.substring(idx+1));
            if(key==null && Constants.OAUTH_TOKEN.equals(k))
                key = v;
            else if(secret==null && Constants.OAUTH_TOKEN_SECRET.equals(k))
                secret = v;
            else
                token.setAttribute(k, v);
        }
        if(key!=null && secret!=null)
            token.set(token.getState()+1, key, secret);
    }
    
    /**
     * Sends the params to the service provided and returns the http response {@link Response}.
     */
    public abstract Response send(UrlEncodedParameterMap params, Endpoint ep, Token token,
            TokenExchange exchange, NonceAndTimestamp nts, Signature signature, 
            HttpConnector connector) throws IOException;
    
    /**
     * Gets the name of this transport.
     */
    public abstract String getName();
    
    /**
     * Gets the method name of this transport.
     */
    public abstract String getMethod();
    
    public final void handleRequestParameter(String key, String value, StringBuilder buffer)
    {
        buffer.append('&').append(key).append('=').append(value);
    }
    
    /**
     * Put the default oauth parameters on the {@link UrlEncodedParameterMap} {@code params}.
     */
    public final void putDefaults(UrlEncodedParameterMap params, Endpoint ep, Token token, 
            TokenExchange exchange, NonceAndTimestamp nts, Signature signature,  
            StringBuilder oauthBuffer, StringBuilder requestBuffer)
    {
        exchange.put(params, ep, token);
        nts.put(params, ep.getConsumerKey());
        signature.generate(params, ep.getConsumerSecret(), token, getMethod(), this, oauthBuffer, 
                requestBuffer);
    }

}
