package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadServerCoordinator implements Runnable  {

	ArrayList<ExtremosStructure> extremos;
	Socket server;
	ArrayList<MasterStructure> masters;

	public ThreadServerCoordinator(ArrayList<ExtremosStructure> extremos, Socket server, ArrayList<MasterStructure> masters) {
		this.server = server;
		this.extremos = extremos;
		this.masters = masters;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		ObjectOutputStream serverOutput;
		serverOutput = new ObjectOutputStream (this.server.getOutputStream());
		serverOutput.flush();
		ObjectInputStream MasterrInput = new ObjectInputStream (this.server.getInputStream());
		Message msg =  (Message) MasterrInput.readObject();
		

			System.err.println(msg.getHeader());

			
				if((msg.header).equals("ADD")) {
					synchronized (this.masters){
						for(MasterStructure master:masters) {
							Socket socketToMaster = new Socket(master.getIp(),master.getPort());
							System.err.println("ADD"+master.getMasterPort());
							ObjectOutputStream SMAout = new ObjectOutputStream (socketToMaster.getOutputStream());
							SMAout.flush();
							SMAout.writeObject(new Message("ADDREMOTE", (ExtremosStructure) msg.body));
							
						}
					}
					synchronized (this.extremos){
						this.extremos.add((ExtremosStructure) msg.getBody());
					}
				}else {
					if((msg.header).equals("ADDREMOTE")) {
							synchronized (this.extremos){
								this.extremos.add((ExtremosStructure) msg.getBody());
							}
						System.err.println("Remote Add Succes: "+((ExtremosStructure) msg.getBody()).getPort());
					}
					
				}
				
				if((msg.header).equals("QUERY")) {
					synchronized (this.extremos){
						for(ExtremosStructure es: this.extremos) {
							System.out.println("Consulta a "+es.getPort());
							
						}
					}
				}
				

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}