`HelloWorldService.java`
```
package com.dyuproject.demos.helloworld;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.codehaus.jra.Get;
import org.codehaus.jra.HttpResource;
import org.codehaus.jra.Post;

import com.dyuproject.web.rest.RequestContext;
import com.dyuproject.web.rest.WebContext;
import com.dyuproject.web.rest.service.AbstractService;


public class HelloWorldService extends AbstractService
{

    protected void init()
    {        
        
    }

    /* --------- VIEWS (JSP) --------- */
    
    @HttpResource(location="/")
    @Get
    public void root() throws IOException, ServletException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getResponse().setContentType("text/html");
        getWebContext().getJSPDispatcher().dispatch("index.jsp", rc.getRequest(), rc.getResponse());
    }
    
    @HttpResource(location="/helloworld")
    @Get
    public void helloworld() throws IOException, ServletException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getRequest().setAttribute("message", "Hello world!");
        rc.getRequest().setAttribute("timestamp", new Date());
        rc.getResponse().setContentType("text/html");
        getWebContext().getJSPDispatcher().dispatch("helloworld/index.jsp", rc.getRequest(), rc.getResponse());
    }
    
    @HttpResource(location="/helloworld")
    @Post
    public void helloworldPost() throws IOException, ServletException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getRequest().setAttribute("message", "This is a POST request");
        rc.getRequest().setAttribute("timestamp", new Date());
        rc.getResponse().setContentType("text/html");
        getWebContext().getJSPDispatcher().dispatch("helloworld/index.jsp", rc.getRequest(), rc.getResponse());
    }
    
    @HttpResource(location="/helloworld/$")
    @Get
    public void helloworldEntity() throws IOException, ServletException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getRequest().setAttribute("message", "get entity: " + rc.getPathElement(1));
        rc.getRequest().setAttribute("timestamp", new Date());
        rc.getResponse().setContentType("text/html");
        getWebContext().getJSPDispatcher().dispatch("helloworld/index.jsp", rc.getRequest(), rc.getResponse());
    }

    /* --------- PLAIN TEXT RESOURCES --------- */
    
    @HttpResource(location="/hello")
    @Get
    public void hello() throws IOException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getResponse().setContentType("text/plain");
        rc.getResponse().getOutputStream().print("hello!");
    }
    
    @HttpResource(location="/hello/$")
    @Get
    public void helloStranger() throws IOException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getResponse().setContentType("text/plain");
        rc.getResponse().getOutputStream().print("hello " + rc.getPathElement(1) + "!");
    }
    
    @HttpResource(location="/hello/$/world")
    @Get
    public void helloStrangerWorld() throws IOException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getResponse().setContentType("text/plain");
        rc.getResponse().getOutputStream().print("hello " + rc.getPathElement(1) + " world!");
    }
    
    @HttpResource(location="/hello/sexy/$")
    @Get
    public void helloSexyStranger() throws IOException
    {
        RequestContext rc = WebContext.getCurrentRequestContext();
        rc.getResponse().setContentType("text/plain");
        rc.getResponse().getOutputStream().print("hello sexy " + rc.getPathElement(2) + "!");
    }

}

```