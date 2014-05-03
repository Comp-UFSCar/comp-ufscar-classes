package s3.base;

import java.awt.Graphics2D;
import java.lang.reflect.Method;
import java.util.List;

import org.jdom.Element;
import s3.entities.S3Entity;

import s3.entities.S3PhysicalEntity;
import s3.entities.WOGrass;
import s3.entities.WOMapEntity;
import s3.entities.WOTree;
import s3.entities.WOWater;


public class S3MapLayer {
	protected WOMapEntity map[][];

	int map_width, map_height;

	public S3MapLayer(int x, int y) {
		map_width = x;
		map_height = y;
		map = new WOMapEntity[map_width][map_height];
	}

	public void parse(Element background) {
		List<Element> rows = background.getChildren();
		for (int i = 0; i < map_height; i++) {
			String row = rows.get(i).getValue();
			for (int j = 0; j < map_width ; j++) {
				char mapChar = row.charAt(j);
				// System.out.print(mapChar);
				setLocation(mapChar, new int[] { j, i });
			}
			// System.out.println();
		}

	}

	public WOMapEntity getMapEntityAtLocation(int x, int y) {
		return map[x][y];
	}

	private boolean setLocation(char mapCharacter, int coords[]) {
		Class<? extends WOMapEntity> mapEntityClass = this
				.getEntityTypeForMapCharacter(mapCharacter);
		if (mapEntityClass == null) {
			map[coords[0]][coords[1]] = null;
			return true;
		} else {
			try {
				WOMapEntity mapEntity = mapEntityClass.newInstance();
				Method setterX = mapEntityClass.getMethod("setX", int.class);
				setterX.invoke(mapEntity, coords[0]);
				Method setterY = mapEntityClass.getMethod("setY", int.class);
				setterY.invoke(mapEntity, coords[1]);
				map[coords[0]][coords[1]] = mapEntity;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	public Class<? extends WOMapEntity> getEntityTypeForMapCharacter(char mapCharacter) {
		if (mapCharacter == 'w')
			return WOWater.class;
		if (mapCharacter == 't')
			return WOTree.class;
		if (mapCharacter == '.')
			return WOGrass.class;
		return null;
	}

	public void draw(Graphics2D g, int x_offset, int y_offset) {
		for (int i = 0; i < map_width; i++)
			for (int j = 0; j < map_height; j++) {
				if (map[i][j]!=null) map[i][j].draw(g, x_offset, y_offset);
			}
	}

	public void fillGrass() {
		for (int i = 0; i < map_width; i++) {
			for (int j = 0; j < map_height; j++) {
				char mapChar = '.'; // code for grass
				setLocation(mapChar, new int[] { i, j });
			}
		}
	}

	public void cycle(S3 s2) {

	}

	public S3Entity collidesWith(S3PhysicalEntity i_pe) {
		int x = i_pe.getX();
		int y = i_pe.getY();

		for (int i = x; i < x + i_pe.getWidth(); i++)
			for (int j = y; j < y + i_pe.getLength(); j++)
				if (i>=0 && j>=0 && i<map.length && j<map[i].length) {
					if (!(map[i][j] instanceof WOGrass)) {
						return map[i][j];
					}
				}
		return null;
	}

	public S3PhysicalEntity nearestMapEntity(int x, int y,
			Class<? extends WOMapEntity> mapEntityType,S3PhysicalEntity home) {
		S3PhysicalEntity ret = null;
		double distance = 0;
		double distance2 = 0;
		
		for(int range =1 ;range<8;range++) {
			for(int i=x-range;i<=x+range;i++) {
				for(int j=y-range;j<=y+range;j++) {
					if (i>=0 && j>=0 && i<map.length && j<map[i].length) {
						if(map[i][j].getClass().equals(mapEntityType)) {
							double d = Math.sqrt((x-i)*(x-i)+(y-j)*(y-j));
							if (ret==null || d<=distance) {
								if (ret==null || d<distance) {
									ret = map[i][j];
									distance = d;	
									if (home!=null) {
										distance2 = Math.sqrt((home.getX()-i)*(home.getX()-i)+(home.getY()-j)*(home.getY()-j));
									} else {
										distance2 = 1000000;	// Just some very large number. 
									}
								} else {
									double d2;
									if (home!=null) {
										d2 = Math.sqrt((home.getX()-i)*(home.getX()-i)+(home.getY()-j)*(home.getY()-j));
									} else {
										d2 = 1000000;	// Just some very large number. 
									}
									if (d2<distance2) {
										ret = map[i][j];
										distance = d;
										distance2 = d2;
									}
								}
							}
						}
					}
				}
			}
			if (ret!=null) return ret;
		}
		return null;
	}
}
