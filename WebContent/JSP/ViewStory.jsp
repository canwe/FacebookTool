<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript"   src="http://code.jquery.com/jquery-1.10.1.min.js" ></script>

<script type="text/javascript">
    function loadMoreComments(more,masterPostId)
    {
    	try
    	{
    		if((more!="") && (masterPostId!=""))
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
		   	    			respContent +="&nbsp;&nbsp;&nbsp;&nbsp;<img src="+data[i].user.userPicLink+" />"+data[i].message+"<br>"+data[i].user.userName+"<br>";
		   				}
	   	    			$("#"+masterPostId).html(respContent);
	   	    		}
	   	    	});
    		}
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
these are the values after redirect: ${ more} , ${redirectCommentId }
	<body onload="loadMoreComments('<c:out value='${more } ' />','<c:out value='${redirectCommentId }' />')">
		${message }
		
		<br>
		<c:forEach items="${storyDetails }" var="story">
			<img src=${story.user.userPicLink } /> ${story.message }<br>
			${story.user.userName }<br>
			<c:forEach items="${story.subComment }" var="subComment">
			
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="${subComment.user.userPicLink }" /> ${subComment.message }<br>
				 ${subComment.user.userName }<br>
				
				
			</c:forEach>
			 
			 <div id=${story.commentId }>
				<a href="#" onClick="loadMoreComments('<c:out value='${story.more}' />','<c:out value='${story.commentId}' />');" >More</a>	<br>
			</div>
			<form:form  action="addComment" modelAttribute="comment"  >
				<input type="hidden" name="commentId" value="${story.commentId }">
				<input type="hidden" name="storyId" value="${storyId }" >
				<table>
					<tr>
						<td>Reply:</td><td><form:textarea path="message" rows="2" cols="25"/></td><td><input type="submit" value="Post">
					</tr>				
				</table>
			</form:form>
			======================================================================================================================<br>
		</c:forEach>
		<form:form  action="addComment" modelAttribute="comment"  >
				<input type="hidden" name="storyId" value="${storyId }" >
				<table>
					<tr>
						<td>Reply:</td><td><form:textarea path="message" rows="2" cols="25"/></td><td><input type="submit" value="Post">
					</tr>				
				</table>
			</form:form>
			
	<a href="viewStoryPagination?storyId=${storyId }&after=${next}">Next</a><br>
	<a href="viewStoryPagination?storyId=${storyId }&before=${previous }">Previous</a>
	</body>
</html>