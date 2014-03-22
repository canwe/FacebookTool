<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var xmlHttp;
	function loadMoreComments(more,masterCommentId)
	{
		try 
	    {
	        if (window.XMLHttpRequest)
	        {
	        	xmlHttp = new XMLHttpRequest();
	        }
	        else if (window.ActiveXObject)
	        {
	        	xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	        }
	        if (!xmlHttp || xmlHttp == null) 
	        {
	            return;
	        }
	        var url = "loadMoreComments?masterCommentId=" +masterCommentId+"&more="+more; // Here, I have mapped controller as "loadMoreComments".
	        xmlHttp.onreadystatechange = StateChanged;
	        xmlHttp.open("GET", url, true);
	        xmlHttp.send(null);
	    }
	    catch (e)
	    {
	        alert("Some error occurred! Please try again");
	        alert(e);
	    }
	}
	function StateChanged()
	{
	    if ((xmlHttp.readyState == 4) && (xmlHttp.status == 200)) 
	    {	       
	  	 	  alert(xmlHttp.responseText);
	    	  location.reload();
	    }
	}
	function temp(more)
	{
		alert("testing");
		alert(more);
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View</title>
</head>
	<body>
		view story
		${message }
		<br>
		<c:forEach items="${storyDetails }" var="story">
			<img src=${story.user.userPicLink } /> ${story.message }<br>
			${story.user.userName }<br>
			<c:forEach items="${story.subComment }" var="subComment">
			
				&nbsp;&nbsp;<img src="${subComment.user.userPicLink }" /> ${subComment.message }<br>
				 ${subComment.user.userName }<br>
				 
				---------------<a href="#" onClick="loadMoreComments('<c:out value='${subComment.more}' />','<c:out value='${subComment.subPostId}' />');" >More</a>------------------------	<br>
			</c:forEach>
		</c:forEach>
		-------------Previous token: ${previous } | ---------------Next Token : ${next} -------<br>
	</body>
</html>