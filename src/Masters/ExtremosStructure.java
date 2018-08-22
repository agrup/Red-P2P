package Masters;

import java.io.Serializable;

public class ExtremosStructure implements Serializable{

	String ip;
	int port;
	int recvPort;
	public ExtremosStructure(String ip, int port) {
		this.ip = ip;
		this.port = port;
		//this.recvPort= recvPort;
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
