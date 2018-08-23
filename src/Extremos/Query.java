package Extremos;
import java.io.Serializable;

import Masters.ExtremosStructure;


 
public class Query implements Serializable{
	public int id;
	int Extremo;
	String Consulta;
	ExtremosStructure ext;
	
	public Query(int id,ExtremosStructure es,String consulta){
		this.ext = es;
		this.Consulta = consulta;
		this.id= id;
		
	}
	
	public ExtremosStructure getExtremoSt() {
		return this.ext;
	}
	public String getConsulta() {
		return this.Consulta;
	}
	public int getPort() {
		return this.ext.getPort();
	}
	public String getIp() {
		return this.ext.getIp();
	}
}
