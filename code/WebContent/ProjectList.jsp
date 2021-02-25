<%@ page import="org.bits.ontodev.controllers.*" %>
<%@ page import="java.util.*" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript">
function openLink(pid) 
{
	 var s1= "file://";
	 var s2 = s1.concat(pid);
	 window.open("file:///F:/Workspace_Eclipse/JCR/WebContent/WEB-INF/Data/1/test.java");
}
</script>

</head>
<body>


	<div align="center">
		<table>
			<tr><td><h2>Project List:</h2></td></tr>
		</table>
		<hr>
	<div><br>
	<table>
	<%
		Vector<Vector<String>> tlist = ConnectionManager.getProjectList(Session.email);
			for(int i=0;i<tlist.size();i++) {
				Vector<String> list = tlist.get(i);
	%> 
			<tr>
			<td>
			<h2><a style="text-decoration: none" href="Dashboard.jsp?tid=<%=list.get(0)%>&pid=<%=list.get(0)%>&pname=<%=list.get(1)%>"><%=list.get(1)%></a></h2>
			</td></tr>
		<%}%>	
	</table>
	</div>
</div>

</body>
</html>