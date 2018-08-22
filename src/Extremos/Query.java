package Extremos;
import java.io.Serializable;

import Masters.ExtremosStructure;


 
public class Query implements Serializable{
	
	int Extremo;
	String Consulta;
	
	public Query(int es,String consulta){
		this.Extremo = es;
		this.Consulta = consulta;
	}
	
	public int getPort() {
		return this.Extremo;
	}
}
