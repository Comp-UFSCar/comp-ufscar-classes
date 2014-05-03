/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.tracer;

import gatech.mmpm.Action;
import gatech.mmpm.Entity;
import gatech.mmpm.Entry;
import gatech.mmpm.GameState;
import gatech.mmpm.GraphMap;
import gatech.mmpm.Map;
import gatech.mmpm.Trace;
import gatech.mmpm.TwoDMap;
import gatech.mmpm.IDomain;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Class that parses an XML file that contains a Trace.
 * <p>
 * It is completely static.
 * 
 * @author Marco Antonio and Pedro Pablo Gomez Martin, based on ParseLmxTrace.
 */
public class TraceParser {

    /**
     * Parse the file and return the trace or null if some
     * error happens.
     * @param fileName Name of the file where the trace is
     * load. If it is a zip file, it load the trace from the
     * very first entry of the file.
     * @param domain Game domain of the traces been parsed.
     *
     * @return The trace loaded from the file or null when
     * the parser is not able to load it from file.
     */
    public static Trace parse(String fileName, IDomain domain) {
        java.io.InputStream input;
        try {
            if (fileName.endsWith(".zip")) {
                java.util.zip.ZipFile zf = new java.util.zip.ZipFile(fileName);
                java.util.zip.ZipEntry ze = zf.entries().nextElement();
                input = zf.getInputStream(ze);
            } else {
                input = new java.io.FileInputStream(fileName);
            }
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return parse(input, domain);

    } // parse(fileName)

    /**
     * Parse a trace from an input stream.
     *
     * @param input Input stream where the trace is loaded from.
     * @param domain Game domain of the traces been parsed.
     *
     * @return The trace loaded from the stream or null when
     * the parser is not able to load it.
     */
    public static Trace parse(java.io.InputStream input, IDomain domain) {
        Document doc;

        // Build the XML document
        try {
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        // Parse the XML
        Element root = doc.getRootElement();

        return parse(root, domain);

    } // parse(InputStream)

    /**
     * Parse a trace from an XML element (org.jdom).
     *
     * @param traceElement XML element where the trace is loaded from.
     * @param domain Game domain of the traces been parsed.
     *
     * @return The trace loaded from the stream or null when
     * the parser is not able to load it.
     */
    public static Trace parse(Element traceElement, IDomain domain) {

        String domainName = null;

        //Get the basic info about the trace file
        Element info = traceElement.getChild("info");
        domainName = info.getChildTextTrim("domain");

        // Check if domain == packageInfo...
        if (!domainName.equals(domain.getName())) {
            System.err.println("Error: trying to load a trace from other domain.");
            System.err.println(" We expected '" + domain.getName() + "' but we found '" + domainName + "'");
            return null;
        }

        Trace trace = new Trace(domainName, info.getChildTextTrim("map"));

        Element log = traceElement.getChild("log");
        List<Element> entries = log.getChildren();
        GameState prev_gs = null;

        for (Element entry : entries) {
            Entry newEntry = new Entry(Integer.parseInt(entry.getAttribute("time").getValue()));
            // Game state
            GameState gs = null;

            if (prev_gs == null) {
                try {
                    gs = parseGameState(entry.getChild("gamestate"), domain);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
                newEntry.addGameState(gs);
            } else {
                try {
                    if (entry.getChild("gamestate")!=null) {
                        gs = parseDifferenceGameState(entry.getChild("gamestate"), prev_gs, domain);
                    } else {
                        gs = prev_gs;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
                newEntry.addGameState(gs);
            }
            prev_gs = gs;

            // Actions
            List<Element> actions = entry.getChildren("Action");

            for (Element xmlAction : actions) {
                Action newAction = Action.loadFromXML(xmlAction);
                //	newAction.initializeActionConditions();
                newEntry.addAction(newAction);
            }

            // Aborted Actions
            List<Element> abortedActions = entry.getChildren("AbortedAction");

            for (Element xmlAction : abortedActions) {
                Action newAction = Action.loadFromXML(xmlAction);
                //	newAction.initializeActionConditions();
                newEntry.addAbortedAction(newAction);
            }

            //add the entry to the trace object
            trace.addEntry(newEntry);
        }

        return trace;

    } // parse(Element)

    /**
     * Parse the Game State from an input stream. In case of error, returns null.
     *
     * @param input Input Stream.
     * @param domain Game domain of the traces been parsed.
     *
     * @return The game state object with the information extracted
     * from the XML.
     * @throws Exception if the game state couldn't be loaded.
     */
    public static GameState parseGameState(java.io.InputStream input, IDomain domain) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        doc = builder.build(input);
        Element root = doc.getRootElement();
        return parseGameState(root, domain);
    }

    /**
     * Parse the Game State from the XML. In case of error, returns null.
     *
     * @param gamestate_xml Info extracted from the XML file.
     * @param domain Game domain of the traces been parsed.
     *
     * @return The game state object with the information extracted
     * from the XML (or null if it couldn't be loaded).
     */
    public static GameState parseGameState(Element gamestate_xml, IDomain domain) throws Exception {

        List<Element> entities = (gamestate_xml.getChildren());

        //Create a new object of type GameState
        GameState gs = new GameState();

        for (Element entity : entities) {
            //Only checking for non-map-entities
            if (entity.getChild("type").getValue().equals("map")) {
                //map-entities
                int width = Integer.parseInt(entity.getChild("width").getValue());
                int height = Integer.parseInt(entity.getChild("height").getValue());
                float cell_width = Float.parseFloat(entity.getChild("cell-width").getValue());
                float cell_height = Float.parseFloat(entity.getChild("cell-height").getValue());
                Map map = new TwoDMap(width, height, cell_width, cell_height);

                List<Element> rows = entity.getChild("background").getChildren();

                for (int i = 0; i < height; i++) {
                    String row = rows.get(i).getValue();
                    for (int j = 0; j < width; j++) {
                        char mapChar = row.charAt(j);
                        map.setCellLocation(mapChar, new int[]{j, i}, domain);
                    }
                }
                gs.addMap(map);
            } else if (entity.getChild("type").getValue().equals("GraphMap")) {
                GraphMap map = (GraphMap) GraphMap.loadFromXML(entity, domain);
                gs.addMap(map);
            } else {
                Entity gsEntity = domain.getEntityByName(
                    entity.getChild("type").getValue(),
                    entity.getAttributeValue("id"),
                    null);

                if (gsEntity == null) {
                    throw new Exception("Entity '" + entity.getChild("type").getValue() + "' cannot be created");
                }

                List<Element> features = entity.getChildren();

                for (Element feature : features) {
                    if (!feature.getName().equals("type")) {
                        gsEntity.setFeatureValue(feature.getName(), feature.getValue());
                    }
                }

                // Add the entity to the gameState object
                gs.addEntity(gsEntity);
            }

        }

        return gs;
    }

    /**
     * Parse the Game State from the XML. In case of error, returns null.
     *
     * @param gamestate_xml Info extracted from the XML file.
     * @param domain Game domain of the traces been parsed.
     *
     * @return The game state object with the information extracted
     * from the XML (or null if it couldn't be loaded).
     */
    public static GameState parseDifferenceGameState(Element gamestate_xml, GameState prev_gameState, IDomain domain) throws Exception {

        List<Element> entities = (gamestate_xml.getChildren());

        //Create a new object of type GameState
        GameState gs = new GameState();

        for (Element entity : entities) {
            if (entity.getChild("type") != null || entity.getAttributeValue("id").equals("0")) {
                //this means there is some difference between the entity in this gamestate and the prev_gameState
                if (entity.getAttributeValue("id").equals("0")) {
                    Map map = (Map) prev_gameState.getMap().clone();
                    String type = "map";
                    if (entity.getChild("type")!=null && entity.getChild("type").equals("map")) type = "map";
                    if (entity.getChild("type")!=null && entity.getChild("type").equals("GraphMap")) type = "GraphMap";
                    if (type.equals("map") || map instanceof TwoDMap) {
                        //there is a prevEntity as it is a map

                        if (entity.getChild("background")!=null) {
                            List<Element> rows = entity.getChild("background").getChildren();
                            float height = ((TwoDMap)map).getSizeInDimension(1);
                            float width = ((TwoDMap)map).getSizeInDimension(0);

                            for (int i = 0; i < height; i++) {
                                String row = rows.get(i).getValue();
                                if (!row.equals("")) {
                                    for (int j = 0; j < width; j++) {
                                        char mapChar = row.charAt(j);
                                        map.setCellLocation(mapChar, new int[]{j, i}, domain);
                                    }
                                }
                            }
                        }
                        gs.addMap(map);
                    }
                    if (type.equals("GraphMap") || map instanceof GraphMap) {
                        gs.addMap(map);
                    }
                } else {
                    Entity gsEntity = domain.getEntityByName(
                        entity.getChild("type").getValue(),
                        entity.getAttributeValue("id"),
                        null);

                    if (gsEntity == null) {
                        throw new Exception("Entity '" + entity.getChild("type").getValue() + "' cannot be created");
                    }

                    //set all the features that are the same after locating the Entity in the previous state
                    Entity prevEntity = prev_gameState.getEntity(gsEntity.getentityID());

                    if (prevEntity != null) {
                        //if the prevState had this entity, then ditch the gsEntity, and clone the prevEntity :)
                        gsEntity = null;
                        gsEntity = (Entity) prevEntity.clone();
                    }

                    List<Element> features = entity.getChildren();
                    //set all the difference features
                    for (Element feature : features) {
                        if (!feature.getName().equals("type")) {
                            gsEntity.setFeatureValue(feature.getName(), feature.getValue());
                        }
                    }

                    // Add the entity to the gameState object
                    gs.addEntity(gsEntity);
                }
            } else {
                //copy the prev_entity completely
                Entity prevEntity = prev_gameState.getEntity(entity.getAttributeValue("id"));
                if (prevEntity == null) {
                    System.err.println("parseDifferenceGameState: couldn't find entity " + entity.getAttributeValue("id") + " in previous state");
                }
                Entity gsEntity = (Entity) prevEntity.clone();

                List<Element> features = entity.getChildren();
                //set all the difference features
                for (Element feature : features) {
                    if (!feature.getName().equals("type")) {
                        gsEntity.setFeatureValue(feature.getName(), feature.getValue());
                    }
                }

                // Add the entity to the gameState object
                gs.addEntity(gsEntity);
            }

        }

        return gs;
    }

    /**
     * Constructor. It is keep private to avoid instances
     * of this class to be created.
     */
    private TraceParser() {
    }
} // class TraceParser

