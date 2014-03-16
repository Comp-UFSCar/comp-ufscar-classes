package Engine;

import Engine.movement.EngineMovement;
import Engine.movement.MovementToken;
import FileHandler.FileHandler;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author ld492
 */
public class Engine {

    public static final int INITIAL_VALID_PIECE_ID = 3, MAIN_PIECE = 2,
            WALL = 1, EXIT = -1, EMPTY_FIELD = 0;

    // all states, loaded sequencially on the directory src/Maps
    FileHandler maps;
    // map which is currently being played.
    int currentMap;
    // current map, being represented by a matrix.
    int[][] map;
    int rows, cols;

    Engine parent;
    MovementToken actionTakenByParent;
    int g, h;

    public Engine() throws Exception {
        maps = new FileHandler();
        parent = null;
        actionTakenByParent = null;
        currentMap = 0;
        g = h = 0;
    }

    /**
     * Induced constructor, build game over existent map.
     *
     * @param _parent
     */
    public Engine(Engine _parent) {
        maps = null;
        parent = null;
        actionTakenByParent = null;
        currentMap = 1;

        rows = _parent.rows;
        cols = _parent.cols;

        g = h = 0;

        map = new int[rows][cols]; // deep copy of matrix.
        for (int i = 0; i < rows; i++) {
            System.arraycopy(_parent.map[i], 0, map[i], 0, cols);
        }
    }

    public Engine parent(Engine _parent, MovementToken _m) {
        parent = _parent;
        actionTakenByParent = _m;
        return this;
    }

    public Engine parent() {
        return parent;
    }

    public MovementToken actionTakenByParent() {
        return actionTakenByParent;
    }

    public Engine g(int _g) {
        g = _g;
        return this;
    }

    public Engine h(int _h) {
        h = _h;
        return this;
    }

    public int g() {
        return g;
    }

    public int h() {
        return h;
    }

    public int f() {
        return g + h;
    }

    /**
     * Jumps to map _map, located in src/Maps folder.
     *
     * @param _map integer which represents the map id in this format: _map + ".txt"
     * @throws Exception
     * @return Engine
     */
    public final Engine load(int _map) throws Exception {
        return load(String.valueOf(_map));
    }

    /**
     * Jumps to map _map, located in src/Maps folder.
     *
     * @param _map
     * @throws Exception
     * @return Engine
     */
    public final Engine load(String _map) throws Exception {
        currentMap = Integer.parseInt(_map.substring(_map.length() - 1));

        map = maps.load(_map);
        rows = map.length;
        cols = map[0].length;

        parent = null;
        actionTakenByParent = null;
        g = h = 0;

        return this;
    }

    public int at(int _i, int _j) {
        return map[_i][_j];
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    /**
     * Deeply-copy the current map.
     *
     * @return int[][]
     */
    public int[][] cloneMap() {
        int[][] clonedMap = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(map[i], 0, clonedMap[i], 0, map[i].length);
        }
        return clonedMap;
    }

    /**
     * Normalize current loaded map.
     *
     * @return Engine
     */
    public Engine normalize() {
        int normalizedID = INITIAL_VALID_PIECE_ID;

        // getting all pieces, except the main_piece.
        Set<Integer> pieces = allPieces();
        pieces.remove(MAIN_PIECE);

        // remembering initial position of all pieces.
        LinkedList<LinkedList<int[]>> allFields = new LinkedList<>();
        for (Integer piece : pieces) {
            allFields.add(fieldsOf(piece));
        }

        // normalizing pieces
        for (LinkedList<int[]> fields : allFields) {
            for (int[] position : fields) {
                int i = ((int[]) position)[0];
                int j = ((int[]) position)[1];

                map[i][j] = normalizedID;
            }
            normalizedID++;
        }

        return this;
    }

    /**
     * Prints the current map of the instanced game in the screen.
     *
     * @return this
     */
    public Engine print() {
        System.out.println("Rows: " + rows + ", Cols: " + cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }

        return this;
    }

    /**
     * Verify if the instanced game had it's current map solved.
     *
     * @return boolean
     */
    public boolean solved() {
        // if there is an element EXIT in the first colunm
        // or the last one, the game isn't solved.
        for (int i = 0; i < rows; i++) {
            if (map[i][0] == EXIT || map[i][cols - 1] == EXIT) {
                return false;
            }
        }
        // if there is an element EXIT in the first row
        // or the last one, the game isn't solved.
        for (int i = 0; i < cols; i++) {
            if (map[0][i] == EXIT || map[rows - 1][i] == EXIT) {
                return false;
            }
        }

        // the game is solved, because no element EXIT were found.
        return true;
    }

    /**
     * Define a set with all pieces located in the map.
     *
     * @return Set<Integer>
     */
    Set<Integer> allPieces() {
        // this monstrous structure called LinedHashSet is a
        // regular HashSet which preserves the insertion order.
        Set<Integer> pieces = new LinkedHashSet<>();

        // adding all pieces to the set, where all elements
        // will have their possibilites of movement evaluated.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] >= MAIN_PIECE) {
                    pieces.add(map[i][j]);
                }
            }
        }

        return pieces;
    }

    /**
     * LinkedList all fields occupied by a _element.
     *
     * @param _el <int> represents an ID for an piece.
     * @return LinkedList<int[]>
     */
    public LinkedList<int[]> fieldsOf(int _el) {
        LinkedList<int[]> fields = new LinkedList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (_el == map[i][j]) {
                    fields.add(new int[]{i, j});
                }
            }
        }

        // return all fields.
        return fields;
    }

    /**
     * Define a set of possible movements which the main piece can make.
     *
     * @return HashSet<MovementToken>
     */
    public Set<MovementToken> movements() {
        return movements(MAIN_PIECE);
    }

    /**
     * Define a set of possible movements which a _piece can make.
     *
     * @param _piece
     * @return HashSet<MovementToken>
     */
    public Set<MovementToken> movements(int _piece) {
        // create a set with a single element, which is the _piece.
        Set<Integer> pieces = new LinkedHashSet<>();
        pieces.add(_piece);

        // returns all possible movements for the piece
        // contained in the set searchedPiece.
        return movements(pieces);
    }

    /**
     * Define a set that contains all possible movements of every piece.
     *
     * @return HashSet<MovementToken>
     */
    public Set<MovementToken> allMovements() {
        return movements(allPieces());
    }

    /**
     * Define a set of possible movements which each piece in _pieces can make.
     *
     * @param Set<Integer>
     * @return HashSet<MovementToken>
     */
    Set<MovementToken> movements(Set<Integer> _pieces) {
        Set<MovementToken> movementsOfPieces = new LinkedHashSet<>();

        // for each piece, let's see which movements it can make
        for (int piece : _pieces) {
            // gets all fields occupied by this piece
            LinkedList<int[]> fields = fieldsOf(piece);

            // we start with all possible movements
            MovementToken mLeft, mRight, mUp, mDown;
            LinkedList<MovementToken> possible = new LinkedList<>();
            possible.add(mLeft = new MovementToken(piece, EngineMovement.LEFT));
            possible.add(mRight = new MovementToken(piece, EngineMovement.RIGHT));
            possible.add(mUp = new MovementToken(piece, EngineMovement.UP));
            possible.add(mDown = new MovementToken(piece, EngineMovement.DOWN));

            // for each movement, if one of those fields occupied by the piece
            // CANNOT execute it, then this especific movement will be removed
            // from the possible movements list.
            for (EngineMovement movement : EngineMovement.values()) {
                for (int[] field : fields) {
                    int x = field[0], y = field[1];

                    // the piece CAN NOT move if the movement is for the left and the
                    // left field isn't empty or isn't being occupied by the same piece
                    // which is moving. Furthermore, if the left field is a exit, the piece
                    // will only be able to move if it is the MAIN_PIECE.
                    if (movement == EngineMovement.LEFT && map[x][y - 1] != EMPTY_FIELD
                            && map[x][y - 1] != piece
                            && (piece != MAIN_PIECE || map[x][y - 1] != -1)) {
                        possible.remove(mLeft);
                    }
                    // Same idea here, but istead checking the left
                    // field, we are checking the right one.
                    if (movement == EngineMovement.RIGHT && map[x][y + 1] != EMPTY_FIELD
                            && map[x][y + 1] != piece
                            && (piece != MAIN_PIECE || map[x][y + 1] != -1)) {
                        possible.remove(mRight);
                    }
                    // now checking the upper field.
                    if (movement == EngineMovement.UP && map[x - 1][y] != EMPTY_FIELD
                            && map[x - 1][y] != piece
                            && (piece != MAIN_PIECE || map[x - 1][y] != -1)) {
                        possible.remove(mUp);
                    }
                    // and finally the last field (bellow the current one).
                    if (movement == EngineMovement.DOWN && map[x + 1][y] != EMPTY_FIELD
                            && map[x + 1][y] != piece
                            && (piece != MAIN_PIECE || map[x + 1][y] != -1)) {
                        possible.remove(mDown);
                    }
                }
            }
            // LinkedList possible has all possible movements for the CURRENT piece,
            // inserts it into the list of possible movements for ALL pieces
            // in the _pieces list.
            movementsOfPieces.addAll(possible);
        }

        // return all possible movements of all pieces in _pieces list.
        return movementsOfPieces;
    }

    /**
     * Execute a movement of the main piece over the map.
     *
     * @param _m
     * @return Engine
     */
    public Engine applyMove(EngineMovement _m) {
        return applyMove(new MovementToken(MAIN_PIECE, _m));
    }

    /**
     * Execute a movement of one defined piece over the map.
     *
     * @param _m
     * @return Engine
     */
    public Engine applyMove(MovementToken _m) {
//        // check if intented movement is possible.
//        boolean isPossible = false;
//        for (MovementToken movement : movements(_m.piece())) {
//            if (_m.movement() == movement.movement()) {
//                isPossible = true;
//                break;
//            }
//        }
//
//        if (!isPossible) {
//            throw new IlegalMovementException();
//        }

        // listing the fields which are occupied by the defined piece.
        LinkedList<int[]> fieldsOfPiece = fieldsOf(_m.piece());

        // ereasing fields occupied by piece before it's movement.
        for (int[] coordinate : fieldsOfPiece) {
            int i = coordinate[0], j = coordinate[1];

            map[i][j] = 0;
        }

        // inserting the piece id in the new fields.
        for (int[] coordinate : fieldsOfPiece) {
            int i = coordinate[0], j = coordinate[1];

            if (EngineMovement.LEFT == _m.movement()) {
                map[i][j - 1] = _m.piece();
            }
            if (EngineMovement.RIGHT == _m.movement()) {
                map[i][j + 1] = _m.piece();
            }
            if (EngineMovement.UP == _m.movement()) {
                map[i - 1][j] = _m.piece();
            }
            if (EngineMovement.DOWN == _m.movement()) {
                map[i + 1][j] = _m.piece();
            }
        }

        return this;
    }

    /**
     * Clone map and apply movement.
     *
     * @param _m
     * @return int[][]
     */
    public Engine applyMoveClonning(MovementToken _m) {
        return new Engine(this)
                .applyMove(_m)
                .parent(this, _m);
    }

    /**
     * Verify if current game is equal to another game, important for the Set<Engine>.contains() comparison.
     *
     * @param _game
     * @return boolean
     */
    @Override
    public boolean equals(Object _game) {
        int[][] _map = ((Engine) _game).map;

        // matrix doesn't have the same lengths, they are different!
        if (_map.length != rows || _map[0].length != cols) {
            return false;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // some of their elements aren't equal, the maps are different!
                if (map[i][j] != _map[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Arrays.deepHashCode(this.map);
        return hash;
    }
}
