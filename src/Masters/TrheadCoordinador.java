package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Extremos.Server;



public class TrheadCoordinador implements Runnable{
	ArrayList<ExtremosStructure> extremos;
	Socket server;

	public TrheadCoordinador(ArrayList<ExtremosStructure> extremos, Socket server) {
		this.extremos = extremos;
		this.server =server;
		
	}

	@Override
	public void run() {
		try {	
			System.out.println("Thread Cordinator");
			ObjectOutputStream serverOutput = new ObjectOutputStream (this.server.getOutputStream());
			serverOutput.flush();
			ObjectInputStream serverInput = new ObjectInputStream (this.server.getInputStream());
			
			Message msg =  (Message) serverInput.readObject();
			
			System.out.println("SERVERADDING - "+((ExtremosStructure) msg.getBody()).getPort());
			// ADD
			ExtremosStructure et = (ExtremosStructure) msg.getBody();
			
			synchronized (this.extremos) {
				if (!(this.extremos.isEmpty())){
					if (msg.getHeader().equals("ADD")){
						System.out.println("ADD SERVER"+msg.getBody() +"TO LIST");
						for(ExtremosStructure ext: this.extremos) {
							ExtremosStructure extFromList = ext;
							Socket serverToMaster = new Socket (ext.getIp(), ext.getPort());
							ObjectOutputStream coordOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
							coordOutput.flush();
							ObjectInputStream coordInput = new ObjectInputStream (serverToMaster.getInputStream());
							System.out.println("[COORD->SERV] - SOCKET / IN / OUT TO SERV START");
							Message msgToServer = new Message("ADD", et); 
							coordOutput.writeObject(msgToServer);
							
						}
						Message etForAddAll = new Message("ADDALL", this.extremos);
						Socket test = new Socket ("localhost", et.getPort());
						ObjectOutputStream testOutput = new ObjectOutputStream (test.getOutputStream());
						testOutput.flush();
						ObjectInputStream testInput = new ObjectInputStream (test.getInputStream());
						testOutput.writeObject(etForAddAll);
					}
					
					
				}else{
					System.out.println(" ES EL PRIMER NODO ; NO HAGO NADA ");
				}
				this.extremos.add(et);
				
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
