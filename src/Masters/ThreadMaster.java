package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;



public class ThreadMaster implements Runnable{

	
	ArrayList<MasterStructure> listaMasters;



	public ThreadMaster(ArrayList<MasterStructure> listaMasters) 
	{
		this.listaMasters = listaMasters;
	}
	
	
	
	@Override
	public void run() {
	
		// tengo que avisarle a todos los masters que hay un nuevo master
		
		for(MasterStructure master: this.listaMasters) {
			try {
				Socket socketToMaster = new Socket(master.getIp(),master.getMasterPort());
				ObjectOutputStream SMAout = new ObjectOutputStream (socketToMaster.getOutputStream());
				SMAout.flush();
				ObjectInputStream SMAin = new ObjectInputStream (socketToMaster.getInputStream());
				
				SMAout.writeObject(new Message("ADDMASTER", null));
				
				Message response = (Message) SMAin.readObject();
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Master port "+master.getMasterPort() +"Offline");
				e.printStackTrace();
			}
		}
		
		
	}


}
