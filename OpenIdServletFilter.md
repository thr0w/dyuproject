A servlet filter that fits your needs out-of-the-box.

```
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
    <url-pattern>/account/*</url-pattern>
  </filter-mapping> 
```

To customize the **`RelyingParty`** used by the servlet filter, see [openid.properties configuration](openid.md).

Or you could implement a **`javax.servlet.ServletContextListener`** and customize programmatically:

```
public class ContextListener implements ServletContextListener
{

    public void contextDestroyed(ServletContextEvent event)
    {        
        
    }

    public void contextInitialized(ServletContextEvent event)
    {
        Properties props = new Properties();
        // customize
        // ...
        event.getServletContext().setAttribute(RelyingParty.class.getName(), 
                    RelyingParty.newInstance(props));      
    }

}
```