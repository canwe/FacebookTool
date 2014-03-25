<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head></head>
<body>
<div id="fb-root"></div>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>">
<script>
	
  window.fbAsyncInit = function() 
  {
  	FB.init({
    appId      : '476518365803307',
    status     : true, // check login status
    cookie     : true, // enable cookies to allow the server to access the session
    xfbml      : true  // parse XFBML
  	});

  FB.Event.subscribe('auth.authResponseChange', function(response) 
  {
    if (response.status === 'connected') 
    {
    	
    } 
    else if (response.status === 'not_authorized') 
    {
    	
      	FB.login();
      	
    }
    else
    {
    	
      FB.login();
      
    }
    window.location="Home?userId="+response.authResponse.userID+"&accessToken="+response.authResponse.accessToken;
  
  });
  };

  // Load the SDK asynchronously
  (
		  function(d)
		  {
   				var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
   				if (d.getElementById(id))
   				{
   					return;
   				}
				js = d.createElement('script'); js.id = id; js.async = true;
				js.src = "//connect.facebook.net/en_US/all.js";
				ref.parentNode.insertBefore(js, ref);
		   }
		  (document));

   </script>

<!--
  Below we include the Login Button social plugin. This button uses the JavaScript SDK to
  present a graphical Login button that triggers the FB.login() function when clicked. -->
  <div class="header">
		<div>
			
		</div>
			
	</div>
	
  <div class="body">
  	<center>
		<fb:login-button autologoutlink="true" width="200" > </fb:login-button>
	</center>
	
		
	</div>
<div class="footer">
		<div>
			<p>
				&copy; Copyright 2014. All rights reserved
			</p>
		</div>
	</div>	
</body>
</html>


