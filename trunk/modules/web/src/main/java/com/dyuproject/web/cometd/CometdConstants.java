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

package com.dyuproject.web.cometd;

import dojox.cometd.Client;

/**
 * @author David Yu
 */

public abstract class CometdConstants 
{
	
	public static final String MSG_ID = "msgId";
	public static final String FROM_CLIENT = "fromClient";
	public static final String LISTENER_ATTR = MessageListener.class.getName();
	public static final String CLIENT_ID_ATTR = Client.class.getName() + ".id";
	public static final String DATA_PARAM = "data";
	
	public static final int MESSAGE_BUFFER = 10;

}
