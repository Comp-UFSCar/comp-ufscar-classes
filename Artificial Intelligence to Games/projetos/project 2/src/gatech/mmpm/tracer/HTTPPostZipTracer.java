/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

/**
 * Tracer that sends the trace using a HTTP Post request to the
 * server. It adds a parameter "user" to the request that
 * indicates who creates the trace (the user is taken from the
 * trace metadata), and the parameter "trace" with the XML file.
 * Instead of sending a text file, it zips the trace into a ZIP
 * file. This is the only difference between HTTPPostZipTracer
 * and HTTPPostTracer.
 * <p>
 * The trace is cached while the game is active and send it completely
 * when endTrace() is called. Doing that, we only make a
 * HTTP request, and the server does not have to store temporary
 * data waiting the rest of the trace.
 * 
 * @author Marco Antonio Gomez Martin
 **/
public class HTTPPostZipTracer extends HTTPPostTracer {
	
	/**
	 * Constructor of the class
	 * @param url Url where the class will post the HTTP request.
	 */
	public HTTPPostZipTracer(String url) throws java.io.IOException {
		super(url);
		_fileNameInHttpRequest = "trace.zip";
//		System.out.println("aqui estamos. oe");
	}
	
	protected void createDecorator() {
		_outputStream = new java.io.ByteArrayOutputStream();
		_zipOutputStream = new java.util.zip.ZipOutputStream(_outputStream);
		try {
			_zipOutputStream.putNextEntry(new java.util.zip.ZipEntry("trace.xml"));
		} catch (java.io.IOException ex) {
			_error = true;
			_errorMsg = "Unexpected error while creating ZIP entry.";
			_errorMsg += ex.getMessage();
			return;
		}
		_decorator = new OutputStreamTracer(_zipOutputStream);
	}
	
	protected byte[] getTraceToSend() throws java.io.IOException {
		
		_zipOutputStream.closeEntry();
		_zipOutputStream.flush();
		_zipOutputStream.close();
		return _outputStream.toByteArray();
	}

	java.util.zip.ZipOutputStream _zipOutputStream;
}
