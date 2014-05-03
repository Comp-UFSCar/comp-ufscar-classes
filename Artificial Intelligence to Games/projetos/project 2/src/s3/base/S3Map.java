package s3.base;

import gatech.mmpm.Map;
import gatech.mmpm.TwoDMap;

import java.awt.Graphics2D;
import java.util.List;

import org.jdom.Element;
import s3.entities.S3Entity;

import s3.entities.S3PhysicalEntity;
import s3.entities.WOGrass;
import s3.entities.WOMapEntity;
import s3.entities.WOTree;
import s3.entities.WOWater;
import s3.entities.WUnit;
import s3.util.Pair;

public class S3Map {
	protected S3MapLayer layers[];

	protected static final int NO_OF_LAYERS = 2;

	protected int width, height;

	private S3MiniMap m_miniMap;
	private boolean m_drawMiniMap = true;


	public S3Map(Element mapEntity) {
		int x = Integer.parseInt(mapEntity.getChild("width").getValue());
		int y = Integer.parseInt(mapEntity.getChild("height").getValue());
		layers = new S3MapLayer[NO_OF_LAYERS];
		// grass layer
		layers[0] = new S3MapLayer(x, y);
		layers[0].fillGrass();
		// entities on grass layer
		layers[1] = new S3MapLayer(x, y);
		layers[1].parse(mapEntity.getChild("background"));

		width = x;
		height = y;
		m_miniMap = new S3MiniMap(width,height);
	}

	public void draw(Graphics2D g, int x_offset, int y_offset) {
		for (int i = 0; i < NO_OF_LAYERS; i++) {
			layers[i].draw(g,x_offset,y_offset);
		}
	}
	
	public void drawMiniMap(List<WUnit> units, Graphics2D g, int x_offset, int y_offset)
	{
		if (!m_drawMiniMap) return;

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (layers[1].getMapEntityAtLocation(i,j) != null)
					m_miniMap.draw(g, layers[1].getMapEntityAtLocation(i,j));
			}
		for (WUnit e : units) {
			if (null == e) continue;
			m_miniMap.draw(g, e);
		}
		m_miniMap.drawMiniMapOutline(g, x_offset, y_offset);
	}

	public void cycle(S3 s2) {
		// iterate thru the layers applying the actions...
		// Ignore layer 0
		layers[1].cycle(s2);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public S3Entity anyLevelCollision(S3PhysicalEntity i_pe) {
		// Only check for level 1 as level 0 is grass
		return layers[1].collidesWith(i_pe);
	}

	public S3PhysicalEntity nearestMapEntity(int x, int y,
			Class<? extends WOMapEntity> mapEntityType,S3PhysicalEntity home) {
		return layers[1].nearestMapEntity(x, y, mapEntityType, home);
	}

	public Map toD2Map(gatech.mmpm.IDomain idomain) {
		int coords[] = new int[2];

		TwoDMap ret = new TwoDMap(width, height,
				1,
				1);

		for (S3MapLayer l : layers) {
			for (int y = 0; y < l.map_height; y++) {
				for (int x = 0; x < l.map_width; x++) {
					coords[0] = x;
					coords[1] = y;
					if (l.map[x][y] != null) {
						if (l.map[x][y] instanceof WOGrass)
							ret.setCellLocation('.', coords, idomain);
						if (l.map[x][y] instanceof WOTree)
							ret.setCellLocation('t', coords, idomain);
						if (l.map[x][y] instanceof WOWater)
							ret.setCellLocation('w', coords, idomain);
					}
				} // for
			} // for
		}

		return ret;
	}

	public S3PhysicalEntity getEntity(int x, int y) {
		for (int l = layers.length - 1; l >= 0; l--) {
			if (layers[l].map[x][y] != null)
				return layers[l].map[x][y];
		}
		return null;
	}
	
	public void toggleDrawMiniMap() {
		m_drawMiniMap = !m_drawMiniMap;
	}

	public Pair<Integer,Integer> isInMiniMap(int current_screen_x, int current_screen_y) {
		if (m_drawMiniMap)
			return m_miniMap.isInMiniMap(current_screen_x, current_screen_y);
		return null;
	}
}
