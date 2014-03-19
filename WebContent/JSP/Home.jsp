<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
</head>
<c:if test="${sessionUserId==null }">
	<jsp:forward page="/index" />
</c:if>
<body>
<c:forEach items="${brandsList }" var="brand">
	${brand.brandName} <a href="getLatestFeed?brandId=${brand.brandId }">Open</a><br>
</c:forEach>
</body>
</html>