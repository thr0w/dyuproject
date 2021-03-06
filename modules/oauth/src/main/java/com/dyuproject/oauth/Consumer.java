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
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dyuproject.oauth.manager.HttpSessionTokenManager;
import com.dyuproject.util.ClassLoaderUtil;
import com.dyuproject.util.http.HttpConnector;
import com.dyuproject.util.http.SimpleHttpConnector;
import com.dyuproject.util.http.UrlEncodedParameterMap;
import com.dyuproject.util.http.HttpConnector.Response;

/**
 * OAuth Consumer helper class which manages the oauth consumer lifecycle
 * 
 * @author David Yu
 * @created May 29, 2009
 */

public final class Consumer
{
    /**
     * The default resource path. ("oauth_consumer.properties")
     */
    public static final String DEFAULT_RESOURCE_PATH = "oauth_consumer.properties";
    
    private static Consumer __instance;
    
    /**
     * Gets the instance configured from the default resource path {@link #DEFAULT_RESOURCE_PATH}.
     */
    public static Consumer getInstance()
    {
        Consumer instance = __instance;
        if(instance==null)
        {
            synchronized(Consumer.class)
            {
                instance = __instance;
                if(instance==null)
                    __instance = instance = newInstance(DEFAULT_RESOURCE_PATH);
            }
        }
        return instance;
    }
    
    /**
     * Creates a new instance configured from the resource path {@code resourceLoc}.
     */
    public static Consumer newInstance(String resourceLoc)
    {        
        URL resource = ClassLoaderUtil.getResource(resourceLoc, Consumer.class);
        if(resource==null)
            throw new RuntimeException(resourceLoc + " not found in the classpath.");
        try
        {
            return newInstance(resource);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }        
    }
    
    /**
     * Creates a new instance configured from the {@link URL} {@code resource}.
     */
    public static Consumer newInstance(URL resource) throws IOException
    {
        return newInstance(resource.openStream());
    }
    
    /**
     * Creates a new instance configured from the {@link InputStream} {@code in}.
     */
    public static Consumer newInstance(InputStream in) throws IOException
    {
        Properties props = new Properties();
        props.load(in);
        return newInstance(props);
    }
    
    /**
     * Creates a new instance configured form the {@link Properties} {@code props}.
     */
    public static Consumer newInstance(Properties props) throws IOException
    {
        String httpConnectorParam = props.getProperty("oauth.consumer.httpconnector");
        HttpConnector httpConnector = httpConnectorParam==null ? SimpleHttpConnector.getDefault() : 
            (HttpConnector)newObjectInstance(httpConnectorParam);
        
        String ntsParam = props.getProperty("oauth.consumer.nonce_and_timestamp");
        NonceAndTimestamp nonceAndTimestamp = ntsParam==null ? SimpleNonceAndTimestamp.DEFAULT : 
            (NonceAndTimestamp)newObjectInstance(ntsParam);
        
        String domains = props.getProperty("oauth.consumer.endpoint.domains");
        if(domains==null)
            throw new IllegalStateException("oauth.consumer.endpoint.domains not found.");
        
        HashMap<String,Endpoint> endpoints = new HashMap<String,Endpoint>();
        
        StringTokenizer tokenizer = new StringTokenizer(domains, ",;");
        while(tokenizer.hasMoreTokens())
        {
            Endpoint ep = Endpoint.load(props, tokenizer.nextToken().trim());
            endpoints.put(ep.getDomain(), ep);
        }
        
        ConsumerContext context = new ConsumerContext(httpConnector, nonceAndTimestamp, endpoints);
        
        String tokenManagerParam = props.getProperty("oauth.consumer.token.manager");
        TokenManager tokenManager = tokenManagerParam==null ? new HttpSessionTokenManager() : 
            (TokenManager)newObjectInstance(tokenManagerParam);
            
        tokenManager.init(props);
        
        return new Consumer(context, tokenManager);
    }
    
    static Object newObjectInstance(String className)
    {        
        try
        {
            return ClassLoaderUtil.newInstance(className, Consumer.class);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private final ConsumerContext _context;
    private final TokenManager _manager;
    
    public Consumer(ConsumerContext context, TokenManager manager)
    {
        _context = context;
        _manager = manager;
    }
    
    /**
     * Gets the consumer context.
     */
    public ConsumerContext getConsumerContext()
    {
        return _context;
    }
    
    /**
     * Gets the token manager.
     */
    public TokenManager getTokenManager()
    {
        return _manager;
    }
    
    /**
     * Gets the endpoint from the given {@code domain}.
     */
    public Endpoint getEndpoint(String domain)
    {
        return _context.getEndpoint(domain);
    }
    
    /**
     * Adds an endpoint {@code ep} which would also be added 
     * to the wrapped {@link ConsumerContext}.
     */
    public void addEndpoint(Endpoint ep)
    {
        _context.addEndpoint(ep);
    }    
    
    /**
     * Gets a token from the {@code request} and delegates to the wrapped 
     * {@link TokenManager} if not found.  The new token will be set in the
     * request attribute.
     */
    public Token getToken(String consumerKey, HttpServletRequest request)
    throws IOException
    {
        Token token = (Token)request.getAttribute(consumerKey);
        if(token==null)
        {
            token = _manager.getToken(consumerKey, request);
            if(token==null)
                token = new Token(consumerKey);
            request.setAttribute(token.getCk(), token);
        }
        return token;
    }
    
    /**
     * Saves the token; Delegates to the {@link TokenManager}.
     */
    public boolean saveToken(Token token, HttpServletRequest request, HttpServletResponse response) 
    throws IOException
    {
        return _manager.saveToken(token, request, response);
    }
    
    /**
     *Invalidates the token based from its key; Delegates to the {@link TokenManager}.
     */
    public boolean invalidate(String consumerKey, HttpServletRequest request, HttpServletResponse response) 
    throws IOException
    {
        return _manager.invalidate(consumerKey, request, response);
    }
    
    /**
     *Invalidates the token.; Delegates to the {@link TokenManager}.
     */
    public boolean invalidate(Token token, HttpServletRequest request, HttpServletResponse response) 
    throws IOException
    {
        return _manager.invalidate(token.getCk(), request, response);
    }

    /**
     * Fetches the token using the given endpoint and token - and 
     * returns the http response {@link Response}.
     */
    public Response fetchToken(Endpoint ep, Token token)
    throws IOException
    {
        return fetchToken(ep, new UrlEncodedParameterMap(), TokenExchange.getExchange(token), token);
    }
    
    /**
     * Fetches the token using the given endpoint, exchange and token - and 
     * returns the http response {@link Response}. 
     */
    public Response fetchToken(Endpoint ep, TokenExchange exchange, Token token)
    throws IOException
    {
        return fetchToken(ep, new UrlEncodedParameterMap(), exchange, token);
    }
    
    /**
     * Fetches the token using the given endpoint, params, exchange and token - and
     * returns the http response {@link Response}.
     */
    public Response fetchToken(Endpoint ep, UrlEncodedParameterMap params, TokenExchange exchange, 
            Token token) throws IOException
    {
        return ep.getTransport().send(params, ep, token, exchange, _context.getNonceAndTimestamp(), 
                ep.getSignature(), _context.getHttpConnector());
    }
    
    

}
