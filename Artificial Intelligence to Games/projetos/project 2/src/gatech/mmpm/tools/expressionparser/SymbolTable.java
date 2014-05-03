/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

public class SymbolTable implements Cloneable {

	public enum EntryType {
		METHOD, PARAMETER, GLOBAL_VAR;
	} // Enum EntryType

	//--------------------------------------------------------------
	
	public SymbolTable() {
		_entries = new java.util.LinkedList<SymbolTableEntry>();
	}

	public SymbolTable(SymbolTable rhs) {

		_entries = new java.util.LinkedList<SymbolTableEntry>(rhs._entries);

	}
	
	public SymbolTable clone() {
		return new SymbolTable(this);
	}
	
	public SymbolTableEntry getEntry(String name) {
		for (SymbolTableEntry e:_entries) {
			if (e.getName().equals(name))
				return e;
		}
		return null;
	}
	
	public boolean addEntry(SymbolTableEntry e) {
		if (getEntry(e.getName()) != null)
			return false;
		else {
			_entries.add(e);
			return true;
		}
	}

	/**
	 * For debug purposes, all the elements are printed.
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (SymbolTableEntry e:_entries) {
			result.append(e.getName());
			result.append(" (");
			result.append(e.getDatatype());
			result.append(")\n");
		}
		return result.toString();
	}
	
	protected java.util.List<SymbolTableEntry> _entries;
	
} // SymbolTable
