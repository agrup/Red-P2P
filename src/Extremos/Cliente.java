package Extremos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Masters.Message;



public class Cliente implements Runnable{

	String consulta;
	int portSend;
	int portListen;
	 String ip;

	public Cliente(String ip,int portListen, int portSend, String Consulta) {
		this.ip = ip;
		this.portListen = portListen;
		this.portSend= portSend;
		this.consulta= Consulta;
		
	}

	@Override
	public void run() {
		
//		try {
			try {
				menu();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Socket s = new Socket ("localhost", this.portSend);
//			ObjectOutputStream serverOutput = new ObjectOutputStream (s.getOutputStream());
//			serverOutput.flush();
//			//ObjectInputStream serverInput = new ObjectInputStream (s.getInputStream());
//			
//			Message msg =  new Message ("QUERY", this.consulta);
//			serverOutput.writeObject(msg);
			
//	} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// TODO Auto-generated method stub
		
	}
	
	private void menu() throws InterruptedException {	
		boolean b = true;
		while (b) {
			Thread.sleep(300);
			System.out.println("Opciones"); 
			System.out.println("1-Listar");
			System.out.println("2-Descargar");
			
			System.out.println("6-Salir");
			System.out.println("Su opción: ");
			System.out.println("");
			System.out.println("");

			System.out.println("");
			System.out.println("");
			System.out.println("");
			Scanner sc = new Scanner(System.in);
			String option = sc.nextLine();

			switch (option) {
				case "1":

					
					try {
					
					Socket sl = new Socket ("localhost", this.portSend);
					ObjectOutputStream serverOutputl = new ObjectOutputStream (sl.getOutputStream());
					serverOutputl.flush();
					//ObjectInputStream serverInput = new ObjectInputStream (s.getInputStream());
					
					Message msgl =  new Message ("Listar","listar");
					serverOutputl.writeObject(msgl);
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.err.println("Error en Listar"); 
					}
					
					
					break;
				case "2":
					try {
						
						System.out.println("Escriba su consulta");
						
						Scanner consulta = new Scanner(System.in);
						String query = consulta.nextLine();
						
						Socket s = new Socket ("localhost", this.portSend);
						ObjectOutputStream serverOutput = new ObjectOutputStream (s.getOutputStream());
						serverOutput.flush();
						//ObjectInputStream serverInput = new ObjectInputStream (s.getInputStream());
						
						Message msg =  new Message ("QUERY",query);
						serverOutput.writeObject(msg);
						
				} catch (IOException e) {
						// TODO Auto-generated catch block
					System.err.println("Error en Consulta"); 
					}
					break;

				case "6":
					b = false;						
					break;
				default: System.out.println("Opción Incorrecta");
			}				
		}			
}
	

}
