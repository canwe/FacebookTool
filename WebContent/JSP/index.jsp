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

  	FB.getLoginStatus(function(response) 
	{
    	if (response.status === 'connected') 
      	{
          // connected
          window.location="Home?userId="+response.authResponse.userID+"&accessToken="+response.authResponse.accessToken;
              // what to do with the response
		// testAPI();
      	}
    	else if (response.status === 'not_authorized') 
    	{
    	//	alert("not_authorized")
          
      	}
    	else
    	{
    	//	alert("Not logged")
        
      }
  });

};

  // Load the SDK asynchronously
  ( function(d)
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
   <script type="text/javascript">
        function loginFacebook() 
        {
            FB.login(function(response) 
            {
                if (response.authResponse) 
                {
                	 window.location="Home?userId="+response.authResponse.userID+"&accessToken="+response.authResponse.accessToken;
                	//alert("connected");
                    // connected
                   
                } 
                else 
                {
                    // cancelled
                }
            },
            {scope: 'user_about_me,user_birthday,user_interests,user_status,publish_stream'});
        }
   </script>

  <div class="header">
		<div>
			
		</div>
			
	</div>
	
  <div class="body">
  	<center>
		<a id="fb_login_link" href="" onclick="loginFacebook(); return false;" ><img src="<c:url value="/resources/images/facebook-connect-button.png" />"></a>
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


