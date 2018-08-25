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
	ArrayList<Response> respoonses;
	MasterStructure ms;


	public ThreadServerCoordinator(ArrayList<Response> responses ,ArrayList<ExtremosStructure> extremos, Socket server, ArrayList<MasterStructure> masters, MasterStructure ms) {
		this.server = server;
		this.extremos = extremos;
		this.masters = masters;
		this.count = 0;
		this.maxCount=1;
		this.respoonses=responses;
		this.ms = ms;
		
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
					synchronized (this.respoonses){
						Response responseadd = new Response(((Query) msg.getBody()).id,((Query) msg.getBody()).getConsulta(),((Query) msg.getBody()).getExtremoSt(),((Query) msg.getBody()).getMinResponse());
						this.respoonses.add(responseadd);
						System.err.println("responsessss"+responseadd.getId() +" size"+this.respoonses.size());
						
					}

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
								
								Query masterquery = new Query(((Query) msg.getBody()).getMinResponse(),this.ms,((Query) msg.getBody()).getConsulta(),((Query) msg.getBody()).getMinResponse());
								MasterToServer.writeObject(new Message ("MASTERQUERY", masterquery));
							}
						}
//						synchronized (this.respoonses){
//							System.out.println("salio del wait 0 "+((Query) msg.getBody()).getMinResponse()+" + "+this.respoonses.size());
//							while(this.respoonses.size()-1<=((Query) msg.getBody()).getMinResponse()) {
//								System.err.println("salio del wait 1"+this.respoonses.size());
//								wait(1000);
//								System.out.println("salio del wait 2"+this.maxCount);
//							}
//							
//						}
						
					
						
					}
				}else {
					if(msg.header.equals("RESPONSE")) { 
						
						Response respuesta  = ((Response) msg.getBody());
//						
//						this.count += 1;
//						respuesta.servermatchs.add();
						System.err.println("responses"+respuesta.getId() +" size"+this.respoonses.size());
						for (Response resp: this.respoonses) {

							//if(resp.getId() == respuesta.getId()&& resp.getConsulta().equals(respuesta.getConsulta()) ){
							if(resp.getConsulta().equals(respuesta.getConsulta()) ){	
								resp.addMatch(respuesta.es);
								//if(respuesta.servermatchs)
								System.err.println("salio del wait 1"+resp.minsServerMatch+" --- "+((Response) msg.getBody()).minsServerMatch);
								if(true) {
									
									Socket Toserver = new Socket(((ExtremosStructure) resp.getExtremo()).getIp(),((ExtremosStructure) resp.getExtremo()).getPort());
									
								
									
									//Socket Toserver = new Socket(((ExtremosStructure) respuesta.es).getIp(),((ExtremosStructure) respuesta.es).getPort());
									
									ObjectOutputStream serverToServer = new ObjectOutputStream (Toserver.getOutputStream());
									serverToServer.flush();
									
									
									
									serverToServer.writeObject(new Message("MASTERRESPONSE", respuesta ));
								}
							}
						}
//						notifyAll();
//						System.err.println("salio del wait 1"+this.resp+" --- "+((Response) msg.getBody()).minsServerMatch);
//						if(this.respoonses.size()-1<=((Response) msg.getBody()).minsServerMatch) {
//							
//						
//							
//							Socket Toserver = new Socket(((ExtremosStructure) respuesta.es).getIp(),((ExtremosStructure) respuesta.es).getPort());
//							
//							ObjectOutputStream serverToServer = new ObjectOutputStream (Toserver.getOutputStream());
//							serverToServer.flush();
//							
//							
//							
//							serverToServer.writeObject(new Message("MASTERRESPONSE", respuesta ));
//						}
					}
				}
				

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
