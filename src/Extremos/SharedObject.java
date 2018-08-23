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
		 System.out.println("In listAllfiles(String path) method");
         try(Stream<Path> paths = Files.walk(Paths.get(path))) {

             paths.forEach(filePath -> {
                 if (Files.isRegularFile(filePath)) {
                     try {
                    	 System.out.println("Archivo  path"+ (this.path));
                    	 System.out.println("Archivo filepath name "+ ((filePath).getFileName()));
                    	 
                         getFilePath(filePath);
                     } catch (Exception e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                 }
             });
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } 
		
		
		
	}
	
	private void getFilePath(Path filePath) {
		
		this.files.add(filePath.getFileName());
		System.out.println("Agregado archivo");
		//System.out.println("Consulta deeee "+ filePath.relativize(this.path));
//	    Files.walk(filePath)
//        .filter(path -> path.toFile().isFile())
//        .forEach(path -> System.out.println(filePath.relativize(path)));	
//		
	}

	public void setpath (String path) {
		this.path = Paths.get(path);
	}
	
	
}

