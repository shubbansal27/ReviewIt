<%@ page import="org.bits.ontodev.controllers.*" %>
<%@ page import="java.util.*" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>


<%
   String pid = new String(""+Session.pid);
   String projName = Session.pname;
   Vector pdetails = ConnectionManager.getProjectDetails(pid);
   String projectName = pdetails.get(1).toString();
   String srcFile,srcPath,xmlFile,xmlPath,imageFile,imagePath;
   
  //source
   if(pdetails.get(2) != null)
   {
	   srcPath = pdetails.get(2).toString();
	   String tmp[] = srcPath.split("\\\\");
	   srcFile = tmp[tmp.length-1];
   }
   else {
	   srcPath = "#";
	   srcFile = "NA";
   }
   //xml
   if(pdetails.get(3) != null)
   {
	   xmlPath = pdetails.get(3).toString();
	   String tmp[] = xmlPath.split("\\\\");
	   xmlFile = tmp[tmp.length-1];
   }
   else {
	   xmlPath = "#";
	   xmlFile = "NA";
   }
   
   //image
   if(pdetails.get(4) != null)
   {
	   imagePath = pdetails.get(4).toString();
	   String tmp[] = imagePath.split("\\\\");
	   imageFile = tmp[tmp.length-1];
   }
   else {
	   imagePath = "#";
	   imageFile = "NA";
   }
%>


<table align="center">
<tr><td><h2>Project Name:</h2></td><td><h2 style="color:#0000FF;"><%=projName%></h2></td></tr>
</table>

<hr><br><br>
<div align="center">
	<form action="codeChecker" method="post">
		<table>
			<tr><td><input type="hidden" name="src" value="<%=srcPath%>"></td></tr>
			<tr><td><input type="hidden" name="xml" value="<%=xmlPath%>"></td></tr>
			<tr><td><input type="hidden" name="image" value="<%=imagePath%>"></td></tr>
			<tr><td><b>Project Source</b></td><td><a href="view?path=<%=srcPath%>"><%=srcFile%></a></td></tr>
			<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
			<tr><td><b>Project Design (xml format)</b></td><td><a href="view?path=<%=xmlPath%>"><%=xmlFile%></a></td></tr>
			<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
			<tr><td><b>Project Design (image format)</b></td><td><a href="view?path=<%=imagePath%>"><%=imageFile%></a></td></tr>
		</table>
		
		<br><br><br><hr style="height: 10px;border: 0;box-shadow: 0 10px 10px -10px #8c8b8b inset"><br><br>
		<input type="submit" name = "genChecklist" value="Generate Checklist"/>	
		<input type="submit" name="genAntiList" value="Analyse antipatterns"/>
	</form>

</div>


</body>
</html>