package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;

import Masters.ExtremosStructure;
import Masters.MasterStructure;
import Masters.Message;

public class ThreadServer implements Runnable{

	Socket earing;
	int serverPort;
	String masterIp;
	int recvport;
	SharedObject so;
	ExtremosStructure es;
	ArrayList<Query> consultas;


	public ThreadServer(ExtremosStructure es,String masterIp, int sendport, Socket earing, SharedObject so, ArrayList<Query> consultas) {
		this.recvport= es.getPort();
		this.earing = earing;
		this.serverPort=sendport;
		this.masterIp=masterIp;
		this.so = so;
		this.es=es;
		this.consultas = consultas;
	}
	
	



	@Override
	public void run() {
		
		
		try {
			
			ObjectInputStream clientInput = new ObjectInputStream (this.earing.getInputStream());
			Message msg = (Message) clientInput.readObject();
			
			
			
			if(msg.getHeader().equals("QUERY")) {
					
				
				
				Socket serverToMaster = new Socket(this.masterIp,this.serverPort);
				ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
				serverOutput.flush();
				ExtremosStructure st = new ExtremosStructure(this.masterIp,this.recvport);
				Query query = new Query(this.consultas.size(),st,(String) msg.getBody());
				
				serverOutput.writeObject(new Message ("QUERY",query));
				
				
				
			}else {
				
				if(msg.getHeader().equals("MASTERQUERY")) {
					boolean flag = false;
					String consulta = ((Query) msg.getBody()).Consulta;
					for(Path fileObject: this.so.files) {
						if(fileObject.toString().equals(consulta)) {
							System.out.println("Consulta de"+ fileObject.toString());
							
							//Socket serverToMaster = new Socket(((Query) msg.getBody()).getIp(),this.serverPort);
							Socket serverToMaster = new Socket (this.masterIp,this.serverPort);
							ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
							serverOutput.flush();
							
							serverOutput.writeObject(new Message("RESPONSE",new Response(1,consulta,this.es)));
							
						}else {System.out.println("Consulta de 2:"+consulta+"  "+fileObject.toString());}
					}
				}else {
					}
				//}
					if(msg.getHeader().equals("RESPONSE")) {
						for(Query id: this.consultas) {
							if (id.id == ((Response) msg.getBody() ).id ) {
								
							}
								
						}
						
						
						System.err.println("Respuesta Nodo con consulta id:"+ ((Response) msg.getBody() ).id+ " consulta:"+((Response) msg.getBody() ).consulta);
//				for(SharedObject so:this.so.getFiles) {
//					
				}
				
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			
	}

}
