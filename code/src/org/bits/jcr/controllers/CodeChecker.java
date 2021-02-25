package org.bits.jcr.controllers;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class CodeChecker extends HttpServlet {
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String srcPath = request.getParameter("src");
        String xmlPath = request.getParameter("xml");
        //String imagePath = request.getParameter("image");
        
        String opt1 = request.getParameter("genChecklist");
        String opt2 = request.getParameter("genAntiList");
        
        //clear OutputXML & OutputCode
        cleanDirectory("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXML");
        cleanDirectory("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputCode");
        cleanDirectory("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\TMP\\out");
        cleanDirectory("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\TMP\\unzip");
        
        if(opt1 != null) {
        	
        	//parse java code
        	new JavaCodeParser().parse(srcPath);
        	//parse xml
        	new XMLParser().parse(xmlPath);
        	
        	out.println("<html><body>");
        	
        	//generate checklist: class data
        	Checklist_Generator cg = new Checklist_Generator();
        	HashMap<String,Vector<String>> hmap = cg.compareDat();
        	Iterator<String> itr = hmap.keySet().iterator();
        	out.println("<center>Class structure</center><center><table>");
        	while(itr.hasNext()) {		
        		String st = itr.next();
        		Vector vt = hmap.get(st);
        		
        		if(vt.size() != 0) {
        			if(!st.equals("Additional Classes")) {
		        		out.println("<tr></tr><tr></tr><tr></tr><tr><td><h3><b>Class</b><span style='color:blue'> ["+st);
		        		out.println(" ]</span></h3></td></tr>");
		        		out.println("<tr>");
        			}
        			else {
        				out.println("<tr></tr><tr></tr><tr></tr><tr><td><h3><b>Class</b><span style='color:#D5D341'> ["+st);
		        		out.println(" ]</span></h3></td></tr>");
		        		out.println("<tr>");
        			}
        			
	        		for(int i=0;i<vt.size();i++) {
	        			String arr[] = vt.get(i).toString().split("\\|");
	        			out.println("<tr>");
	        			for(int j=0;j<arr.length;j++) {
	        				
	        				if(j==1){
	        					if(!arr[j].contains("Extra"))
	        						out.println("<td style='border: 1px solid;'><p style='color:red'>"+arr[j]+"</p></td>");
	        					else
	        						out.println("<td style='border: 1px solid;'><p style='color:#D5D341'>"+arr[j]+"</p></td>");
	        				}
	        				else if(j==2){
	        					out.println("<td style='border: 1px solid;'><p style='color:blue'>"+arr[j]+"</p></td>");
	        				}
	        				else if(j!=0) {
	        					out.println("<td style='border: 1px solid;'><p style='color:black'>"+arr[j]+"</p></td>");
	        				}
	        			}
	        			out.println("</tr>");
	        		}
	        		out.println("</tr>");
        		}	
        	}
        	out.println("</center></table>");
        	
        	
        	hmap = cg.compareRel();
        	itr = hmap.keySet().iterator();
        	
        	
        	out.println("<br><hr><center>Generalisation and Association</center><center><table>");
        	while(itr.hasNext()) {		
        		String st = itr.next();
        		Vector vt = hmap.get(st);
        		
        		if(vt.size() != 0) {
	        		out.println("<tr></tr><tr></tr><tr></tr><tr><td><h3><b>Class</b><span style='color:blue'> ["+st);
	        		out.println(" ]</span></h3></td></tr>");
	        		out.println("<tr>");
	        		
	        		for(int i=0;i<vt.size();i++) {
	        			String arr[] = vt.get(i).toString().split("\\|");
	        			out.println("<tr>");
	        			for(int j=0;j<arr.length;j++) {
	        				if(j==1){
	        					out.println("<td style='border: 1px solid;'><p style='color:red'>"+arr[j]+"</p></td>");
	        				}
	        				else if(j==2){
	        					out.println("<td style='border: 1px solid;'><p style='color:blue'>"+arr[j]+"</p></td>");
	        				}
	        				else if(j!=0) {
	        					out.println("<td style='border: 1px solid;'><p style='color:black'>"+arr[j]+"</p></td>");
	        				}
	        			}
	        			out.println("</tr>");
	        		}
	        		out.println("</tr>");
        		}	
        	}
        	out.println("</table></center>");
        	out.println("</body></html>");
        	
        }
        else if(opt2 != null) {
        	
        	//parse xml
        	new XMLParser().parse(xmlPath);
        	
        	//antipattern code
        	HashMap<String,String> hmap = new AntiPatternDetector().getAntiPatterns("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXML");
        	System.out.println(hmap);
        	Iterator<String> itr = hmap.keySet().iterator();
        	out.println("<html><body><center><h2>Anti-Patterns</h2><hr></center><center><table>");
        	while(itr.hasNext()) {		
        		String st = itr.next();
        		String vt = hmap.get(st);
        		
        		out.println("<tr>");
        		
        		String arr[] = vt.split(",");
        		out.println("<tr></tr><tr></tr><tr></tr><tr><td><h3><b>Class</b><span style='color:blue'> ["+st);
	        	out.println(" ]</span></h3></td></tr>");
	        	out.println("<tr>");
        		
        		for(int i=0;i<arr.length;i++) {
        			if(arr[i].trim().equals("NO ANTI PATTERN"))
        				out.println("<td style='border: 1px solid;'><p style='color:black'>"+arr[i]+"</p></td>");
        			else
        				out.println("<td style='border: 1px solid;'><p style='color:red'>"+arr[i]+"</p></td>");
        		}
	        	
        		out.println("</tr>");
        	}
        	
        	out.println("</table></center></body></html>");
        	
        }
    }  
    
    /**/
    public void cleanDirectory(String path) {
    	File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }
    }
    
    /**/
    private void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }
    
}