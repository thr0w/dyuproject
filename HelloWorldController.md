**As of dyuproject-1.1.x, this is deprecated.  See [HelloWorldService](HelloWorldService.md) instead.**


Below is a dyuproject-1.0.x configuration.

With 1.1.x, package name has changed from _com.dyuproject.web.mvc_ to _com.dyuproject.web.**rest**.mvc_

You can see the source [here](http://dyuproject.googlecode.com/svn/branches/dyuproject-1.0/modules/demos/helloworld/src/main/java/com/dyuproject/demos/helloworld/HelloWorldController.java)

`HelloWorldController.java`
```

package com.dyuproject.demos.helloworld;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dyuproject.util.format.FormatConverter;
import com.dyuproject.util.format.JSONConverter;
import com.dyuproject.util.format.XMLConverter;
import com.dyuproject.util.format.FormatConverter.Builder;
import com.dyuproject.web.mvc.AbstractController;

/**
 * A simple hello world controller
*/

public class HelloWorldController extends AbstractController
{
    
    /**
     * unique identifier
     * handles http://localhost:8080/helloworld
     */
    public static final String IDENTIFIER = "helloworld";
    
    /**
     * /helloworld/${token}
     * To get the value, you call request.getAttribute(IDENTIFIER_ATTR);
     */    
    public static final String IDENTIFIER_ATTR = HelloWorldController.class.getName(); 
    
    public HelloWorldController()
    {
        // set the identifier (required)
        setIdentifier(IDENTIFIER);
        
        // (optional)
        setIdentifierAttribute(IDENTIFIER_ATTR);
    }

    /**
     * initialize your controller
     */
    protected void init()
    {
        // Object a = getWebContext().getAttribute("someAttribute");
        // String b = getWebContext().getProperty("someEnvProperty");
        
    }

    /**
     * handle the request
     * you can handle the request depending on the request method.(GET, POST, PUT, DELETE, etc)
     */
    public void handle(String mime, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        // you can allow certain mimetypes, like xml and json w/c are common for webservices
        // WEB-INF/mime.properties will be parsed
        // format would be:
        // $ xml=text/xml
        //   json = text/json
        
        // /helloworld/${verbOrId}
        String verbOrId = getVerbOrId(request);        
        
        if("xml".equals(mime))
        {
            response.setContentType("text/xml");
            // generate xml response
            ServletOutputStream out = response.getOutputStream();
            out.print(XMLConverter.getInstance().toString(new HelloWorldBean(verbOrId), null));
        }
        else if("json".equals(mime))
        {
            response.setContentType("text/plain");
            // generate json response
            ServletOutputStream out = response.getOutputStream();
            out.print(JSONConverter.getInstance().toString(new HelloWorldBean(verbOrId), 
                    request.getParameter("callback")));
        }
        else
        {
            response.setContentType("text/html");            
            request.setAttribute("helloWorldBean", new HelloWorldBean(verbOrId));
            
            
            // for demo purposes, 2 templating engines are used.
            // normally, you'd only have to choose one.
            // velocity is configured via: WebContext.addViewDispatcher("velocity", new VelocityDispatcher());
            if("vm".equals(mime))
            {
                getWebContext().getViewDispatcher("velocity").dispatch("helloworld/index.vm", 
                        request, response);
            }
            else
                getWebContext().getJSPDispatcher().dispatch("helloworld/index.jsp", 
                    request, response);
        }        
        
    }
    
    // POJO to xml/json string
    public static class HelloWorldBean implements FormatConverter.Bean
    {
        
        private long _timestamp = System.currentTimeMillis();
        private String _message;
        private String _verbOrId;
        
        public HelloWorldBean(String verbOrId)
        {
            _message = "Hello World from controller! @ " + new Date(_timestamp);
            _verbOrId = verbOrId;
        }
        
        public long getTimestamp()
        {
            return _timestamp;
        }
        
        public String getMessage()
        {
            return _message;
        }
        
        public String getVerbOrId()
        {
            return _verbOrId;
        }
        
        public void convert(Builder builder, String format)
        {
            builder.put("message", getMessage());
            builder.put("verbOrId", getVerbOrId());
            builder.put("timestamp", getTimestamp());
        }
        
    }

}

```