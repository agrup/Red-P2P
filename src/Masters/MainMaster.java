package Masters;

import java.util.ArrayList;

public class MainMaster {

	static ArrayList<MasterStructure> listaMasters;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//Lista de nosdos Masters para agregar

		
		listaMasters.add(new MasterStructure("localhost",3000));
		listaMasters.add(new MasterStructure("localhost",3001));

		
		Master master = new Master (9000, listaMasters,3000);
		Thread masterThread = new Thread (master);
		masterThread.start();
		
		Master master2 = new Master (9001,listaMasters ,3001);
		Thread master2Thread = new Thread (master2);
		master2Thread.start();
	}

}
