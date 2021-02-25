package org.bits.jcr.controllers;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.output.*;


public class FileUpload extends HttpServlet {
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    	   File file ;
    	   int maxFileSize = 5000 * 1024;
    	   int maxMemSize = 5000 * 1024;
    	   String filePath = "F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\Data\\"+Session.pid+"\\";
    	   
    	   //create output folder
           //remove old folder
           File fileOut = new File(filePath);
           if(fileOut.exists()) {
               fileOut.delete();
           }
           else {
               fileOut.mkdir();
           }
           
    	   
    	   String contentType = request.getContentType();
    	   if ((contentType.indexOf("multipart/form-data") >= 0)) {

    	      DiskFileItemFactory factory = new DiskFileItemFactory();
    	      factory.setSizeThreshold(maxMemSize);
    	      factory.setRepository(new File("c:\\temp"));
    	      ServletFileUpload upload = new ServletFileUpload(factory);
    	      upload.setSizeMax( maxFileSize );
    	      try{ 
    	    	 List fileItems = upload.parseRequest(request);
    	         Iterator i = fileItems.iterator();
    	         while ( i.hasNext () ) 
    	         {
    	        	FileItem fi = (FileItem)i.next();
    	        	if (fi.getSize() != 0 && !fi.isFormField () )  {
    	            	
    	            	String fieldName = fi.getFieldName();
    	                String fileName = fi.getName();
    	                file = new File( filePath + fileName) ;
    	                System.out.println(">>> "+file.getAbsolutePath());
    	                fi.write( file ) ;
    	                
    	                //add in database
    	                if(fieldName.equals("fileProject")) {
    	                	ConnectionManager.addSrcPath(""+Session.pid,filePath+fileName);
    	                }
    	                if(fieldName.equals("fileDesignXML")) {
    	                	ConnectionManager.addXmlPath(""+Session.pid,filePath+fileName);
    	                }
    	                if(fieldName.equals("fileDesignImage")) {
    	                	ConnectionManager.addImagePath(""+Session.pid,filePath+fileName);
    	                }	
    	                
    	            }
    	         }
    	         
    	         RequestDispatcher rs = request.getRequestDispatcher("Dashboard.jsp");
    	         rs.forward(request, response);
    	         
    	      }catch(Exception ex) {
    	         //file upload error
    	      }
    	   }else{
    		   //no file uploaded
    	   }
    }  
}