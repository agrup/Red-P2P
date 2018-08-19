package Extremos;



public class MainExtremo {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Server s = new Server (8000, 9000);
		Thread sThread = new Thread (s);
		sThread.start();
		
		Thread.sleep(1000);
		
		Server s1 = new Server (8001, 9000);
		Thread s1Thread = new Thread (s1);
		s1Thread.start();
		
		Thread.sleep(1000);
		
		Cliente c = new Cliente (6000, 8000);
		Thread cThread = new Thread (c);
		cThread.start();

	}

}
