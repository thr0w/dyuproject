# Openid Login Without Leaving Page #

This is based on http://www.sociallipstick.com/2009/02/?y%/how-to-accept-openid-in-a-popup-without-leaving-the-page/

This demo makes use of the extension http://wiki.openid.net/f/openid_ui_extension_draft01.html

See it live at http://dyuproject.appspot.com/popup_login.html

## Setting it up ##

Files required (browse [here](http://dyuproject.googlecode.com/svn/trunk/modules/demos/openid-servlet/src/main/webapp)):
  * WEB-INF/web.xml
  * login.jsp
  * popup\_login.html
  * popup\_verify.html

`PopupVerifyServlet.java`
```

package com.dyuproject.demos.openidservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.util.ajax.JSON;

import com.dyuproject.openid.Constants;
import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;
import com.dyuproject.openid.http.UrlEncodedParameterMap;

/**
 * Based from http://www.sociallipstick.com/2009/02/how-to-accept-openid-in-a-popup-without-leaving-the-page/
 * See http://wiki.openid.net/f/openid_ui_extension_draft01.html
 * 
 */

@SuppressWarnings("serial")
public class PopupVerifyServlet extends HttpServlet
{
    
    RelyingParty _relyingParty = RelyingParty.getInstance()
        .addListener(new RelyingParty.Listener()
        {
            public void onAccess(OpenIdUser user, HttpServletRequest request)
            {                
            }
            public void onAuthenticate(OpenIdUser user, HttpServletRequest request)
            {                
            }
            public void onDiscovery(OpenIdUser user, HttpServletRequest request)
            {                
            }
            public void onPreAuthenticate(OpenIdUser user, HttpServletRequest request, 
                    UrlEncodedParameterMap params)
            {
                // the popup sign-in magic
                if("true".equals(request.getParameter("popup")))
                {
                    String returnTo = params.get(Constants.OPENID_TRUST_ROOT) + "/popup_verify.html";                    
                    params.put(Constants.OPENID_RETURN_TO, returnTo);
                    params.put(Constants.OPENID_REALM, returnTo);                    
                    params.put("openid.ns.ui", "http://specs.openid.net/extensions/ui/1.0");
                    params.put("openid.ui.mode", "popup");
                }
            } 
        });
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        if("true".equals(request.getParameter("logout")))
        {
            _relyingParty.invalidate(request, response);
            response.setStatus(200);
            return;
        }
        
        try
        {
            OpenIdUser user = _relyingParty.discover(request);
            if(user!=null)
            {
                if(user.isAuthenticated() || 
                        (user.isAssociated() && RelyingParty.isAuthResponse(request) && 
                                _relyingParty.verifyAuth(user, request, response)))
                {
                    response.setContentType("text/json");
                    response.getWriter().write(JSON.toString(user));
                    return;
                }   
            }         
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        response.setStatus(401);
    }

}


```

`WEB-INF/web.xml`
```
<?xml version="1.0" encoding="ISO-8859-1"?>
<web-app 
   xmlns="http://java.sun.com/xml/ns/javaee" 
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" 
   version="2.5" metadata-complete="true">
  
  <servlet>
    <servlet-name>popup-verify-servlet</servlet-name>
    <servlet-class>com.dyuproject.demos.openidservlet.PopupVerifyServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
  </servlet>

  <servlet-mapping>
    <servlet-name>popup-verify-servlet</servlet-name>
    <url-pattern>/popup_verify/</url-pattern>
  </servlet-mapping>
  
  <filter>
    <filter-name>openid-filter</filter-name>
    <filter-class>com.dyuproject.openid.OpenIdServletFilter</filter-class>
    <load-on-startup>1</load-on-startup>
      <init-param>
        <param-name>forwardUri</param-name>
        <param-value>/login.jsp</param-value>
      </init-param>
  </filter>

  <filter-mapping>
    <filter-name>openid-filter</filter-name>
    <url-pattern>*.jsp</url-pattern>
  </filter-mapping>  
    
</web-app>

```

`login.jsp`
```
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Login</title>
</head>
<body>
<div id="container">
  <div style="color:red;font-size:1.4em">&nbsp;${openid_servlet_filter_msg}</div>
  <p>Login with your <span style="color:orange">openid</span></p>
  <form method="POST">
    <input id="openid_identifier" name="openid_identifier" type="text" size=80/>
    <input class="btn" type="submit" value="send"/>
  </form>
</div>

</body>
</html>
```

`popup_verify.html`
```
<html>
<head>
</head>
<body onload="window.opener.handleOpenIDResponse((window.location+'').split('?')[1]);window.close();">
</body>
</html>

```

`popup_login.html`
```
<html>
<head>
<title>OpenID Login without refreshing or leaving the page</title>
</head>
<body>
<div id="container">
  <div class="info">
    <p>OpenID Login without refreshing or leaving the page</p>
  </div>
  <div>
    <button id="login">Login</button>
    <button id="logout">Logout</button>
    <div id="msg"></div>
  </div>
</div>
</body>
</html>

```
`Using jQuery for this example`
```
<script type="text/javascript">
//<!--
function getCenteredCoords(width, height) {
  return {x: $(window).width()/2-width/2, y: $(window).height()/2-height/2};
}

function handleOpenIDResponse(openid_args) {
  $("#msg").hide().html("<p>Verifying authentication ...</p>").show();
  $.ajax({
    type: "POST",
    url: "/popup_verify/",
    data: openid_args,
    dataType: "json",
    success: function(user){
     $("#login").hide();
     $("#logout").show();
     $("#msg").hide().html("<p><span class='green'>" + user.b + "</span> is authenticated.</p>").show(1500);
    },
    error: function(xhr, errStatus, errMessage) {
      $("#msg").html("<p class='red'>Login failed.  Please try again.</p>");
    }
  });
}

$(function(){
  $("#logout").hide();
  $("#login").hide();
  $("#login").click(function(e){
    var w = window.open("/home.jsp?popup=true", "openid_popup", "width=450,height=500,location=1,status=1,resizable=yes");
    var coords = getCenteredCoords(450,500);
    w.moveTo(coords.x, coords.y);
  });
  $("#logout").click(function(e){
    $.ajax({
      type: "GET",
      url: "/popup_verify/?logout=true",
      success: function(text){
        $("#logout").hide();
        $("#login").show();
        $("#msg").hide().html("<p>You are currently not logged in.</p>").show(1500);
      }
    });
  });
  $.ajax({
    type: "GET",
    url: "/popup_verify/",
    dataType: "json",
    success: function(user){
     $("#login").hide();
     $("#logout").show();
     $("#msg").hide().html("<p><span class='green'>" + user.b + "</span> is authenticated.</p>").show(1500);
    },
    error: function(xhr, errStatus, errMessage) {
      $("#logout").hide();
      $("#login").show();
      $("#msg").html("<p>You are currently not logged in.</p>");
    }
  });
  
});
//-->
</script>
```