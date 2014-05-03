/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;


import gatech.mmpm.util.XMLWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jdom.Element;



public class GameState implements java.io.Serializable {
	
	static final long serialVersionUID = 0x34341288;

	protected Map mapEntity = null;
	protected ArrayList <Entity> entities = new ArrayList<Entity>();
	
	// This metaData hashmap acts as a cache for all the additional metadata that any module might want to
	// add to a game state (e.g. PotentialFields, GameStateFeatures, etc.). Before recomputing them for a particular
	// game state, a component should check if the meta data has already been computed for this game state.
	// The meta data is not copied when cloning.
	protected HashMap<String,Object> metaData = new HashMap<String,Object>();
	
	public GameState() {
	}
	
	public GameState(Map map,List<Entity> a_entities) {
		mapEntity = map;
		entities.addAll(a_entities);
	}	
	
	public void addMetaData(String ID,Object data) {
		metaData.put(ID,data);
	}
	
	public Object getMetaData(String ID) {
		return metaData.get(ID);
	}

        public void clearMetadata() {
                metaData.clear();
        }
	
	public void addEntity(Entity e)
	{
		entities.add(e);
	}
	
	public void clearEntities()
	{
		entities = new ArrayList<Entity>();
	}
	
	public void addMap(Map m)
	{
		mapEntity = m;
	}
	
	public Map getMap()
	{
		return mapEntity;
	}
	
	/**
	 * Returns the Entity if its present (based on entityID) or returns null
	 * @param entityID The entityID of the entity requested
	 * @return The Entity having this entityID
	 */
	public Entity getEntity(String entityID)
	{
		for ( Entity e : entities)
		{
			if ( e.getentityID().equals(entityID))
				return e;
		}
		return null;
	}
	

	public void deleteEntity(String entityID)
	{
		for ( Entity e : entities)
		{
			if ( e.getentityID().equals(entityID)) {
				entities.remove(e);
				return;
			}
		}

		if (mapEntity!=null) mapEntity.deleteEntity(entityID);
	}	
	
	public void deleteEntity(Entity e)
	{
		if (e.getentityID()!=null) {
			for ( Entity e2 : entities)
			{
				if ( e2.getentityID().equals(e.getentityID())) {
					entities.remove(e2);
					return;
				}
			}
		} // if
		
		if (mapEntity!=null && e instanceof PhysicalEntity) {
			mapEntity.deleteEntity((PhysicalEntity)e);
		}
	}	
	
	//return arrayList of entities based on type
	public ArrayList <Entity> getEntityByTypeAndOwner(Class<?> c, String owner)
	{
		ArrayList <Entity> el = new ArrayList <Entity> ();
		try
		{
			for ( Entity e : entities) {
				if ( c.isInstance(e) && 
                                     ((owner==null && e.getowner()==null) ||
                                     owner.equals(e.getowner())))
					el.add(e);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return el;
	}


        //return arrayList of entities based on type
	public ArrayList <Entity> getEntityByType(Class<?> c)
	{
		ArrayList <Entity> entitiesByType = new ArrayList <Entity> ();
		try
		{
			for ( Entity e : entities)
			{
				if ( c.isInstance(e))
					entitiesByType.add(e);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();			
		}
		
		return entitiesByType;
	}
	
	//return arrayList of entities based on type
	public ArrayList <Entity> getEntityByType(String type)
	{
		ArrayList <Entity> entitiesByType = new ArrayList <Entity> ();
		try
		{
			for ( Entity e : entities)
			{
				if ( e.getClass().getSimpleName().equals(type))
					entitiesByType.add(e);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();			
		}
		
		return entitiesByType;
	}
	
	//return arrayList of entities based on player
	public ArrayList <Entity> getEntityByOwner(String owner)
	{
		ArrayList <Entity> entitiesByOwner = new ArrayList <Entity> ();
		for ( Entity e : entities)
		{
			if ( e.getowner()!=null && e.getowner().equals(owner))
				entitiesByOwner.add(e);
		}
		
		return entitiesByOwner;
	}
	
	//returns all entities
	public ArrayList <Entity> getAllEntities()
	{	
		return entities;
	}
	
	public Set<String> getAllPlayers() {
		Set<String> players = new HashSet<String>();
		for(Entity e:entities) {
			if (e.getowner()!=null)	players.add(e.getowner());
		}
		return players;
	}
	
	
	public int getNumberOfPlayers() {
		Set<String> players = new HashSet<String>();
		for(Entity e:entities) players.add(e.getowner());		
		return players.size();
	}
	
	
	public List<PhysicalEntity> getCollisionsOf(PhysicalEntity e) {
		List<PhysicalEntity> l;
		
		if (mapEntity==null) {
			l = new LinkedList<PhysicalEntity>();
		} else {
			l = mapEntity.getCollisionsOf(e);
		}
		
		for(Entity e2:entities) {
			if (e!=e2 &&
				e2 instanceof PhysicalEntity) {
				if (e.collision((PhysicalEntity)e2)) l.add((PhysicalEntity)e2);
			}
		}
		
		return l;
	}
	
	public String getFreeID() {
		int id = 0;
		
		while(true) {
			if (getEntity(""+id)==null) return ""+id;
			id++;
		}
	}
	
	public Object clone() {
		GameState gs = new GameState();
		gs.mapEntity = (Map) mapEntity.clone();
		gs.entities = new ArrayList<Entity>();
		for(Entity e:entities) gs.entities.add((Entity)e.clone());
		return gs;
	}
		
	public String toString() {
		String out = "GameState:\n";
		if (mapEntity!=null) {
			out += mapEntity.toString() + "\n";
		}
		for(Entity e:entities) out += e.toString() + "\n";
		return out;
	}
	
	/**
	 * Writes the GameState info to an XMLWriter object
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tag("gamestate");
		mapEntity.writeToXML(w);
		for(Entity e:entities)
			e.writeToXML(w);
		w.tag("/gamestate");
	}
	
	
	public void writeToXMLDifference(XMLWriter w,GameState previousGameState) {
		if(previousGameState == null) {
			writeToXML(w);
		} else {
			w.tag("gamestate");
			getMap().writeDifferenceToXML(w,previousGameState.getMap());
			for (gatech.mmpm.Entity e : entities)
				e.writeDifferenceToXML(w,previousGameState.getEntity(e.getentityID()));
			w.tag("/gamestate");
		}
	}

	
	public static GameState loadFromXML(Element xml, gatech.mmpm.IDomain idomain) {
		try {
			ParseLmxTrace parser = new ParseLmxTrace(idomain.getName());
			GameState gs = parser.parseGameState(xml,idomain);
			
			return gs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public static GameState loadDifferenceFromXML(Element xml, GameState prev_gs, gatech.mmpm.IDomain idomain) {
		try {
			String name = "";
			if(idomain != null)
				name = idomain.getName();
			ParseLmxTrace parser = new ParseLmxTrace(name);
			GameState gs = parser.parseDifferenceGameState(xml,prev_gs,idomain);
			
			return gs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	


	public PhysicalEntity getEntityAt(float[] coords) {
		for(Entity e:entities) {
			if (e instanceof PhysicalEntity) {
				if (((PhysicalEntity) e).collision(coords)) {
					return (PhysicalEntity)e;
				}
			}
		}
		
		int cell = mapEntity.toCell(coords);
//		System.out.println("cell: " + cell + " -> " + coords[0] + "," + coords[1]);
		return mapEntity.get(cell);
	}
	
}
