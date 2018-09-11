package Extremos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
			//System.err.println("mgs en t server:"+ msg.getHeader());
			
			
			if(msg.getHeader().equals("QUERY")) {
					
				
				
				Socket serverToMaster = new Socket(this.masterIp,this.serverPort);
				ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
				serverOutput.flush();
				ExtremosStructure st = new ExtremosStructure(this.masterIp,this.recvport);
				

				
				Query query = new Query(this.listaconsultas.size()+1,st,(String) msg.getBody(),this.minResponse);
				synchronized (this.listaconsultas) {
				this.listaconsultas.add(query);}
				serverOutput.writeObject(new Message ("QUERY",query));
				
				
				
			}else {
				
				if(msg.getHeader().equals("MASTERQUERY")) {
					boolean flag = false;
					String consulta = ((Query) msg.getBody()).Consulta;
					for(Path fileObject: this.so.files) {
						if(fileObject.toString().equals(consulta)) {
//							System.out.println("Consulta de "+ fileObject.toString()+" ");
//							System.out.println("Body "+((Query) msg.getBody()).ms.getIp());
							//Socket serverToMaster = new Socket(((Query) msg.getBody()).getIp(),this.serverPort);
							//Socket serverToMaster = new Socket (this.masterIp,this.serverPort);
							Socket serverToMaster = new Socket (((Query) msg.getBody()).ms.getIp(),((Query) msg.getBody()).ms.getPort());
							//System.err.println("Server To master"+this.serverPort);
							ObjectOutputStream serverOutput = new ObjectOutputStream (serverToMaster.getOutputStream());
							serverOutput.flush();
							Query query = (Query) msg.getBody();

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
							
							boolean flag = true;
							
							for(Query id: this.listaconsultas) {
								
								
//								System.out.println("id"+id.Consulta);
//								System.out.println("consulat2"+respuesta.getConsulta());
								
								
								if (id.Consulta.equals(respuesta.getConsulta() )) {
//									
//									System.out.println("consulat casa "+respuesta.servermatchs.size());
//									
									//System.out.println("port"   +this.es.getPort());
									
						
									for(ExtremosStructure server: respuesta.servermatchs) {
										
//										System.out.println("extremo"+server);
										
										Socket wgetToServer = new Socket (server.getIp(),server.getPort());
							
										
										
										ObjectOutputStream serverToServer = new ObjectOutputStream (wgetToServer.getOutputStream());
										serverToServer.flush();
										
										
										if(flag) {
											flag= false;
										
										serverToServer.writeObject(new Message("WGET", new Query (this.es, respuesta.getConsulta() )));
										
										
										}
										
										
									}
								}
									
							}
						
						}

					}else {
						if(msg.getHeader().equals("WGET")) {
							

							for (Path file: this.so.files) {
								
								Query message = (Query) msg.getBody();

								boolean flag = true;
								
								if(message.Consulta.equals(file.toString())) {
								
									
									
									Socket socket = new Socket (message.getIp(),message.ext.getPort());
									
									
									ObjectOutputStream serverToServer = new ObjectOutputStream (socket.getOutputStream());
									serverToServer.flush();


									ArrayList<Byte> FileToSend = new ArrayList<Byte>();
									
									
									FileInputStream fileInputStream = null;
									
									try {
										
										
										File fileBytes = new File(so.path.toString()+"/"+file.toString());
										byte[] bFile = new byte[(int) fileBytes.length()];
										
										fileInputStream = new FileInputStream(fileBytes);
							            fileInputStream.read(bFile);
										


											serverToServer.writeObject(new Message("WPUSH", new fileShared(((Query) msg.getBody()).getConsulta(),bFile)));
										
									}finally {
								      
									
									}
									
									
								}
							}
							
						}else {
							if(msg.getHeader().equals("WPUSH")) {
								
								byte[] bFile = ((fileShared) msg.getBody()).bFile;
								
								String title = ((fileShared) msg.getBody()).name;
								
								
								
								 try (FileOutputStream fileOuputStream = new FileOutputStream(so.path.toString()+"/"+title)) {
							            fileOuputStream.write(bFile);
							            this.so.refrshFiles();
							            System.out.println("New files ");
							            this.so.listFiles();
							            
							            
							        } catch (IOException e) {
							            e.printStackTrace();
							        }


								 System.out.println("Archivo "+title+" Download Complete");

								
								
							}else {
								if(msg.getHeader().equals("Listar")) {
									this.so.listFiles();
								}
							}
						}
					}
					
				}
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en Server"); 
		}
		
			
			
	}

}
