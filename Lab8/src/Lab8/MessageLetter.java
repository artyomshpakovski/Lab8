package Lab8;

import java.io.Serializable;


public class MessageLetter extends Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String ord;
	public String address;
	
	public MessageLetter( String ord, String txt ) {
		
		super( Protocol.CMD_ORDER );
		this.ord = ord;
		this.address = txt;
	}
	
}