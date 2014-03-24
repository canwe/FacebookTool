<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head></head>
<body>
<div id="fb-root"></div>
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
  (function(d){
   var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
   if (d.getElementById(id)) {return;}
   js = d.createElement('script'); js.id = id; js.async = true;
   js.src = "//connect.facebook.net/en_US/all.js";
   ref.parentNode.insertBefore(js, ref);
  }(document));

   </script>

<!--
  Below we include the Login Button social plugin. This button uses the JavaScript SDK to
  present a graphical Login button that triggers the FB.login() function when clicked. -->

<fb:login-button autologoutlink="true" show-faces="true" width="200" max-rows="1" > </fb:login-button>

</body>
</html>


