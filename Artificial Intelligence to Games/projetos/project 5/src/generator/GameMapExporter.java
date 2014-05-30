package generator;

import java.io.File;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author lucasdavid
 */
public class GameMapExporter {

    Document doc;
    GameMap map;

    public GameMapExporter(GameMap _map) {
        
        map = _map;
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            doc = docBuilder.newDocument();
            
            // <map version="1.0" orientation="orthogonal" width="33" height="24" tilewidth="64" tileheight="64">
            Element r = doc.createElement("map");
            r.setAttribute("version", "1.0");
            r.setAttribute("orientation", "orthogonal");
            r.setAttribute("width",  String.valueOf(map.width()));
            r.setAttribute("height", String.valueOf(map.height()));
            r.setAttribute("tilewidth",  "64");
            r.setAttribute("tileheight", "64");
            
            Element properties = doc.createElement("properties");
            
            // <property name="name" value="Blackrock"/>
            Element property = doc.createElement("property");
            property.setAttribute("name", "name");
            property.setAttribute("value", map.name);
            
            // all properties
            properties.appendChild(property);
            
            // <tileset firstgid="1" name="graphics" tilewidth="64" tileheight="64">
            Element tileset = doc.createElement("tileset");
            tileset.setAttribute("firstgid", "1");
            tileset.setAttribute("name", "graphics");
            tileset.setAttribute("tilewidth", "64");
            tileset.setAttribute("tileheight", "64");
            
            // <image source="graphics2x.png" width="320" height="640"/>
            Element image = doc.createElement("image");
            image.setAttribute("source", "graphics2x.png");
            image.setAttribute("width" , "320");
            image.setAttribute("height", "640");
            
            // all elements of tileset
            tileset.appendChild(image);
            
            // <layer name="Tile Layer 1" width="33" height="24">
            Element layer = doc.createElement("layer");
            layer.setAttribute("name", "Tile Layer 1");
            layer.setAttribute("width" , String.valueOf(map.width()));
            layer.setAttribute("height", String.valueOf(map.height()));

            Element data = doc.createElement("data");
            
            for (int i = 0; i < map.height(); i++) {
                for (int j = 0; j < map.width(); j++) {
                    Element tile = doc.createElement("tile");
                    tile.setAttribute("gid", String.valueOf(map.field(i, j)));
                    data.appendChild(tile);
                }
            }
            
            // layer > data
            layer.appendChild(data);
            
            // <objectgroup name="messages" width="33" height="24">
            Element objectgroup = doc.createElement("objectgroup");
            objectgroup.setAttribute("name", "messages");
            objectgroup.setAttribute("width", "33");
            objectgroup.setAttribute("height", "24");

            r.appendChild(properties);
            r.appendChild(tileset);
            r.appendChild(layer);
            r.appendChild(objectgroup);
            
            doc.appendChild(r);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public GameMapExporter export() {
        System.out.println("GameMapExporter@export()");

        try {
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/generatedmaps/map_" + (new Date()).getTime() + ".tmx"));
            transformer.transform(source, result);
            
            System.out.println("GameMapExporter@export() has finished.");

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

        return this;
    }

}
