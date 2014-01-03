package Assignment_4;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class PlayerLikeABossInATournament extends OthelloPlayer {

    boolean maximize;
    long initialExecutionTime, maximumExecutionTime;

    public PlayerLikeABossInATournament maximumExecutionTime(long _maximumExecutionTime) {
        // convert milliseconds to nanoseconds
        maximumExecutionTime = _maximumExecutionTime * 1000000;
        return this;
    }

    public OthelloMove getMove(OthelloState _state, long _time) {
        maximumExecutionTime(_time);
        return getMove(_state);
    }

    @Override
    public OthelloMove getMove(OthelloState _state) {
        maximize = _state.nextPlayerToMove == OthelloState.PLAYER1;
        
        OthelloMove best = null;

        // reseting settings for further analyses
        List<Thread> threads = new LinkedList<>();
        List<OthelloMove> moves = _state.generateMoves();

        AlphaBetaThread.initialExecutionTime = System.nanoTime();
        AlphaBetaThread.maximumExecutionTime = maximumExecutionTime;

        // setting params for this movement
        AlphaBetaThread.root = _state;
        AlphaBetaThread.depth = 0;
        AlphaBetaThread.maximize = maximize;
        AlphaBetaThread.interrupted = false;

        // there are possible moves to be done
        if (!moves.isEmpty()) {
            while (!AlphaBetaThread.interrupted) {
                AlphaBetaThread.depth++;
                AlphaBetaThread.best = null;
                AlphaBetaThread.score = maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;

                // assuming that the first move is the best
                AlphaBetaThread.score = _state.applyMoveCloning(moves.get(0)).score();
                AlphaBetaThread.best = moves.get(0);

                for (OthelloMove move : moves) {
                    AlphaBetaThread thread = new AlphaBetaThread(move);
                    threads.add(thread);
                    thread.setPriority(Thread.MAX_PRIORITY);
                    thread.start();
                }

                for (Thread thread : threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                // catches the last response which was not interrupted
                if (!AlphaBetaThread.interrupted || best == null) {
                    best = AlphaBetaThread.best;
                }
            }
        }

        return best;
    }
}
