package s3.ai;

import java.util.LinkedList;
import java.util.List;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;

public class AStar {

    /**
     * Inner class that represents a node from the path tree.
     *
     */
    class AStarNode {

        Pair<Double, Double> location;

        AStarNode parent;
        double f;

        AStarNode(Double _m_a, Double _m_b, AStarNode _parent) {
            this(new Pair(_m_a, _m_b), _parent);
        }

        AStarNode(Pair<Double, Double> _location, AStarNode _parent) {
            this(_location, _parent.f + 1 + heuristic(_location));
            parent = _parent;
        }

        AStarNode(Pair<Double, Double> _location, double _f) {
            location = _location;
            f = _f;
            parent = null;
        }

        /**
         * Override method equals(Object o) so the method contains() from LinkedList<AStarNode>
         * can work properly.
         *
         * @param o
         * @return boolean
         */
        @Override
        public boolean equals(Object o) {
            try {
                AStarNode node = (AStarNode) o;
                return location.m_a.equals(node.location.m_a) && location.m_b.equals(node.location.m_b);

            } catch (ClassCastException e) {
                return false;
            }
        }
    }

    S3PhysicalEntity entity;
    S3 game;

    AStarNode start;
    AStarNode goal;

    public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
            S3PhysicalEntity i_entity, S3 the_game) {

        AStar a = new AStar(start_x, start_y, goal_x, goal_y, i_entity, the_game);
        List<Pair<Double, Double>> path = a.computePath();

        if (path != null) {
            return path.size();
        }

        return -1;
    }

    public AStar(double start_x, double start_y, double goal_x, double goal_y,
            S3PhysicalEntity i_entity, S3 the_game) {

        start = new AStarNode(new Pair(start_x, start_y), 0);
        goal = new AStarNode(new Pair(goal_x, goal_y), 0);

        entity = i_entity;
        game = the_game;
    }

    /**
     * Manhattan distance of _location to goal.
     * 
     * @param _location starting location in which Manhattan distance is based
     * @return double
     * 
     */
    double heuristic(Pair<Double, Double> _location) {
        return Math.abs(goal.location.m_a - _location.m_a)
                + Math.abs(goal.location.m_b - _location.m_b);
    }

    public List<Pair<Double, Double>> computePath() {

        List<AStarNode> closed = new LinkedList<>();
        List<AStarNode> open = new LinkedList<>();

        open.add(start);

        while (!open.isEmpty()) {

            // gets the closest node (smallest f = g + h)
            AStarNode closest = open.get(0);
            for (AStarNode current : open) {
                if (current.f < closest.f) {
                    closest = current;
                }
            }

            open.remove(closest);
            closed.add(closest);

            if (closest.equals(goal)) {
                return backtrackPath(closest);
            }

            // for each children of M that is not in closed or open
            AStarNode[] children = new AStarNode[4];
            children[0] = new AStarNode(closest.location.m_a - 1, closest.location.m_b, closest);
            children[1] = new AStarNode(closest.location.m_a + 1, closest.location.m_b, closest);
            children[2] = new AStarNode(closest.location.m_a, closest.location.m_b - 1, closest);
            children[3] = new AStarNode(closest.location.m_a, closest.location.m_b + 1, closest);

            for (AStarNode child : children) {
                // if it is a movable location and it hasn't been explored yet.
                if (game.isSpaceFree(1, child.location.m_a.intValue(), child.location.m_b.intValue())
                        && !open.contains(child)
                        && !closed.contains(child)) {

                    open.add(child);
                }
            }
        }

        return null;
    }

    /**
     * Backtrack _node parents until starting point.
     * 
     * @param _node
     * @return List<Pair<Double, Double>>
     * 
     */
    List<Pair<Double, Double>> backtrackPath(AStarNode _node) {
        LinkedList<Pair<Double, Double>> path = new LinkedList<>();

        while (null != _node) {
            path.add(0, _node.location);
            _node = _node.parent;
        }

        return path;
    }

}
