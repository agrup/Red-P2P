package Extremos;


import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SharedObject implements Serializable{

	Path path;
	ArrayList<Path> files;
	Integer publicValue;
	Integer privateValue;
	
	public SharedObject (String path) {
		this.path= Paths.get(path);
		this.files = new ArrayList<Path>();
		
         try(Stream<Path> paths = Files.walk(Paths.get(path))) {

             paths.forEach(filePath -> {
                 if (Files.isRegularFile(filePath)) {
                     try {

//                    	 System.out.println("File ADD name "+ ((filePath).getFileName()));
                    	 
                         addFilePath(filePath);
                     } catch (Exception e) {
//                    	 System.err.println("Errot on file directory");
                         e.printStackTrace();
                     }
                 }
             });
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } 
		
		
		
	}
	
	private void addFilePath(Path filePath) {
		
		this.files.add(filePath.getFileName());
		//System.out.println("Agregado archivo");

	}

	public void setpath (String path) {
		this.path = Paths.get(path);
	}
	
	
}

