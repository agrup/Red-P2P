package Masters;

import java.util.ArrayList;

public class MainMaster {

	
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		ArrayList<MasterStructure> listaMasters= new ArrayList<>();
		
		//Lista de nosdos Masters para agregar

		//Master IP / PORT to extremos / Port to others Masters
		listaMasters.add(new MasterStructure("localhost",9000,3000));
		listaMasters.add(new MasterStructure("localhost",9001,3001));
		listaMasters.add(new MasterStructure("localhost",9002,3002));

		
		Master master = new Master ("localhost",9000, listaMasters,3000);
		Thread masterThread = new Thread (master);
		masterThread.start();
		
		Thread.sleep(6000);
		
		Master master2 = new Master ("localhost",9001,listaMasters ,3001);
		Thread master2Thread = new Thread (master2);
		master2Thread.start();
		
		Thread.sleep(6000);
		
		Master master3 = new Master ("localhost",9002,listaMasters ,3002);
		Thread master3Thread = new Thread (master3);
		master3Thread.start();
	}

}
