package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Masters.ExtremosStructure;
import Masters.Message;

public class ThreadServer implements Runnable{

	Socket earing;
	int serverPort;
	String masterIp;


	public ThreadServer(String masterIp, int sendport, Socket earing) {
		this.earing = earing;
		this.serverPort=sendport;
		this.masterIp=masterIp;
	}
	
	
	@Override
	public void run() {
		
		
		try {
			
			ObjectInputStream clientInput = new ObjectInputStream (this.earing.getInputStream());
			Message msg = (Message) clientInput.readObject();
			
			
			
			
			
			Socket serverToMaster = new Socket(this.masterIp,this.serverPort);
			ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
			serverOutput.flush();
			
			serverOutput.writeObject(new Message ("QUERY", msg.getBody()));
			
			System.err.println("QUERY");
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			
	}

}
