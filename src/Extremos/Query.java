package Extremos;
import java.io.Serializable;

import Masters.ExtremosStructure;


 
public class Query implements Serializable{
	public int id;
	int Extremo;
	String Consulta;
	ExtremosStructure ext;
	int minResponse;
	
	public Query(int id,ExtremosStructure es,String consulta, int min){
		this.ext = es;
		this.Consulta = consulta;
		this.id= id;
		this.minResponse = min;
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

	public int getMinResponse() {
		
		return this.minResponse;
	}
}
