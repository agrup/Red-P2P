package Extremos;

import java.io.BufferedReader;
import java.io.FileReader;
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
				
				System.out.println("------------------------------------------"+st.getIp());
				
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
							System.out.println("quer -mmS  "+query.ms.getIp());
							serverOutput.writeObject(new Message("RESPONSE",new Response(((Query) msg.getBody()).id,consulta,this.es)));
							
						}//else {System.out.println("Consulta de 2:"+consulta+"  "+fileObject.toString());}
					}
				}else {
					//}
				//}
					if(msg.getHeader().equals("MASTERRESPONSE")) {
						synchronized (this.listaconsultas) {
							Response respuesta = (Response) msg.getBody();
//							System.err.println("Server response"+this.serverPort);
							
							
							
							for(Query id: this.listaconsultas) {
								
								
								System.out.println("id"+id.Consulta);
								System.out.println("consulat2"+respuesta.getConsulta());
								
								
								if (id.Consulta.equals(respuesta.getConsulta() )) {
									
									System.out.println("consulat casa "+respuesta.servermatchs.size());
									
									System.out.println(respuesta.servermatchs.get(0));
						
									for(ExtremosStructure server: respuesta.servermatchs) {
										
										System.out.println("extremo"+server);
										
										Socket wgetToServer = new Socket (server.getIp(),server.getPort());
							
										
										
										ObjectOutputStream serverToServer = new ObjectOutputStream (wgetToServer.getOutputStream());
										serverToServer.flush();
										
										
										
										serverToServer.writeObject(new Message("WGET", new Query (this.es, respuesta.getConsulta() )));
										
										
										
										
										
									}
								}
									
							}
						
						}
						//System.err.println("Consulatas"+this.listaconsultas);
						//System.err.println("Respuesta Nodo con consulta id:::"+ ((Response) msg.getBody() ).id+ " consulta:"+((Response) msg.getBody() ).consulta);
//				for(SharedObject so:this.so.getFiles) {
//					
					}else {
						if(msg.getHeader().equals("WGET")) {
							for (Path file: this.so.files) {
								Query message = (Query) msg.getBody();
							//	if(message.Consulta.equals(file)) {
									System.out.println("File find");
									
									ObjectOutputStream serverToServer = new ObjectOutputStream (this.earing.getOutputStream());
									serverToServer.flush();
									System.out.println("File find");
								      String cadena;
								      FileReader f = new FileReader(message.getConsulta());
								      BufferedReader b = new BufferedReader(f);
								      while((cadena = b.readLine())!=null) {
								          System.out.println(cadena);
								      }
								      b.close();
									
									//serverToServer.writeObject(new Message("WPUSH",  ));
									
									
									
									
								//}
							}
							
						}
					}
					
				}
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			
	}

}
