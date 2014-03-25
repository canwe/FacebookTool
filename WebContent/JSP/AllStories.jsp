<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
         <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Stories</title>
</head>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>">
<script type="text/javascript">
 function messageAlert()
 {
	 var message=document.getElementById("message").value;
	 console.log(message);
	 if(message!=null)
		 alert(message);
	 
 }
 
 function confirmation(form)
 {
	 var r = confirm("Are you sure want to delete!");
	 if (r == true)
	 {
	   	return true;
	 }
	 else
	 {
	   return false;
	 }
 }
</script>
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
   		window.location="index";
    }
   
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
<script type="text/javascript">
 function messageAlert()
 {
	 var message=document.getElementById("message").value;
	 console.log(message);
	 if(message!=null)
		 alert(message);
	 
 }
 
 function confirmation(form)
 {
	 var r = confirm("Are you sure want to delete!");
	 if (r == true)
	 {
	   	return true;
	 }
	 else
	 {
	   return false;
	 }
 }
</script>
<c:if test="${sessionUserId==null }">
	<jsp:forward page="/index" />
</c:if>

<body>

<div class="header">
	
	</div>
	<div class="body">
	
		<div>
			<div id="content">
			
				<div>
				<center>
				<form:form action="searchBrand" modelAttribute="brand">
				<table width="708">
				<tr>
					<td  align="center"><h3><a href="index"><img src="<c:url value="/resources/images/fb_home.jpg" />" alt="home" width="80" height="25"></a></h3></td>
					<td align="center"><form:input path="brandName" id="search"/>
					<input type="submit" value="Search" id="searchbtn"></td>
					<td  align="center"><fb:login-button autologoutlink="true" width="100" height="30"> </fb:login-button></td>
				</tr>
				<tr>
					<td></td><td align="center"><input type="radio" name="searchParam" value="New">New<input type="radio" name="searchParam" value="Existing" checked> Existing</td><td></td>
				</tr>
				</table>
				</form:form>
					
				</center>
				<ul>
				<c:forEach items="${brandStoryList }" var="brand">
						<li>
					<table  width="680">
							<tr>
								<td align="left"><h5>${brand.message }</h5>
								<p>
								<a href="viewStory?storyId=${brand.storyId }" >View</a>&nbsp;&nbsp;|&nbsp;&nbsp;Comment:${brand.commentCount }&nbsp;|&nbsp;Like:${brand.likeCount }&nbsp;|&nbsp;
								</p>
							</td>
								<td align="right">
									<c:if test="${brand.picLink !=null}">
										<img alt="samsung mobile" src="${brand.picLink }" height="155" width="200">
									</c:if>
									<c:if test="${brand.videoLink !=null}">
										<iframe height="155" width="200"  src="${brand.videoLink}" frameborder="0"  ></iframe>
									</c:if>
									</td>
									</tr>
						</table>
						</li></c:forEach>
				</ul>
				<div align="right">
					<a href="loadMoreStories?brandId=${brandId }&nextLinkIndex=${nextLinkIndex }">Next</a><br>
					<a href="loadMoreStories?brandId=${brandId }&previousLinkIndex=${previousLinkIndex }">Previous</a>
				</div>
				</div>
				</div>
				</div>
				<div>
					<div>
						<h3>Get Updates</h3>
						<a href="https://www.facebook.com/pulkit.sharva" target="_blank" id="facebook">Facebook</a>
						<a href="http://www.youtube.com/user/pulkitb4Mv" target="_blank" id="youtube">Youtube</a>
						<a href="https://plus.google.com/117192008917331015868/posts" target="_blank" id="googleplus">Google&#43;</a>
						<a href="http://stackoverflow.com/users/2219920/pulkit" target="_blank" id="stackoverflow">Stack Overflow</a>
						<a href="https://github.com/pulkitsharva" target="_blank" id="github">Github</a>
					</div>
				</div>
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