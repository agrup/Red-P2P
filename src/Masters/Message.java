package Masters;

import java.io.Serializable;

public class Message implements Serializable{
	String header;
	Object body;
	
	public Message (String header, Object body){
		this.header = header;
		this.body = body;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
}
