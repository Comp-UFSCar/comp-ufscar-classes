package Assignment_4;

/**
 *
 * @author santi
 */
public class Test {

    public static void main(String args[]) {
        // Create the game state with the initial position for an 8x8 board:
        OthelloState state = new OthelloState(8);

        OthelloPlayer players[] = {
            new PlayerLikeABossInATournament()
            .maximumExecutionTime(10000)
            .maximize(true)
            .depth(5),
            new PlayerLikeAlphaBeta()
            .depth(6)
            .maximize(false)
        };

        long deltaTotal[] = {0, 0};

        do {
            long deltaMove = System.nanoTime();
            OthelloMove move = players[state.nextPlayerToMove].getMove(state);
            deltaTotal[state.nextPlayerToMove] += System.nanoTime() - deltaMove;

            System.out.println(state.nextPlayerToMove + ": " + (System.nanoTime() - deltaMove) / 1000000);
            state = state.applyMoveCloning(move);

        } while (!state.gameOver());

        // Show the result of the game:
        System.out.println("\nFinal state with score: " + state.score());
        System.out.println("\nDeltaTotal: " + deltaTotal[0] + ". " + deltaTotal[1]);

        System.out.println(state);
    }
}
