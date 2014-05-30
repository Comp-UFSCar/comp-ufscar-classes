package generator;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lucasdavid
 */
public class Generator {

    public static final int MIN_HEIGHT = 10, MAX_HEIGHT = 20, MIN_WIDTH = 10, MAX_WIDTH = 20;

    List<GameMap> maps;

    public Generator() {
        maps = new LinkedList<>();
    }

    public GameMap create() {
        return create(
                (int) (Math.random() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT),
                (int) (Math.random() * (MAX_WIDTH - MIN_WIDTH) + MIN_WIDTH)
        );
    }

    public GameMap create(int _width, int _height) {
        GameMap newMap = new GameMap(_width, _height);
        maps.add(newMap);
        
        for (int i = 0; i < newMap.height(); i++) {
                for (int j = 0; j < newMap.width(); j++) {
                    newMap.define(i, j, MapElement.GROUND);
                }
            }
        
        return newMap;
    }

    public Generator delete(int _map) {
        maps.remove(_map);
        return this;
    }

    public Generator delete(GameMap _map) {
        maps.remove(_map);
        return this;
    }

}
