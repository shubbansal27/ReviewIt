<%@ page import="org.bits.ontodev.actors.*" %>
<%@ page import="org.bits.ontodev.controllers.*" %>
<%@ page import="org.bits.ontodev.models.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.sql.*" %>
<%ResultSet resultset =null;%>
<script type="text/javascript"> 

function stopRKey(evt) { 
  var evt = (evt) ? evt : ((event) ? event : null); 
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
  if ((evt.keyCode == 13) && (node.type=="text"))  {return false;} 
} 

document.onkeypress = stopRKey; 

</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<div>
	<div>
		<div>
			<h3 align="center"><u>Add New Project</u></h3>
		</div>
		<div align="center">
			<form class="AddProject" action="addProject" method="post">
			    <table>
			    	
			    	 <tr><td><b>Project Name</b></td><td><input type="text" name="projectName"/></td>
					 <tr><td><b>Project Description</b></td><td><input type="text" name="projectDesc"/></td></tr>  
					 
					
					 <tr><td><b>Designer</td>
					 <td><select name ="designer">
					 <option>Choose A Name</option>
					  <%
					  	System.out.println("In JSP now");
					  				Vector list = ConnectionManager.getDesignerList();
					  				System.out.println("sIZE OF LIST RECV:"+list.size());
					  				for(int i=0;i<list.size();i++) {
					  					System.out.println("In JSP:"+list.get(i));
					  %>
		   		     <option value="<%=list.get(i)%>"><%=list.get(i)%></option>
		        	  <%
		        	  	}
		        	  %>  
                        </select> 
           
				 </td></tr>
					 <tr><td><b>Developer</td>
					 <td><select name ="developer">
					 <option >Choose A Name</option>
					  <%
					  	System.out.println("In JSP now");
					  				list = ConnectionManager.getDeveloperList();
					  				System.out.println("sIZE OF LIST RECV:"+list.size());
					  				for(int i=0;i<list.size();i++) {
					  					System.out.println("In JSP developer:"+list.get(i));
					  %>
		   		     <option value="<%=list.get(i)%>"><%=list.get(i)%></option>
		        	  <%
		        	  	}
		        	  %>  
                        </select> 
           
				 </td></tr> 					 
					 <tr><td><b>Manager</td>
					 <td><select name ="manager">
					 <option>Choose A Name</option>
					  <%
					  	System.out.println("In JSP now");
					  				list = ConnectionManager.getManagerList();
					  				System.out.println("sIZE OF LIST RECV:"+list.size());
					  				for(int i=0;i<list.size();i++) {
					  					System.out.println("In JSP developer:"+list.get(i));
					  %>
		   		     <option value="<%=list.get(i)%>"><%=list.get(i)%></option>
		        	  <%
		        	  	}
		        	  %>  
                        </select> 
           
				 </td></tr> 
					  <tr><td><b>Reviewer</td>
					 <td><select name ="reviewer">
					 <option>Choose A Name</option>
					  <%
					  	System.out.println("In JSP now");
					  				list = ConnectionManager.getReviewerList();
					  				System.out.println("sIZE OF LIST RECV:"+list.size());
					  				for(int i=0;i<list.size();i++) {
					  					System.out.println("In JSP developer:"+list.get(i));
					  %>
		   		     <option value="<%=list.get(i)%>"><%=list.get(i)%></option>
		        	  <%}%>  
                        </select> 
           
				 </td></tr> 
					 </table>
					 <br>
					 <input type="submit" value="AddProject"/>
				 
			</form>
		</div>
	</div>
 </div>	

</body>
</html>