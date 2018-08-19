package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;



public class TokenArThread implements Runnable{
	
	ArrayList<ExtremosStructure> extremos;
	
	public TokenArThread(ArrayList<ExtremosStructure> extremos) {
		this.extremos = extremos;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(this.extremos.isEmpty()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println(" Ya hay nodos para pasar el token ");
		
		int counter =0;
		while(true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			synchronized (this.extremos) {
				try {
					ExtremosStructure st = this.extremos.get(counter);
					Socket tokenAssignation = new Socket(st.getIp(), st.getPort());
					
					ObjectOutputStream tAout = new ObjectOutputStream (tokenAssignation.getOutputStream());
					tAout.flush();
					ObjectInputStream tAin = new ObjectInputStream (tokenAssignation.getInputStream());
					
					tAout.writeObject(new Message("TOKEN", null));
					
					Message response = (Message) tAin.readObject();
					
					counter++;
					if (counter>=this.extremos.size()) counter=0;
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		}
		
	}

}
