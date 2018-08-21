package Masters;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master implements Runnable{
	int port;
	int portMasters;
	ArrayList<MasterStructure> masters;
	ArrayList<ExtremosStructure> extremos;
	ArrayList<MasterStructure> mastersKnow;
	String ip;
	
public Master(String ip,int port ,ArrayList<MasterStructure> listaMasters, int portMasters) {
	 this.port = port;
	 this.portMasters= portMasters;
	 this.extremos= new ArrayList<ExtremosStructure>();
	 this.mastersKnow  = listaMasters;
	 this.masters = new ArrayList<MasterStructure>();
	 this.ip = ip;
	// this.masterStructure = new MasterStructure(this.ip,this.port,this.portMasters);
	 
}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			

			//open server soket to others masters
			ServerSocket mastermaster = new ServerSocket (this.portMasters);
			System.out.println("Master Online to Masters"+this.portMasters);
			
			
			MasterStructure masterst = new MasterStructure(this.ip,this.port,this.portMasters);
			
			
			//aviso a todos los masters que estoy online
			ThreadMaster Tmaster = new ThreadMaster(this.mastersKnow,masterst,mastermaster);
			Thread TmasterThread = new Thread(Tmaster);
			TmasterThread.start();
			
			
//			TokenArThread token = new TokenArThread(this.extremos);
//			Thread tokenThread = new Thread (token);
//			tokenThread.start();
			
			
			//open socket server to extremos
			
			ServerSocket master = new ServerSocket (this.port);
			System.out.println("Master Online To servers"+this.port);
			
			while (true){
				
				Socket server = master.accept();
				System.out.println("NEW SERVER RECEIVED");

				ThreadMasterCoordinator tmc = new ThreadMasterCoordinator(this.masters,server,masterst);
				Thread tm = new Thread (tmc);
				tm.start();
				
//				ThreadCordinador tc = new ThreadCordinador (this.extremos, server);
//				Thread tcThread = new Thread (tc);
//				tcThread.start();
//				
//				Socket admin = mastermaster.accept();
//				System.out.println("NEW Master send messege");
				
//				ThreadMasterCoordinator tmc = new ThreadMasterCoordinator(this.masters,admin);
//				Thread tm = new Thread (tmc);
//				tm.start();
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
