/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.tools.expressionparser.SymbolTable.EntryType;

public class SymbolTableEntry {
	
	public SymbolTableEntry(EntryType entryType, String name, ActionParameterType datatype) {
		_entryType = entryType;
		_name = name;
		_datatype = datatype;
	} // constructor

	public EntryType getEntryType() {
		return _entryType;
	}
	
	public String getName() {
		return _name;
	}
	
	public ActionParameterType getDatatype() {
		return _datatype;
	}
	
	protected EntryType _entryType;
	protected String _name;
	protected ActionParameterType _datatype;
	
} // class SymbolTableEntry;
