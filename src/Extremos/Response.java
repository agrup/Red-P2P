package Extremos;

import java.io.Serializable;
import java.util.ArrayList;

import Masters.ExtremosStructure;

public class Response implements Serializable{

	int id;
	String consulta;
	public ExtremosStructure es;
	public ArrayList<ExtremosStructure> servermatchs;
	
	public Response (int id,String consulta, ExtremosStructure es) {
	
	this.id=id;
	this.consulta=consulta;
	this.es =es;
	this.servermatchs = new ArrayList<ExtremosStructure>();
	}
}
