/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

/**
 * 
 */
package gatech.mmpm.tracer;

import gatech.mmpm.IDomain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class used to generates traces and save them to zip files.
 * (or a generic output stream).
 * 
 * @author David LLanso
 * @note The file _is never closed_, so this class relies on the
 * JVM and the operating system... The reason is that the ITracer
 * does not have any method to allow users to specify when
 * they have finished working with the object.
 */
public class OutputStreamZipTracer extends OutputStreamTracer {

	public OutputStreamZipTracer(String fileName) {
		try {
			OutputStream fos = new FileOutputStream(fileName);
			java.util.zip.ZipOutputStream zos;
			zos = new java.util.zip.ZipOutputStream(fos);
			zos.putNextEntry(new java.util.zip.ZipEntry("trace.xml"));
			fos = zos;
			setOutputStream(fos);
		} catch (IOException ex) {
			m_error = true;
			m_errorStr = ex.getMessage() + "\n" + ex.getStackTrace().toString();
		}
	}

	/* (non-Javadoc)
	 * @see d2.tracer.ITracer#endTrace()
	 */
	public void endTrace(IDomain idomain, String winner) {
		super.endTrace(idomain, winner);
		java.util.zip.ZipOutputStream zos;
		zos = (java.util.zip.ZipOutputStream)m_outputStream;
		try {
			zos.closeEntry();
			zos.flush();
			zos.close();
		} catch (IOException e) {
			System.out.println("An error happened when trying to close the zip file.");
			e.printStackTrace();
		}
	}

}
