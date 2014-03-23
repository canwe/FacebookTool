<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript"   src="http://code.jquery.com/jquery-1.10.1.min.js" ></script>

<script type="text/javascript">
    function loadMoreComments(more,masterPostId)
    {
    	try
    	{
    	$.ajax
   	 ({
   			url: 'loadMoreComments?more='+more+'&masterPostId='+masterPostId,
   	    	type: "GET",
			   	success: function(data)
   	    	{
   	    		alert("loading data");
   	    		var respContent="";
   	    		$("."+masterPostId).empty();
   	    		for ( var i = 0, len = data.length; i < len; ++i) 
   	    		{
   	    			respContent +="&nbsp;&nbsp;<img src="+data[i].user.userPicLink+" />"+data[i].message+"<br>"+data[i].user.userName+"<br>";
   					 
   	    	    }
   	    		$("#"+masterPostId).html(respContent);
   	    		

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
		${message }
		
		<br>
		<c:forEach items="${storyDetails }" var="story">
			<img src=${story.user.userPicLink } /> ${story.message }<br>
			${story.user.userName }<br>
			<c:forEach items="${story.subComment }" var="subComment">
			
				&nbsp;&nbsp;<img src="${subComment.user.userPicLink }" /> ${subComment.message }<br>
				 ${subComment.user.userName }<br>
				
				
			</c:forEach>
			 
			 <div id=${story.commentId }>
				<a href="#" onClick="loadMoreComments('<c:out value='${story.more}' />','<c:out value='${story.commentId}' />');" >More</a>	<br>
			</div>
			======================================================================================================================<br>
		</c:forEach>
	<a href="viewStoryPagination?storyId=${storyId }&next=${next}">Next</a><br>
	<a href="viewStoryPagination?storyId=${storyId }&previous=${previous }">Previous</a>
	</body>
</html>