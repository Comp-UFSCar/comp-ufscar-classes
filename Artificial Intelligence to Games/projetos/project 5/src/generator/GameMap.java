package generator;

import java.util.Date;

/**
 *
 * @author lucasdavid
 */
public class GameMap {

    private int[][] m;
    public String name;

    public GameMap() {
        this(1, 1);
    }

    public GameMap(int _height, int _width) {
        m = new int[_height][_width];
        name = "map_" + new Date().getTime();
    }

    /**
     * Set a element in the map.
     *
     * @param _i integer
     * @param _j integer
     * @param _e element that will be set at m[_i][_j]
     * @throws ArrayIndexOutOfBoundsException
     *
     * @return this
     */
    public GameMap define(int _i, int _j, int _e) throws ArrayIndexOutOfBoundsException {
        m[_i][_j] = _e;
        return this;
    }

    public int field(int _i, int _j) throws ArrayIndexOutOfBoundsException {
        return m[_i][_j];
    }

    public int height() {
        return m.length;
    }

    public int width() {
        return m[0].length;
    }

    public GameMap height(int _height) {
        int[][] matrix = new int[_height][m[0].length];
        overlayMaps(m, matrix);
        m = matrix;

        return this;
    }

    public GameMap width(int _width) {
        int[][] matrix = new int[m.length][_width];
        overlayMaps(m, matrix);
        m = matrix;

        return this;
    }

    /**
     * Replaces all elements in matrix _n by their correspondent element in matrix _m, respecting _n size.
     *
     * @param _m matrix source
     * @param _n matrix result
     *
     * @exception ArrayIndexOutOfBoundsException
     */
    void overlayMaps(int[][] _m, int[][] _n) {
        for (int i = 0; i < _m.length; i++) {
            for (int j = 0; j < _m[0].length; j++) {
                try {
                    _n[i][j] = _m[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
                    // does nothing, simply does not copy element...
                }
            }
        }
    }

}
