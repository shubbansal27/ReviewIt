<%@ page import="org.bits.ontodev.controllers.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>

	<ul>
		  <li><a href="Dashboard.jsp?tid=-3">Edit Project members</a></li><br>
		  <li><a href="Dashboard.jsp?tid=<%=Session.pid%>">Project Data</a></li><br>
		  <li><a href="Results.jsp" target="_blank">Results</a></li><br>
		  <li><a href="Dashboard.jsp?tid=-2">Comments</a></li><br>
	</ul>


</body>
</html>