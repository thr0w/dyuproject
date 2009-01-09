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

/**
 * Discovery
 * 
 * @author David Yu
 * @created Sep 10, 2008
 */

public interface Discovery
{
    
    public static final String OPENID_SERVER = "openid.server";
    public static final String OPENID_DELEGATE = "openid.delegate";
    public static final String OPENID2_PROVIDER = "openid2.provider";
    public static final String OPENID2_LOCALID = "openid2.local_id";
    
    public OpenIdUser discover(String claimedId, String url, OpenIdContext context) 
    throws Exception;

}
