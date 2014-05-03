/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tools.expressionparser;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.tools.expressionparser.SymbolTable.EntryType;

import java.util.List;

public class SymbolTableMethodEntry extends SymbolTableEntry {


	public SymbolTableMethodEntry(String name, ActionParameterType datatype, List<SymbolTableEntry> parameters) {
		super(EntryType.METHOD, name, datatype);
		for (SymbolTableEntry e: parameters) {
			if (e.getEntryType() != EntryType.PARAMETER)
				throw new RuntimeException("Invalid entry type in method parameter.");
		}
		_parameters = parameters;
	}

	List<SymbolTableEntry> getParameters() {
		return _parameters;
	}

	protected List<SymbolTableEntry> _parameters;
	
} // class SymbolTableMethodEntry
