package Masters;

import java.io.Serializable;

public class MasterStructure  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ip;
	int port;
	int masterPort;
	public MasterStructure(String ip, int port, int masterPort) {
		// TODO Auto-generated constructor stub
		this.masterPort=masterPort;
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
	public int getMasterPort() {
		return masterPort;
	}



}
