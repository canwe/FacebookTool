<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>">
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
		   	    			respContent +="<table><tr><td align='left'><img src="+data[i].user.userPicLink+" /></td><td align='left'>"+data[i].message+"</td></tr>";
		   	    			respContent+="<tr><td>"+data[i].user.userName+"</td><td></tr></table>";
	
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
	<body onload="loadMoreComments('<c:out value='${more } ' />','<c:out value='${redirectCommentId }' />')">
	
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
					<td  align="center"><fb:login-button datasize="large" autologoutlink="true" width="100" height="30"> </fb:login-button></td>
				</tr>
				<tr>
					<td></td><td align="center"><input type="radio" name="searchParam" value="New">New<input type="radio" name="searchParam" value="Existing" checked> Existing</td><td></td>
				</tr>
				</table>
				</form:form>
					</center>
					
					<br><br>
					<div>
					&nbsp;&nbsp;&nbsp;&nbsp;<h3>${message }</h3>
					<br><br>
					
					<c:forEach items="${storyDetails }" var="story">
						
						<table>
							<tr>
								<td align="left"><img src=${story.user.userPicLink } /></td>
								<td align="left"> ${story.message }</td>
							</tr>
							<tr>
								<td>${story.user.userName }</td><td>
							</tr>
						</table>
							<div id=${story.commentId }>
							<c:forEach items="${story.subComment }" var="subComment" >
								<table>
									<tr>
										<td align="left"><img src=${subComment.user.userPicLink } /></td>
										<td align="left">${subComment.message }</td>
									</tr>
									<tr>
										<td>${subComment.user.userName }</td>
									</tr>
								</table>
						</c:forEach>
							<a href="#" onClick="loadMoreComments('<c:out value='${story.more}' />','<c:out value='${story.commentId}' />');" >More</a>
						</div>
						<form:form  action="addComment" modelAttribute="comment"  >
							<input type="hidden" name="commentId" value="${story.commentId }">
							<input type="hidden" name="storyId" value="${storyId }" >
							<table>
								<tr>
									<td>Reply:</td>
										<td></td><td><form:textarea path="message" rows="4" cols="60"/></td>
								</tr>
								<tr>
								
									<td></td><td><input type="submit" value="Post"></td>
								</tr>
							</table>
						</form:form>
					</c:forEach>
			<form:form  action="addComment" modelAttribute="comment"  >
				<input type="hidden" name="storyId" value="${storyId }" >
				
				<table>
					<tr>
						<td>Reply:</td>
						<td></td><td><form:textarea path="message" rows="6" cols="60"/></td>
					</tr>
					<tr>
						<td></td><td><input type="submit" value="Post">
					</tr>				
				</table>
			</form:form>
				
			
			<div align="right">
				<a href="viewStoryPagination?storyId=${storyId }&after=${next}">Next</a><br>
				<a href="viewStoryPagination?storyId=${storyId }&before=${previous }">Previous</a>
			</div>
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