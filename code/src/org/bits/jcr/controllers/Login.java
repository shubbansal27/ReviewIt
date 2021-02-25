package org.bits.jcr.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Login extends HttpServlet {
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");
        
        if(ConnectionManager.checkUser(email, pass))
        {
            RequestDispatcher rs = request.getRequestDispatcher("Dashboard.jsp");
            Session.email = email;
            Session.userType = ConnectionManager.getEmail(email);
            Session.tid = -1;
            rs.forward(request, response);
        }
        else
        {
           Session.loginStat = 0;
           response.sendRedirect("index.jsp");
        }
    }  
}