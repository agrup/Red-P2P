package Masters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;



public class ThreadMaster implements Runnable{

	
	ArrayList<MasterStructure> listaMasters;
	MasterStructure master;
	ServerSocket mastermaster;
	ArrayList<MasterStructure> MastersOnline;


	public ThreadMaster(ArrayList<MasterStructure> listaMasters ,ArrayList<MasterStructure> masters, MasterStructure masterStructure, ServerSocket mastermaster) 
	{
		this.listaMasters = listaMasters; 
		this.master = masterStructure;
		this.mastermaster = mastermaster;
		this.MastersOnline = masters;
	}
	
	
	
	@Override
	public void run() {
	
		// tengo que avisarle a todos los masters que hay un nuevo master
		
		for(MasterStructure master: this.listaMasters) {
			try {
				
				
				Socket socketToMaster = new Socket(master.getIp(),master.getMasterPort());
				ObjectOutputStream SMAout = new ObjectOutputStream (socketToMaster.getOutputStream());
				SMAout.flush();
				//ObjectInputStream SMAin = new ObjectInputStream (socketToMaster.getInputStream());
				int port1 = ((MasterStructure) this.master).getPort();
				int port2 =  master.getPort();

				if(port1 != port2){
				System.out.println("[mast->mast] Socket / IN / OUT OK in port: "+( (MasterStructure) this.master).port+"- to -"+master.getMasterPort());
				SMAout.writeObject(new Message("ADDMASTER", this.master));
				}
				//Message response = (Message) SMAin.readObject();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Master port "+master.getMasterPort() +" Offline");
				//e.printStackTrace();
			}
		}
		try {
			while (true) {
			
				Socket admin = mastermaster.accept();
	
				System.out.println("NEW Master send messege");
				ThreadMasterCoordinator tmc = new ThreadMasterCoordinator(this.MastersOnline,admin, this.master);
				Thread tm = new Thread (tmc);
				tm.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
