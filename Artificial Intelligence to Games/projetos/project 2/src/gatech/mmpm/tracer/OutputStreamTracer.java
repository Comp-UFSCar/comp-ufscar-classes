/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

import gatech.mmpm.IDomain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Class used to generates traces and save them to files
 * (or a generic output stream).
 * 
 * @author Marco Antonio Gmez Martn
 * @note The file _is never closed_, so this class relies on the
 * JVM and the operating system... The reason is that the ITracer
 * does not have any method to allow users to specify when
 * they have finished working with the object.
 */
public class OutputStreamTracer extends ITracer {

	public OutputStreamTracer() {
	}

	public OutputStreamTracer(String fileName) {
		try {
			setOutputStream(new FileOutputStream(fileName));
		} catch (IOException ex) {
			m_error = true;
			m_errorStr = ex.getMessage() + "\n" + ex.getStackTrace().toString();
		}
	}

	public OutputStreamTracer(OutputStream outputStream) {
		setOutputStream(outputStream);
	}

	protected void setOutputStream(OutputStream outputStream) {
		this.m_outputStream = outputStream;
		this.m_writer = new java.io.BufferedWriter(
				new java.io.OutputStreamWriter(m_outputStream));
		this.m_xmlWriter = new gatech.mmpm.util.XMLWriter(m_writer);
	}

	/* (non-Javadoc)
	 * @see d2.tracer.ITracer#beginTrace()
	 */
	public void beginTrace() {
		if (m_error) return;
		m_xmlWriter.tag("gametrace");
		m_logOpened = false;
	}

	/* (non-Javadoc)
	 * @see d2.tracer.ITracer#endTrace()
	 */
	public void endTrace(IDomain idomain, String winner) {
		if (m_error) return;	
		if (m_logOpened)
			m_xmlWriter.tag("/log");
		m_logOpened = false;
		m_xmlWriter.tag("/gametrace");
		m_xmlWriter.flush();
	}

	public void putMetadata(java.util.Properties props) {
		if (m_error) return;
		m_xmlWriter.tag("info");
		for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof String) {
				String s = (String) o;
				m_xmlWriter.tag(s, props.getProperty(s));
			}
		}
		m_xmlWriter.tag("/info");
		/*
		for (String s : props.stringPropertyNames())
			m_xmlWriter.tag(s, props.getProperty(s));
		m_xmlWriter.tag("/info");
		 */
	}


	/* (non-Javadoc)
	 * @see d2.tracer.ITracer#beginGameCicle()
	 */
	public void beginGameCycle(int number) {
		if (m_error) return;
		if (!m_logOpened) { m_xmlWriter.tag("log"); m_logOpened = true; }
		m_xmlWriter.tag("entry time=\"" + number + "\"");
	}

	/* (non-Javadoc)
	 * @see d2.tracer.ITracer#endGameCicle()
	 */
	public void endGameCycle() {
		if (m_error) return;
		m_xmlWriter.tag("/entry");
		m_xmlWriter.flush();
	}

	/* (non-Javadoc)
	 * @see d2.tracer.ITracer#putGameState()
	 */
//	public void putGameState(domain.GameState gs) {
//		if (m_error) return;
//		m_xmlWriter.tag("gamestate");
//
//		gs.getMap().writeToXML(m_xmlWriter);
//
//		for (domain.Entity e : gs.getAllEntities())
//			e.writeToXML(m_xmlWriter);
//		m_xmlWriter.tag("/gamestate");
//	}
	
//	public void putGameState(domain.GameState gs) {
//		putDifferenceGameState(gs);
//	}

	public gatech.mmpm.GameState previousGameState = null;
	public void putGameStateInternal(gatech.mmpm.GameState gs) {
		if (m_error) return;

		if(previousGameState == null) {
			m_xmlWriter.tag("gamestate");
			gs.getMap().writeToXML(m_xmlWriter);
			for (gatech.mmpm.Entity e : gs.getAllEntities())
				e.writeToXML(m_xmlWriter);
			m_xmlWriter.tag("/gamestate");
		}
		else {
			m_xmlWriter.tag("gamestate");
			gs.getMap().writeDifferenceToXML(m_xmlWriter,previousGameState.getMap());
			for (gatech.mmpm.Entity e : gs.getAllEntities())
				e.writeDifferenceToXML(m_xmlWriter,previousGameState.getEntity(e.getentityID()));
			m_xmlWriter.tag("/gamestate");
		}
//		System.out.println("putGameStateInternal: saving " + gs.getAllEntities().size() + " entities");
		previousGameState = gs;

	}

	public void putAction(gatech.mmpm.Action a) {
		// HACK: we use the "toXMLString" method of plans.Action
		// classes. I do not like this solution, because we are
		// in fact forcing the game developer (who implements this actions)
		// to adhere to our XML format)...
		a.writeToXML(m_xmlWriter);
	}

	public void putAbortedAction(gatech.mmpm.Action a) {
		// HACK: we use the "toXMLString" method of plans.Action
		// classes. I do not like this solution, because we are
		// in fact forcing the game developer (who implements this actions)
		// to adhere to our XML format)...
		a.writeToXMLNode(m_xmlWriter, "AbortedAction");
	}
	
	public boolean success() {
		return !m_error;
	}

	public String getErrorMessage() {
		return m_errorStr;
	}

	protected boolean m_error = false;
	protected String m_errorStr = "";
	protected OutputStream m_outputStream;
	protected java.io.Writer m_writer;

	protected gatech.mmpm.util.XMLWriter m_xmlWriter;

	protected boolean m_logOpened;
}	

