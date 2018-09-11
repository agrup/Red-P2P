package Extremos;

public class MainExtreme2 {

	public MainExtreme2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException {
		Server s1 = new Server ("localhost",8001, 9001,"Compartido/Server1");
		
		Thread s2Thread = new Thread (s1);
		s2Thread.start();
		
		Thread.sleep(1000);
//		
		Cliente c1 = new Cliente ("localhost",6001, 8001,"img.jpg");
		Thread c1Thread = new Thread (c1);
		c1Thread.start();

	}

}
