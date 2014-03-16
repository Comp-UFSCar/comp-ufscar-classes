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
            new PlayerLikeAlphaBeta()
            .depth(3),
            new PlayerLikeABossInATournament()
            .maximumExecutionTime(1000),
        };
        
        System.out.println("Assignment - 4");

        long deltaTotal[] = {0, 0};

        do {
            OthelloMove move = players[state.nextPlayerToMove].getMove(state);
            
            System.out.print(players[state.nextPlayerToMove] + " : " + move + " : ");
            
            state.applyMove(move);
            System.out.println(state.score());
        } while (!state.gameOver());

        // Show the result of the game:
        System.out.println("\nFinal state with score: " + state.score());
        System.out.println("\nDeltaTotal: " + deltaTotal[0] + ". " + deltaTotal[1]);

        System.out.println(state);
    }
}
