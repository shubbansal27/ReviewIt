package org.bits.jcr.controllers;

import java.sql.*;
import java.util.Vector;

public class ConnectionManager
 {
	
	public static Connection con = null;
	
	public static void getConnection() {
		try {
			if(con == null) {
				con=DriverManager.getConnection
			        ("jdbc:mysql://localhost:3306/JCR","root","toor");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
     public static boolean checkUser(String email,String pass) 
     {
      boolean st =false;
      try{

	 //loading drivers for mysql
         Class.forName("com.mysql.jdbc.Driver");

         getConnection();
 	 //creating connection with the database 
         PreparedStatement ps =con.prepareStatement
                             ("select * from register where email=? and pass=?");
         ps.setString(1, email);
         ps.setString(2, pass);
         ResultSet rs =ps.executeQuery();
         st = rs.next();
        
      }catch(Exception e)
      {
          e.printStackTrace();
      }
         return st;                 
  }   
     
  public static String getEmail(String email) {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select userType from register where email=?");
		         ps.setString(1, email);
		        
		         ResultSet rs =ps.executeQuery();
		         if(rs.next()) {
		        	 return rs.getString("userType");
		         }
		         else {
		        	 return "NA";
		         }
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return "NA";
		      }
  }
   
  /* method: getProjectName() */
  public static Vector getProjectDetails(String pid) {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select pid,pname,srcPath,xmlPath,imagePath from project where pid=?");
		         ps.setString(1, pid);
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		         if(rs.next()) {
		        	 list.add(rs.getString("pid"));
		        	 list.add(rs.getString("pname"));
		        	 list.add(rs.getString("srcPath"));
		        	 list.add(rs.getString("xmlPath"));
		        	 list.add(rs.getString("imagePath"));
		        	 return list;
		         }
		         else {
		        	 return null;
		         }
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
  
  public static void addSrcPath(String pid,String path){ 
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("update project set srcPath = ? where pid=?");
		         ps.setString(1, path);
		         ps.setString(2, pid);
		        
		         ps.execute();
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		      }
  }

  public static void addXmlPath(String pid,String path){ 
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("update project set xmlPath = ? where pid=?");
		         ps.setString(1, path);
		         ps.setString(2, pid);
		        
		         ps.execute();
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		      }
  }
  
  public static void addImagePath(String pid,String path){ 
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("update project set imagePath = ? where pid=?");
		         ps.setString(1, path);
		         ps.setString(2, pid);
		        
		         ps.execute();
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		      }
  }
 
  
  /* method: getProjectList() */
  public static Vector getProjectList(String email) {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select t1.pid,t1.pname from userProjects t2, project t1 where t1.pid=t2.pid and t2.email = ?");
		         ps.setString(1, email);
		        
		         Vector<Vector<String>> list = new Vector<Vector<String>>();
		         ResultSet rs =ps.executeQuery();
		         while(rs.next()) {
		        	 Vector<String> tlist = new Vector<String>();
		        	 tlist.add(rs.getString("pid"));
		        	 System.out.println(rs.getString("pname"));
		        	 tlist.add(rs.getString("pname"));
		        	 list.add(tlist);
		         }
		         return list;
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }

  public static boolean addProject(String projectName,String projectDesc,String designer,String developer,String manager,String reviewer) 
  {
   boolean st =false;
   try{

	 //loading drivers for mysql
      Class.forName("com.mysql.jdbc.Driver");
      String names[]={designer,developer,manager,reviewer};

      getConnection();
	 //creating connection with the database 
      PreparedStatement ps =con.prepareStatement("select max(pid)as maxp from project");
      ResultSet rs= ps.executeQuery();
      st = rs.next();
      int pid= rs.getInt("maxp")+1;
      System.out.println("PID:"+pid);
      
      ps =con.prepareStatement
                          ("insert into project values("+pid+",?,?,?,?,?)");
      //ps.setInt(1,pid);
      ps.setString(1, projectName);
      ps.setString(2, projectDesc);
      ps.setString(3,"NULL");
      ps.setString(4,"NULL");
      ps.setString(5, "NULL");
      
      System.out.println(ps);
      int rows=ps.executeUpdate();
      System.out.println("Rows inserted:"+rows);
      
      for(int i=0;i<4;i++){
      ps =con.prepareStatement
              ("select email from register where name=?");
      ps.setString(1,names[i]);
      System.out.println(ps);
      rs =ps.executeQuery();
      st = rs.next();
      String mailId=rs.getString("email");
      
      ps =con.prepareStatement
              ("insert into userprojects values(?,"+pid+")");
      ps.setString(1,mailId);
      System.out.println(ps);
      rows=ps.executeUpdate();
      System.out.println("Rows inserted:"+rows);
      
      }
      
      
     
   }catch(Exception e)
   {
       e.printStackTrace();
   }
      return st;                 
}   
  
  public static Vector getDesignerList() {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select name from register where usertype = ?");
		         ps.setString(1, "designer");
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		         while(rs.next()) {
		        	 list.add(rs.getString("name"));
		        	 //list.add(rs.getString("pname"));
		        	 //System.out.println(list.get(0));
		        	 
		         }
		        
		         return list;	
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
  public static Vector getDeveloperList() {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select name from register where usertype = ?");
		         ps.setString(1, "developer");
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		         while(rs.next()) {
		        	 list.add(rs.getString("name"));
		        	 //list.add(rs.getString("pname"));
		        	// System.out.println(list.get(0));
		        	 
		         }
		         return list;
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
  
  public static Vector getManagerList() {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select name from register where usertype = ?");
		         ps.setString(1, "manager");
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		         while(rs.next()) {
		        	 list.add(rs.getString("name"));
		        	 //list.add(rs.getString("pname"));
		        	 System.out.println(list.get(0));
		        	 
		         }
		         return list;
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
  
  public static Vector getReviewerList() {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select name from register where usertype = ?");
		         ps.setString(1, "reviewer");
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		         while(rs.next()) {
		        	 list.add(rs.getString("name"));
		        	 //list.add(rs.getString("pname"));
		        	 //System.out.println(list.get(0));
		        	 
		         }
		         return list;
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
  
  public static Vector getComments(int pid) {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         PreparedStatement ps =con.prepareStatement
		                             ("select * from comments where pid = ?");
		         ps.setInt(1, pid);
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		         while(rs.next()) {
		        	 String total_str="[From]:"+rs.getString("sendername")+
		        			 " [To]:"+rs.getString("receivername")+" [Message]:"+rs.getString("message");
		        	 System.out.println(total_str);
		        	 list.add(total_str);
		        	 //list.add(rs.getString("pname"));
		        	 //System.out.println(list.get());
		        	 
		         }
		         return list;
		        
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
  
  
  
  public static Vector getDesListComment(int pid,String email) {
	  try{

			 //loading drivers for mysql
		         Class.forName("com.mysql.jdbc.Driver");

		         getConnection();
		         System.out.println("reached here");
		         PreparedStatement ps =con.prepareStatement
		                             ("select name from register r,userprojects u where r.email=u.email and u.email<> ? and r.userType <> ? and u.pid="+pid);
		         ps.setString(1, email);
		         ps.setString(2, "admin");
		         System.out.println(ps);
		        
		         Vector<String> list = new Vector<String>();
		         ResultSet rs =ps.executeQuery();
		        while(rs.next()) {
		        	 list.add(rs.getString("name"));
		        	 //list.add(rs.getString("pname"));
		        	 //System.out.println(list.get(i));
		        	 
		         }
		        
		         return list;	
		      }catch(Exception e)
		      {
		          e.printStackTrace();
		          return null;
		      }
  }
 public static void addComments(String receiver,String msg) {
  try{

		 //loading drivers for mysql
	         Class.forName("com.mysql.jdbc.Driver");
	         System.out.println("Inside addComments:Session.pid="+Session.pid);
	         System.out.println("Inside addComments:Session.email="+Session.email);
	         getConnection();
	         PreparedStatement ps =con.prepareStatement("select name from register where email=?");
	         ps.setString(1, Session.email);
	         ResultSet rs =ps.executeQuery();
	         String name;
	         if(rs.next())
	         {name=rs.getString("name");
	         
	         ps =con.prepareStatement
	                             ("select name from register r,userprojects u where r.email=u.email and u.email<> ? and r.userType<>? and u.pid="+Session.pid);
	         ps.setString(1, Session.email);
	         ps.setString(2,"admin" );
	         System.out.println(ps);
	        
	         Vector<String> list = new Vector<String>();
	         rs =ps.executeQuery();
	         if(rs.next()) {
	        	 list.add(rs.getString("name"));
	        	 //list.add(rs.getString("pname"));
	        	 System.out.println(list.get(0));
	        	 
	         }
	         ps=con.prepareStatement("insert into comments values(?,?,?,?)");
	         ps.setInt(1,Session.pid);
	         ps.setString(2,name);
	         ps.setString(3,receiver);
	         ps.setString(4,msg);
	         
	         System.out.println(ps);
	         int rows=ps.executeUpdate();
	         System.out.println("Rows inserted:"+rows);
	         }//endif       
	         	
	      }catch(Exception e)
	      {
	          e.printStackTrace();
	          
	      }
}
  
}