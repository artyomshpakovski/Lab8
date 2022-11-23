package Lab8;

import java.io.Serializable;


public class MessageCheckMailResult extends MessageResult implements

Serializable {

	private static final long serialVersionUID = 1L;

	public String[] letters = null;

	public MessageCheckMailResult( String errorMessage ) { //Error
		super( Protocol.CMD_ORDER, errorMessage );
	}

	public MessageCheckMailResult( String[] letters ) { // No errors
		super( Protocol.CMD_ORDER );
		this.letters = letters;
	}

}
