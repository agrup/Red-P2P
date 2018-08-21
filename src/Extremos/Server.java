package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Masters.ExtremosStructure;
import Masters.Message;


public class Server implements Runnable{
	int port;
	ArrayList<ExtremosStructure> neighs;
	SharedObject so;
	int masterPort; 
	public Server(int portReciv, int portSend ) {
		this.port = portReciv;
		
		this.so = new SharedObject();
		this.masterPort = portSend;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket (this.port);
			
			Socket serverToMaster = new Socket("localhost", this.masterPort);
			
			ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
			serverOutput.flush();
			
			ObjectInputStream serverInput = new ObjectInputStream (serverToMaster.getInputStream());
			System.out.println("[SERV->Master] Socket / IN / OUT OK in port: "+this.masterPort);
			serverOutput.writeObject(new Message ("ADD", new ExtremosStructure("localhost",this.port)));
			
			while (true){
				Socket earing = server.accept();
//				ThreadServer ts = new ThreadServer (this.neighs, earing, this.port, this.so);
//				Thread tsThread = new Thread (ts);
//				tsThread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
