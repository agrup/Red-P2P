package Extremos;

import java.io.Serializable;

import Masters.ExtremosStructure;

public class Response implements Serializable{

	int id;
	String consulta;
	public ExtremosStructure es;
	
	public Response (int id,String consulta, ExtremosStructure es) {
	
	this.id=id;
	this.consulta=consulta;
	this.es =es;
	}
}
