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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentHashMap - in-memory consumer keys
 * @author David Yu
 * @created Jun 8, 2009
 */

public final class ConcurrentMapHashStore extends HashStore
{
    
    private final ConcurrentMap<String,String> _consumers = new ConcurrentHashMap<String,String>();
    
    public ConcurrentMapHashStore(String secretKey, String macSecretKey)
    {
        super(secretKey, macSecretKey);
    }
    
    public ConcurrentMapHashStore(String secretKey, String macSecretKey, String macAlgorithm, 
            long accessTimeout, long exchangeTimeout, long loginTimeout)
    {
        super(secretKey, macSecretKey, macAlgorithm, accessTimeout, exchangeTimeout, loginTimeout);
    }

    protected String getConsumerSecret(String consumerKey)
    {
        return _consumers.get(consumerKey);
    }
    
    /**
     * Adds a consumer entry (consumerKey and consumerSecret); 
     * If there is an existing consumerKey, it will not be added.
     */
    public ConcurrentMapHashStore addConsumer(String consumerKey, String consumerSecret)
    {
        _consumers.putIfAbsent(consumerKey, consumerSecret);
        return this;
    }

}
