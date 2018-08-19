package Masters;

import java.io.Serializable;

public class ExtremosStructure implements Serializable{

	String ip;
	int port;
	public ExtremosStructure(String ip, int port) {
		// TODO Auto-generated constructor stub
		// TODO Auto-generated constructor stub
		
		this.ip = ip;
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}


}
