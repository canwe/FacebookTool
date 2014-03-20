<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>All Stories</title>
</head>
<body>
	<table>
		<c:forEach items="${brandStoryList }" var="brand">
			<tr><td>${brand.message }</td></tr>
			<c:if test="${brand.picLink !=null}">
				<tr><td><img alt="samsung mobile" src="${brand.picLink }" height="200" width="300"></td></tr>
			</c:if>
			<c:if test="${brand.videoLink !=null}">
				<tr><td><iframe width="300" height="200" src="${brand.videoLink}" frameborder="0"  ></iframe></td></tr>
			</c:if>
			<tr><td>Like:${brand.likeCount }&nbsp;|&nbsp;Comment:${brand.commentCount }</td></tr>
			
		</c:forEach>
	</table>
	<a href="loadMoreStories?brandId=${brandId }&nextLinkIndex=${nextLinkIndex }">Next</a><br>
	<a href="loadMoreStories?brandId=${brandId }&previousLinkIndex=${previousLinkIndex }">Previous</a>
	Next: ${nextLinkIndex }</br>
Previous: ${previousLinkIndex }</br>
</body>
</html>