<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

<div>
	<div>
		<div>
			<h3 align="center"><u>Add New User</u></h3>
		</div>
		<div align="center">
			<form>
			     <table>
			     	<tr><td><b>Name</b></td><td><input type="text" name="name"/></td>
				     <tr><td><b>UserType</b></td><td>
				     <select>
						  <option value="Designer">Designer</option>
						  <option value="Developer">Developer</option>
						  <option value="Manager">Manager</option>
						  <option value="Reviewer">Reviewer</option>
						</select>
					 </td></tr>
					 <tr><td><b>Email</b></td><td><input type="text" name="email"/></td>
					 <tr><td><b>Password</b></td><td><input type="text" name="pass"/></td></tr>  
					 </table>
					 <br>
					 <input type="submit" value="Add User"/>
				 
			</form>
		</div>
	</div>
 </div>	
</body>
</html>