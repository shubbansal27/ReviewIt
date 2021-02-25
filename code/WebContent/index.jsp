<%@ page import="org.bits.ontodev.controllers.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/login.css" />
<title>Java Code Reviewer: Login</title>
</head>
<body>

<div>
	<br>
	<h1 style="color:blue;font-size:48px" align="center">ReviewIT</h1>
	<form class="login" action="home" method="post">
		<input type="text" placeholder="Email" name="email">  
		<input type="password" placeholder="Password" name="pass">  
		<center><input id="submit" type="submit" value="Sign In"></center>
	</form>
	<br><br>
	<% if(Session.loginStat == 0) { %>
		<p id="loginStatus" style="color:red;" align="center">Username or Password incorrect</p>
	<%}%>
</div>

<% 
Session.loginStat = 1;
%>

</body>
</html>