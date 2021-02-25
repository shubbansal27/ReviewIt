<%@ page import="org.bits.ontodev.controllers.*" %>

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
<div align="center">
	
			<h3 align="center"><u>Comments</u></h3>
		
		
			<form class="Comments" action="comments" method="post">
			    
			    <div style="height:250px; overflow:auto"> 
			    <table>
			     <%System.out.println("In JSP now");
			    	 
			    	    int pid=Session.pid;
						Vector list = ConnectionManager.getComments(pid);
						System.out.println("sIZE OF LIST RECV:"+list.size());
						for(int i=0;i<list.size();i++) {
							System.out.println("In JSP:"+list.get(i));%>
		   		    <tr>
		   		    <td><%=list.get(i)%></td></tr>
		   		    
		        	  <%}%>  
                       
           
				 </td></tr>
				 
					 </table>
					 </div>
					 <br><br><br><br><br>
					 
					 
					 
					 <input type="text" name="msg" placeholder="Enter Message" />
					 <tr><td><b>[TO]</td>
					 <td><select name ="receiver">
					 <option value="" disabled selected>Choose A Name</option>
					  <%System.out.println("In JSP now here");
					  	pid=3;
						list = ConnectionManager.getDesListComment(pid,"manpreet@gmail.com");
						System.out.println("sIZE OF LIST RECV:"+list.size());
						for(int i=0;i<list.size();i++) {
							System.out.println("In JSP developer:"+list.get(i));%>
		   		     <option value="<%=list.get(i)%>"><%=list.get(i)%></option>
		        	  <%}%>  
                        </select> 
           
				 </td></tr> 
					 <input type="submit" value="Submit"/>
				 
			</form>
		</div>
	 	

</body>
</html>