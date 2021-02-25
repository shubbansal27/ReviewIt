package org.bits.jcr.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AntiPatternDetector {
	
	private int totalOperations = 0;
	private  int totalClasses = 0;
	private  int totalAttributes = 0;
	private  int totalAssociations = 0;
	private  int totalGeneralizations = 0;
	
	public HashMap<String, Integer> getClassdata(File datFile, File relFile) throws FileNotFoundException
	{

		HashMap<String, Integer> classData = new HashMap<>();
		
		
		BufferedReader br;
		int operationCount = 0;
		Integer attributeCount = 0;
		int associationCount = 0;
		int generalizationCount = 0;
		
		br = new BufferedReader(new FileReader(datFile));
		
		String line;
		try {
			while((line = br.readLine())!=null)
			{
				if(line.substring(0, 14).equals("operation name")|| line.substring(0, 14).equals("attribute name"))
				{
					//operationCount++;
					int endIndex = 0;
					for(int i=14;i<line.length();i++)
					{
						if(line.charAt(i)==' ')
						{
							endIndex=i+1;
							break;
						}
							
					}
					
					if(line.substring(0, 14).equals("operation name"))
					{
						operationCount++;
						//String operationName = line.substring(15,endIndex);
					}
					if(line.substring(0, 14).equals("attribute name"))
					{
						attributeCount++;
						//String attributeName = line.substring(15,endIndex);
					}
					
					
				}
				
				
				
			}
			
			totalAttributes = totalAttributes + attributeCount;
			totalOperations = totalOperations + operationCount;
			classData.put("attributeCount", attributeCount);
			classData.put("operationCount", operationCount);
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		br = new BufferedReader(new FileReader(relFile));
		

		String[] generalizations = null;
		String[] associations = null;
		
		String allGeneralizations;
		try {
			allGeneralizations = br.readLine();
			String allAssociations = br.readLine();

		if(allGeneralizations.substring(15).equals(""))
		{
			classData.put("generalizationCount", 0);
		}
		else
		{
			allGeneralizations = allGeneralizations.substring(15);
			generalizations = allGeneralizations.split(",");
			generalizationCount = generalizations.length;
			totalGeneralizations = totalGeneralizations + generalizationCount;
			classData.put("generalizationCount", generalizationCount);
			
		}
		
		if(allAssociations.substring(12).equals(""))
		{
			classData.put("associationCount", 0);
		}
		else
		{
			allAssociations = allAssociations.substring(12);
			associations = allAssociations.split(",");
			associationCount = associations.length;
			classData.put("associationCount", associationCount);
			totalAssociations = totalAssociations + associationCount;
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return classData;
	}
	

	public  HashMap<String, String> getAntiPatterns(String directory)
	{
		float MIN_THRESHOLd = 0.25f;
		float MAX_THRESHOLD = 0.75f;
		HashMap<String, String> results = new HashMap<>();
		HashMap<String, Map<String, Integer>> allData = new HashMap<>();
		Path path = Paths.get(directory);
		int numberOfFiles = 0;
		try 
		{
			File datFile = null;
			File relFile = null;
			String className;
			DirectoryStream<Path> stream = Files.newDirectoryStream(path);
			for (Path p : stream)
			{
				String fileName = p.getFileName().toString();
				
				
				File file = p.toFile();
				
				int extensionSeperator = fileName.lastIndexOf('.');
				className = fileName.substring(0, extensionSeperator);
				
				String extension =fileName.substring(extensionSeperator + 1);
				
		
				if(extension.equals("dat"))
				{
					datFile = file;
				}
				
				if(extension.equals("rel"))
				{
					relFile = file;
					
					//AntiPatternDetector ap1 = new AntiPatternDetector();
					
					HashMap classData = this.getClassdata(datFile, relFile);
					allData.put(className, classData);
					
				}
				numberOfFiles++;
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		totalAssociations = totalAssociations/2;
		totalClasses = numberOfFiles/2;
		
		float averageOperations =   (float)totalOperations / totalClasses;
		float averageAssociations = (float)totalAssociations / totalClasses;
		float averageGeneralizations = (float)totalGeneralizations / totalClasses;
		float averageAttributes = (float)totalAttributes / totalClasses;
		
		
		for(Map.Entry m:allData.entrySet())
		{  
			boolean doesAntiPatternExist = false;
			String antiPatterns = "";
			
			HashMap<String, Integer> data = (HashMap<String, Integer>) m.getValue();
			
			int operationCount = data.get("operationCount");
			int attributeCount = data.get("attributeCount");
			int generalizationCount = data.get("generalizationCount");
			int associationCount = data.get("associationCount");
			
			/*boolean highOperations = operationCount > 1.3 * averageOperations;
			boolean highAttributes = attributeCount > 1.3 * averageAttributes;
			boolean highChildren = generalizationCount > 1.3 * averageGeneralizations;
			boolean highCoupling = associationCount > 1.3 * averageAssociations;
			boolean lowAttributes = attributeCount < 0.7 * averageAttributes;
			boolean lowOperations = operationCount < 0.7 * averageOperations;
			boolean lowCoupling = associationCount < 0.7 * averageAssociations;*/
			
			boolean highOperations = operationCount > MAX_THRESHOLD  * totalOperations;
			boolean highAttributes = attributeCount > MAX_THRESHOLD * totalAttributes;
			boolean highChildren = generalizationCount > MAX_THRESHOLD * averageGeneralizations; //to be changed
			boolean highCoupling = associationCount > MAX_THRESHOLD * averageAssociations; //to be changed
			boolean lowAttributes = attributeCount < MIN_THRESHOLd * totalAttributes;
			boolean lowOperations = operationCount < MIN_THRESHOLd * totalOperations;
			boolean lowCoupling = associationCount < MIN_THRESHOLd * averageAssociations; //to be changed
			
			if(highOperations && highCoupling)
			{
				doesAntiPatternExist = true;
				antiPatterns = antiPatterns + "COMPLEX CLASS,";
			}
			
			if(highAttributes)
			{
				doesAntiPatternExist = true;
				antiPatterns = antiPatterns + "LARGE CLASS,";
			}
			
			if(lowAttributes && lowOperations && lowCoupling && generalizationCount==0)
			{
				doesAntiPatternExist = true;
				antiPatterns = antiPatterns + "LAZY CLASS,";
			}
			
			if(highAttributes && lowOperations)
			{
				doesAntiPatternExist = true;
				antiPatterns = antiPatterns + "MANY FIELDS ATTRIBUTES BUT NOT COMPLEX,";
			}
			
			if(doesAntiPatternExist==false)
			{
				results.put((String) m.getKey(), "NO ANTI PATTERN");
			}
			else
			{
				results.put((String) m.getKey(), antiPatterns.substring(0, antiPatterns.length()-1));
			}
		}
		
		return results;
		
	}
}
