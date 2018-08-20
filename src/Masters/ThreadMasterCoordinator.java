package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Extremos.SharedObject;


public class ThreadMasterCoordinator implements Runnable{
	
	ArrayList<MasterStructure> masterNeighs;
	Socket admin;
	int masterPort;
	SharedObject so;
	int port;

	public ThreadMasterCoordinator(ArrayList<MasterStructure> masterNeighs, int port, int masterPorts, Socket admin ) {
		this.masterNeighs = masterNeighs;
		this.admin = admin;
		this.masterPort = masterPorts;
		this.port=port;
		//this.so = so;
	}
	
	
	@Override
	public void run() {
		try {
			
		ObjectOutputStream serverOutput;
		
		serverOutput = new ObjectOutputStream (this.admin.getOutputStream());
		
		serverOutput.flush();
		
		ObjectInputStream MasterrInput = new ObjectInputStream (this.admin.getInputStream());
		
		Message msg =  (Message) MasterrInput.readObject();
		
		
		
		System.out.println("SERVER: "+this.masterPort+ " MSG RECv : "+msg.getHeader().toString());
		
		
		// check if list of master is empty
		// and send msg to all
		synchronized (this.masterNeighs) {
			if(!this.masterNeighs.isEmpty()) {
				
				for(MasterStructure master: this.masterNeighs) {
					if(master.getPort() != this.masterPort) {
						Socket masterToMaster = new Socket(master.getIp(),master.getMasterPort());
						
						ObjectOutputStream masterOutput = new ObjectOutputStream (masterToMaster.getOutputStream());
						masterOutput.flush();
						
						ObjectInputStream masterInput = new ObjectInputStream (masterToMaster.getInputStream());
						System.out.println("[master("+this.masterPort+")  ->master("+master.getMasterPort()+")  ] - IN/OUT Object start");
						
						
						Message msgToMaster = new Message("ADDMASTER", new MasterStructure("localhos",this.port,this.masterPort)   );
						masterOutput.writeObject(msgToMaster);
		
					}
				}
				
			}else
			{
				System.out.println(" Primer Master Online ");
			}
		
		}
		
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

		
}
