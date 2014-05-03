/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

/**
 * Exception that signals errors while configuring D2.
 * @author Marco Antonio Gomez Martin
 */
public class ConfigurationException extends Exception {


	public ConfigurationException(String msg) {
		super(msg);
	}
	
	public ConfigurationException() {
		super();
	}
	
	static final long serialVersionUID = 0x1463743;
}
