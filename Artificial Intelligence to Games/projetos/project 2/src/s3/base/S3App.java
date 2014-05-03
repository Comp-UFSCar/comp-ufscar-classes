package s3.base;

import gatech.mmpm.Action;
import gatech.mmpm.Entity;
import gatech.mmpm.GameState;
import gatech.mmpm.learningengine.IMEExecutor;
import gatech.mmpm.learningengine.MEExecutorFactory;
import gatech.mmpm.tracer.ITracer;
import jargs.gnu.CmdLineParser;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import s3.ai.AI;
import s3.ai.AIEmpty;
import s3.ai.builtin.ArchersRush;
import s3.ai.builtin.CatapultRush;
import s3.ai.builtin.DefensiveKnightsRush;
import s3.ai.builtin.FootmenRush;
import s3.ai.builtin.KnightsRush;
import s3.ai.builtin.RushAI;
import s3.ai.builtin.RushAI2;
import s3.entities.*;
import s3.util.KeyInputHandler;
import s3.util.MouseHandler;
import s3.util.Pair;

public class S3App extends Canvas {

    private static final boolean TIME_DEBUG = false;
    private static final String PLAY = "Click the mouse to continue...";
    private static final String COMMENTS = "For any comment/question, contact: santi.ontanon@gmail.com";
    private static final String COMMENTS2 = "Based on S2 by Jai Rad and Kane Bonnette";
    private static final String TECH = "IIIA-CSIC";
    private static final String AUTHORS = "Santiago Ontanon Villar (2010)";
    private static final String TITLE = "S3";
    /**
     * size of a cell side
     */
    public static int CELL_SIZE = 32;
    /**
     * Leeway for mouse click
     */
    public static int MOUSE_CLICK_LEEWAY = 5;
    private static final long serialVersionUID = 1L;
    private BufferStrategy strategy;
    private boolean gameRunning = true;
    public static final int SCREEN_X = 1024;
    public static final int SCREEN_Y = 720;
    public static int REDRAWING_PERIOD = 1;
    public static int MAX_FRAMESKIP = 1000;
    public static final int m_trace_interval = 500;
    public static final int STATE_INIT = 0;
    public static final int STATE_GAME = 1;
    public static final int STATE_QUITTING = 2;
    public static final int STATE_QUIT = 3;
    private int m_state = STATE_INIT;
    private int m_state_cycle = 0;
    private List<PlayerInput> m_pi_l = new LinkedList<PlayerInput>();
    KeyInputHandler m_keyboardState;
    private MouseHandler m_mouse_handler;
    String mouse_player = null;
    private List<S3Action> mousePlayerActions = new LinkedList<S3Action>();
    private Font m_font32, m_font16;
    String _userName = null;
    private S3 m_game;
    private String mapName;
    JFrame container = null;
    protected S3Action selectedAction = null;
    protected HUD theHUD;
    /**
     * the offset of the view pane (in pixels).
     */
    private int desired_x_offset = 0, desired_y_offset = 0;
    private int actual_x_offset = 0, actual_y_offset = 0;
    // coordinates for the selection drag box
    private int startDragX = -1, startDragY = -1, currDragX = -1, currDragY = -1;
    protected Set<WUnit> selectedEntities = null;
    List<AI> m_ai_l = new LinkedList<AI>();
    gatech.mmpm.IDomain idomain = null;
    gatech.mmpm.tracer.ITracer tracer = null;
    HashMap<S3Action, Action> m_actionMaps = new HashMap<S3Action, Action>();

    public void setUserName(String userName) {
        _userName = userName;
    }

    public void gameLoop(int maxCycles) {
        long time = System.currentTimeMillis();
        long actTime;

        boolean need_to_redraw = true;

        if (tracer != null) {
            tracer.beginTrace();
            java.util.Properties prop = new java.util.Properties();
            prop.setProperty("domain", "s3");
            prop.setProperty("map", mapName);
            {
                String players_string = "";
                for (PlayerInput p : m_pi_l) {
                    players_string += "<player>" + p.m_playerID + "</player>";
                }
                prop.setProperty("players", players_string);
            }
            if (_userName != null) {
                prop.setProperty("user", _userName);
            }
            tracer.putMetadata(prop);
        }

        while (gameRunning) {
            actTime = System.currentTimeMillis();
            if (actTime - time >= REDRAWING_PERIOD) {
                int max_frame_step = MAX_FRAMESKIP;
                do {
                    time += REDRAWING_PERIOD;
                    if (!cycle(maxCycles)) {
                        gameRunning = false;
                    }
                    need_to_redraw = true;
                    actTime = System.currentTimeMillis();
                    max_frame_step--;
                } while (actTime - time >= REDRAWING_PERIOD && max_frame_step > 0);
                if ((actTime - time) > MAX_FRAMESKIP * REDRAWING_PERIOD) {
                    time = actTime;
                }
            } /*
             * if
             */

            /*
             * Redraw
             */
            if (need_to_redraw) {
                need_to_redraw = false;

                draw();
            } /*
             * if
             */

            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (maxCycles >= 0 && m_game.getCycle() > maxCycles) {
                gameRunning = false;
            }
        }

        if (tracer != null) {
            tracer.endTrace(idomain, getWinner());
            if (!tracer.success()) {
                System.err.println("There were some errors saving the trace:");
                System.err.println(tracer.getErrorMessage());
            }
        }

        container.getContentPane().remove(this);
        container.dispose();

        for (AI ai : m_ai_l) {
            ai.gameEnd();
        }
    }

    public S3App(String map, int traceInterval, List<PlayerInput> players,
            gatech.mmpm.tracer.ITracer a_tracer) throws Exception {

//        try {
//            idomain = new s3.mmpm.S3Domain();
//            d2.core.Config.setDomain(idomain);
//        } catch (gatech.mmpm.ConfigurationException e1) {
//            e1.printStackTrace();
//        }
        tracer = a_tracer;

        try {
            container = new JFrame("S3");
        } catch (HeadlessException e) {
            System.err.println("Error opening window");
            System.exit(1);
        }
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(SCREEN_X, SCREEN_Y));
        panel.setLayout(null);

        setBounds(0, 0, SCREEN_X, SCREEN_Y);
        panel.add(this);

        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Open configuration
        getConfigInfo(map, traceInterval, players, tracer);

        requestFocus();
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        m_font32 = new Font("Arial", Font.PLAIN, 32);
        m_font16 = new Font("Arial", Font.PLAIN, 16);

        selectedEntities = new HashSet<WUnit>();
    }

    // Reads the config file and loads the game info : mouse, AI, map etc
    private void getConfigInfo(String map, int traceInterval, List<PlayerInput> players,
            gatech.mmpm.tracer.ITracer a_tracer) throws Exception, IOException {

        tracer = a_tracer;
        mapName = map;
        // create the Game object if the file exists
        Document game_doc = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            // When BattleCity is a JAR
            InputStream f = this.getClass().getResourceAsStream("/" + mapName);
            game_doc = builder.build(f);
        } catch (Exception e) {
            // When it's not:
            game_doc = builder.build(mapName);
        }

        if (game_doc != null) {
            m_game = new S3(game_doc);
        } else {
            System.err.println("No MAP file found!");
        }

        m_pi_l = players;
        for (PlayerInput pi : players) {
            if (pi.m_inputType == PlayerInput.INPUT_MOUSE) {
                pi.m_inputType = PlayerInput.INPUT_MOUSE;
                mouse_player = pi.m_playerID;
            }
            if (pi.m_inputType == PlayerInput.INPUT_AI) {
                AI ai;

                if (pi.AIType.equals("ai-empty")) {
                    ai = new AIEmpty(pi.m_playerID);
                    ai.gameStarts();
                    pi.m_inputType = PlayerInput.INPUT_AI;
                } else if (pi.AIType.equals("ai-rush")) {
                    ai = new RushAI(pi.m_playerID);
                    ai.gameStarts();
                } else if (pi.AIType.equals("ai-rush2")) {
                    ai = new RushAI2(pi.m_playerID);
                    ai.gameStarts();
                } else if (pi.AIType.equals("ai-footmen-rush")) {
                    ai = new FootmenRush(pi.m_playerID);
                    ai.gameStarts();
                } else if (pi.AIType.equals("ai-archers-rush")) {
                    ai = new ArchersRush(pi.m_playerID);
                    ai.gameStarts();
                } else if (pi.AIType.equals("ai-catapults-rush")) {
                    ai = new CatapultRush(pi.m_playerID);
                    ai.gameStarts();
                } else if (pi.AIType.equals("ai-knights-rush")) {
                    ai = new KnightsRush(pi.m_playerID);
                    ai.gameStarts();
                } else if (pi.AIType.equals("ai-defensive-knights-rush")) {
                    ai = new DefensiveKnightsRush(pi.m_playerID);
                    ai.gameStarts();
                } else {
                    throw new Exception(pi.m_playerID + " AIType must be \"ai-empty\", \"ai-rush\" or \"ME\".");
                }

                m_ai_l.add(ai);
            }
            m_game.getPlayer(pi.m_playerID).setInputType(pi.m_inputType);

        }
        theHUD = new HUD(m_game, mouse_player);
        m_mouse_handler = new MouseHandler(SCREEN_X, SCREEN_Y, this);
        m_keyboardState = new KeyInputHandler();
        addMouseListener(m_mouse_handler);
        addMouseMotionListener(m_mouse_handler);
        addKeyListener(m_keyboardState);

    }

    private boolean cycle(int maxCycles) {
        int old_state = m_state;

        switch (m_state) {
            case STATE_INIT:
                m_state = init_cycle();
                break;
            case STATE_GAME:
                m_state = game_cycle();
                break;
            case STATE_QUITTING:
                m_state = quitting_cycle(maxCycles);
                break;
            default:
                return false;
        }

        if (old_state == m_state) {
            m_state_cycle++;
        } else {
            m_state_cycle = 0;
        }

        return true;
    }

    private boolean draw() {
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, SCREEN_X, SCREEN_Y);

        switch (m_state) {
            case STATE_INIT:
                init_draw(g);
                break;
            case STATE_GAME:
                game_draw(g);
                break;
            case STATE_QUITTING:
                quitting_draw(g);
                break;
        }
        g.dispose();
        strategy.show();

        return true;
    }

    private int init_cycle() {

        // Only wait for mouse click if there is one human in the game:
        {
            boolean isThereAnyHuman = false;
            for (PlayerInput pi : m_pi_l) {
                if (pi.m_inputType == PlayerInput.INPUT_MOUSE) {
                    isThereAnyHuman = true;
                }
            }
            if (!isThereAnyHuman) {
                return STATE_GAME;
            }
        }

        return STATE_INIT;
    }

    private void init_draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(m_font32);
        g.drawString(TITLE, (getWidth() - g.getFontMetrics().stringWidth(TITLE)) / 2, 235);
        g.setFont(m_font16);
        g.drawString(AUTHORS, (getWidth() - g.getFontMetrics().stringWidth(AUTHORS)) / 2, 295);
        g.drawString(TECH, (getWidth() - g.getFontMetrics().stringWidth(TECH)) / 2, 315);
        g.drawString(COMMENTS, (getWidth() - g.getFontMetrics().stringWidth(COMMENTS)) / 2, 355);
        g.drawString(COMMENTS2, (getWidth() - g.getFontMetrics().stringWidth(COMMENTS2)) / 2, 375);
        g.drawString(PLAY, (getWidth() - g.getFontMetrics().stringWidth(PLAY)) / 2, 415);
    }

    private int game_cycle() {
        List<S3Action> failedActions = new LinkedList<S3Action>();
        List<S3Action> actions; // = new LinkedList<Action>();
        int cycle = m_game.getCycle();
        boolean traceEntryStarted = false;
        long start_time = System.currentTimeMillis();
        long t1, t2, t3, t4;

        actions = update_input();

        m_keyboardState.cycle();
        if (selectedEntities.size() == 0) {
            m_game.setGameControls(m_keyboardState);
        }
        {
            boolean done = false;
            for (WUnit unit : selectedEntities) {
                S3Action tmp = theHUD.cycle(unit, m_keyboardState);
                if (tmp != null) {
                    selectedAction = tmp;
                    if (selectedAction != null && actionNextParamType(selectedAction) == -1) {
                        mousePlayerActions.add(selectedAction);
                        done = true;
                    }
                }
            }
            if (done) {
                cleanUpState();
            }
        }

        t1 = System.currentTimeMillis();

        if (!m_game.cycle(failedActions)) {
            // Save the last state:
            if (tracer != null) {
                tracer.beginGameCycle(m_game.getCycle());
                tracer.putGameState(s3.mmpm.Game2D2Converter.toGameState(m_game, idomain));
                tracer.endGameCycle();
            }
            return STATE_QUITTING;
        }

        t2 = System.currentTimeMillis();

        // Save trace entry:
        if (tracer != null) {
            if (m_game != null && (cycle % m_trace_interval) == 0 || !actions.isEmpty()) {
                traceEntryStarted = true;
                tracer.beginGameCycle(cycle);
                GameState gs = s3.mmpm.Game2D2Converter.toGameState(m_game, idomain);
                tracer.putGameState(gs);
                for (S3Action a : actions) {
                    Entity target = gs.getEntity("" + a.m_targetUnit);
                    if (target != null) {
                        List<Action> d2a = s3.mmpm.Game2D2Converter.toD2Action(a, gs, target.getowner());
                        for (Action act : d2a) {
                            m_actionMaps.put(a, act);
                            tracer.putAction(act);
                        }
                    }
                }
            }
        }

        t3 = System.currentTimeMillis();

        // perform the action
        performActions(actions);

        t4 = System.currentTimeMillis();

        for (S3Action a : failedActions) {

            if (tracer != null) {
                if (!traceEntryStarted) {
                    traceEntryStarted = true;
                    tracer.beginGameCycle(cycle);
                    // tracer.putGameState(s2.d2.Game2D2Converter.toGameState(m_game,idomain));
                }
                gatech.mmpm.Action a2 = m_actionMaps.get(a);
                if (a2 != null) {
                    tracer.putAbortedAction(a2);
                }
            }
        }

        if (traceEntryStarted) {
            tracer.endGameCycle();
        }

        if (TIME_DEBUG) {
            System.out.println("Time: " + start_time + ": input " + (t1 - start_time) + " - cycle "
                    + (t2 - t1) + " - trace " + (t3 - t2) + " - actions " + (t4 - t3));
        }

        // Scroll:
        if (m_keyboardState.m_keyboardStatus[39]) {
            inc_x_offset(S3PhysicalEntity.CELL_SIZE);
        }
        if (m_keyboardState.m_keyboardStatus[37]) {
            inc_x_offset(-S3PhysicalEntity.CELL_SIZE);
        }
        if (m_keyboardState.m_keyboardStatus[40]) {
            inc_y_offset(S3PhysicalEntity.CELL_SIZE);
        }
        if (m_keyboardState.m_keyboardStatus[38]) {
            inc_y_offset(-S3PhysicalEntity.CELL_SIZE);
        }
        actual_x_offset = (3 * actual_x_offset + desired_x_offset) / 4;
        actual_y_offset = (3 * actual_y_offset + desired_y_offset) / 4;

        return STATE_GAME;
    }

    private void game_draw(Graphics2D g) {
        m_game.draw(g, selectedEntities, actual_x_offset, actual_y_offset);
        // draw the HUD (only the first entity so it doesn't look weird)
        theHUD.draw(g, getRepresentativeUnit(), selectedAction, m_pi_l, m_game.getPlayer(theHUD.owner));
        drawDragBox(g);
    }

    /**
     * draws the box for drag and select
     *
     * @param g
     */
    private void drawDragBox(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.drawLine(startDragX, startDragY, startDragX, currDragY);
        g.drawLine(startDragX, startDragY, currDragX, startDragY);
        g.drawLine(startDragX, currDragY, currDragX, currDragY);
        g.drawLine(currDragX, startDragY, currDragX, currDragY);
    }

    private int quitting_cycle(int maxCycles) {

        if (maxCycles >= 0) {
            return STATE_QUIT;
        }

        return STATE_QUITTING;
    }

    private void quitting_draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(m_font32);
        String over = "GAME OVER";
        g.drawString(over, (getWidth() - g.getFontMetrics().stringWidth(over)) / 2, 235);

    }

    private List<S3Action> update_input() {
        List<S3Action> actions = new LinkedList<S3Action>();
        // VirtualController selected_vc;

        for (PlayerInput pi : m_pi_l) {
            switch (pi.m_inputType) {
                case PlayerInput.INPUT_MOUSE:
                    while (!mousePlayerActions.isEmpty()) {
                        S3Action a = mousePlayerActions.remove(0);
                        if (a == null) {
                            System.err.println("Mouse player generated null action!");
                        }
                        actions.add(a);
                    }

                    break;
                case PlayerInput.INPUT_AI: {
                    // Find the AI:
                    AI selected_AI = null;
                    for (AI ai : m_ai_l) {
                        if (ai.getPlayerId().equals(pi.m_playerID)) {
                            selected_AI = ai;
                        }
                    }

                    if (selected_AI != null) {
                        try {
                            selected_AI.game_cycle(m_game, m_game.getPlayer(pi.m_playerID), actions);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
        } // while

        return actions;
    }

    public S3 getGame() {
        return m_game;
    }

    /**
     * handles the start of a drag action
     *
     * @param current_screen_x
     * @param current_screen_y
     */
    public void mousePress(int current_screen_x, int current_screen_y) {
        if (m_state == STATE_GAME) {
            startDragX = current_screen_x;
            startDragY = current_screen_y;
            currDragX = current_screen_x;
            currDragY = current_screen_y;
        }
    }

    /**
     * handles dragging actions
     *
     * @param current_screen_x
     * @param current_screen_y
     */
    public void mouseDrag(int current_screen_x, int current_screen_y) {
        if (m_state == STATE_GAME) {
            currDragX = current_screen_x;
            currDragY = current_screen_y;
        }
    }

    /**
     * handles end of dragging actions, and selecting all units in the given
     * area. If multiple units are selected, only select the player's units. If
     * multiple units are still selected, only select troops.
     *
     * @param current_screen_x
     * @param current_screen_y
     */
    public void mouseRelease(int current_screen_x, int current_screen_y) {
        if (m_state == STATE_GAME) {
            if (current_screen_y > HUD.HUD_Y_LOC || selectedAction != null) {
                startDragX = -1;
                startDragY = -1;
                currDragX = -1;
                currDragY = -1;
                return;
            }

            // convert to map coords
            int map_x_start = startDragX + get_x_offset();
            int map_y_start = startDragY + get_y_offset();
            int map_x_end = current_screen_x + get_x_offset();
            int map_y_end = current_screen_y + get_y_offset();
            map_x_start = map_x_start / CELL_SIZE;
            map_y_start = map_y_start / CELL_SIZE;
            map_x_end = map_x_end / CELL_SIZE;
            map_y_end = map_y_end / CELL_SIZE;

            selectedEntities.clear();

            // get selected units
            for (int i = map_x_start < map_x_end ? map_x_start : map_x_end; i < (map_x_start > map_x_end ? map_x_start
                    : map_x_end) + 1; i++) {
                for (int j = map_y_start < map_y_end ? map_y_start : map_y_end; j < (map_y_start > map_y_end ? map_y_start
                        : map_y_end) + 1; j++) {
                    WUnit unit = m_game.entityAt(i, j);
                    if (null != unit) {
                        selectedEntities.add(unit);
                    }
                }
            }

            // clean up drag constants
            startDragX = -1;
            startDragY = -1;
            currDragX = -1;
            currDragY = -1;

            // just use this one
            if (selectedEntities.size() <= 1) {
                return;
            }

            List<WUnit> toRemove = new LinkedList<WUnit>();
            String player = null;
            // get player
            for (WPlayer p : m_game.getPlayers()) {
                if (p.getInputType() == PlayerInput.INPUT_MOUSE) {
                    player = p.owner;
                    break;
                }
            }
            // get enemy units
            for (WUnit u : selectedEntities) {
                if (u.owner == null || !u.owner.equals(player)) {
                    toRemove.add(u);
                }
            }

            if (toRemove.size() == selectedEntities.size()) {
                // whoops, leave one;
                WUnit toKeep = getRepresentativeUnit();
                selectedEntities.clear();
                selectedEntities.add(toKeep);
                return;
            } else {
                selectedEntities.removeAll(toRemove);
            }

            // just use this one
            if (selectedEntities.size() == 1) {
                return;
            }

            toRemove.clear();

            // remove Buildings
            for (WUnit u : selectedEntities) {
                if (u instanceof WBuilding) {
                    toRemove.add(u);
                }
            }

            if (toRemove.size() == selectedEntities.size()) {
                // select the buildings
                return;
            } else {
                selectedEntities.removeAll(toRemove);
            }

        }
    }

    public void rightMouseClick(int current_screen_x, int current_screen_y) {
        if (m_state == STATE_GAME) {
            S3PhysicalEntity clicked = null;
            if (current_screen_y < HUD.HUD_Y_LOC) {
                int map_x = current_screen_x + get_x_offset();
                int map_y = current_screen_y + get_y_offset();
                clicked = m_game.entityVisuallyAt(map_x, map_y);
                WUnit selectedEntity = getRepresentativeUnit();
                if (selectedEntity != null && selectedEntity.getOwner().equals(mouse_player)) {
                    // if selected peasant, and click mine or tree -> harvest
                    if (selectedEntity instanceof WPeasant && clicked != null
                            && clicked instanceof WGoldMine) {
                        if (selectedAction == null) {
                            for (WUnit unit : selectedEntities) {
                                selectedAction = new S3Action(unit.entityID,
                                        S3Action.ACTION_HARVEST, clicked.entityID);
                                mousePlayerActions.add(selectedAction);
                            }
                            selectedAction = null;
                            theHUD.resetButtons();
                            return;
                        }
                    }
                    if (selectedEntity instanceof WPeasant && clicked == null) {
                        S3PhysicalEntity e = m_game.getEntity(map_x / S3PhysicalEntity.CELL_SIZE,
                                map_y / S3PhysicalEntity.CELL_SIZE);
                        if (selectedAction == null && e != null) {
                            if (e instanceof WOGrass) {
                                for (WUnit unit : selectedEntities) {
                                    selectedAction = new S3Action(unit.entityID,
                                            S3Action.ACTION_MOVE, e.getX(), e.getY());
                                    mousePlayerActions.add(selectedAction);
                                }
                                selectedAction = null;
                                theHUD.resetButtons();
                                return;
                            } else {                                
                                for (WUnit unit : selectedEntities) {
                                    selectedAction = new S3Action(unit.entityID,
                                            S3Action.ACTION_HARVEST, e.getX(), e.getY());
                                    mousePlayerActions.add(selectedAction);
                                }
                                selectedAction = null;
                                theHUD.resetButtons();
                                return;
                            }
                        } 
                    }

                    // if selected wtroop, and click on enemy -> attack
                    if (selectedEntity instanceof WTroop && clicked != null
                            && clicked.getOwner() != null
                            && !clicked.getOwner().equals(mouse_player)) {
                        if (selectedAction == null) {
                            for (WUnit unit : selectedEntities) {
                                selectedAction = new S3Action(unit.entityID,
                                        S3Action.ACTION_ATTACK, clicked.entityID);
                                mousePlayerActions.add(selectedAction);
                            }
                            selectedAction = null;
                            theHUD.resetButtons();
                            return;
                        }
                    }
                }

            }
            snapToCenter(current_screen_x, current_screen_y);
        }
    }

    /**
     * handle mouse click events.
     *
     * @param current_screen_x
     * @param current_screen_y
     */
    public void mouseClick(int current_screen_x, int current_screen_y) {
        int map_x = current_screen_x + get_x_offset();
        int map_y = current_screen_y + get_y_offset();

        if (m_state == STATE_INIT) {
            boolean isThereAnyHuman = false;
            for (PlayerInput pi : m_pi_l) {
                if (pi.m_inputType == PlayerInput.INPUT_MOUSE) {
                    isThereAnyHuman = true;
                }
            }
            if (isThereAnyHuman) {
                m_state = STATE_GAME;
            }
            return;
        }

        if (m_state == STATE_GAME) {
            gameMouseClick(current_screen_x, current_screen_y, map_x, map_y);
        }

        if (m_state == STATE_QUITTING) {
            gameRunning = false;
        }

    }

    private void gameMouseClick(int current_screen_x, int current_screen_y, int map_x, int map_y) {
        S3PhysicalEntity clicked = null;
        boolean click_on_map = false;

        Pair<Integer, Integer> retValue = m_game.isInMiniMap(current_screen_x, current_screen_y);
        if (retValue != null) {
            snapSetToCenter(retValue.m_a, retValue.m_b);
            return;
        }

        if (current_screen_y < HUD.HUD_Y_LOC) {
            // click on map:
            clicked = m_game.entityVisuallyAt(map_x, map_y);
            click_on_map = true;
            if (selectedAction != null
                    && (actionNextParamType(selectedAction) == 1 || actionNextParamType(selectedAction) == 3)
                    && clicked == null) {
                selectedAction.m_parameters.add(map_x / S3PhysicalEntity.CELL_SIZE);
                selectedAction.m_parameters.add(map_y / S3PhysicalEntity.CELL_SIZE);
                for (WUnit unit : selectedEntities) {
                    S3Action sa = new S3Action(selectedAction);
                    sa.m_targetUnit = unit.entityID;
                    mousePlayerActions.add(sa);
                }
                cleanUpState();
                return;
            }
            if (selectedAction != null
                    && (actionNextParamType(selectedAction) == 2 || actionNextParamType(selectedAction) == 3)
                    && clicked != null) {
                selectedAction.m_parameters.add(clicked.entityID);
                for (WUnit unit : selectedEntities) {
                    S3Action sa = new S3Action(selectedAction);
                    sa.m_targetUnit = unit.entityID;
                    mousePlayerActions.add(sa);
                }
                cleanUpState();
                return;
            }
        } else {
            boolean done = false;
            for (WUnit unit : selectedEntities) {
                selectedAction = theHUD.mouseClick(unit, current_screen_x, current_screen_y);
                if (selectedAction != null && actionNextParamType(selectedAction) == -1) {
                    mousePlayerActions.add(selectedAction);
                    done = true;
                }
            }
            if (done) {
                cleanUpState();
                return;
            }
        }

        if (click_on_map) {
            selectedEntities.removeAll(selectedEntities);
            if (clicked != null && clicked instanceof WUnit) {
                selectedEntities.add((WUnit) clicked);
                snapToCenter(current_screen_x, current_screen_y);
                selectedAction = null;
                theHUD.resetButtons();
                return;
            }
        }
    }

    /**
     *
     */
    private void cleanUpState() {
        selectedAction = null;
        selectedEntities.removeAll(selectedEntities);
        theHUD.resetButtons();
    }

    private WUnit getRepresentativeUnit() {
        WUnit unit = null;
        Iterator<WUnit> iter = selectedEntities.iterator();
        if (iter.hasNext()) {
            unit = iter.next();
        }
        return unit;
    }

    public void snapSetToCenter(int current_screen_x, int current_screen_y) {
        set_inc_x_offset(current_screen_x - 800 / 2);
        set_inc_y_offset(current_screen_y - 525 / 2); // 525 is the fix
        // for HUD considering 1/8th of screen for HUD
    }

    public void snapToCenter(int current_screen_x, int current_screen_y) {
        inc_x_offset(current_screen_x - 800 / 2);
        inc_y_offset(current_screen_y - 525 / 2); // 525 is the fix
        // for HUD considering 1/8th of screen for HUD
    }

    // -1 action complete
    // 0 click on HUD
    // 1 map location
    // 2 map entity
    // 3 either location or entity
    private int actionNextParamType(S3Action action) {
        WUnit u = null;
        if (action != null) {
            u = m_game.getUnit(action.m_targetUnit);
        } else {
            return -1;
        }
        if (u != null) {
            if (action.m_action == S3Action.ACTION_TRAIN) {
                if (action.m_parameters.size() < 1) {
                    return 0;
                }
            } else if (action.m_action == S3Action.ACTION_ATTACK) {
                if (action.m_parameters.size() < 1) {
                    return 2;
                }
            } else if (action.m_action == S3Action.ACTION_BUILD) {
                if (action.m_parameters.size() < 1) {
                    return 0;
                }
                if (action.m_parameters.size() < 2) {
                    return 1;
                }
            } else if (action.m_action == S3Action.ACTION_HARVEST) {
                if (action.m_parameters.size() < 1) {
                    return 3;
                }
            } else if (action.m_action == S3Action.ACTION_MOVE) {
                if (action.m_parameters.size() < 1) {
                    return 1;
                }
            } else if (action.m_action == S3Action.ACTION_REPAIR) {
                if (action.m_parameters.size() < 1) {
                    return 2;
                }
            } else if (action.m_action == S3Action.ACTION_STAND_GROUND) {
                if (action.m_parameters.size() < 1) {
                    return -1;
                }
            }
        }

        return -1;
    }

    private void performActions(List<S3Action> actions) {
        for (S3Action action : actions) {
            WUnit u = m_game.getUnit(action.m_targetUnit);
            if (u != null) {
                u.performAction(action);
            }
        }
    }

    public String getWinner() {
        String owner = null;
        for (S3Entity e : m_game.getUnits()) {
            if (null == owner) {
                owner = e.getOwner();
            } else {
                if (!owner.equals(e.getOwner())) {
                    // we have units from more than one player
                    System.out.println("Two Players remaining: '" + owner + "' and '"
                            + e.getOwner() + "'");
                    return null;
                }
            }

        }

        // this is the only player with units; they must have won:
        for (PlayerInput p : m_pi_l) {
            if (p.m_playerID.equals(owner)) {
                return p.m_playerName;
            }
        }

        return owner;
    }

    public int get_x_offset() {
        return actual_x_offset;
    }

    public int get_y_offset() {
        return actual_y_offset;
    }

    public void inc_x_offset(int i_x_offset) {
        desired_x_offset += i_x_offset;
        if (desired_x_offset < 0) {
            desired_x_offset = 0;
        }
        if (desired_x_offset > (m_game.getMap().layers[0].map_width * S3PhysicalEntity.CELL_SIZE) - SCREEN_X) {
            desired_x_offset = (m_game.getMap().layers[0].map_width * S3PhysicalEntity.CELL_SIZE) - SCREEN_X;
        }
    }

    public void inc_y_offset(int i_y_offset) {
        desired_y_offset += i_y_offset;
        if (desired_y_offset < 0) {
            desired_y_offset = 0;
        }
        if (desired_y_offset > (m_game.getMap().layers[0].map_height * S3PhysicalEntity.CELL_SIZE) - (SCREEN_Y - 92)) // Fix
        // for HUD
        {
            desired_y_offset = (m_game.getMap().layers[0].map_height * S3PhysicalEntity.CELL_SIZE) - (SCREEN_Y - 92);
        }
    }

    public void set_inc_x_offset(int i_x_offset) {

        desired_x_offset = i_x_offset;
        if (desired_x_offset < 0) {
            desired_x_offset = 0;
        }
        if (desired_x_offset > (m_game.getMap().layers[0].map_width * S3PhysicalEntity.CELL_SIZE) - 800) {
            desired_x_offset = (m_game.getMap().layers[0].map_width * S3PhysicalEntity.CELL_SIZE) - 800;
        }
    }

    public void set_inc_y_offset(int i_y_offset) {
        desired_y_offset = i_y_offset;
        if (desired_y_offset < 0) {
            desired_y_offset = 0;
        }
        if (desired_y_offset > (m_game.getMap().layers[0].map_height * S3PhysicalEntity.CELL_SIZE) - 525) // Fix
        // for HUD
        {
            desired_y_offset = (m_game.getMap().layers[0].map_height * S3PhysicalEntity.CELL_SIZE) - 525;
        }
    }
}
