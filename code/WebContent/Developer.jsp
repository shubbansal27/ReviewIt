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
	alert(pid);
	 var s1= "view?path=";
	 var s2 = s1.concat(pid);
	 
	 window.open(s2);
}
</script>
</head>
<body>


<%
   String pid = new String(""+Session.pid);
   String projName = Session.pname;
   Vector pdetails = ConnectionManager.getProjectDetails(pid);
   String projectName = pdetails.get(1).toString();
   String srcFile,srcPath;
   //xml
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
%>


<table align="center">
<tr><td><h2>Project Name:</h2></td><td><h2 style="color:#0000FF;"><%=projName%></h2></td></tr>
</table>

<hr><br><br><br>

<div align="center">
	<form action="fileupload" enctype="multipart/form-data" method="post">
		<table>
			<tr><td><b>Upload Project(.java/.zip)</b></td><td><input type="file" name="fileProject" value="Add"/></td><td><input type="submit" name="btnUploadProject" value="Upload"/></td></tr>
		</table>
	</form>

	<br><br><br><hr style="height: 10px;border: 0;box-shadow: 0 10px 10px -10px #8c8b8b inset"><br><br>
	<table>
			<tr><td><b>Project Source</b></td><td><a href="view?path=<%=srcPath%>"><%=srcFile%></a></td><td><input type="button" name="btnRemoveSource" value="Remove"/></td></tr>
	</table>

</div>





</body>
</html>