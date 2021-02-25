package org.bits.jcr.controllers;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class Checklist_Generator {

	
	public HashMap<String,Vector<String>> compareDat() {
		
		//listing classes from XML output
		File file = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXML");
		File files[] = file.listFiles(new FilenameFilter() {
			
			 @Override
		     public boolean accept(File dir, String name) {
				 return name.toLowerCase().endsWith(".dat");
		     }
		});
		
		//listing classes from code output
		File fCode = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputCode");
		File filesCode[] = fCode.listFiles(new FilenameFilter() {
					
			 @Override
		     public boolean accept(File dir, String name) {
				 return name.toLowerCase().endsWith(".dat");
		     }
		});
		
		//list extra classes which are not present in design
		Vector<String> cList = new Vector<String>();
		for(int i=0;i<files.length;i++) {
           String className = files[i].getName().split("\\.")[0].trim();
           cList.add(className);
		}
		
		
		Vector<String> classList = new Vector<String>();
        HashMap<String,Vector<String>> classMap = new HashMap<String,Vector<String>>();
        Vector<String> commentList1 = new Vector<String>();
		boolean fl=false;
        for(int i=0;i<filesCode.length;i++) {
			String codeClassName = filesCode[i].getName().split("\\.")[0].trim();
			//System.out.println(">> " + codeClassName);
			if(!cList.contains(codeClassName)) {
				fl = true;
				//System.out.println("checkkkkk");
				commentList1.add("Class Name:"+codeClassName + "|Type:Extra Class" +  "|Message: This class is not a part of design" + "|Class Name:"+codeClassName);
			}
		}
		if(fl) classMap.put("Additional Classes", commentList1);		
          
          for(int i=0;i<files.length;i++) {
                //System.out.println(files[i].getName());
                String className = files[i].getName().split("\\.")[0];
				File fileXML = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXml/"+className+".dat");
				File fileCode = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputCode/"+className+".dat");
				Vector<String> commentList = new Vector<String>();
                                
                                //1
				if(!fileCode.exists()) {
					System.out.println("Class doesn't exists in code");
                    commentList.add("Class Name:"+className + "|Type:Class Existence" +  "|Message: class not found in code" + "|Class name:"+className);
				}
                else {
					//parse both file and find matching
                     parse(fileXML,fileCode,className,commentList);
                     parseReverse(fileCode,fileXML,className,commentList);
				}
                     classList.add(className);
                     classMap.put(className,commentList);
		
                     //debug
                     /*for(int k=0;k<commentList.size();k++) {
                      	System.out.println(commentList.get(k));
                     }*/
                }
		
                return classMap;
	}
	
	

	private void parseReverse(File fileXML,File fileCode,String className,Vector<String> commentList) {
		try {
            //opening file handler
			BufferedReader brXML = new BufferedReader(new FileReader(fileXML));
            BufferedReader brCode = new BufferedReader(new FileReader(fileCode));
			String lineXML,lineCode;
			
                        
                        lineXML = brXML.readLine();       //className  line#1
                        lineCode = brCode.readLine();
                        
                        //check class-visibility
                        System.out.println(className);
                        lineXML = brXML.readLine();    //visibility line#2
                        lineCode = brCode.readLine().toLowerCase().split("class")[1];
                        
                        //check attribute and operations
                        //store code-data into hashmap
                        HashMap<String,HashMap<String,String>> attribMap = new HashMap<String,HashMap<String,String>>();
                        HashMap<String,HashMap<String,String>> operationMap = new HashMap<String,HashMap<String,String>>();
                        //parse code
                        while((lineCode = brCode.readLine()) != null) {
                                lineCode = lineCode.trim();
                                String token[] = lineCode.split(" ");
                                String tag = token[0];
                                if(tag.equals("attribute")) {
                                	String name="",visibility="",type="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("type")) {
                                			type = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                    HashMap<String,String> hmap = new HashMap<String,String>();
                                    hmap.put("type", type);
                                    hmap.put("visibility", visibility);
                                    attribMap.put(name, hmap);
                                }
                                else if(tag.equals("operation")) {
                                    
                                	String name="",visibility="",returnType="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("returnType")) {
                                			returnType = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                	HashMap<String,String> hmap = new HashMap<String,String>();
                                    hmap.put("returnType", returnType);
                                    hmap.put("visibility", visibility);
                                    
                                    //Check: parameter
                                    /*
                                    String arr[] = lineCode.split("#");
                                    if(arr.length == 2) {
                                        String paramArr[] = arr[1].split(",");
                                        for(int i=0;i<paramArr.length;i++) {
                                            paramArr[i] = paramArr[i].trim();
                                            String paramToken[] = paramArr[i].split(" ");
                                            String pname=paramToken[0].trim();
                                            String ptype=paramToken[1].trim();
                                            hmap.put("param"+(i+1)+"_name", pname);
                                            hmap.put("param"+(i+1)+"_type", ptype);
                                        }
                                    }*/
                                        
                                    operationMap.put(name, hmap);
                                }
			}
			
                        //parse xml
                        while((lineXML = brXML.readLine()) != null) {
                        		lineXML = lineXML.trim();
                                String token[] = lineXML.split(" ");
                                String tag = token[0];
                                if(tag.equals("attribute")) {
                                	
                                	String name="",visibility="",type="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("type")) {
                                			type = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                	//match with code
                                    if(!attribMap.containsKey(name)) {
                                        commentList.add("Class Name:"+className + "|Type:Extra Attribute" + "|Message:This attribute is not part of design" +  "|Attribute name:" + name);
                                    }
                                                                        
                                }
                                else if(tag.equals("operation")) {
                                	
                                	String name="",visibility="",returnType="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("returnType")) {
                                			returnType = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                    //match with code
                                    if(!operationMap.containsKey(name)) {
                                        commentList.add("Class Name:"+className + "|Type:Extra Operation" + "|Message:This operation is not a part of design" + "|Operation:" + name);
                                    }
                                    else {
                                        
                                    
                                        /*
                                        //Check: parameter
                                        String arr[] = lineXML.split("#");
                                        if(arr.length == 2) {
                                            String paramArr[] = arr[1].split(",");
                                            for(int i=0;i<paramArr.length;i++) {
                                                paramArr[i] = paramArr[i].trim();
                                                String paramToken[] = paramArr[i].split(" ");
                                                String pname=paramToken[0].trim();
                                                String ptype=paramToken[1].trim();

                                                //match parameters with code
                                                if(!operationMap.get(name).containsKey("param"+(i+1)+"_name")) {
                                                    commentList.add("Class Name:"+className + "|Type:Parameter Existence" +  "|Operation:" + name+ "|Parameter:parameter-" + (i+1) +"|Message:parameter doesn't exist in code");
                                                }
                                                else {
                                                    
                                                    String pn = operationMap.get(name).get("param"+(i+1)+"_name");
                                                    String pt = operationMap.get(name).get("param"+(i+1)+"_type");    

                                                    if(!pn.equals(pname)) {
                                                        commentList.add("Class Name:"+className + "|Type:Parameter name mismatch" +  "|Operation:" + name+ "|Parameter:" + pname + "|Message: parameter name in code doesn't match with Design" + "|Design:" + pname + "|Code:" + pn);
                                                    }
                                                    if(!pt.equals(ptype)) {
                                                        commentList.add("Class Name:"+className + "|Type:Parameter type mismatch" +  "|Operation:" + name+ "|Parameter:" + pname + "|Message: parameter type in code doesn't match with Design" + "|Design:" + ptype + "|Code:" + pt);
                                                    }
                                                }


                                            }
                                        }*/


                                    }
                                }
                        }
                        
                        
                        //closing file handler
			brXML.close();
                        brCode.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private void parse(File fileXML,File fileCode,String className,Vector<String> commentList) {
		try {
                        //opening file handler
			BufferedReader brXML = new BufferedReader(new FileReader(fileXML));
            BufferedReader brCode = new BufferedReader(new FileReader(fileCode));
			String lineXML,lineCode;
			
                        
                        lineXML = brXML.readLine();       //className  line#1
                        lineCode = brCode.readLine();
                        
                        //check class-visibility
                        lineXML = brXML.readLine().toLowerCase().split("class")[1];    //visibility line#2
                        lineCode = brCode.readLine();
                        //System.out.println(lineXML + " --> " + lineCode);
                        if(!lineXML.equals(lineCode)) {
                            commentList.add("Class Name:"+className + "|Type:Class Visibility" +  "|Message: class visiblity in code doesn't match with Design" + "|Class name:"+className + "|Design:" + lineXML + "|Code:" + lineCode);
                        }
                        
                        //check attribute and operations
                        //store code-data into hashmap
                        HashMap<String,HashMap<String,String>> attribMap = new HashMap<String,HashMap<String,String>>();
                        HashMap<String,HashMap<String,String>> operationMap = new HashMap<String,HashMap<String,String>>();
                        //parse code
                        while((lineCode = brCode.readLine()) != null) {
                                lineCode = lineCode.trim();
                                String token[] = lineCode.split(" ");
                                String tag = token[0];
                                if(tag.equals("attribute")) {
                                	String name="",visibility="",type="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("type")) {
                                			type = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                    HashMap<String,String> hmap = new HashMap<String,String>();
                                    hmap.put("type", type);
                                    hmap.put("visibility", visibility);
                                    attribMap.put(name, hmap);
                                }
                                else if(tag.equals("operation")) {
                                    
                                	String name="",visibility="",returnType="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("returnType")) {
                                			returnType = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                	HashMap<String,String> hmap = new HashMap<String,String>();
                                    hmap.put("returnType", returnType);
                                    hmap.put("visibility", visibility);
                                    
                                    //Check: parameter
                                    /*
                                    String arr[] = lineCode.split("#");
                                    if(arr.length == 2) {
                                        String paramArr[] = arr[1].split(",");
                                        for(int i=0;i<paramArr.length;i++) {
                                            paramArr[i] = paramArr[i].trim();
                                            String paramToken[] = paramArr[i].split(" ");
                                            String pname=paramToken[0].trim();
                                            String ptype=paramToken[1].trim();
                                            hmap.put("param"+(i+1)+"_name", pname);
                                            hmap.put("param"+(i+1)+"_type", ptype);
                                        }
                                    }*/
                                        
                                    operationMap.put(name, hmap);
                                }
			}
			
                        //parse xml
                        while((lineXML = brXML.readLine()) != null) {
                        		lineXML = lineXML.trim();
                                String token[] = lineXML.split(" ");
                                String tag = token[0];
                                if(tag.equals("attribute")) {
                                	
                                	String name="",visibility="",type="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("type")) {
                                			type = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                	//match with code
                                    if(!attribMap.containsKey(name)) {
                                        commentList.add("Class Name:"+className + "|Type:Attribute Existence" + "|Message:attribute doesn't exist in code" +  "|Attribute name:" + name);
                                    }
                                    else {
                                        String t = attribMap.get(name).get("type");
                                        String v = attribMap.get(name).get("visibility");
                                        if(!t.equals(type)) {
                                            commentList.add("Class Name:"+className + "|Type:Attribute Type" +  "|Message: attribute type in code doesn't match with Design" + "|Attribute name:" + name + "|Design:" + type + "|Code:" + t);
                                        }
                                        if(!v.equals(visibility)) {
                                            commentList.add("Class Name:"+className + "|Type:Attribute Visibility" +  "|Message: attribute visibility in code doesn't match with Design" +  "|Attribute name:" + name + "|Design:" + visibility + "|Code:" + v);
                                        }
                                    }
                                    
                                }
                                else if(tag.equals("operation")) {
                                	
                                	String name="",visibility="",returnType="";
                                	for(int t=0;t<token.length;t++)  {
                                		String ttag = token[t].split("=")[0].trim();
                                		
                                		if(ttag.equals("name")) {
                                			name = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("returnType")) {
                                			returnType = token[t].split("=")[1];
                                		}
                                		else if(ttag.equals("visibility")) {
                                			visibility = token[t].split("=")[1];
                                		}
                                	}
                                	
                                    //match with code
                                    if(!operationMap.containsKey(name)) {
                                        commentList.add("Class Name:"+className + "|Type:Operation Existence" + "|Message:operation doesn't exist in code" + "|Operation:" + name);
                                    }
                                    else {
                                        String t = operationMap.get(name).get("returnType");
                                        String v = operationMap.get(name).get("visibility");
                                        if(!t.equals(returnType)) {
                                            commentList.add("Class Name:"+className + "|Type:Operation returnType" +  "|Message: operation return type in code doesn't match with Design" + "|Operation:" + name + "|Design:" + returnType + "|Code:" + t);
                                        }
                                        if(!v.equals(visibility)) {
                                            commentList.add("Class Name:"+className + "|Type:Operation Visibility" +  "|Message: operation visibility in code doesn't match with Design" + "|Operation:" + name + "|Design:" + visibility + "|Code:" + v);
                                        }
                                    
                                        /*
                                        //Check: parameter
                                        String arr[] = lineXML.split("#");
                                        if(arr.length == 2) {
                                            String paramArr[] = arr[1].split(",");
                                            for(int i=0;i<paramArr.length;i++) {
                                                paramArr[i] = paramArr[i].trim();
                                                String paramToken[] = paramArr[i].split(" ");
                                                String pname=paramToken[0].trim();
                                                String ptype=paramToken[1].trim();

                                                //match parameters with code
                                                if(!operationMap.get(name).containsKey("param"+(i+1)+"_name")) {
                                                    commentList.add("Class Name:"+className + "|Type:Parameter Existence" +  "|Operation:" + name+ "|Parameter:parameter-" + (i+1) +"|Message:parameter doesn't exist in code");
                                                }
                                                else {
                                                    
                                                    String pn = operationMap.get(name).get("param"+(i+1)+"_name");
                                                    String pt = operationMap.get(name).get("param"+(i+1)+"_type");    

                                                    if(!pn.equals(pname)) {
                                                        commentList.add("Class Name:"+className + "|Type:Parameter name mismatch" +  "|Operation:" + name+ "|Parameter:" + pname + "|Message: parameter name in code doesn't match with Design" + "|Design:" + pname + "|Code:" + pn);
                                                    }
                                                    if(!pt.equals(ptype)) {
                                                        commentList.add("Class Name:"+className + "|Type:Parameter type mismatch" +  "|Operation:" + name+ "|Parameter:" + pname + "|Message: parameter type in code doesn't match with Design" + "|Design:" + ptype + "|Code:" + pt);
                                                    }
                                                }


                                            }
                                        }*/


                                    }
                                }
                        }
                        
                        
                        //closing file handler
			brXML.close();
                        brCode.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	////////////////////////////////////////////////////////////////
	
	public HashMap<String,Vector<String>> compareRel() {
		
		//listing classes from XML output
		File file = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXML");
		File files[] = file.listFiles(new FilenameFilter() {
			
			 @Override
		     public boolean accept(File dir, String name) {
				 return name.toLowerCase().endsWith(".rel");
		     }
		});
		
                //Vector<String> classList = new Vector<String>();
                HashMap<String,Vector<String>> classMap = new HashMap<String,Vector<String>>();
                for(int i=0;i<files.length;i++) {
                    
                	//System.out.println(files[i].getName());
	                String className = files[i].getName().split("\\.")[0];
					File fileXML = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXml/"+className+".rel");
					File fileCode = new File("F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputCode/"+className+".rel");
					Vector<String> commentList = new Vector<String>();
	                
					if(!fileCode.exists()) {
						//System.out.println("Class doesn't exits in code");
	                    commentList.add("Class Name:"+className + "|Type:Class Existence" +  "|Message: class not found in code" + "|Class name:"+className);
					}
					else {
		                //parse both file and find matching
		                parseRel(fileXML,fileCode,className,commentList);
		            }
					classMap.put(className, commentList);
                }
		
                return classMap;
	}
	
	private void parseRel(File fileXML,File fileCode,String className,Vector<String> commentList) {
		try {
                        //opening file handler
			BufferedReader brXML = new BufferedReader(new FileReader(fileXML));
            BufferedReader brCode = new BufferedReader(new FileReader(fileCode));
			String lineXML,lineCode;
			
						//check generalisation
						HashSet<String> hset = new HashSet<String>();
                        
						String arrCode[] = brCode.readLine().split("\\=");
						if(arrCode.length == 2) {
							lineCode = arrCode[1];
							String arr[] = lineCode.split("\\,");
							for(int i=0;i<arr.length;i++) {
								hset.add(arr[i].trim());
							}
						}
						
						String arrXML[] = brXML.readLine().split("\\=");
						if(arrXML.length == 2) {
							lineXML = arrXML[1];
						
	                        String arr[] = lineXML.split("\\,");
	                        for(int i=0;i<arr.length;i++) {
	                        	if(!hset.contains(arr[i].trim())) {
	                        		commentList.add("Class Name:"+className + "|Type:Generalisation Missing " +  "|Message: Generalisation is missing in code" + "|End1:"+className + "|End2:" + arr[i]);
	                        	}
	                        }
						}
                        
						//check association
						arrCode = brCode.readLine().split("\\=");
						if(arrCode.length == 2) {
							lineCode = arrCode[1];
							String arr[] = lineCode.split("\\,");
							for(int i=0;i<arr.length;i++) {
								hset.add(arr[i].trim());
							}
						}
						
						arrXML = brXML.readLine().split("\\=");
						if(arrXML.length == 2) {
							lineXML = arrXML[1];
						
	                        String arr[] = lineXML.split("\\,");
	                        for(int i=0;i<arr.length;i++) {
	                        	if(!hset.contains(arr[i].trim())) {
	                        		commentList.add("Class Name:"+className + "|Type:Association Missing " +  "|Message: Association is missing in code" + "|End1:"+className + "|End2:" + arr[i]);
	                        	}
	                        }
						}
                                               
                        //closing file handler
                        brXML.close();
                        brCode.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
