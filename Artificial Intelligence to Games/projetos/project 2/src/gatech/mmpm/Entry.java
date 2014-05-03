/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

import java.util.ArrayList;



public class Entry {
	private int timeStamp;
	private GameState gameState = null;
	private ArrayList <Action> actions = new ArrayList <Action>();
	private ArrayList <Action> abortedActions = new ArrayList <Action>();
	
	public Entry() {}
	
	public Entry(int timeStamp, GameState gs)
	{
		this.timeStamp = timeStamp;
		this.gameState = gs;
	}
	
	public Entry (int timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public void addGameState(GameState gs)
	{
		gameState = gs;
	}
	
	public GameState getGameState()
	{
		return gameState;
	}
	
	public int getTimeStamp()
	{
		return timeStamp;
	}

	public String toString()
	{
		String out = "Entry object timeStamp: " + this.timeStamp + "\n" + gameState.toString() + "\n";
		for(Action a:actions) out += a.toString() + "\n";
		
		return out;
	}
	

	public ArrayList<Action> getActions() 
	{
		return actions;
	}

	public ArrayList<Action> getAbortedActions() 
	{
		return abortedActions;
	}

	
	public void addAction(Action action) 
	{
		actions.add(action);
	}

	public void addAbortedAction(Action action) 
	{
		abortedActions.add(action);
	}
}
