<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript"
    src="http://code.jquery.com/jquery-1.10.1.min.js"></script>

<script type="text/javascript">
    function loadMoreComments(more,masterPostId)
    {
    	try
    	{
    		alert("sending to requets");
    	$.ajax
   	 ({
   			url: 'loadMoreComments?more='+more+'&masterPostId='+masterPostId,
   	    	type: "GET",
			   	success: function(data)
   	    	{
   	    		alert("got user from controller");
   	    		for ( var i = 0, len = data.length; i < len; ++i) 
   	    		{
   	    	            alert(data[i].message);
   	    	    }
   	    	
   	    		//var respContent = "";

   			//respContent += "<span class="success">Smartphone was created: [";
   			//respContent += smartphone.producer + " : ";
   			//respContent += smartphone.model + " : " ;
   			//respContent += smartphone.price + "]</span>";

   	    		//$("#sPhoneFromResponse").html(respContent);   		
   	    	}
   	    });
    	}
    	catch(e)
    	{
    		alert(e);
    	}

    }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View</title>
</head>
	<body>
		<a href="#" onClick="ajaxTest()">Click</a>
		${message }
		
		<br>
		<c:forEach items="${storyDetails }" var="story">
			<img src=${story.user.userPicLink } /> ${story.message }<br>
			${story.user.userName }<br>
			<c:forEach items="${story.subComment }" var="subComment">
			
				&nbsp;&nbsp;<img src="${subComment.user.userPicLink }" /> ${subComment.message }<br>
				 ${subComment.user.userName }<br>
				 
				<a href="#" onClick="loadMoreComments('<c:out value='${subComment.more}' />','<c:out value='${subComment.subPostId}' />');" >More</a>	<br>
				
			</c:forEach>
			======================================================================================================================
		</c:forEach>
		-------------Previous token: ${previous } | ---------------Next Token : ${next} -------<br>
	</body>
</html>