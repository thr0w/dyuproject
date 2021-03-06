<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>Welcome</title>
</head>
<body>
<div>
  <p><span style="color:green;font-size:1.4em">Welcome</span></p><br/>
  <div>
    <p>    
      html | <a href="<c:url value="/helloworld"/>">helloworld</a>
    </p>
    <p>    
      html | <a href="<c:url value="/helloworld.vm"/>">helloworld from velocity</a>
    </p>    
    <p>    
      xml | <a href="<c:url value="/helloworld.xml"/>">helloworld.xml</a>
    </p>
    <p>    
      json | <a href="<c:url value="/helloworld.json"/>">helloworld.json</a>
    </p>
  </div>
  <hr/>
  <div>
    <p style="font-weight:bold">/helloworld/{id}</p>
    <p>    
      html | <a href="<c:url value="/helloworld/1"/>">helloworld/1</a>
    </p>
    <p>    
      html | <a href="<c:url value="/helloworld/2.vm"/>">helloworld/2 from velocity</a>
    </p>    
    <p>    
      xml | <a href="<c:url value="/helloworld/3.xml"/>">helloworld/3.xml</a>
    </p>
    <p>    
      json | <a href="<c:url value="/helloworld/4.json"/>">helloworld/4.json</a>
    </p>  
  </div>
  <hr/>
  <div>
    <p style="font-weight:bold">/helloworld/{verb}</p>
    <p>    
      html | <a href="<c:url value="/helloworld/create"/>">helloworld/create</a>
    </p>
    <p>    
      html | <a href="<c:url value="/helloworld/edit"/>">helloworld/edit</a>
    </p>
    <p>    
      html | <a href="<c:url value="/helloworld/delete"/>">helloworld/delete</a>
    </p>
  </div>
  <hr/>
  <div>
    <p style="font-weight:bold">page with authentication</p>
    <p><a href="<c:url value="/protected"/>">protected</a></p>
    <p><b>credentials:</b> hello/world, foo/bar</p>
  </div>  
</div>
</body>