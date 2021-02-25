<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/dashboard.css" />
<title>ReviewIt</title>
</head>
<body>

<%
String email = Session.email;
String userType = Session.userType.toUpperCase();
%>
<% 
	if(request.getParameter("pid") != null) {
		Session.pname = request.getParameter("pname");
		Session.pid = Integer.parseInt(request.getParameter("pid"));
		if(Session.pid == -1)
			Session.subOptions = 0;
		else {
			Session.subOptions = 1;
		}
	}
	if(request.getParameter("tid") != null) {
		Session.tid = Integer.parseInt(request.getParameter("tid"));
	}
%>



<div class="divDashboardHeader" style="background-color:lightgray;">
	<div id="UserType"><h2>Dashboard: <%=userType%></h2></div>
	<div id="Email">
		<h4>Email: <%=email%> </h4>
		<a href="logout.jsp">SignOut</a>
	</div>
</div>

<br>
<br>

<div class="divDashboardBody">
	
	<div style="background-color:lightgray; float:left;height:100%;width:20%">
		<h3 align="left">&nbsp;&nbsp;Options</h3>
		<hr>
		<% if(Session.userType.equals("manager")) {%>
			<%@ include file="ManagerOptions.html" %>
		<%}%>
		<% if(Session.userType.equals("designer")) {%>
			<%@ include file="DesignerOptions.html" %>
		<%}%>
		<% if(Session.userType.equals("developer")) {%>
			<%@ include file="DeveloperOptions.html" %>
		<%}%>
		<% if(Session.userType.equals("reviewer")) {%>
			<%@ include file="ReviewerOptions.html" %>
		<%}%>	
		<% if(Session.userType.equals("admin")) {%>
			<%@ include file="AdminOptions.html" %>
		<%}%>	
	</div>

	<% if(Session.subOptions == 1) {%>
	<div style="background-color:#ADD8E6; float:right;height:100%;width:15%">
		<h2 align="center"></h2>
		<hr>
			<% if(Session.userType.equals("manager")) {%>
				<%@ include file="ManagerSubOptions.jsp" %>
			<%}%>
			<% if(Session.userType.equals("designer") || Session.userType.equals("developer") || Session.userType.equals("reviewer")) {%>
				<%@ include file="ProjectSubOptions.jsp" %>
			<%}%>
	</div>
	<%}%>
	
	<div style="width:100%; height:100%">
		<% 
		if(Session.tid >= 1) {%>
		 	<% 
			if(userType.equals("DESIGNER")) { %>
				<%@ include file="Designer.jsp" %>	
			<%
					}
				%>
			<%
				if(userType.equals("DEVELOPER")) {
			%>
				<%@ include file="Developer.jsp" %>	
			<%
					}
				%>
			<%
				if(userType.equals("MANAGER")) {
			%>
				<%@ include file="Manager.jsp" %>	
			<%
					}
				%>
			<%
				if(userType.equals("REVIEWER")) {
			%>
				<%@ include file="Reviewer.jsp" %>	
			<%
					}
				%>
			<%
				if(userType.equals("ADMIN")) {
			%>
				<%@ include file="Admin.jsp" %>	
			<%
					}
				%>
		<%
			} else if(Session.tid == -1) {
		%>
			<%
				if(userType.equals("ADMIN")) {
			%>
				<%
					if(request.getParameter("id") == null){
				%>
					<%@ include file="Admin.jsp" %>
				<%
					} else {
				%>
					<%@ include file="AdminEditEntry.jsp" %>
					<%
						}
					%>		
			<%
						} else {
					%>
				<%@ include file="ProjectList.jsp" %>	
			<%
					}
				%>
		<%
			}else if(Session.tid == -2) {
		%>
			<%@ include file="Comments.jsp" %>	
	
	   <%
			   	} else if(Session.tid == -3) {
			   %>
			<%@ include file="ManagerAddEntry.jsp" %>	
		<%}%>
		
	</div>	
</div>



</body>
</html>