/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s3.base;

import gatech.mmpm.ConfigurationException;
import gatech.mmpm.learningengine.IMEExecutor;
import gatech.mmpm.learningengine.MEExecutorFactory;
import gatech.mmpm.tracer.ITracer;
import jargs.gnu.CmdLineParser;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author santi
 */
public class Main {

    public static ITracer configureTracer(String appArg) {
        try {
            return gatech.mmpm.tracer.TracerFactory.BuildTracer(appArg);
        } catch (gatech.mmpm.ConfigurationException ex) {
            System.err.println("Error creating the tracer: ");
            System.err.println(ex.getMessage());
            System.err.println("Traces will not be generated");
            ex.printStackTrace();
            return null;
        }
    }

    public static void printUsage() {
        System.out.println("S3: play S3 according to the config file.");
        System.out.println();
        System.out.println("Usage: S3 -m map -i interval [-t method] [-u user] [-p playerType|idname|AIType|ME]...");
        System.out.println();
        System.out.println("\t-m|--map: map file rute name.");
        System.out.println("\t-i|--interval: int, interval trace.");
        System.out.println(gatech.mmpm.tracer.TracerFactory.getUserFriendlyHelp());
        System.out.println("\t-u|--user: player name who generates the trace.");
        System.out.println("\t-p|--player: playerType|idname|AIType|ME. Note: | is a separator of player fields.");
        System.out.println("\t             Where playerType: an int: ");
        System.out.println("\t		             INPUT_NONE = -1");
        System.out.println("\t		             INPUT_MOUSE = 0");
        System.out.println("\t		             INPUT_AI = 1");
        System.out.println("\t		             INPUT_EXTERNAL = 2");
        System.out.println("\t             Where idname: player name");
        System.out.println("\t             Where AIType (in case playerType = 1): ai-random|ME");
        System.out.println("\t             Where ME (in case AIType = ME):");
        System.out.println(MEExecutorFactory.getUserFriendlyHelp());
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  S3 -t file:trace.xml config.xml -p 0|Peter|ia-random -m ./map1.xml -i 50");
        System.out.println("     Launches S3 and save the traces to trace.xml");
        System.out.println();
        System.out.println("  S3 -t file config.xml [...]");
        System.out.println("     Launches S3 and save the trace to a file whose name");
        System.out.println("     is chosen based on the current time.");
        System.out.println();
        System.out.println("  S3 -t remote:S3portal.com:8888 config.xml [...]");
        System.out.println("     Launches S3 and send the trace to the server S3portal.com");
    }

    public static void main(String args[]) {
        internalMain(args, -1);
    }

    public static String internalMain(String args[], int maxCycles) {

        CmdLineParser parser = new CmdLineParser();

        CmdLineParser.Option traceOpt = parser.addStringOption('t', "trace");
        CmdLineParser.Option userOpt = parser.addStringOption('u', "user");
        CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");

        CmdLineParser.Option playerOpt = parser.addStringOption('p', "player");
        CmdLineParser.Option mapOpt = parser.addStringOption('m', "map");
        CmdLineParser.Option intervalOpt = parser.addIntegerOption('i', "interval");

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(1);
        }

        boolean help = (Boolean) parser.getOptionValue(helpOpt, false);
        if (help) {
            printUsage();
            System.exit(0);
        }

        String saveTraceOpt;
        String userName = null;
        String map = null;
        Integer traceInterval = -1;
        List<PlayerInput> players = new LinkedList<PlayerInput>();
        ITracer tracerUsed = null;

        saveTraceOpt = (String) parser.getOptionValue(traceOpt);
        userName = (String) parser.getOptionValue(userOpt);
        map = (String) parser.getOptionValue(mapOpt);
        if (map==null) map = "maps/ISLANDS2.xml";
        traceInterval = (Integer) parser.getOptionValue(intervalOpt);
        if (traceInterval==null) traceInterval = 50;

        // Analyze properties parameters and create the Properties
        // object.
        java.util.Vector<?> propStrings = parser.getOptionValues(playerOpt);
        for (Object prop : propStrings) {
            String current = (String) prop;
            // We must split the string using the '|' as separator.
            String[] splitStr = current.split("\\|");
            if ((splitStr.length < 2) || (splitStr.length > 4)) {
                printUsage();
                System.out.println("A player specification must be something like -p int|id|name|AItype|ME");
                return null;
            }
            PlayerInput pi = new PlayerInput();
            pi.m_inputType = new Integer(splitStr[0]);
            if ((pi.m_inputType < 0) || (pi.m_inputType > 2)) {
                System.out.println("A Type of a player specification must be an integer:");
                System.out.println("		INPUT_NONE = -1");
                System.out.println("		INPUT_MOUSE = 0");
                System.out.println("		INPUT_AI = 1");
                return null;
            }
            pi.m_playerID = splitStr[1];
            pi.m_playerName = splitStr[1];
            if (splitStr.length > 2) {
                pi.AIType = splitStr[2];
                if (splitStr.length > 3) {
                    pi.ME = splitStr[3];
                }
            }
            players.add(pi);
        } // for
        
        // default players: AI vs human
        if (players.size()!=2) {
            {
                PlayerInput pi = new PlayerInput();
                pi.m_inputType = PlayerInput.INPUT_MOUSE;
                pi.m_playerID = "player1";
                pi.m_playerName = "Human";
                players.add(pi);
            }
            {
                PlayerInput pi = new PlayerInput();
                pi.m_inputType = PlayerInput.INPUT_AI;
                pi.m_playerID = "player2";
                pi.m_playerName = "FootmenRush";
                pi.AIType = "ai-footmen-rush";
                players.add(pi);
            }
        }

        if (saveTraceOpt != null) {
            tracerUsed = configureTracer(saveTraceOpt);
        }


        String[] remainingOpts = parser.getRemainingArgs();

        if (remainingOpts.length > 0) {
            printUsage();
            System.exit(1);
        }

        // At this point, the parameters have been validated. Start!
        try {
            S3App app = new S3App(map, traceInterval, players, tracerUsed);
            if (userName != null) {
                app.setUserName(userName);
            }
            app.gameLoop(maxCycles);
            String retval = app.getWinner();
            return retval;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
