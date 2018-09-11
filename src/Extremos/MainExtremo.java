package Extremos;



public class MainExtremo {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Server s = new Server ("localhost",8000, 9000,"Compartido/Server");
		Thread sThread = new Thread (s);
		sThread.start();
		
		Thread.sleep(1000);
//		
//		Server s1 = new Server ("localhost",8001, 9001,"Compartido/Server2");
//		
//		Thread s1Thread = new Thread (s1);
//		s1Thread.start();
//		
		
		Server s2 = new Server ("localhost",8002, 9002,"Compartido/Server2");
		
		Thread s2Thread = new Thread (s2);
		s2Thread.start();
		
		
		Thread.sleep(1000);
//		
		Cliente c = new Cliente ("localhost",6000, 8000,"img.jpg");
		Thread cThread = new Thread (c);
		cThread.start();

	}

}
