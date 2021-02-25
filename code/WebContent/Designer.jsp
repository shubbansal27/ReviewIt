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
   String xmlFile,xmlPath,imageFile,imagePath;
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

<hr><br><br><br>
<div align="center">
	<form action="fileupload" enctype="multipart/form-data" method="post">
		<table>
		     <tr><td><b>Upload Design (class diagram: xml format)</b></td><td><input type="file" name="fileDesignXML" value="Add"/></td><td><input type="submit" name="btnUploadXML" value="Upload"/></td></tr>
			<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
			<tr><td><b>Upload Design (class diagram: image format)</b></td><td><input type="file" name="fileDesignImage" value="Add"/></td><td><input type="submit" name="btnUploadImage" value="Upload"/></td></tr>
		</table>
	</form>

	<br><br><br><hr style="height: 10px;border: 0;box-shadow: 0 10px 10px -10px #8c8b8b inset"><br>
	<table>
			<tr><td><b>Design (xml format)</b></td><td><a href="view?path=<%=xmlPath%>"><%=xmlFile%></a></td><td><input type="button" name="btnRemoveXML" value="Remove"/></td></tr>
			<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
			<tr><td><b>Design (image format)</b></td><td><a href="view?path=<%=imagePath%>"><%=imageFile%></a></td><td><input type="button" name="btnRemoveImage" value="Remove"/></td></tr>
	</table>
</div>



</body>
</html>