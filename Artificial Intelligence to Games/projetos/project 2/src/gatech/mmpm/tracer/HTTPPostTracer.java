/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

import gatech.mmpm.Action;
import gatech.mmpm.GameState;
import gatech.mmpm.IDomain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;


/**
 * Tracer that sends the trace using a HTTP Post request to the
 * server. It adds a parameter "user" to the request that
 * indicates who creates the trace (the user is taken from the
 * trace metadata), and the parameter "trace" with the XML file.
 * <p>
 * The trace is cached while the game is active and send it completely
 * when endTrace() is called. Doing that, we only make a
 * HTTP request, and the server does not have to store temporary
 * data waiting the rest of the trace.
 * 
 * @author Marco Antonio Gomez Martin
 **/
public class HTTPPostTracer extends ITracer {
	
	/**
	 * Constructor of the class
	 * @param url Url where the class will post the HTTP request.
	 */
	public HTTPPostTracer(String url) throws java.io.IOException {
		_url = new URL(url);
	}
	
	protected void createDecorator() {
		_outputStream = new java.io.ByteArrayOutputStream();
		_decorator = new OutputStreamTracer(_outputStream);
	}

	protected byte[] getTraceToSend() throws java.io.IOException {
		return _outputStream.toByteArray();
	}
	
	public void beginGameCycle(int number) {
		if (_error) return;
		_decorator.beginGameCycle(number);
	}
	
	public void putMetadata(java.util.Properties prop) {
		if (_error) return;
		_decorator.putMetadata(prop);
		// If the metadata has user, we keep it.
		if (prop.containsKey("user"))
			_user = prop.getProperty("user");
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

	public void endTrace(IDomain idomain, String winningPlayer) {
		System.out.println("n: " + _fileNameInHttpRequest);
		System.out.flush();
		if (_error) return;
		System.out.println("n: " + _fileNameInHttpRequest);
		System.out.flush();
		_decorator.endTrace(idomain, winningPlayer);
		
		try {
			// Create the HTTP Post connection
			ClientHttpRequest hr =
				new ClientHttpRequest(_url);

			InputStream is = new ByteArrayInputStream(getTraceToSend());

			if (_user != null)
				hr.setParameter("user", _user);
			else
				hr.setParameter("user", "unknown");
			hr.setParameter("trace", _fileNameInHttpRequest, is);
			hr.setParameter("domain", idomain.getName());
			
			double max_winGameGoal = 0.0;
			if (winningPlayer==null) winningPlayer = "";
			// Get players
			if (m_firstGameState!=null) {
				String playersString = "";
				for(String p:m_firstGameState.getAllPlayers()) {
					if (playersString.equals("")) {
						playersString = p;
					} else {
						playersString = playersString +" " + p;
					}
				}
				hr.setParameter("players",playersString);
			} else {
				hr.setParameter("players","");
			}
			
			// Get Winner
			if (!winningPlayer.equals(""))
				hr.setParameter("winner",winningPlayer);
			else
				hr.setParameter("winner","");
			
			hr.post();
			/*
			java.io.InputStream ris = hr.post();
			while (ris.available() > 0)
				System.out.print((char)ris.read());
			*/
		} catch (java.io.IOException ex) {
			_error = true;
			_errorMsg = "Error sending the trace over the network: ";
			_errorMsg += ex.getMessage();
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

	boolean _error;
	String _errorMsg;

	URL _url;
	
	String _user;
	
	/** Override by subclasses. */
	String _fileNameInHttpRequest = "trace.xml";
}
