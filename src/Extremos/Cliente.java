package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Masters.Message;



public class Cliente implements Runnable{

	public Cliente(int i, int j) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		try {
			Socket s = new Socket ("localhost", 8000);
			ObjectOutputStream serverOutput = new ObjectOutputStream (s.getOutputStream());
			serverOutput.flush();
			ObjectInputStream serverInput = new ObjectInputStream (s.getInputStream());
			
			Message msg =  new Message ("QUERY", 1234);
			serverOutput.writeObject(msg);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

}
