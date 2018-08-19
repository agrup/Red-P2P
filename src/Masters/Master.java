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
	
public Master(int port ,ArrayList<MasterStructure> listaMasters, int portMasters) {
	 this.port = port;
	 this.portMasters= portMasters;
	 this.extremos= new ArrayList<ExtremosStructure>();
	 this.masters  = listaMasters;
	 
}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			

			
			ServerSocket mastermaster = new ServerSocket (this.portMasters);
			System.out.println("Master Online to Masters"+this.portMasters);
			
			
			
			TokenArThread token = new TokenArThread(this.extremos);
			Thread tokenThread = new Thread (token);
			tokenThread.start();
			
			
			
			ServerSocket master = new ServerSocket (this.port);
			System.out.println("Master Online To servers"+this.port);
			
			while (true){
				Socket server = master.accept();
				System.out.println("NEW SERVER RECEIVED");
				ThreadCordinador tc = new ThreadCordinador (this.extremos, server);
				Thread tcThread = new Thread (tc);
				tcThread.start();
				
				Socket admin = mastermaster.accept();
				ThreadMasterCoordinator tmc = new ThreadMasterCoordinator(this.masters, this.port, null);
				Thread tm = new Thread (tmc);
				tm.start();
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
