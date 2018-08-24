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
	public ArrayList<Query> consultas;
	int minResponse;



	public ThreadServer(ExtremosStructure es,String masterIp, int sendport, Socket earing, SharedObject so, ArrayList<Query> consultas,int minResponse) {
		this.recvport= es.getPort();
		this.earing = earing;
		this.serverPort=sendport;
		this.masterIp=masterIp;
		this.so = so;
		this.es=es;
		this.consultas = consultas;
		this.minResponse= minResponse;
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
				Query query = new Query(this.consultas.size()+1,st,(String) msg.getBody(),this.minResponse);
				synchronized (this.consultas) {
				this.consultas.add(query);}
				serverOutput.writeObject(new Message ("QUERY",query));
				

				System.err.println(this.consultas+"agregada consulta"+this.consultas.size());
				
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
							
							serverOutput.writeObject(new Message("RESPONSE",new Response(((Query) msg.getBody()).id,consulta,this.es)));
							
						}//else {System.out.println("Consulta de 2:"+consulta+"  "+fileObject.toString());}
					}
				}else {
					//}
				//}
					if(msg.getHeader().equals("RESPONSE")) {
						synchronized (this.consultas) {
							System.err.println("Respuesta"+this.consultas);
						for(Query id: this.consultas) {
							System.err.println("ids:"+((Query)id).id);
							if (id.id == ((Response) msg.getBody() ).id ) {
								System.err.println("Respuesta");
							}
								
						}
						
						}
						System.err.println("Consulatas"+this.consultas);
						System.err.println("Respuesta Nodo con consulta id:::"+ ((Response) msg.getBody() ).id+ " consulta:"+((Response) msg.getBody() ).consulta);
//				for(SharedObject so:this.so.getFiles) {
//					
				}
				}
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			
	}

}
