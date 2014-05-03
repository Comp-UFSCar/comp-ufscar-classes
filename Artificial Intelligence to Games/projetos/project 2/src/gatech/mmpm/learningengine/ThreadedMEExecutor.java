/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.learningengine;

import gatech.mmpm.Action;
import gatech.mmpm.GameState;

import java.util.List;


public class ThreadedMEExecutor extends LazyMEExecutor {

	WorkerThread workerThread = null;
	
	GameState gameState;
	int gameCycle;
	Object think = new Object();

    boolean gameEndSignal = false;
	boolean dataReady = true;
	boolean waiting = false;
	List<Action> actions = new java.util.LinkedList<Action>();
	
	public ThreadedMEExecutor() {
	}
	
	public ThreadedMEExecutor(IMEExecutor orig) {
		super(orig);
	}
	
	public void gameEnd() {
		m_aiOrig.gameEnd();
        gameEndSignal = true;
		synchronized(think) {
		think.notifyAll();}
	}

	public void gameStart(String playerName) {
		
		workerThread = new WorkerThread();
		workerThread.privateAI = m_aiOrig;
		m_aiOrig.gameStart(playerName);
		workerThread.start();
	}
/*
	public void getActions(GameState gs, List<Action> la) {
		if (canThink())
			privateThink(gs, actions);
	}
	
	public void getActions(IGameStateProvider gameStateProvider, java.util.List<plans.Action> actions) {
		if (canThink())
			privateThink(gameStateProvider.getGameState(), actions);
	
	}
	*/
	protected boolean canThink() {
		// If the worker thread is idle, we can send it
		// the next game state.
		return dataReady;
	}
	
	protected void think(int cycle, GameState gs, java.util.List<gatech.mmpm.Action> la) {
		if (!dataReady) return;

		// Copy the thread response
		for (Action a : this.actions)
			la.add(a);
		
		// Back to work!
		dataReady = false;
		actions.clear();
		gameState = (GameState)gs.clone();
		gameCycle = cycle;
		while(!waiting);
		synchronized(think) {
			think.notifyAll();
		}
	}

	private class WorkerThread extends Thread {

		// We cache the IMEExecutor to avoid problems when the
		// game has finished and other starts shortly...
		public IMEExecutor privateAI;
		
		public void run() {
			System.out.println("ThreadedMEExecutor: worker thread started");
			while (!gameEndSignal) {
				// Wait orders
				try {
					synchronized(think) {
						waiting = true;
						think.wait();
					}
				} catch (InterruptedException ex) {
					// Game is finish
					privateAI.gameEnd();
					break;
				}
				// Think!
//				long start_time = System.currentTimeMillis();
				privateAI.getActions(gameCycle, gameState, actions);
//				System.out.println("D2 took " + (System.currentTimeMillis() - start_time));
				// Notify
				dataReady = true;
			}
            System.out.println("ThreadedMEExecutor: worker thread done");
		}
	}
	
}
