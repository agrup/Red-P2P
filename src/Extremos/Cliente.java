package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Masters.Message;



public class Cliente implements Runnable{

	String consulta;
	int portSend;
	int portListen;
	 String ip;

	public Cliente(String ip,int portListen, int portSend, String Consulta) {
		this.ip = ip;
		this.portListen = portListen;
		this.portSend= portSend;
		this.consulta= Consulta;
		
	}

	@Override
	public void run() {
		
		try {
			Socket s = new Socket ("localhost", this.portSend);
			ObjectOutputStream serverOutput = new ObjectOutputStream (s.getOutputStream());
			serverOutput.flush();
			//ObjectInputStream serverInput = new ObjectInputStream (s.getInputStream());
			System.err.println("serverQuery");
			Message msg =  new Message ("QUERY", this.consulta);
			serverOutput.writeObject(msg);
			
	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

}
