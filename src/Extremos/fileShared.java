package Extremos;

import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class fileShared implements Serializable{
	
	String name;
	byte[] bFile; 
	
	
	public fileShared(String consulta, byte[] f) {
		this.name = consulta;
		this.bFile=f;
		
	}

	
	

}
