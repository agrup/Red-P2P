package Masters;

import java.io.Serializable;

public class MasterStructure  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ip;
	int port;
	public MasterStructure(String ip, int port) {
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
