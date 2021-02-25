package org.bits.jcr.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


 
public class MergerFiles {
 
    private String INPUT_ZIP_FILE = "C:\\Users\\kartikeya\\Documents\\NetBeansProjects\\OOAD_Java_Code_Reviewer_Phase1\\codeInput.zip";
    private String TMP_UNZIP = "C:\\Users\\kartikeya\\Documents\\NetBeansProjects\\OOAD_Java_Code_Reviewer_Phase1\\TMP_UNZIP";
    private String mergedFilePath = "C:\\Users\\kartikeya\\Documents\\NetBeansProjects\\OOAD_Java_Code_Reviewer_Phase1\\TMP_PROCESS\\merged.java";
   
    public MergerFiles(String INPUT_ZIP_FILE, String TMP_UNZIP, String MERGED_FILE){
    	this.INPUT_ZIP_FILE = INPUT_ZIP_FILE;
    	this.mergedFilePath = MERGED_FILE;
    	this.TMP_UNZIP = TMP_UNZIP;
    }
   
	public void merge() {
		
				System.out.println("in merge");
                //extract zip folder
                UnzipUtility unzipper = new UnzipUtility();
                try {
                    unzipper.unzip(INPUT_ZIP_FILE, TMP_UNZIP);
                } catch (Exception ex) {
                    // some errors occurred
                    ex.printStackTrace();
                }
                 
                
                //now process extracted folder
                //String basePath = "C:\\Users\\kartikeya\\Documents\\NetBeansProjects\\OOAD_Java_Code_Reviewer_Phase1\\codeInput";
                String basePath = TMP_UNZIP;
                Vector<File> filesList = new Vector<>();
                listAllFiles(basePath, filesList);
                for(int i=0;i<filesList.size();i++) {
                    System.out.println(filesList.get(i).getAbsolutePath());
                }  
                
                
                //merge code
                File mergedFile = new File(mergedFilePath);
                mergeFiles(filesList, mergedFile);
	}
 
        
        private  void unzipFile(String zipFile, String outputFolder) {
            byte[] buffer = new byte[1024];

     try{

    	//create output directory is not exists
    	File folder = new File(TMP_UNZIP);
    	if(!folder.exists()){
    		folder.mkdir();
    	}

    	//get the zip file content
    	ZipInputStream zis =
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();

    	while(ze!=null){

    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);

           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
    	}

        zis.closeEntry();
    	zis.close();

    	System.out.println("Done");

    }catch(IOException ex){
       ex.printStackTrace();
    }
        }

        
        
           private void listAllFiles(String dir, Vector<File> list) {

                   //store .java files
                   File file = new File(dir);
                   File files[] = file.listFiles(new FilenameFilter() {

                            @Override
                        public boolean accept(File dir, String name) {
                                    return name.toLowerCase().endsWith(".java");
                        }
                   });
                   for(int i=0;i<files.length;i++) list.add(files[i]);

                   //recursive call on sub-folders
                   File fileDir = new File(dir);
                   files = fileDir.listFiles();
                   FileFilter fileFilter = new FileFilter() {
                       public boolean accept(File file) {
                           return file.isDirectory();
                   }};
                   files = fileDir.listFiles(fileFilter);
                   for(int i=0;i<files.length;i++) listAllFiles(files[i].getAbsolutePath(), list);
           }

           
           private void mergeFiles(Vector<File> files, File mergedFile) {

                   FileWriter fstream = null;
                   BufferedWriter out = null;
                   try {
                           fstream = new FileWriter(mergedFile, true);
                            out = new BufferedWriter(fstream);
                   } catch (IOException e1) {
                           e1.printStackTrace();
                   }

                   for (File f : files) {
                           System.out.println("merging: " + f.getAbsolutePath());
                           FileInputStream fis;
                           try {
                                   fis = new FileInputStream(f);
                                   BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                                   String aLine;
                                   while ((aLine = in.readLine()) != null) {
                                           out.write(aLine);
                                           out.newLine();
                                   }

                                   in.close();
                           } catch (IOException e) {
                                   e.printStackTrace();
                           }
                   }

                   try {
                           out.close();
                   } catch (IOException e) {
                           e.printStackTrace();
                   }

	}
}
