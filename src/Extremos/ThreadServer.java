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
	public ArrayList<Query> listaconsultas;
	int minResponse;



	public ThreadServer(ExtremosStructure es,String masterIp, int sendport, Socket earing, SharedObject so, ArrayList<Query> consultas,int minResponse) {
		this.recvport= es.getPort();
		this.earing = earing;
		this.serverPort=sendport;
		this.masterIp=masterIp;
		this.so = so;
		this.es=es;
		this.listaconsultas = consultas;
		this.minResponse= minResponse;
	}
	
	



	@Override
	public void run() {
		
		
		try {
			
			ObjectInputStream clientInput = new ObjectInputStream (this.earing.getInputStream());
			Message msg = (Message) clientInput.readObject();
			System.err.println("mgs en t server:"+ msg.getHeader());
			
			
			if(msg.getHeader().equals("QUERY")) {
					
				
				
				Socket serverToMaster = new Socket(this.masterIp,this.serverPort);
				ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
				serverOutput.flush();
				ExtremosStructure st = new ExtremosStructure(this.masterIp,this.recvport);
				Query query = new Query(this.listaconsultas.size()+1,st,(String) msg.getBody(),this.minResponse);
				synchronized (this.listaconsultas) {
				this.listaconsultas.add(query);}
				serverOutput.writeObject(new Message ("QUERY",query));
				
				System.err.println("mgs en thread server:"+ msg.getHeader());
				
			}else {
				
				if(msg.getHeader().equals("MASTERQUERY")) {
					boolean flag = false;
					String consulta = ((Query) msg.getBody()).Consulta;
					for(Path fileObject: this.so.files) {
						if(fileObject.toString().equals(consulta)) {
							System.out.println("Consulta de "+ fileObject.toString()+" ");
							System.out.println("Body "+((Query) msg.getBody()).ms.getIp());
							//Socket serverToMaster = new Socket(((Query) msg.getBody()).getIp(),this.serverPort);
							//Socket serverToMaster = new Socket (this.masterIp,this.serverPort);
							Socket serverToMaster = new Socket (((Query) msg.getBody()).ms.getIp(),((Query) msg.getBody()).ms.getPort());
							System.err.println("Server To master"+this.serverPort);
							ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
							serverOutput.flush();
							Query query = (Query) msg.getBody();
							serverOutput.writeObject(new Message("RESPONSE",new Response(((Query) msg.getBody()).id,consulta,query.getExtremoSt(),((Query) msg.getBody()).minResponse)));
							
						}//else {System.out.println("Consulta de 2:"+consulta+"  "+fileObject.toString());}
					}
				}else {
					//}
				//}
					if(msg.getHeader().equals("MASTERRESPONSE")) {
						synchronized (this.listaconsultas) {
//							System.err.println("Server response"+this.serverPort);
							System.err.println("Respuesta"+this.listaconsultas.size());
						for(Query id: this.listaconsultas) {
							System.out.println("id"+id.Consulta);
							System.out.println("id"+id.Extremo);
							if (id.Consulta.equals(((Response) msg.getBody() ).getConsulta() )) {
								System.err.println("Respuesta");
							}
								
						}
						
						}
						//System.err.println("Consulatas"+this.listaconsultas);
						//System.err.println("Respuesta Nodo con consulta id:::"+ ((Response) msg.getBody() ).id+ " consulta:"+((Response) msg.getBody() ).consulta);
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
