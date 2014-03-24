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
</script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
</head>
<c:if test="${sessionUserId==null }">
	<jsp:forward page="/index" />
</c:if>
<body onLoad="messageAlert();">
<form:form action="searchBrand" modelAttribute="brand">
	<table><caption>
	Brand</caption>
	<tr><td><form:input path="brandName"/></td><td><input type="submit" value="Search"></td></tr>
	<tr><td><input type="radio" name="searchParam" value="New">New<input type="radio" name="searchParam" value="Existing" checked> Existing</td></tr>
	</table>

</form:form>
<c:forEach items="${brandsList }" var="brand">
<img src=${brand.pictureLink } >
	${brand.brandName}&nbsp;|&nbsp;Like: ${brand.likes } <a href="getLatestFeed?brandId=${brand.brandId }">Open</a><br>
</c:forEach>

<c:forEach items="${searchBrandList }" var="brand">
<img src=${brand.pictureLink } >
	${brand.brandName}&nbsp;|&nbsp;Like: ${brand.likes }
	<form action="addBrand">
		<input type="hidden" name="brandName" value="${brand.brandName }"/>
		<input type="hidden" name="brandId" value="${brand.brandId }"/>
		<input type="hidden" name="likes" value="${brand.likes }"/>
		<input type="hidden" name="about" value="${brand.about }"/>
		<input type="hidden" name="pictureLink" value="${brand.pictureLink }"/>
		<input type="submit" value="Add">
	</form>
	<br>
</c:forEach>

<c:if test="${message!=null }">
	<input type="hidden" id="message" value="${message }">
</c:if>

</body>
</html>