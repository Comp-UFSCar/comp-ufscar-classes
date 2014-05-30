package gamemapgraph;

import generator.GameMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class GameMapGraph {

    public final static int MIN_WEIGHT = 1, MAX_WEIGHT = 100;

    class GameMapGraphLink {

        GameMapGraphNode a, b;
        int weight;

        public GameMapGraphLink(GameMapGraphNode _a, GameMapGraphNode _b, int _weight) {
            a = _a;
            b = _b;
            weight = _weight;
        }
    }

    class GameMapGraphNode {

    }

    List<int[]> adjacencies;

    public GameMapGraph(GameMap _map) {
        adjacencies = new ArrayList<>();
        for (int i = 0; i < _map.height(); i++) {
            for (int j = 0; j < _map.width(); j++) {
                // link to node on top, right side, botom and left side
                int[] adjacency = new int[]{
                    i > 0 ? (int) (Math.random() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT) : 0,
                    j < _map.width() - 1 ? (int) (Math.random() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT) : 0,
                    i < _map.height() - 1 ? (int) (Math.random() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT) : 0,
                    j > 0 ? (int) (Math.random() * (MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT) : 0
                };
                adjacencies.add(adjacency);
            }
        }

    }

}
