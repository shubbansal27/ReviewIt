package org.bits.jcr.controllers;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.xml.sax.SAXException;

public class XMLParser {

    private static String outputPath="F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputXML";
    private static String inputPath="";
    
    public void parse(String xmlPath) {
        
        this.inputPath = xmlPath;
        System.out.println(this.inputPath);
        
        try{
            File inp=new File(inputPath);
        
            PrintWriter writer = null, wRel=null;
            DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inp);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"+ doc.getDocumentElement().getNodeName());
            //list of class nodes
            NodeList nClassList = doc.getElementsByTagName("packagedElement");
            
            
            HashMap<String, String> classHash=new HashMap<>();
            HashMap<String, HashSet<String>> assoHash=new HashMap<String, HashSet<String>>();
            Vector<String> tempStr=new Vector<String>();
            
            //generate hash-map
             for (int x=0; x < nClassList.getLength(); x++){
                Element eClass = (Element) nClassList.item(x);
                String xmlType=eClass.getAttribute("xmi:type");
                if(xmlType.equals("uml:Class")){
                    String tagName=eClass.getAttribute("name");
                    String classId=eClass.getAttribute("xmi:id");
                    classHash.put(classId, tagName);
                }
             }
                          
            for (int t= 0; t < nClassList.getLength(); t++) {
                Node nClassNode= nClassList.item(t);
              //  PrintWriter writer = null, wRel=null;   
                    if(nClassNode.getNodeType()== Node.ELEMENT_NODE) {
                       Element eClass = (Element) nClassNode;
                       String tagName=eClass.getAttribute("name");
                       String classId=eClass.getAttribute("xmi:id");
                       String xmlType=eClass.getAttribute("xmi:type");
                       if(!xmlType.equals("uml:Class"))
                           continue;
                       writer = new PrintWriter(outputPath + "\\" + tagName+".dat", "UTF-8");
                       wRel = new PrintWriter(outputPath + "\\" + tagName+".rel", "UTF-8");
                       writer.println("className="+tagName);
                       writer.println("classVisibility="+eClass.getAttribute("visibility"));
                        
                        NodeList nAttrList = ((Element)nClassNode).getElementsByTagName("ownedAttribute");
                        for(int i=0; i<nAttrList.getLength();i++){
                            Node nAttrNode= nAttrList.item(i);
                            if(nAttrNode.getNodeType()==Node.ELEMENT_NODE){
                                Element eAttr= (Element) nAttrNode;
                                String tagName2=eAttr.getAttribute("name");
                                System.out.println("%%%%%%%%%%%%%%%%%%% "+tagName + "->" + tagName2);
                                if(tagName2.length()==0)
                                    continue;
                                String attrType=eAttr.getAttribute("type").split("_")[0];
                                writer.println("attribute name="+tagName2+
                                        " visibility="+eAttr.getAttribute("visibility")+
                                        " type="+attrType);
                                
                            }
                        }
                        wRel.print("generalization=");
                        NodeList nGenList= ((Element) nClassNode).getElementsByTagName("generalization");
                        for(int n=0; n<nGenList.getLength(); n++){
                            Node nGenNode= nGenList.item(n);
                            Element eGen= (Element)nGenNode;
                            if(n!=0) wRel.print(",");
                            String ch=eGen.getAttribute("general");
                            wRel.print(classHash.get(ch));
                        }
                        
                        wRel.println();
                     //   wRel.print("association=");
                     //////////--------------/////////////
                        NodeList nAssoList= ((Element) nClassNode).getElementsByTagName("ownedMember");
                        for(int k=0; k<nAssoList.getLength();k++){
                            Node nAssoNode=nAssoList.item(k);
                            NodeList nEndList= ((Element) nAssoNode).getElementsByTagName("ownedEnd");
                            Element e1=(Element) nEndList.item(0);
                            String c1=e1.getAttribute("type");
                            Element e2=(Element) nEndList.item(1);
                            String c2=e2.getAttribute("type");
                           
                            String class1=classHash.get(c1);
                            String class2=classHash.get(c2);
                            
                            /*System.out.println("ïteration"+k+":"+class1);
                            System.out.println("ïteration"+k+":"+class2);
                            */
                            
                            if (assoHash.get(class1) != null) {
                            	assoHash.get(class1).add(class2);
                            } else {
                                // Key might be present...
                                if (assoHash.containsKey(class1)) {
                                   // Okay, there's a key but the value is null
                                	//HashSet<String> hSet=new HashSet<String>();
                                	//hSet.add(class2);
                               	 	assoHash.get(class1).add(class2);
                                } else {
                                   // Definitely no such key
                                	HashSet<String> hSet=new HashSet<String>(); 
                               	 hSet.add(class2);
                               	 assoHash.put(class1,hSet);
                                }
                            }
                            
                            if (assoHash.get(class2) != null) {
                            	assoHash.get(class2).add(class1);
                            } else {
                                // Key might be present...
                                if (assoHash.containsKey(class2)) {
                                   // Okay, there's a key but the value is null
                                	//HashSet<String> hSet=new HashSet<String>();
                                	//hSet.add(class2);
                               	 	assoHash.get(class2).add(class1);
                                } else {
                                   // Definitely no such key
                                	HashSet<String> hSet=new HashSet<String>(); 
                               	 hSet.add(class1);
                               	 assoHash.put(class2,hSet);
                                }
                            }
                            
                         /*if(assoHash.containsKey(c1)==false){
                        	 HashSet<String> hSet=new HashSet<String>(); 
                        	 //hSet.add(null);
                        	 assoHash.put(class1,hSet);
                        	 System.out.println("IN null1 condition:"+assoHash.get(class1));
                         }
                        
                        	 
                        	 assoHash.get(class1).add(class2);
                        	 System.out.println("out null1:"+class1+"->"+assoHash.get(class1));
                         /*
                         if(assoHash.containsKey(c2)==false){
                        	 HashSet<String> hSet2=new HashSet<String>(); 
                        	 //hSet2.add(null);
                        	 assoHash.put(class2,hSet2);
                        	 System.out.println("IN null2 condition:"+assoHash.get(class2));
                         }
                         //else{
                        	 
                        	 assoHash.get(class2).add(class1);
                        	 System.out.println("out null2 :"+class2+"->"+assoHash.get(class2));*/
                           
                         //}// System.out.println("Values= "+e1);
                            
                           /* if(k!=0) wRel.print(",");
                            if(classHash.get(c1).equals(tagName))
                                wRel.print(classHash.get(c2));
                            else
                                wRel.print(classHash.get(c1));*/
                            
                        }
                        /*for (String name: assoHash.keySet()){

                            String key =name.toString();
                            String value = assoHash.get(name).toString();  
                            System.out.println("IN Final:"+key + " " + value);  


                } */
                        
                        //////////------------------------------------//////////////
                        
                        //wRel.println();
                        
                        NodeList nOprList = ((Element)nClassNode).getElementsByTagName("ownedOperation");
                        
                        for(int i=0; i<nOprList.getLength();i++){
                            Node nOprNode= nOprList.item(i);
                            if(nOprNode.getNodeType()==Node.ELEMENT_NODE){
                                Element eOpr= (Element) nOprNode;                    
                                
                                NodeList nParList = ((Element)nOprNode).getElementsByTagName("ownedParameter");
                                String returnType=null;
                                Node nParNode;
                                Element ePar;
                                //extract return type
                                for(int j=0; j<nParList.getLength();j++){
                                	nParNode= nParList.item(j);
                                    ePar= (Element) nParNode;
                                    //System.out.println(ePar.getAttribute("direction").equals("return"));
                                    if(ePar.getAttribute("direction").equals("return"))
                                    	returnType=ePar.getAttribute("type").split("_")[0];
                                }
                                nParNode= nParList.item(nParList.getLength()-1);
                                ePar= (Element) nParNode;
                                writer.print("operation name="+eOpr.getAttribute("name")+
                                        " visibility="+eOpr.getAttribute("visibility")+
                                        " returnType="+returnType+" # ");
                                
                                for(int j=0,flg=0; j<nParList.getLength();j++){     
                                    nParNode= nParList.item(j);
                                    ePar= (Element) nParNode;
                                    if(ePar.getAttribute("direction").equals("in")){
                                    	String parType=ePar.getAttribute("type").split("_")[0];       
	                                    if(flg!=0)
	                                        writer.print(",");
	                                    
	                                    flg=1;
	                                    writer.print("ParamName="+ePar.getAttribute("name")+
	                                            " paramType="+parType);
                                    }
                                }
                                writer.println();
                            }
                        }
                        writer.close();
                        wRel.print("association=");
                        wRel.close();
                        //finding associations
                    }            
            
            }
            
            for (String name: assoHash.keySet()){

                String key =name.toString();
                String value = assoHash.get(name).toString();  
                System.out.println("IN the final:"+key + " " + value); 
                String filename=outputPath + "\\" + key+".rel";
                //PrintWriter wRe = new PrintWriter( new FileOutputStream(filename), true);
                FileWriter fw = new FileWriter(filename,true);
                
                //wRe.print("association=");
               // HashSet<String> Set=new HashSet<String>();
               // PrintWriter pw = new PrintWriter(new FileOutputStream(
                	    //new File("persons.txt"), 
                	    //true /* append = true */)); 
                //fw.write("association=");
                Iterator itr = assoHash.get(name).iterator();
                
                while(itr.hasNext())
                  	{
                	 fw.write(itr.next()+",");
                  	}
                
                fw.close();

    }
           
            for (String name: classHash.keySet()){

                String key =name.toString();
                String value = classHash.get(name).toString();  
                System.out.println("IN the class:"+key + " " + value);  


    }
        }
        catch(ParserConfigurationException | SAXException | IOException e){
                e.printStackTrace();
        }    }   
}