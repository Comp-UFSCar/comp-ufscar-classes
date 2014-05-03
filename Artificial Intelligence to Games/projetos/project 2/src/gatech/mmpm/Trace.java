/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

import java.util.ArrayList;
import java.util.List;


public class Trace {
	protected int traceID;
	protected String domain = null;
	protected String mapName = null;
	protected List <Entry> entries = new ArrayList<Entry>();

	public Trace(String domain, String mapName) {
		this.domain = domain;
		this.mapName = mapName;
		this.traceID = 0;
	}

	public Trace( int traceID, String domain, String mapName)
	{
		this.traceID = traceID;
		this.domain = domain;
		this.mapName = mapName;
	}
	
	public Trace()
	{}

	/**
	 * Remove the aborted actions (both the information of its
	 * execution and its failure).
	 */
	public void cleanUpAbortedActions() {
		for (Entry e : entries) {
			for (gatech.mmpm.Action aborted : e.getAbortedActions()) {
				int id = aborted.getActionID();
				// Search the id in previous entries...
				java.util.Iterator<Entry> itEntries = entries.iterator();
				boolean actionFound = false;
				while (itEntries.hasNext() && ! actionFound) {
					Entry entry = itEntries.next();
					java.util.Iterator<gatech.mmpm.Action> itActions = 
											entry.getActions().iterator();
					
					while (!actionFound && itActions.hasNext()) {
						gatech.mmpm.Action a = itActions.next();
						if (a.getActionID() == id) {
							itActions.remove();
							System.out.println("Action " + id + " removed.");
							actionFound = true;
						}
					}
					
					// The action will not appear in the future.
					if (!actionFound && (e == entry)) {
						System.out.println("Warning: aborted action not found (" + id + ").");
						break;
					}
				}
			}
		}
	}
	
	public void setTraceID(int newID) {
		this.traceID = newID;
	}
	
	public void addEntry (Entry newEntry)
	{
		entries.add(newEntry);
	}
	
	public void addGameState(int timeStamp, GameState gs)
	{
		entries.add(new Entry(timeStamp, gs));
	}
	
	public List<Entry> getEntries() {
		return entries;
	}
	
	public GameState getGameState(int timeStamp)
	{
		for ( Entry e : entries )
		{
			if (e.getTimeStamp()==timeStamp)
				return e.getGameState();
		}
		return null;
	}
	
	public int getID() {
		return traceID;
	}
	
	public void printTrace()
	{
		System.out.println("domain   --> " + domain + "\nMap-name --> " + mapName);
		for ( Entry e : entries)
		{
			System.out.println(e.toString());
		}
	}
		
}
