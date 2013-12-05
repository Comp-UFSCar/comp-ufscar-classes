package Assignment_5;

/**
 *
 * @author lucasdavid
 */
public class Test {
    
    public static void main(String args[]) {
        for (int i = 0; i < 1; i++) {
            // Create the game state with the initial position for an 8x8 board:
            OthelloState state = new OthelloState(8);

            OthelloPlayer players[] = {
                new PlayerLikeAMonteCarlo()
                .maximize(true)
                .iteraction(1000),
                new PlayerLikeAMonteCarloInATournament()
                .maximize(false)
                .time(2000),
            };

            do {
                System.gc();
                OthelloMove move = players[state.nextPlayerToMove].getMove(state);
                state.applyMove(move);
                System.out.println(state.score() + " ");
            } while (!state.gameOver());

            System.out.println(state.score());
            System.out.println(state);
        }
    }
}
