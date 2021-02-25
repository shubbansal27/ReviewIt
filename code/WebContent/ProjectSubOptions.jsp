<%@ page import="org.bits.ontodev.controllers.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script>
function openResult1() {
	document.getElementById("resultForm1").submit();
}
function openResult2() {
	document.getElementById("resultForm2").submit();
}
</script>
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



<body>


	
	<form id="resultForm1" action="codeChecker" method="post">
		<input type="hidden" name="src" value="<%=srcPath%>">
		<input type="hidden" name="xml" value="<%=xmlPath%>">
		<input type="hidden" name = "genChecklist" value="GC"/>	
	</form>
	<form id="resultForm2" action="codeChecker" method="post">
		<input type="hidden" name="src" value="<%=srcPath%>">
		<input type="hidden" name="xml" value="<%=xmlPath%>">
		<input type="hidden" name = "genAntiList" value="AP"/>	
	</form>

	<ul>
		  <li><a href="Dashboard.jsp?tid=<%=Session.pid%>">Project Data</a></li><br>
		  <li><a onclick="javascript:openResult1()" href="#">Results: Checklist</a></li><br>
		  <li><a onclick="javascript:openResult2()" href="#">Results: Anti-Pattern</a></li><br>
		  <li><a href="Dashboard.jsp?tid=-2">Comments</a></li><br>
	</ul>


</body>
</body>
</html>