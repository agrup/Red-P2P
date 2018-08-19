package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Masters.ExtremosStructure;
import Masters.Message;

public class ThreadServer implements Runnable{
	ArrayList<ExtremosStructure> neighs;
	Socket earing;
	int serverPort;
	SharedObject so;

	public ThreadServer(ArrayList<ExtremosStructure> neighs, Socket earing, int port, SharedObject so) {
		this.neighs = neighs;
		this.earing = earing;
		this.serverPort = port;
		this.so = so;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

			try {
				ObjectOutputStream serverOutput = new ObjectOutputStream (this.earing.getOutputStream());
				serverOutput.flush();
				ObjectInputStream serverInput = new ObjectInputStream (this.earing.getInputStream());
				
				Message msg =  (Message) serverInput.readObject();
				
				System.out.println("SERVER: "+this.serverPort+ " MSG RECv : "+msg.getHeader().toString());
				
				if(msg.getHeader().equals("SETCLIDATA")) {
					synchronized (this.so) {
						this.so.privateValue= 1234;
						System.err.println("MSG DEL CLIENTE ");
					}
				}else {
					if (msg.getHeader().equals("ADD")){
						// body IP : PUERTO
						ExtremosStructure et = (ExtremosStructure) msg.getBody();
						synchronized (this.neighs) {
							System.out.println("ADD|"+et.getPort());
							this.neighs.add(et);
						}
						// ADDALL y ADD
					}else{
						if (msg.getHeader().equals("MIRROR")){
							Integer pval = (Integer) msg.getBody();
							synchronized (this.so) {
								this.so.publicValue = pval;
								
							}
							System.err.println("VALOR REPLICADO EN SERVER: "+this.serverPort);
							}else {
								if (msg.getHeader().equals("TOKEN")){
									System.out.println("SERVER: "+this.serverPort+" / TOKEN HAS RECEIVED");
									Thread.sleep(5000);
									synchronized (this.so) {
										if (this.so.privateValue!=null) {
											System.err.println(" ES MI TURNO ");

											for (int i = 0 ; i< this.neighs.size(); i++) {
												ExtremosStructure st = this.neighs.get(i);
												
												Socket coordToServer = new Socket (st.getIp(), st.getPort());
												
												ObjectOutputStream coordOutput = new ObjectOutputStream (coordToServer.getOutputStream());
												coordOutput.flush();
												ObjectInputStream coordInput = new ObjectInputStream (coordToServer.getInputStream());
										
												Message msgToServer = new Message("MIRROR", this.so.privateValue); 
												coordOutput.writeObject(msgToServer);
												// ACAVENDRIA LA RESPUesTA
												System.out.println("enviando MIRROR: "+i);
												Thread.sleep(1000);
												
											}
											this.so.publicValue = this.so.privateValue;
										}
									}
									
									serverOutput.writeObject(new Message("nada", null));
									}else {
										System.out.println("ADDALL");
										ArrayList<ExtremosStructure> alEt = (ArrayList<ExtremosStructure>) msg.getBody();  
										
										synchronized (this.neighs) {
											for (ExtremosStructure ext: alEt){
												System.out.println("ADDALL|"+ext.getPort());
												this.neighs.add(ext);
											}
										}
										}
								}
						}
					}
				
				
				
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}

}
