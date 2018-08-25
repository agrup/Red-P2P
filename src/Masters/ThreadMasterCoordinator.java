package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Extremos.SharedObject;


public class ThreadMasterCoordinator implements Runnable{
	
	ArrayList<MasterStructure> masterNeighs;
	Socket server;
	int masterPort;
	SharedObject so;
	int port;
	MasterStructure masterStructure;

	public ThreadMasterCoordinator(ArrayList<MasterStructure> masterNeighs, Socket server, MasterStructure master ) {
		this.masterNeighs = masterNeighs;
		this.server = server;
		this.masterStructure= master;
//		this.masterPort = masterPorts;
//		this.port=port;
		//this.so = so;
	}
	
	



	@Override
	public void run() {
		try {
			
		ObjectOutputStream serverOutput;
		
		serverOutput = new ObjectOutputStream (this.server.getOutputStream());
		
		serverOutput.flush();
		
		ObjectInputStream MasterrInput = new ObjectInputStream (this.server.getInputStream());
		
		Message msg =  (Message) MasterrInput.readObject();
		
		
		System.out.println("Master: "+this.masterStructure.getMasterPort()+ " MSG RECv : "+msg.getHeader().toString()+" from"+( (MasterStructure) msg.getBody()).getMasterPort());
		
		
		// check if list of master is empty
		// and send msg to all
		synchronized (this.masterNeighs) {

				if((msg.header).equals("ADD")) {
					for(MasterStructure master: this.masterNeighs) {
						if(master.getPort() != this.masterPort) {
							Socket masterToMaster = new Socket(master.getIp(),master.getMasterPort());
							
							ObjectOutputStream masterOutput = new ObjectOutputStream (masterToMaster.getOutputStream());
							masterOutput.flush();
							
							ObjectInputStream masterInput = new ObjectInputStream (masterToMaster.getInputStream());
							System.out.println("[master("+this.masterPort+")  ->master("+master.getMasterPort()+")  ] - IN/OUT Object start");
							
							
							Message msgToMaster = new Message("ADD", ((ExtremosStructure) msg.getBody()) );
							masterOutput.writeObject(msgToMaster);
			
						}
					}
					
				}else {
					if((msg.header).equals("ADDMASTER")) {
						this.masterNeighs.add( (MasterStructure) msg.getBody() );
						//System.out.println("Master Adding - "+((MasterStructure) msg.getBody()).getPort()+"at"+this.masterStructure.getMasterPort());
						System.out.println("Master Adding - "+((MasterStructure) msg.getBody()).getMasterPort()+" IN "+this.masterStructure.getMasterPort());
						
						Message msgToMaster = new Message("ACKMASTER", this.masterStructure  );
						
						Socket masterToMaster = new Socket( ((MasterStructure) msg.getBody()).getIp() , ((MasterStructure) msg.getBody()).getMasterPort());
						
						ObjectOutputStream masterOutput = new ObjectOutputStream (masterToMaster.getOutputStream());
						masterOutput.flush();
						
						masterOutput.writeObject(msgToMaster);
						

						
					}else {
						if((msg.header).equals("ACKMASTER")) {
							this.masterNeighs.add( (MasterStructure) msg.getBody() );
							System.out.println("Master Adding - "+((MasterStructure) msg.getBody()).getMasterPort()+"at"+this.masterStructure.getMasterPort());
							
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
