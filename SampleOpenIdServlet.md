A sample openid servlet using dyuproject-openid

It is recommended that you use the [OpenIdServletFilter](OpenIdServletFilter.md) instead and just add
your custom **`RelyingParty.Listener`**

**See the [Quick Start Guide](QuickStartOpenid.md)**

**`HomeServlet.java`**
```
/**
 * Home Servlet. If authenticated, goes to the home page. If not, goes to the login page.
 * 
 */
@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet
{
    static
    {
        RelyingParty.getInstance()
        .addListener(new SRegExtension()
            .addExchange("email")
            .addExchange("country")
            .addExchange("language")
        )
        .addListener(new AxSchemaExtension()
            .addExchange("email")
            .addExchange("country")
            .addExchange("language")
        )
        .addListener(new RelyingParty.Listener()
        {
            public void onDiscovery(OpenIdUser user, HttpServletRequest request)
            {
                System.err.println("discovered user: " + user.getClaimedId());
            }            
            public void onPreAuthenticate(OpenIdUser user, HttpServletRequest request,
                    UrlEncodedParameterMap params)
            {
                System.err.println("pre-authenticate user: " + user.getClaimedId());
            }            
            public void onAuthenticate(OpenIdUser user, HttpServletRequest request)
            {
                System.err.println("newly authenticated user: " + user.getIdentity());
                Map<String,String> sreg = SRegExtension.remove(user);
                Map<String,String> axschema = AxSchemaExtension.remove(user);
                if(sreg!=null && !sreg.isEmpty())
                {
                    System.err.println("sreg: " + sreg);
                    user.setAttribute("info", sreg);
                }
                else if(axschema!=null && !axschema.isEmpty())
                {                    
                    System.err.println("axschema: " + axschema);
                    user.setAttribute("info", axschema);
                }          
            }            
            public void onAccess(OpenIdUser user, HttpServletRequest request)
            {        
                System.err.println("user access: " + user.getIdentity());
                System.err.println("info: " + user.getAttribute("info"));
            }   
        });
    }

    RelyingParty _relyingParty = RelyingParty.getInstance();
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        String loginWith = request.getParameter("loginWith");
        if(loginWith!=null)
        {
            // If the ui supplies a LoginWithGoogle or LoginWithYahoo link/button, 
            // this will speed up the openid process by skipping discovery. 
            // The override is done by adding the OpenIdUser to the request attribute.
            if(loginWith.equals("google"))
            {
                OpenIdUser user = OpenIdUser.populate("https://www.google.com/accounts/o8/id", 
                        YadisDiscovery.IDENTIFIER_SELECT, 
                        "https://www.google.com/accounts/o8/ud");
                request.setAttribute(OpenIdUser.ATTR_NAME, user);
                
            }
            else if(loginWith.equals("yahoo"))
            {
                OpenIdUser user = OpenIdUser.populate("http://yahoo.com/", 
                        YadisDiscovery.IDENTIFIER_SELECT, 
                        "https://open.login.yahooapis.com/openid/op/auth");
                request.setAttribute(OpenIdUser.ATTR_NAME, user);
            }
        }
        
        String errorMsg = OpenIdServletFilter.DEFAULT_ERROR_MSG;
        try
        {
            OpenIdUser user = _relyingParty.discover(request);
            if(user==null)
            {                
                if(RelyingParty.isAuthResponse(request))
                {
                    // authentication timeout                    
                    response.sendRedirect(request.getRequestURI());
                }
                else
                {
                    // set error msg if the openid_identifier is not resolved.
                    if(request.getParameter(_relyingParty.getIdentifierParameter())!=null)
                        request.setAttribute(OpenIdServletFilter.ERROR_MSG_ATTR, errorMsg);
                    
                    // new user
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
                return;
            }
            
            if(user.isAuthenticated())
            {
                // user already authenticated
                request.getRequestDispatcher("/home.jsp").forward(request, response);
                return;
            }
            
            if(user.isAssociated() && RelyingParty.isAuthResponse(request))
            {
                // verify authentication
                if(_relyingParty.verifyAuth(user, request, response))
                {
                    // authenticated                    
                    // redirect to home to remove the query params instead of doing:
                    // request.setAttribute("user", user); request.getRequestDispatcher("/home.jsp").forward(request, response);
                    response.sendRedirect(request.getContextPath() + "/home/");
                }
                else
                {
                    // failed verification
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
                return;
            }
            
            // associate and authenticate user
            StringBuffer url = request.getRequestURL();
            String trustRoot = url.substring(0, url.indexOf("/", 9));
            String realm = url.substring(0, url.lastIndexOf("/"));
            String returnTo = url.toString();            
            if(_relyingParty.associateAndAuthenticate(user, request, response, trustRoot, realm, 
                    returnTo))
            {
                // successful association
                return;
            }          
        }
        catch(UnknownHostException uhe)
        {
            System.err.println("not found");
            errorMsg = OpenIdServletFilter.ID_NOT_FOUND_MSG;
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("could not be resolved");
            errorMsg = OpenIdServletFilter.DEFAULT_ERROR_MSG;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            errorMsg = OpenIdServletFilter.DEFAULT_ERROR_MSG;
        }
        request.setAttribute(OpenIdServletFilter.ERROR_MSG_ATTR, errorMsg);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

}
```