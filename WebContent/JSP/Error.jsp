<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>Food &amp; Recipes</title>
		<meta name="keywords" content="404" />
		<link rel="stylesheet" href="<c:url value="/resources/css/mystyle.css"/>" type="text/css"  media="all" />
	</head>
	<body>
		<!--start-wrap--->
		<div class="wrap">
			<!---start-header---->
				<div class="header">
					<div class="logo">
					
						<h1><a href="index">Ohh</a></h1>
					</div>
				</div>
			<!---End-header---->
			<!--start-content------>
			<div class="content">
				<img src="<c:url value="/resources/images/error-img.png" />" title="error" />
				<p><span><label>O</label>hh.....</span>You Requested the page that is no longer There.</p>
				<a href="index">Back To Home</a>
				<div class="copy-right">
					<p>&copy; Copyright 2014. All rights reserved </a></p>
				</div>
   			</div>
			<!--End-Cotent------>
		</div>
		<!--End-wrap--->
	</body>
</html>

