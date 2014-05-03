package s3.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;

import s3.entities.S3Entity;
import s3.entities.S3PhysicalEntity;
import s3.entities.WGoldMine;
import s3.entities.WOGrass;
import s3.entities.WOMapEntity;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WTroop;
import s3.entities.WUnit;
import s3.util.KeyInputHandler;
import s3.util.Pair;

public class S3 {
	/** the current map. */
	private S3Map m_map;

	/** the entities that are not map parts or metadata. Usually units. */
	private List<WUnit> units;
	
	// Auxiliary list for the bullets of towers, catapults and archers
	private List<Bullet> bullets;

	/** units to add at the end of a cycle. */
	private List<WUnit> newUnits;

	/** entities that are not displayed, like the player. */
	private List<WPlayer> players;

	/** what game cycle is currently being executed. */
	private int m_cycle;

	/** the next entityID to be assigned. */
	private int nextID = 0;

	/** warning message for the user */
	private String message = "Press 'M' to toogle minimap";
	
	/**
	 * Default constructor.
	 * 
	 * @param game_doc
	 *            game definition.
	 * @throws Exception
	 */
	public S3(Document game_doc) throws Exception {
		units = new LinkedList<WUnit>();
		newUnits = new LinkedList<WUnit>();
		players = new LinkedList<WPlayer>();
		bullets = new LinkedList<Bullet>();
		Element root = game_doc.getRootElement();
		// parse XML to create S2 Map object
		List<Element> entities = root.getChildren();
		
		WPlayer.playerCount = 0;
				
		for (Element entity : entities) {
			String entity_type = entity.getChild("type").getValue();
			// MAP
			if (entity_type.equals("map")) {
				m_map = new S3Map(entity);
			} else {
				// NON-MAP NON-PHYSICAL ENTITY
				if (entity_type.equals("WPlayer")) {

					Class<? extends S3Entity> entityClass = (Class<? extends S3Entity>) Class
							.forName("s3.entities." + entity_type);
					WPlayer gsEntity = (WPlayer) entityClass.newInstance();

					setFeaturesForEntity(entity, entityClass, gsEntity);

					setEntityID(entity, entityClass, gsEntity);
					players.add(gsEntity);
				} else {
					units.add(getEntityFromElement(entity));
				}
			}
		} // end for

		m_cycle = 0;
	}

	private WUnit getEntityFromElement(Element entity) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {
		String entityName = entity.getChild("type").getValue();

		Class<? extends WUnit> entityClass;

		if (entityName.equals("WFarm")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WFortress")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WGoldMine")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WHumanbarracks")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WHumanblacksmith")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WLumberMill")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WTownhall")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WTower")) {
			entityClass = (Class<? extends WUnit>) Class.forName("s3.entities."
					+ entityName);
		} else if (entityName.equals("WArcher")) {
			entityClass = (Class<? extends WUnit>) Class
					.forName("s3.entities." + entityName);
		} else if (entityName.equals("WCatapult")) {
			entityClass = (Class<? extends WUnit>) Class
					.forName("s3.entities." + entityName);
		} else if (entityName.equals("WFootman")) {
			entityClass = (Class<? extends WUnit>) Class
					.forName("s3.entities." + entityName);
		} else if (entityName.equals("WKnight")) {
			entityClass = (Class<? extends WUnit>) Class
					.forName("s3.entities." + entityName);
		} else if (entityName.equals("WPeasant")) {
			entityClass = (Class<? extends WUnit>) Class
					.forName("s3.entities." + entityName);
		} else {
			throw new ClassNotFoundException(entityName);
		}

		WUnit gsEntity = entityClass.newInstance();

		// gsEntity.setColor()

		setFeaturesForEntity(entity, entityClass, gsEntity);

		setEntityID(entity, entityClass, gsEntity);

		for (WPlayer p : players) {
			if (p.getOwner().equals(gsEntity.getOwner())) {
				gsEntity.setColor(p.getColor());
				break;
			}
		}

		return gsEntity;
	}

	private void setEntityID(Element entity, Class<? extends S3Entity> entityClass,
			S3Entity gsEntity) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		// Get the entityID and set it
		Method setEntityID = entityClass.getMethod("setEntityID", int.class);
		int id = Integer.parseInt(entity.getAttributeValue("id"));
		setEntityID.invoke(gsEntity, id);
		// keep track of highest unused entityID
		if (id >= nextID) {
			nextID = id++;
		}
	}

	private void setFeaturesForEntity(Element entity, Class<? extends S3Entity> entityClass,
			S3Entity gsEntity) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		List<Element> features = entity.getChildren();

		for (Element feature : features) {
			if (!feature.getName().equals("type")) {
				String featureName = feature.getName();
				featureName = featureName.substring(0, 1).toUpperCase() + featureName.substring(1);
				Method setter;
				try {
					setter = entityClass.getMethod("set" + featureName, String.class);
					setter.invoke(gsEntity, feature.getValue());
				} catch (NoSuchMethodException e) {
					setter = entityClass.getMethod("set" + featureName, int.class);
					setter.invoke(gsEntity, Integer.parseInt(feature.getValue()));
				}
			}
		}
	}

	public boolean cycle(List<S3Action> failedActions) {
		// get the actions and do the game!
		m_cycle++;

		if (m_map == null) {
			return false;
		}
		m_map.cycle(this);

		List<S3PhysicalEntity> toRemove = new LinkedList<S3PhysicalEntity>();
		{
			List<WUnit> l = new LinkedList<WUnit>(); // This variable is just
														// to avoid a concurrent
														// modification
			l.addAll(units);
			for (WUnit unit : l) {
				unit.cycle(m_cycle, this, failedActions);
				if (unit.getCurrent_hitpoints() <= 0) {
					toRemove.add(unit);
					if (unit instanceof WPeasant) {
						// clearn any construction it might have been doing:
						((WPeasant)unit).clearConstruction(this);
					}
				}
			}

		}
		units.removeAll(toRemove);

		units.addAll(newUnits);
		newUnits.removeAll(newUnits);
		
		{
			List<Bullet> toDelete = new LinkedList<Bullet>();
			for(Bullet b:bullets) {
				if (b.cycle()) toDelete.add(b);
			}
			bullets.removeAll(toDelete);
		}

		String owner = null;
		for (WUnit unit : units) {
			if (unit instanceof WGoldMine) {
				continue;
			}

			if (null == owner) {
				owner = unit.getOwner();
				continue;
			}

			if (!owner.equals(unit.getOwner())) {
				return true;
			}
		}
		return false;
	}

	public void draw(Graphics2D g, Set<WUnit> selectedEntities, int x_offset, int y_offset) {
		// draw the map
		m_map.draw(g, x_offset, y_offset);
		// draw the entities
		for (WUnit e : units) {
			if (null == e) {
				continue;
			}
			e.draw(g, x_offset, y_offset);
						
			// mark the Entity selected
			if (selectedEntities.contains(e)) {
				g.setColor(Color.GREEN);
				if (e instanceof WTroop) {
					WTroop t = (WTroop)e;
					g.drawRect(t.getActualX() - x_offset, 
							t.getActualY() - y_offset, 
							e.getWidth() * S3PhysicalEntity.CELL_SIZE, e.getLength() * S3PhysicalEntity.CELL_SIZE);						
				} else {
					g.drawRect((e.getX() * S3PhysicalEntity.CELL_SIZE - x_offset), 
								e.getY() * S3PhysicalEntity.CELL_SIZE - y_offset, 
								e.getWidth() * S3PhysicalEntity.CELL_SIZE, e.getLength() * S3PhysicalEntity.CELL_SIZE);
				}
			}
		}
		for(Bullet b:bullets) {
			g.setColor(Color.BLUE);
			g.fillOval(b.current_x - x_offset, b.current_y - y_offset, 8, 8);
		}
		
		m_map.drawMiniMap(units, g, x_offset, y_offset);
	}
	
	public void addBullet(int x,int y,int target_x,int target_y) {
		Bullet b = new Bullet();
		b.current_x = x;
		b.current_y = y;
		b.target_x = target_x;
		b.target_y = target_y;
		bullets.add(b);
	}

	public WUnit entityVisuallyAt(int map_x, int map_y) {
		// get the unit at the location
		for (WUnit e : units) {
			if (e instanceof WTroop) {
				int ax = ((WTroop)e).getActualX();
				int ay = ((WTroop)e).getActualY();
				int dx = e.getWidth()*S3PhysicalEntity.CELL_SIZE;
				int dy = e.getLength()*S3PhysicalEntity.CELL_SIZE;
				if (map_x>=ax && map_x<ax+dx &&
					map_y>=ay && map_y<ay+dy) return e;
			} else {
				if (e.isEntityAt(map_x/S3PhysicalEntity.CELL_SIZE, map_y/S3PhysicalEntity.CELL_SIZE)) {
					return e;
				}
			}
		}
		for (WUnit e : newUnits) {
			if (e.isEntityAt(map_x/S3PhysicalEntity.CELL_SIZE, map_y/S3PhysicalEntity.CELL_SIZE)) {
				return e;
			}
		}
		return null;
	}
	
	public WUnit entityAt(int map_x, int map_y) {
		// get the unit at the location
		for (WUnit e : units) {
			if (e.isEntityAt(map_x, map_y)) {
				return e;
			}
		}
		for (WUnit e : newUnits) {
			if (e.isEntityAt(map_x, map_y)) {
				return e;
			}
		}
		return null;
	}


	public WOMapEntity mapEntityAt(int map_x, int map_y) {
		// get the map tile for that location.
		if (map_x >= 0 && map_y >= 0 && map_x < m_map.layers[1].map.length
				&& map_y < m_map.layers[1].map[map_x].length)
			return m_map.layers[1].map[map_x][map_y];

		return null;
	}

	/**
	 * Method returns the nearest map entity of the particular type from the
	 * given x and y co-ordinates
	 * 
	 * @param x
	 *            current x co-ordinate
	 * @param y
	 *            current y co-ordinate
	 * @param mapEntityType
	 *            type of the map entity
	 * @param home
	 *            find the wood closest to this entity
	 * @return the mapEntity if found, or null
	 */

	public S3PhysicalEntity locateNearestMapEntity(int x, int y,
			Class<? extends WOMapEntity> mapEntityType, S3PhysicalEntity home) {
		return m_map.layers[1].nearestMapEntity(x, y, mapEntityType, home);
	}

	/**
	 * removes a mapEntity at the given x,y coordinates.
	 * 
	 * @param x
	 * @param y
	 */
	public void clearMapEntity(int x, int y) {
		// System.out.println("Clearing --> (" + x + "," + y + ")");
		WOGrass clearedZone = new WOGrass();
		clearedZone.setX(x);
		clearedZone.setY(y);
		m_map.layers[1].map[x][y] = clearedZone;
	}

	public void setMapEntity(int x, int y, WOMapEntity me) {
		// System.out.println("Clearing --> (" + x + "," + y + ")");
		me.setX(x);
		me.setY(y);
		m_map.layers[1].map[x][y] = me;
	}

	/**
	 * Returns the first instance of the given unit for the given player.
	 * 
	 * @param player
	 * @param type
	 * @param game
	 * @return
	 */
	public WUnit getUnitType(WPlayer player, Class<? extends WUnit> unitType) {
		for (WUnit unit : units) {
			if (unit.getClass().equals(unitType)) {
				if (null == player && null == unit.owner) {
					return unit;
				}
				if (player != null && unit.owner.equals(player.owner)) {
					return unit;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the all instances of the given unit for the given player.
	 * 
	 * @param player
	 * @param type
	 * @param game
	 * @return
	 */
	public List<WUnit> getUnitTypes(WPlayer player, Class<? extends WUnit> unitType) {
		List<WUnit> list = new LinkedList<WUnit>();
		for (WUnit unit : units) {
			if (unit.getClass().equals(unitType)) {
				if (null == player && null == unit.owner) {
					list.add(unit);
				} else if (null == player) {
					continue;
				} else if (unit.owner.equals(player.owner)) {
					list.add(unit);
				}
			}
		}
		return list;
	}

	/**
	 * retrieves the unit with the given ID.
	 * 
	 * @param id
	 *            the entityID.
	 * @return the corresponding unit.
	 */
	public WUnit getUnit(int id) {
		for (WUnit entity : units) {
			if (entity.entityID == id) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * lets S2 know to add the given unit at the end of the cycle.
	 * 
	 * @param unit
	 */
	public void addUnit(WUnit unit) {
		newUnits.add(unit);
	}

	/**
	 * returns the next unused entityID and increments the ID
	 * 
	 * @return
	 */
	public int nextID() {
		nextID++;
		return nextID;
	}

	public List<WPlayer> getPlayers() {
		return players;
	}

	/**
	 * checks if the unit collides with another unit
	 * 
	 * @param i_pe
	 * @return true if a collision occurs
	 */
	public S3Entity anyLevelCollision(S3PhysicalEntity i_pe) {
		for (WUnit e : units) {
			if (i_pe.collision(e))
				if (i_pe.entityID != e.entityID) {
					// System.out.println("COLLISION with " + e);
					return e;
				}
		}
		// return false;
		return m_map.anyLevelCollision(i_pe);
	}

	public S3Map getMap() {
		return m_map;
	}

	public List<WUnit> getUnits() {
		return units;
	}

	public List<S3Entity> getAllUnits() {
		List<S3Entity> ret = new LinkedList<S3Entity>();

		ret.addAll(units);
		ret.addAll(players);
		return ret;
	}

	public int getCycle() {
		return m_cycle;
	}

	public S3PhysicalEntity getEntity(int x, int y) {
		if (x < 0 || y < 0 || x >= m_map.getWidth() || y >= m_map.getHeight())
			return null;

		for (WUnit u : units) {
			if (u.getX() <= x && u.getX() + u.getWidth() > x && u.getY() <= y
					&& u.getY() + u.getLength() > y) {
				return u;
			}
		}

		return m_map.getEntity(x, y);
	}

	public WPlayer getPlayer(String m_playerid) {
		for (S3Entity e : players) {
			if (e instanceof WPlayer && e.getOwner().equals(m_playerid))
				return (WPlayer) e;
		}
		return null;
	}

	/**
	 * finds the square of free space that is size x size large that is closest
	 * to startx and starty.
	 * 
	 * @param startx
	 * @param starty
	 * @param size
	 * @return the pair representing the top left location of the free square.
	 *         null if one doesn't exist.
	 */
	public Pair<Integer, Integer> findFreeSpace(int startx, int starty, int size) {
		int x = startx;
		int y = starty;

		for (int i = 0; i < getMap().width / 2 && i < getMap().height / 2; i++) {
			for (int j = -1; j <= i; j++) {
				if (isSpaceFree(size, x + j, y + i)) {
					return new Pair<Integer, Integer>(x + j, y + i);
				}
				if (isSpaceFree(size, x + i, y + j)) {
					return new Pair<Integer, Integer>(x + i, y + j);
				}
				if (isSpaceFree(size, x + j, y - i)) {
					return new Pair<Integer, Integer>(x + j, y - i);
				}
				if (isSpaceFree(size, x - i, y + j)) {
					return new Pair<Integer, Integer>(x - i, y + j);
				}
			}
		}

		return null;
	}

	/**
	 * @param size
	 * @param x
	 * @param y
	 */
	public boolean isSpaceFree(int size, int x, int y) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int xLoc = x + i;
				int yLoc = y + j;
				if (xLoc <= 0 || yLoc <= 0 || xLoc >= getMap().width || yLoc >= getMap().height) {
					return false;
				}
				if (!(mapEntityAt(xLoc, yLoc) instanceof WOGrass) || null != entityAt(xLoc, yLoc)) {
					return false;
				}
			}
		}
		return true;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void removeUnit(WUnit u) {
		units.remove(u);
	}

	//hardcoded the letter 'm' here for this. I guess we could declare it as a constant maybe
	//there could be other letters used here for game-controls, pause, volume, etc
	public void setGameControls(KeyInputHandler k) {
		if (k.m_keyboardStatus['M'] && !k.m_old_keyboardStatus['M'])
			m_map.toggleDrawMiniMap();
	}

	public Pair<Integer,Integer> isInMiniMap(int current_screen_x, int current_screen_y) {
		return m_map.isInMiniMap(current_screen_x, current_screen_y);
	}
}
