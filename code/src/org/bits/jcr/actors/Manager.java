package org.bits.jcr.actors;

import java.io.*;
import org.bits.jcr.actors.*;
import org.bits.jcr.controllers.*;
import org.bits.jcr.models.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Manager extends HttpServlet {
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         
    	String projectName = request.getParameter("projectName");
        String projectDesc = request.getParameter("projectDesc");
        String designer = request.getParameter("designer");
        String developer = request.getParameter("developer");
        String manager = request.getParameter("manager");
        String reviewer = request.getParameter("reviewer");
        
        
        if(ConnectionManager.addProject(projectName,projectDesc,designer,developer,manager,reviewer))
        {
            response.sendRedirect("Dashboard.jsp?tid=-1&pid=-1");
           /* Session.email = email;
            Session.userType = ConnectionManager.getEmail(email);
            Session.pid = -1;
            rs.forward(request, response);*/
        }
       /* else
        {
           Session.loginStat = 0;
           response.sendRedirect("index.jsp");
        }*/
    	
    	}  
}