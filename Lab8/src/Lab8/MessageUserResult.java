package Lab8;

import java.io.Serializable;

public class MessageUserResult extends MessageResult implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	public String[] userNics = null;
	
	public MessageUserResult( String errorMessage )
	{ // Error
		super( Protocol.CMD_ORDER, errorMessage );
	}
	
	public MessageUserResult( String[] userNics ) 
	{ // No errors
		super( Protocol.CMD_ORDER );
		this.userNics = userNics;
	}
}