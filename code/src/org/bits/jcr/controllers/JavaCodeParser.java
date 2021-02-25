/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bits.jcr.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class JavaCodeParser {

    private String outputPath="F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\OutputCode";
    private String inputPath="";
    
    public void parse(String codePath) {
        
    	this.inputPath = codePath;
    	System.out.println(this.inputPath);
    	
    	//check codePath is java file or 
    	File ftmp = new File(codePath);
    	if(ftmp.exists()) {
    		System.out.println("file found");
    		String ext = ftmp.getName().split("\\.")[1].trim();
    		System.out.println("extension = " + ext);
    		if(ext.equals("java")) {
    			System.out.println("in .java");
    			parseMergedFile();
    		}
    		else if(ext.equals("zip") || ext.equals("rar")) {
    			System.out.println("in .zip");
    			new MergerFiles(this.inputPath,"F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\TMP\\unzip","F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\TMP\\out\\merged.java").merge();
    			this.inputPath = "F:\\Workspace_Eclipse\\JCR\\WebContent\\WEB-INF\\TMP\\out\\merged.java";
    			parseMergedFile();
    		}
    		else {
    			System.out.println("in else");
    		}
    	}
    	else {
    		System.out.println("File not found !!");
    		
    	}
    	
    }
    
    
    public void parseMergedFile() {

    	//this.inputPath = this.basePath + "/" + codePath;
        //System.out.println(">> " + this.inputPath);
        FileInputStream fis = null;
        String[][] list = new String[10000][2];
        int classIndex[] = new int[10000];
        int totalClass=0;
        int sizeList = 0;
        
        try {
            File file = new File(inputPath);
            fis = new FileInputStream(file);
            char current;
            String buffer = "";
            Stack<Character> stack = null;
            int flag = 1;
            while (fis.available() > 0)
            {
                current = (char) fis.read();                
                
                if(current == '{' && buffer.length()>0 && flag == 1)
                {
                    buffer = buffer.trim();
                    //System.out.println(buffer);
                    if(buffer.charAt(buffer.length()-1) == ')' ) {
                        list[sizeList][0] = buffer;
                        list[sizeList][1] = "m";
                        buffer = "";
                        sizeList++;
                        flag = 0;
                        stack = new Stack<>();
                        stack.push('{');
                    }
                    else
                    {
                        list[sizeList][0] = buffer;
                        list[sizeList][1] = "c";
                        buffer = "";
                        classIndex[totalClass++] = sizeList;
                        sizeList++;
                    }
                    
                }
                else if(current == ';' && buffer.length()>0 && flag == 1)
                {
                    if(!buffer.contains("import") && !buffer.contains("package"))
                    {
                        list[sizeList][0] = buffer.trim();
                        list[sizeList][1] = "v";
                        buffer = "";
                        sizeList++;                        
                    }
                    else
                    {
                        buffer = "";
                    }
                }
                else if(flag == 1)
                {
                    
                    buffer = buffer + current;
                    //sizeBuffer++;
                } 
                else if(flag==0)
                {
                    if(current == '{')
                        stack.push('{');
                    else if(current == '}')
                    {
                        stack.pop();
                        if(stack.isEmpty())
                            flag=1;
                    }
                }
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        /*for(int i=0; i < sizeList; i++)
        {
            System.out.println(list[i][0] + "-->" + list[i][1]);
        }*/
        
        classIndex[totalClass++] = sizeList;
        createList(list, sizeList, classIndex, totalClass);
        
        
        //update association in .rel files
        parseAssociation(outputPath);
        
    }

    
    private void parseAssociation(String inPath) { //"Output/OutputCode"
        
    	File file = new File(inPath);
        File files[] = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".dat");
            }
        });
        
        HashSet<String> tokenSet = new HashSet<>();
        HashMap<String, String> assoc = new HashMap<>();
        
        for(int i=0;i<files.length;i++) {
            String className = files[i].getName().split("\\.")[0];
        	tokenSet.add(className);
            try {
	            PrintWriter writer = new PrintWriter(new FileWriter(inPath+"\\"+className+".rel",true));
	            writer.print("\nassociation=");
	            writer.close();
            }
            catch(Exception e){
            	e.printStackTrace();
            }
        }
        
        FileInputStream fis = null;
        
        try {
            //s = new Scanner(new BufferedReader(new FileReader("C:\\test\\testing.txt")));
            file = new File(inputPath);
            fis = new FileInputStream(file);
            char current;
            String buffer = "";
            //int fl=0;
            while (fis.available() > 0)
            {
                current = (char) fis.read();                
                if(buffer.contains("class") && buffer.contains("{")){
                    //System.out.println("debug1");
                    String className = buffer.split("class")[1].trim().split("\\s")[0].trim();
                    //System.out.println(className);
                    buffer = "";
                    while(fis.available()>0 && tokenSet.contains(className)){
                        current = (char) fis.read();
                        //System.out.println("debug3");
                        if(!buffer.contains("new") && buffer.contains(";")) buffer = ""; 
                        if(buffer.contains("new") && buffer.contains(";")){
                            //System.out.println("debug2");
                            if(assoc.keySet().contains(className)){ 
                                assoc.put(className, assoc.get(className) + " , " + buffer.split("new")[1].trim().split("\\(")[0].trim());
                            }
                            else {
                                //System.out.println(buffer);
                                assoc.put(className, buffer.split("new")[1].trim().split("\\(")[0].trim());
                            }
                            buffer = "";
                        }
                        else if(buffer.contains("class"))
                            break;
                        else
                            buffer = buffer + current;
                    }
                    buffer = "class ";
                }
                else
                    buffer = buffer + current;                                
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        //System.out.println("reached here !!");
        //now print hashmap
        /*Iterator<String> itr = assoc.keySet().iterator();
        while(itr.hasNext()) {
            String key = itr.next();
            System.out.println(key + " - >" + assoc.get(key));
        }*/
        
        //create adjacency list for bidirectional association
        HashMap<String,HashSet<String>> adcList = new HashMap<>();
        Iterator<String> itr = assoc.keySet().iterator();
        while(itr.hasNext()) {
            String key = itr.next();
            if(!adcList.containsKey(key)) {
                adcList.put(key, new HashSet<String>());
            }
            String arr[] =  assoc.get(key).split("\\,");
            HashSet<String> vt = adcList.get(key);
            for(int i=0;i<arr.length;i++) {
                String item = arr[i].trim();
                //add item to list of key
                vt.add(item);
                //now addd key to list of item
                if(!adcList.containsKey(item)) {
                    adcList.put(item, new HashSet<String>());
                }
                HashSet<String> vt1 = adcList.get(item);
                vt1.add(key);
                adcList.put(item,vt1);
            }
            adcList.put(key,vt);
        }
        
        //now update .rel file
        Iterator<String> it = adcList.keySet().iterator();
        while(it.hasNext()) {
            
            String key = it.next();
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter(inPath+"/"+key+".rel",true));
                HashSet set = adcList.get(key);
                for(Object val:set){
                    writer.print(val.toString() + ",");
                }
                writer.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private void createList(String[][] list, int sizeList, int [] classIndex, int totalClass)
    {
        for(int i=0; i<totalClass-1; i++)
        {
            //opening file handler
            PrintWriter writer = processClass(list[classIndex[i]][0]);
            
            for(int j=classIndex[i]+1;j<=classIndex[i+1]-1;j++) {
                if(list[j][1].equals("v")) {
                    processVariable(list[j][0],writer);
                }
            }
            
            for(int j=classIndex[i]+1;j<=classIndex[i+1]-1;j++) {
                if(list[j][1].equals("m")) {
                       processMethod(list[j][0],writer);
                }
            }  
            
            //closing file handler
            if(writer != null)
                writer.close();
            
        }
        
        
    }
    
    private PrintWriter processClass(String listItem)
    {
       HashSet<String> tokenSet = new HashSet<>();
       String name, accessModifier, extendClass=null, implementClass[]={};
       String[] tokens = listItem.split("class");
       System.out.println("listItem= "+listItem);
       name = tokens[1].trim().split(" ")[0];
       
       tokenSet.addAll(Arrays.asList(tokens[0].trim().split(" ")));
       
       if(tokenSet.contains("private"))
           accessModifier = "private";
       else if(tokenSet.contains("public"))
           accessModifier = "public";
       else
           accessModifier = "public";
       
       tokenSet.clear();
       
       if(tokens[1].contains("extends"))
            extendClass = tokens[1].trim().split("extends")[1].trim().split("implements")[0].trim();
       if(tokens[1].contains("implements"))
            implementClass = tokens[1].trim().split("implements")[1].trim().split(",");
       
       //writing into file
       PrintWriter writer = null, writerRel=null;
       try {
        writer = new PrintWriter(outputPath+"/"+name+".dat", "UTF-8");
        writerRel = new PrintWriter(outputPath+"/"+name+".rel", "UTF-8");
        
        writer.println("className="+name);
        writer.println("visibility="+accessModifier);
        
        writerRel.print("generalisation=");
        if(extendClass!=null)
            writerRel.print(extendClass);
        if(implementClass.length != 0) {
            for(int i=0;i<implementClass.length;i++) {
                if(i!=0 || extendClass!=null) writerRel.print(",");
                writerRel.print(implementClass[i]);
            }
        }
        
        
       }
       catch(Exception e) {
           e.printStackTrace();
       }
       finally {
           if(writerRel != null) writerRel.close();
       }
       
       return writer;
    }
    
    private void processVariable(String listItem, PrintWriter writer)
    {
        HashSet<String> tokenSet = new HashSet<>();
        String[] variable = new String[listItem.split(",").length];
        String type="", accessModifier="";
        String[] tokens = listItem.split(",");
        for(int i = listItem.split(",").length - 1; i >= 0; i--)
        {
            variable[i] = tokens[i];// .replaceAll("[^a-zA-Z0-9]+","");
            if(i==0)
            {
                variable[0] = tokens[0].trim().split(" ")[tokens[0].split(" ").length-1];
                type = tokens[0].trim().split(" ")[tokens[0].split(" ").length-2].trim();
                tokenSet.addAll(Arrays.asList(tokens[0].trim().split(" ")));
                if(tokenSet.contains("private"))
                    accessModifier = "private";
                else if(tokenSet.contains("public"))
                    accessModifier = "public";
                else
                    accessModifier = "public";
            }          
        }
        
        for(int i=0;i<variable.length;i++) {
            writer.print("attribute ");
            writer.print("name="+variable[i].trim()+" ");
            writer.print("type="+type+ " ");
            writer.print("visibility="+accessModifier+" ");
            writer.println();
        }
    }
    
    private void processMethod(String listItem, PrintWriter writer)
    {
        //System.out.println(listItem);
        String accessModifier, parameter[][], name, returnType;
        HashSet<String> tokenSet = new HashSet<>();
        String[] tokens = listItem.split("\\(")[0].trim().split(" ");
        tokenSet.addAll(Arrays.asList(tokens));
        if(tokenSet.contains("private"))
            accessModifier = "private";
        else if(tokenSet.contains("public"))
            accessModifier = "public";
        else
            accessModifier = "public";
        
        name = listItem.split("\\(")[0].trim().split(" ")[listItem.split("\\(")[0].trim().split(" ").length - 1];
        returnType = listItem.split("\\(")[0].trim().split(" ")[listItem.split("\\(")[0].trim().split(" ").length - 2];
        tokens = listItem.split("\\(")[1].trim().split(",");
        parameter = new String[tokens.length][2];
        
        
        writer.print("operation ");
        writer.print("name=" + name + " ");
        writer.print("returnType=" + returnType + " ");
        writer.print("visibility=" + accessModifier + " # ");
        
        for(int i=0; i<tokens.length; i++)
        {
            if(i!=0) writer.print(",");
            
            if(tokens[i].trim().split(" ").length == 2){
                parameter[i][0] = tokens[i].trim().split(" ")[0].replaceAll("[^a-zA-Z0-9_]+","");
                parameter[i][1] = tokens[i].trim().split(" ")[1].replaceAll("[^a-zA-Z0-9_]+","");
                writer.print("paramName="+parameter[i][1]+" ");
                writer.print("paramType="+parameter[i][0]+" ");
            }
        }
        
        writer.println();
    }
    
}
