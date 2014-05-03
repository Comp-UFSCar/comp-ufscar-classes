/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.templates;

import java.lang.StringBuffer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Stores a text template and eases substitution of
 * string patterns with other values.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class TextTemplate {

	public TextTemplate() {
	}

	public void init(String template) {
		_template = template;
		reset();
	}

	public void initFromFile(String fileName) throws java.io.IOException {

		java.io.InputStream input = null;
		try {
			// Try in the path of this class
			java.net.URI u = getClass().getResource("/gatech/mmpm/tools/templates/" + fileName).toURI();
			input = new java.io.FileInputStream(new java.io.File(u));
		} catch (java.io.IOException ex) {}
		  catch (java.lang.NullPointerException ex) {}
		  catch(java.net.URISyntaxException ex) {}

		if (input == null) {
			try {
				// Error; try in the file system
				input = new java.io.FileInputStream(fileName);
			} catch (java.io.IOException ex) {
			}
		}
		if (input == null)
			throw new java.io.IOException("File not found");

		BufferedReader br;

		br = new  BufferedReader(new InputStreamReader(input));

		StringBuffer sb = new StringBuffer();
		String newLine;
		
		newLine = br.readLine();
		while(newLine != null) {
			sb.append(newLine);
			sb.append("\n");
			newLine = br.readLine();
		} // newLine

		_template = sb.toString();

		reset();

	} // initFromFile

	public void reset() {

		if (_template == null)
			throw new java.lang.RuntimeException("Call init first!");

		_workingTemplate = new StringBuffer(_template);

	} // reset

	public String getResult() {

		return _workingTemplate.toString();

	} // getResult

	// mark should be strange enough to not appear
	// never, neither while other replaces has been
	// previously done!
	public int replace(String mark, String value) {

		int result = 0;
		
		if (_workingTemplate == null)
			throw new java.lang.RuntimeException("Call init first!");

		int markLength = mark.length();
		int nextMark;
		
		nextMark = _workingTemplate.indexOf(mark);

		while (nextMark != -1) {
			++result;
			_workingTemplate.replace(nextMark, nextMark + markLength, value);
			nextMark = _workingTemplate.indexOf(mark);
		} // while

		return result;

	} // replace

	/**
	 * Original template with no changes.
	 */
	String _template;

	/**
	 * Template with the incremental asked
	 * changes applied. 
	 */
	StringBuffer _workingTemplate;
	
} // TextTemplate
