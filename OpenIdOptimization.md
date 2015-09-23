# OpenID Optimization #

For custom implementation of caching (e.g using memcache), implement `UserCache`.

```

public interface Discovery
{
    
    public OpenIdUser discover(Identifier identifier, OpenIdContext context) 
    throws Exception;
    
    
    /**
     * The cached user objects used by relyingParty will be filled with data.
     * The relying party always gets from the cache with clone set to true.
     *
     */
    public interface UserCache
    {
        public OpenIdUser get(String key, boolean clone);
        public void put(String key, OpenIdUser user);
    }

}

```

That can be configured via:
`openid.properties`
```
# the default cache is com.dyuproject.openid.IdentifierSelectUserCache
openid.user.cache = com.example.MyCustomCache
```

or programmatically via:
```
RelyingParty.getInstance().setUserCache(cache);
```

If not configured, `IdentifierSelectUserCache` will be used by default.
Simply add `identifier_select.properties` to your classpath.

`identifier_select.properties`

```
providers = google,yahoo

# the identifiers must be normalized
google.openid_server = https://www.google.com/accounts/o8/ud
google.identifier.0 = https://google.com/accounts/o8/id
google.identifier.1 = https://www.google.com/accounts/o8/id

# an added mapping just in-case someone enters "google.com"
google.identifier.2 = http://www.google.com/
google.identifier.3 = http://google.com/

yahoo.openid_server = https://open.login.yahooapis.com/openid/op/auth
yahoo.identifier.0 = http://www.yahoo.com/
yahoo.identifier.1 = http://yahoo.com/

```

With this configuration, the discovery process (1-2 http roundtrips) will be skipped when a user uses google/yahoo to login to your site.


The RelyingParty will call UserCache.put(openid\_identifier, OpenIdUser) for every discovery.
The `IdentifierSelectUserCache` does not do anything on put though as it is read-only.

To combine it with your own cache, you can do:
```

public class MyCustomCache implements Discovery.UserCache
{
    
    private final StandardJSON _json = new StandardJSON();
    private final Discovery.UserCache _wrapped;
    
    public MyCustomCache(Discovery.UserCache cache)
    {
        _wrapped = cache;
    }

    public OpenIdUser get(String key, boolean clone)
    {
        OpenIdUser user = _wrapped.get(key, clone);
        if(user==null)
        {            
            Reader reader = null; // fetched from memcache or some other datasource            
            
            if(reader!=null)
                user = (OpenIdUser)_json.parse(new JSON.ReaderSource(reader));
        }
        return user;
    }

    public void put(String key, OpenIdUser user)
    {
        String serialized = _json.toJSON(user);
        // put in memcache or smoe other datasource
    }

}

```

Then configure the RelyingParty via:
```
RelyingParty rp = new RelyingParty(new HttpSessionUserManager(), new MyCustomCache(new IdentifierSelectUserCache()));
```