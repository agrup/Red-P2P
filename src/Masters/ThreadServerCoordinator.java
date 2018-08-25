package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Extremos.Query;
import Extremos.Response;

public class ThreadServerCoordinator implements Runnable  {

	ArrayList<ExtremosStructure> extremos;
	Socket server;
	ArrayList<MasterStructure> masters;
	private int count;
	int maxCount;

	public ThreadServerCoordinator(ArrayList<ExtremosStructure> extremos, Socket server, ArrayList<MasterStructure> masters) {
		this.server = server;
		this.extremos = extremos;
		this.masters = masters;
		this.count = 0;
		this.maxCount=1;
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
		

			System.err.println("mgs en tsc"+ msg.getHeader());

			
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
						this.maxCount=((Query) msg.getBody()).getMinResponse();
						ArrayList<ExtremosStructure> response = new  ArrayList<ExtremosStructure>();
						for(ExtremosStructure es: this.extremos) {
							if((es.port)!=((Query) msg.getBody()).getPort()) {
								
							
								System.out.println("Consulta a "+es.getPort());
//								System.out.println("Consulta de "+ ((Query) msg.getBody()).getPort());
//								System.out.println("Consulta es "+ ((Query) msg.getBody()).getConsulta());
								Socket Toserver = new Socket(es.getIp(),es.port);
								
								ObjectOutputStream MasterToServer = new ObjectOutputStream (Toserver.getOutputStream());
								MasterToServer.flush();
								
								MasterToServer.writeObject(new Message ("MASTERQUERY", msg.getBody()));
							}
						};
						while(this.count<this.maxCount) {
							wait();
						}
						
					}
				}else {
					if(msg.header.equals("RESPONSE")) {
						
						Response respuesta  = ((Response) msg.getBody());
						
						
						
						Socket Toserver = new Socket(((ExtremosStructure) respuesta.es).getIp(),((ExtremosStructure) respuesta.es).getPort());
						
						ObjectOutputStream serverToServer = new ObjectOutputStream (Toserver.getOutputStream());
						serverToServer.flush();
						
						serverToServer.writeObject(new Message("RESPONSE", respuesta ));
						
					}
				}
				

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
