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

package com.dyuproject.oauth.sp;

/**
 * Simple service token to hold the keys
 * 
 * @author David Yu
 * @created Jun 8, 2009
 */

public final class SimpleServiceToken implements ServiceToken
{
    
    private static final long serialVersionUID = 2009100694L;
    
    private final String _consumerSecret, _key, _secret, _id;
    
    public SimpleServiceToken(String consumerSecret, String key, String secret)
    {
        this(consumerSecret, key, secret, null);
    }
    
    public SimpleServiceToken(String consumerSecret, String key, String secret, String id)
    {
        _consumerSecret = consumerSecret;
        _key = key;
        _secret = secret;
        _id = id;
    }

    public String getConsumerSecret()
    {
        return _consumerSecret;
    }

    public String getKey()
    {
        return _key;
    }

    public String getSecret()
    {
        return _secret;
    }
    
    public String getId()
    {
        return _id;
    }

}
