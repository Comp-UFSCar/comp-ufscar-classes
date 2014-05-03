/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

import gatech.mmpm.Action;
import gatech.mmpm.GameState;
import gatech.mmpm.IDomain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Tracer that sends the trace using a socket connection to a server/port.
 * <p>
 * The trace is cached while the game is active and send it completely
 * when endTrace() is called. Doing that, we minimize the resources
 * needed in the server (otherwise it had to have a connection open
 * per game being played).
 * 
 * @author Marco Antonio Gomez Martin
 */
public class SocketTracer extends ITracer {

	/**
	 * Constructor
	 * @param hostAndPort String with the server and port where
	 * the tracer will send the trace (host:port).
	 */
	public SocketTracer(String hostAndPort) {
		
		if (!hostAndPort.contains(":")) {
			_error = true;
			_errorMsg = "Server data format error: it should be <server>:<port>.";
		} else {
			String server = hostAndPort.substring(0, hostAndPort.indexOf(':'));
			String portStr = hostAndPort.substring(hostAndPort.indexOf(':') + 1);
			
			configure(server, Integer.parseInt(portStr));
			System.out.println("Socket tracer will send trace to '" + server + "':'" + portStr + "'.");
		}
	}
	
	public SocketTracer(String host, int port) {
		configure(host, port);
	}
	
	private void configure(String host, int port) {
		_host = host;
		_port = port;
	}
	
	private void createDecorator() {
		_outputStream = new java.io.ByteArrayOutputStream();
		_decorator = new OutputStreamTracer(_outputStream);
	}
	
	public void beginGameCycle(int number) {
		if (_error) return;
		_decorator.beginGameCycle(number);
	}
	
	public void putMetadata(java.util.Properties prop) {
		if (_error) return;
		_decorator.putMetadata(prop);
	}
	
	public void beginTrace() {
		if (_error) return;
		createDecorator();
		_decorator.beginTrace();
		
	}

	public void endGameCycle() {
		if (_error) return;
		_decorator.endGameCycle();
		
	}

	public void endTrace(IDomain idomain, String winner) {
		if (_error) return;
		_decorator.endTrace(idomain, winner);
		
		// Send it over the network...
		Socket s = null;
		PrintWriter out = null;

		try {
			s = new Socket(_host, _port);
			out = new PrintWriter(s.getOutputStream());
			out.print(_outputStream.toString());
			out.close();
			s.close();
		} catch (IOException ex) {
			_error = true;
			_errorMsg = "Error sending the trace over the network:";
			_errorMsg += ex.getMessage();
			_errorMsg += "\n" + ex.getStackTrace();
			return;
		}
	}

	public String getErrorMessage() {
		if (_error) return _errorMsg;
		
		return _decorator.getErrorMessage();
	}

	public void putAction(Action a) {
		if (_error) return;
		_decorator.putAction(a);
	}

	public void putAbortedAction(Action a) {
		if (_error) return;
		_decorator.putAbortedAction(a);
	}
	
	public void putGameStateInternal(GameState gs) {
		if (_error) return;
		_decorator.putGameStateInternal(gs);
	}

	public boolean success() {
		return !_error && ((_decorator == null) || _decorator.success());
	}
	
	OutputStreamTracer _decorator;
	ByteArrayOutputStream _outputStream;
	
	String _host;
	int _port;
	boolean _error;
	String _errorMsg;
}
