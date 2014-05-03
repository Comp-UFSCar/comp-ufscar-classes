/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;


import java.io.File;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ParseLmxTrace {

	Document doc;

	String packageInfo;
	
	public ParseLmxTrace(String packageInfo)
	{
		this.packageInfo = packageInfo;
	}
	
	public GameState parseDifferenceGameState(Element gamestate_xml, GameState prev_gameState, gatech.mmpm.IDomain idomain) throws Exception {
		List<Element> entities = (gamestate_xml.getChildren());

		//Create a new object of type GameState
		GameState gs = (GameState) new GameState();
		
		for (Element entity : entities)
		{
			if(entity.getChild("type") != null) {
				//this means there is some difference between the entity in this gamestate and the prev_gameState
				if(entity.getChild("type").getValue().equals("map")) {
					//there is a prevEntity as it is a map
					Map map = (TwoDMap) prev_gameState.getMap().cloneWithSameEntities();

					List <Element> rows = entity.getChild("background").getChildren();
					float height = map.getCellSizeInDimension(0);
					float width = map.getCellSizeInDimension(1);

					for(int i=0; i<height; i++)
					{
						String row = rows.get(i).getValue();
						if (row.equals(""))
							for(int j=0; j<width; j++)
							{
								char mapChar = row.charAt(j);
								map.setCellLocation(mapChar,new int[]{j,i},idomain);						
							}
					}
					gs.addMap(map);
				} else {
					Entity gsEntity = null;

					//set all the features that are the same after locating the Entity in the previous state
					Entity prevEntity = prev_gameState.getEntity(entity.getAttributeValue("id"));

					if(prevEntity != null) {
						//if the prevState had this entity, then ditch the gsEntity, and clone the prevEntity :)
						gsEntity = (Entity)prevEntity.clone();
					} else {
						gsEntity = idomain.getEntityByName(
								entity.getChild("type").getValue(), 
								entity.getAttributeValue("id"),
								null);

						if (gsEntity == null)
							throw new Exception("Entity '" + entity.getChild("type").getValue() + "' cannot be created");						
					}

					List <Element> features = entity.getChildren();
					//set all the difference features
					for (Element feature : features) {
						if (!feature.getName().equals("type"))
							gsEntity.setFeatureValue(feature.getName(), feature.getValue());
					}

					// Add the entity to the gameState object
					gs.addEntity(gsEntity);
				}
			}
			else {
				if (entity.getAttributeValue("id").equals("0")) {
					//map
					Map map = (TwoDMap) prev_gameState.getMap().cloneWithSameEntities();
					gs.addMap(map);
				}
				else {
					//copy the prev_entity completely
					Entity prevEntity = prev_gameState.getEntity(entity.getAttributeValue("id"));
					Entity gsEntity = (Entity)prevEntity.clone();

					List <Element> features = entity.getChildren();
					//set all the difference features
					for (Element feature : features) {
						if (!feature.getName().equals("type"))
							gsEntity.setFeatureValue(feature.getName(), feature.getValue());
					}

					// Add the entity to the gameState object
					gs.addEntity(gsEntity);
				}
			}

		}

		return gs;
	}

	
	public GameState parseGameState(Element gamestate_xml, gatech.mmpm.IDomain idomain) throws Exception {
		List<Element> entities = (gamestate_xml.getChildren());

		//Create a new object of type GameState
		GameState gs = new GameState();

		for (Element entity : entities)
		{
			if (entity.getChild("type")==null) {
				System.err.println("entry does not specify type!! " + entity);
			}
			
			//Only checking for non-map-entities
			if ( ! entity.getChild("type").getValue().equals("map") &&
				 ! entity.getChild("type").getValue().equals("GraphMap"))
			{
				Entity gsEntity;
				gsEntity = idomain.getEntityByName(
								entity.getChild("type").getValue(), 
								entity.getAttributeValue("id"),
								null);
				
				if (gsEntity == null)
					throw new Exception("Entity '" + entity.getChild("type").getValue() + "' cannot be created");
				
				List<Element> features = entity.getChildren();

				for (Element feature : features) {
					if (!feature.getName().equals("type"))
						gsEntity.setFeatureValue(feature.getName(), 
												 feature.getValue());
				}

				//Get the entityID and set it
//				Method setEntityID = entityClass.getMethod("setentityID", String.class);
//				setEntityID.invoke(gsEntity, entity.getAttributeValue("id"));

				//Add the entity to the gameState object
				gs.addEntity(gsEntity);
			}
			else
			{
				if (entity.getChild("type").getValue().equals("GraphMap")) {
					Map map = GraphMap.loadFromXML(entity,idomain);
					gs.addMap(map);					
				} else {
					//map-entities
					int width = Integer.parseInt(entity.getChild("width").getValue());
					int height = Integer.parseInt(entity.getChild("height").getValue());
					float cell_width = Float.parseFloat(entity.getChild("cell-width").getValue());
					float cell_height = Float.parseFloat(entity.getChild("cell-height").getValue());
					Map map = new TwoDMap(width, height,cell_width,cell_height);
	
					List<Element> rows = entity.getChild("background").getChildren();
	
					for(int i=0; i<height; i++)
					{
						String row = rows.get(i).getValue();
						for(int j=0; j<width; j++)
						{
							char mapChar = row.charAt(j);
							map.setCellLocation(mapChar,new int[]{j,i},idomain);
						}
					}
					gs.addMap(map);
				}
			}

		}
		
		return gs;
	}

	/*
	public Trace parse(int traceCount) throws Exception
	{
		String domain = null;
		Element root = doc.getRootElement();

		//Get the basic info about the trace file
		Element info = root.getChild("info");
		domain = info.getChildTextTrim("domain");
		
		if (!domain.equals(packageInfo)) {
			throw new Exception("Trace from a different domain. It is from '" + domain + "' but should be '" + packageInfo + "'.\n");
		}
		
		Trace trace = new Trace(traceCount, domain, info.getChildTextTrim("map"));        

		Element log = root.getChild("log");
		List<Element> entries = log.getChildren();

		for ( Element entry : entries )
		{
			Entry newEntry = new Entry(Integer.parseInt(entry.getAttribute("time").getValue()));

			//Retrieve the entities that make up the game-state

			GameState gs = parseGameState(entry.getChild("gamestate"), domain);
			
			//add gameState to the Entry
			newEntry.addGameState(gs);

			//parse the actions
			List <Element> actions = entry.getChildren("action");

			for ( Element action : actions )
			{
//				System.out.println("ACTION NAME : " + action.getAttributeValue("name"));
//				System.out.println("ACTION ACTOR: " + action.getAttributeValue("actor"));
//				System.out.println("ACTION ID   : " + action.getAttributeValue("unit-id"));
				Action newAction;
				
				newAction = plans.ActionLibrary.createAction(
										action.getAttributeValue("name"), 
										action.getAttributeValue("unit-id"), 
										action.getAttributeValue("actor"));
				/*
				 // Legacy method of action creation.
				Class <?> actionClass = Class.forName("plans." + packageInfo + "." + action.getAttributeValue("name"));

				Constructor<?> m = actionClass.getConstructor(String.class,String.class);

				Action newAction = (Action) m.newInstance(action.getAttributeValue("unit-id"),
						action.getAttributeValue("actor"));
				*/
/*
				List <Element> actionParams = action.getChildren();
				for( Element param : actionParams )
				{
//					System.out.println("\tACTION PARAMS: " + param.getName() + " = " + param.getValue());
					newAction.setParameterValue(param.getName(), param.getValue());
					/*
	 				 // Legacy method of action creation.
					Method setEntityID = actionClass.getMethod("set" + param.getName(), String.class);
					setEntityID.invoke(newAction, param.getValue());
					*/
/*
				}

				newAction.initializeActionConditions();
				newEntry.addAction(newAction);
			}

			//add the entry to the trace object
			trace.addEntry(newEntry);
		}

		return trace;
	}
*/
	
	public boolean initializeDOMParser(String fileName)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder();
			System.out.println(fileName);
			if (fileName.endsWith(".zip")) {
				ZipFile zf = new ZipFile(fileName);
				ZipEntry ze = zf.entries().nextElement();
				doc = builder.build(zf.getInputStream(ze));
				zf.close();
			} else {
				File f = new File(fileName);
				doc = builder.build(f);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}


	}

}
