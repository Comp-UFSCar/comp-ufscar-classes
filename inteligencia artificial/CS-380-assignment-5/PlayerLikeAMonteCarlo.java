package Assignment_5;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
final class PlayerLikeAMonteCarlo extends OthelloPlayer {

    boolean maximize = true;
    int iterations = 1000;

    class OthelloStateNode {

        LinkedList<OthelloStateNode> children;
        OthelloStateNode parent;

        OthelloMove movement;
        OthelloState state;
        float average;
        int visits;

        public OthelloStateNode() {
            children = null;
            state = null;
            parent = null;
            movement = null;
            average = 0;
            visits = 0;
        }

        public OthelloStateNode(OthelloState _state) {
            this();
            state = _state;
        }

        /**
         * Increments OthelloStateNode@visits and calculate the weight average between OthelloStateNode and _average,
         * repeating this procedure to its OthelloStateNode@parent.
         *
         * @param _average int the value that has to be used in the average calculation
         * @return OthelloStateNode
         */
        public void backup(int _average) {
            visits++;
            average = (float) (visits -1) / visits * average + 1f / visits * _average;
            
            if (parent != null) {
                parent.backup(_average);
            }
        }

        public List<OthelloStateNode> generateChildren() {
            // expand children, if necessary
            if (children == null) {
                children = new LinkedList<>();
                for (OthelloMove move : state.generateMoves()) {
                    OthelloStateNode child = new OthelloStateNode(state.applyMoveCloning(move));
                    child.movement = move;
                    child.parent = this;

                    children.add(child);
                }
            }

            return children;
        }

        /**
         * Returns the child which occupies the position "Math.random() * children.size()" position.
         *
         * @return OthelloStateNode
         */
        public OthelloStateNode getRandomChild() {
            return generateChildren().get(
                (int) (Math.random() * generateChildren().size())
            );
        }

        /**
         * Returns the child with greatest OthelloStateNode@state@average()
         *
         * @return OthelloStateNode
         */
        public OthelloStateNode bestChild() {
            OthelloStateNode best = generateChildren().get(0);
            
            for (OthelloStateNode current : children) {
                if (current.average > best.average) {
                    best = current;
                }
            }

            return best;
        }

        /**
         * Returns the child with lowest OthelloStateNode@state@average()
         *
         * @return OthelloStateNode
         */
        public OthelloStateNode worseChild() {
            OthelloStateNode worse = generateChildren().get(0);

            for (OthelloStateNode current : children) {
                if (current.average < worse.average) {
                    worse = current;
                }
            }

            return worse;
        }
    }
    
    public PlayerLikeAMonteCarlo iteraction(int _iterations) {
        iterations = _iterations;
        return this;
    }

    @Override
    public OthelloMove getMove(OthelloState _state) {
        maximize = _state.nextPlayerToMove == OthelloState.PLAYER1;
        return MonteCarloTreeSearch(_state);
    }

    OthelloMove MonteCarloTreeSearch(OthelloState _state) {
        OthelloStateNode root = new OthelloStateNode(_state);

        // if root is a leaf - no other movement can be achieved from _node
        if (root.generateChildren().isEmpty()) {
            return null;
        }

        for (int i = 0; i < iterations; i++) {
            OthelloStateNode current = treePolicy(root, maximize);
            current.backup(defaultPolicy(current));
        }

        // return the movement of the child of best interest
        return maximize
            ? root.bestChild().movement
            : root.worseChild().movement
            ;
    }

    OthelloStateNode treePolicy(OthelloStateNode _node, boolean _maximize) {
        // if _node is a leaf (no other movement can be achieved from _node)
        if (_node.generateChildren().isEmpty()) {
            return _node;
        }

        // Pick up the first child OthelloStateNode of _node.
        OthelloStateNode child = _node.children.removeFirst();

        // If there is a node which wasn't been evaluated yet - and if there is, it will be necessarily
        // the element that was just removed -, return it.
        if (child.visits == 0) {
            // put it in the end of the list, so the second element will assume the first position.
            _node.children.add(child);
            return child;
        } else {
            // if we are here, all the children had been visited and they are in the original position.
            // put the removed element on its right place, so epsilon-greedy won't be affected by this operation.
            _node.children.add(0, child);
        }

        // epsilon-greedy strategy
        // in 10% of the cases, return a random node
        if (Math.random() < .1) {
            return treePolicy(_node.getRandomChild(), !_maximize);
        }

        // in 90% of the cases, return the best interest child
        return treePolicy(
                _maximize
                ? _node.bestChild()
                : _node.worseChild()
                , !_maximize
        );
    }

    int defaultPolicy(OthelloStateNode _node) {
        // clone state to avoid modification over the original board.
        OthelloState state = _node.state.clone();
        
        // apply random movements until the current game end with the winning of one of the players
        List<OthelloMove> moves;
        while (!(moves = state.generateMoves()).isEmpty()) {
            state.applyMove(moves.get((int) (Math.random() * moves.size())));
        }
        
        // return the state.
        return state.score();
    }
}
