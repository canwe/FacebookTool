<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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

<fb:login-button autologoutlink="true" show-faces="true" width="200" max-rows="1" > </fb:login-button>
<form:form action="searchBrand" modelAttribute="brand">
	<table><caption>
	Brand</caption>
	<tr><td><form:input path="brandName"/></td><td><input type="submit" value="Search"></td></tr>
	<tr><td><input type="radio" name="searchParam" value="New">New<input type="radio" name="searchParam" value="Existing" checked> Existing</td></tr>
	</table>

</form:form>
<c:forEach items="${brandsList }" var="brand">
<table>
	<tr><td><img src=${brand.pictureLink } ></td>
	<td>${brand.brandName}&nbsp;&nbsp;</td><td>|&nbsp;&nbsp;Like: ${brand.likes }</td>
	<td><a href="getLatestFeed?brandId=${brand.brandId }"><input type="button" value="Open"></a></td>
	<td><form action="deleteBrand" onsubmit="return confirmation(this);">
		<input type="hidden" name="brandId" value="${brand.brandId }"/><input type="submit" value="delete">
	</form></td>
	</tr></table>
</c:forEach>
<br>
<c:forEach items="${searchBrandList }" var="brand">
<table>
	<tr><td><img src=${brand.pictureLink } ></td><td>${brand.brandName}&nbsp;&nbsp;</td><td>|&nbsp;&nbsp;Like: ${brand.likes }</td>
	<td><form action="addBrand">
		<input type="hidden" name="brandName" value="${brand.brandName }"/>
		<input type="hidden" name="brandId" value="${brand.brandId }"/>
		<input type="hidden" name="likes" value="${brand.likes }"/>
		<input type="hidden" name="about" value="${brand.about }"/>
		<input type="hidden" name="pictureLink" value="${brand.pictureLink }"/>
		<input type="submit" value="Add">
	</form></td>
	</tr>
</table>

	<br>
</c:forEach>

<c:if test="${message!=null }">
	<input type="hidden" id="message" value="${message }">
</c:if>
</body>
</html>