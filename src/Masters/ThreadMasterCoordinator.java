package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Extremos.SharedObject;


public class ThreadMasterCoordinator implements Runnable{
	
	ArrayList<MasterStructure> neighs;
	Socket earing;
	int masterPort;
	SharedObject so;

	public ThreadMasterCoordinator(ArrayList<MasterStructure> neighs, int port, SharedObject so) {
		this.neighs = neighs;
		//this.earing = earing;
		//this.masterPort = port;
		this.so = so;
	}
	
	
	@Override
	public void run() {
		try {
			
		ObjectOutputStream serverOutput;
		
		serverOutput = new ObjectOutputStream (this.earing.getOutputStream());
		
		serverOutput.flush();
		
		ObjectInputStream serverInput = new ObjectInputStream (this.earing.getInputStream());
		
		Message msg =  (Message) serverInput.readObject();
		
		
		
		System.out.println("SERVER: "+this.masterPort+ " MSG RECv : "+msg.getHeader().toString());
		
		for(MasterStructure master: this.neighs) {
			if(master.getPort() != this.masterPort) {
				Socket masterToMaster = new Socket(master.getIp(),master.getPort());
				
				ObjectOutputStream masterOutput = new ObjectOutputStream (masterToMaster.getOutputStream());
				masterOutput.flush();
				
				ObjectInputStream masterInput = new ObjectInputStream (masterToMaster.getInputStream());
				System.out.println("[master->master] - Replicando");
				
				Message msgToMaster = new Message("ADDMASTER", (MasterStructure) msg.getBody());
				masterOutput.writeObject(msgToMaster);

			}
		}
		
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

		
}
