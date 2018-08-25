package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.ArrayList;

import Masters.ExtremosStructure;
import Masters.MasterStructure;
import Masters.Message;


public class Server implements Runnable{
	int port;
	ArrayList<ExtremosStructure> neighs;
	SharedObject so;
	int masterPort;
	String masterIp;
	String ip;
	ArrayList<Query> consultas;
	private int minResponse;
	
	
	
	public Server(String ip,int portReciv, int portSend, String path ) {
		this.port = portReciv;
		this.masterIp="localhost";
		this.so = new SharedObject(path);
		this.masterPort = portSend;
		this.ip = ip;
		this.consultas = new ArrayList<Query>();
		this.minResponse=2;
		
	}
	
	public void setSO (String path) {
		 this.so.setpath(path);;
	}
	
	@Override
	public void run() {
		try {
			
			
			//Shared Object read a folder and contain a arrayList of string with name off file 
			
			
			
			ServerSocket server = new ServerSocket (this.port);
			
			Socket serverToMaster = new Socket("localhost", this.masterPort);
			
			ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
			serverOutput.flush();
			
			ObjectInputStream serverInput = new ObjectInputStream (serverToMaster.getInputStream());
			System.out.println("[SERV->Master] Socket / IN / OUT OK in port: "+this.masterPort);
			serverOutput.writeObject(new Message ("ADD", new ExtremosStructure("localhost",this.port)));
			
			while (true){
				Socket earing = server.accept();
				ExtremosStructure es = new ExtremosStructure(this.ip,this.port);
				MasterStructure ms = new MasterStructure(this.masterIp,this.masterPort);
				ThreadServer ts = new ThreadServer (es,this.masterIp,this.masterPort,earing,this.so,this.consultas,this.minResponse);
				Thread tsThread = new Thread (ts);
				tsThread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
