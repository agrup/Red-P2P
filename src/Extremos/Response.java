package Extremos;

import java.io.Serializable;
import java.util.ArrayList;

import Masters.ExtremosStructure;

public class Response implements Serializable{

	private int id;
	private String consulta;
	public ExtremosStructure es;
	public ArrayList<ExtremosStructure> servermatchs;
	public int minsServerMatch;
	
	public Response (int id,String consulta, ExtremosStructure es, int min) {
	
	this.setId(id);
	this.setConsulta(consulta);
	this.es =es;
	this.servermatchs = new ArrayList<ExtremosStructure>();
	this.minsServerMatch = min;
	}

	public ExtremosStructure getExtremo() {
		return this.es;
	}
	
	public void addMatch(ExtremosStructure extremo ) {
		this.servermatchs.add(extremo);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

}
