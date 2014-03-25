<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Home</title>
</head>
<c:if test="${sessionUserId==null }">
	<jsp:forward page="/index" />
</c:if>
<body onLoad="messageAlert();">
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
						<c:forEach items="${brandsList }" var="brand">
							<li>
								<img src=${brand.pictureLink } alt="image" height="168" width="168">
									<div>
										<h3>${brand.brandName}</h3>
										
										<a href="getLatestFeed?brandId=${brand.brandId }"><input type="button" value="Open"></a>
										<form action="deleteBrand" onsubmit="return confirmation(this);">
											<input type="hidden" name="brandId" value="${brand.brandId }"/><input type="submit" value="delete">
										</form>
										<p>
										${brand.about }... <a href="#"></a>
										</p>
									</div>
								</li>
						</c:forEach>
						<c:forEach items="${searchBrandList }" var="brand">
							<li>
								<img src=${brand.pictureLink } alt="image" height="168" width="168">
									<div>
										<h3>${brand.brandName}</h3>
										<a href="getLatestFeed?brandId=${brand.brandId }"><input type="button" value="Open"></a>
										<div id=${brand.brandId }>
											<form action="addBrand">
											<input type="hidden" name="brandName" value="${brand.brandName }"/>
											<input type="hidden" name="brandId" value="${brand.brandId }"/>
											<input type="hidden" name="likes" value="${brand.likes }"/>
											<input type="hidden" name="about" value="${brand.about }"/>
											<input type="hidden" name="pictureLink" value="${brand.pictureLink }"/>
											<input type="submit" value="Add">
											</form>
										</div>
										<p>
										${brand.about }... <a href="#"></a>
										</p>
									</div>
								</li>
						</c:forEach>
					</ul>
					</div>
				</div>
			</div>
			<div>
				<div>
				</div>
				<div>
				</div>
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

<c:if test="${message!=null }">
	<input type="hidden" id="message" value="${message }">
</c:if>
</body>
</html>