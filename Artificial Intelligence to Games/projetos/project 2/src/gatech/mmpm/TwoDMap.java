/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

import gatech.mmpm.util.XMLWriter;

import java.util.LinkedList;
import java.util.List;




public class TwoDMap extends Map {

	private PhysicalEntity map[][];
	int size[];

	public TwoDMap(int a_width, int a_height, float a_cell_width, float a_cell_height)
	{
		super(2);
		size = new int[2];
		size[0]= a_width;
		size[1]= a_height;
		cell_size[0] = a_cell_width;
		cell_size[1] = a_cell_height;
		map = new PhysicalEntity[size[0]][size[1]];
	}

	public int getSizeInDimension(int d) 
	{
		return size[d];
	}


	public void addEntity(PhysicalEntity pe) {
		if(pe == null || pe.get_Coords() == null)
			return;
		int cc[] = toCellCoords(pe.get_Coords());
		map[cc[0]][cc[1]] = pe;
	}

	public boolean setCellLocation(char mapCharacter, int cellCoords[], gatech.mmpm.IDomain idomain)
	{
		float coords[] = toCoords(cellCoords);
		return setCellLocation(mapCharacter, coords, idomain);
	}

	public boolean setCellLocation(char mapCharacter, float coords[], gatech.mmpm.IDomain idomain)
	{
		if(coords == null)
			return false;
		int cellCoords[] = toCellCoords(coords);

		map[cellCoords[0]][cellCoords[1]] = null;
		if (mapCharacter!='.') {
			PhysicalEntity mapEntity = (PhysicalEntity) idomain.getEntityByShortName(mapCharacter, null, null);
			
			if(mapEntity != null){
				mapEntity.setx(coords[0]);
				mapEntity.sety(coords[1]);
			}
			
			map[cellCoords[0]][cellCoords[1]] = mapEntity;
		}
		return true;
		
	}

	public void printMap()
	{
		for(int i=0; i<size[1]; i++)
		{
			for(int j=0; j<size[0]; j++)
			{
				if ( map[j][i] != null )
					System.out.print(map[j][i].instanceShortName());
				else
					System.out.print(".");
			}
			System.out.println();
		}
	}

	public PhysicalEntity getCellLocation(int cellCoords[]) 
	{
		if(cellCoords == null)
			return null;
		return map[cellCoords[0]][cellCoords[1]];
	}

	public PhysicalEntity getCellLocation(float coords[]) 
	{
		return getCellLocation(toCellCoords(coords));
	}

        // Objects in the map are NOT cloned, since the assumption is that
        // objects in the map are undistinguishable (they have no ID, etc.)
	public Object clone() 
	{
		int i,j;
		TwoDMap m = new TwoDMap(size[0],size[1],cell_size[0],cell_size[1]);

		for(i=0;i<size[0];i++) {
			for(j=0;j<size[1];j++) {
                                // If we wanted the entities to be cloned too, then we should swap
                                // the following two lines:
//				m.map[i][j] = (map[i][j]==null ? null:(PhysicalEntity)(map[i][j].clone()));
				m.map[i][j] = map[i][j];
			}
		}

		return m;
	}

	public Object cloneWithSameEntities()
	{
		int i,j;
		TwoDMap m = new TwoDMap(size[0],size[1],cell_size[0],cell_size[1]);

		for(i=0;i<size[0];i++) {
			for(j=0;j<size[1];j++) {
				m.map[i][j] = map[i][j];
			}
		}

		return m;		
	}
	
	public int size()
	{
		return size[0]*size[1];
	}

	public PhysicalEntity get(int i)
	{
		int x = i%size[0];
		int y = i/size[0];
		if (x>=0 && x<size[0] &&
				y>=0 && y<size[1]) return map[x][y];
		return null;
	}

	public String toString() 
	{
		return "a " + size[0] + "x" + size[1] + " 2dmap";
	}

	/**
	 * Writes the 2DMap info to an XMLWriter object
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) 
	{
		w.tagWithAttributes("entity","id=\"0\"");
		w.tag("type","map");
		w.tag("width",size[0]);
		w.tag("height",size[1]);
		w.tag("cell-width",cell_size[0]);
		w.tag("cell-height",cell_size[1]);
		w.tag("background");

		for(int i=0;i<size[1];i++) {
			char s[] = new char[size[0]];
			for(int j=0;j<size[0];j++) {
				s[j]=(map[j][i]!=null ? map[j][i].instanceShortName() : '.');
			}
			w.tag("r",new String(s));
		}
		w.tag("/background");
		w.tag("/entity");
	}
	
	public void writeDifferenceToXML(XMLWriter w, Map previousMap) 
	{
		w.tagWithAttributes("entity","id=\"0\"");
		w.tag("background");
		String row = "";
		for(int i=0;i<size[1];i++) {
			char s[] = new char[size[0]];
			char prev_s[] = new char[size[0]];
			for(int j=0;j<size[0];j++) {
				s[j]=(map[j][i]!=null ? map[j][i].instanceShortName() : '.');
				prev_s[j]=(previousMap.getCellLocation(new int[]{j,i})!=null ? 
						previousMap.getCellLocation(new int[]{j,i}).instanceShortName() : '.');
			}
			if(new String(s).equals(new String(prev_s)))
				row +="<r></r>";
				//w.tag("r","");
			else
				row += "<r>" + new String(s) + "</r>";
				//w.tag("r",new String(s));
		}
		w.rawXML(row);
		w.tag("/background");
		w.tag("/entity");
	}

	public void deleteEntity(String id) 
	{
		for(int x=0;x<size[0];x++) {
			for(int y=0;y<size[1];y++) {
				if (map[x][y]!=null &&
						map[x][y].getentityID().equals(id)) {
					map[x][y]=null;
					return;
				}
			}			
		}
	}

	public void deleteEntity(PhysicalEntity e) 
	{
		if(e == null || e.get_Coords() == null)
			return;
		int cellCoords[] = toCellCoords(e.get_Coords());
		int x = cellCoords[0];
		int y = cellCoords[1];

		/*		if (map[x][y]!=null &&
			map[x][y]==e) {
			map[x][y]=null;
		}
		 */
		if (map[x][y]!=null &&
				map[x][y].getClass()==e.getClass()) {
			map[x][y]=null;
		}
	}

	public List<PhysicalEntity> getCollisionsOf(PhysicalEntity e) 
	{
		List<PhysicalEntity> l = new LinkedList<PhysicalEntity>(); 
		if(e == null || e.get_Coords() == null)
			return l;
		int x1 = (int)(e.getx()/cell_size[0]);
		int y1 = (int)(e.gety()/cell_size[1]);
		int x2 = (int)((e.getx()+e.getwidth())/cell_size[0]);
		int y2 = (int)((e.gety()+e.getlength())/cell_size[1]);

		for(int x=x1;x<=x2;x++) {
			for(int y=y1;y<=y2;y++) {
				if (x>=0 && x<size[0] &&
						y>=0 && y<size[1] &&
						map[x][y]!=null && e.collision(map[x][y])) l.add(map[x][y]);
			}			
		}

		return l;
	}

	public float[] toCoords(int pos1)
	{
		float coords[] = new float[3];
		coords[0]=(pos1%size[0])*cell_size[0];
		coords[1]=(pos1/size[0])*cell_size[1];
		coords[2]=0;

		return coords;
	}


	public double distance(int cellCoords1[],int cellCoords2[])
	{
		return distance(toCoords(cellCoords1), toCoords(cellCoords2));
	}

	public double distance(float coords1[],float coords2[])
	{
		if(coords1 == null || coords2 == null)
			return Float.MAX_VALUE;
		return Math.sqrt((coords2[0]-coords1[0])*(coords2[0]-coords1[0])+(coords2[1]-coords1[1])*(coords2[1]-coords1[1]));
	}

	public float squareDistance(int cellCoords1[],int cellCoords2[])
	{
//		return squareDistance(toCoords(cellCoords1), toCoords(cellCoords2));
		if(cellCoords1 == null || cellCoords1 == null)
			return Float.MAX_VALUE;
		return (cellCoords1[0]-cellCoords2[0])*(cellCoords1[0]-cellCoords2[0])+(cellCoords1[1]-cellCoords2[1])*(cellCoords1[1]-cellCoords2[1]);
	}

	public float squareDistance(float coords1[],float coords2[])
	{
		if(coords1 == null || coords2 == null)
			return Float.MAX_VALUE;
		return (coords2[0]-coords1[0])*(coords2[0]-coords1[0])+(coords2[1]-coords1[1])*(coords2[1]-coords1[1]);
	}
	
	public boolean areNeighbors(int cellCoords1[],int cellCoords2[]) 
	{
		if(cellCoords1 == null || cellCoords2 == null)
			return false;
		if( ( (cellCoords1[0] == cellCoords2[0]) && 
				(Math.abs(cellCoords1[1] - cellCoords2[1]) == 1) ) ||
			( (cellCoords1[1] == cellCoords2[1]) && 
				(Math.abs(cellCoords1[0] - cellCoords2[0]) == 1) )	)
			return true;
		else
			return false;
	}
	
	public boolean areNeighbors(float coords1[],float coords2[]) 
	{
		int cellCoords1[] = toCellCoords(coords1);
		int cellCoords2[] = toCellCoords(coords2);
		return areNeighbors(cellCoords1, cellCoords2);
	}

	public int[] toCellCoords(int pos1) 
	{
		int coords[] = new int[3];
		coords[0]=(pos1%size[0]);
		coords[1]=(pos1/size[0]);
		coords[2]=0;

		return coords;		
	}


	public void toCellCoords(int pos1,int []cellCoords)
	{
		cellCoords[0]=(pos1%size[0]);
		cellCoords[1]=(pos1/size[0]);
		cellCoords[2]=0;		
	}


	public int[] toCellCoords(float coords[]) {
		int cellCoords[] = new int[3];
		if(coords != null)
		{
			cellCoords[0]=(int)(coords[0]/cell_size[0]);
			cellCoords[1]=(int)(coords[1]/cell_size[1]);
			cellCoords[2]=0;
		}
		return cellCoords;				
	}

	public float[] toCoords(int cellcoords[]) {
		float coords[] = new float[3];
		if(cellcoords != null)
		{
			coords[0]=(cellcoords[0])*cell_size[0];
			coords[1]=(cellcoords[1])*cell_size[1];
			coords[2]=0;
		}
		return coords;				
	}

	public int toCell(float coords[]) 
	{
		int cellCoords[] = toCellCoords(coords);
		return toCell(cellCoords);
	}

	public int toCell(int cellCoords[])
	{
		if(cellCoords == null)
			return 0;
		int cell;
		cell=cellCoords[0];
		cell+=cellCoords[1]*size[0];

		return cell;		
	}

	public void toCellCoords(float[] coords_in, int[] coords_out) {
		if(coords_in == null)
			return;
		coords_out[0]=(int) (coords_in[0]/cell_size[0]);
		coords_out[1]=(int) (coords_in[1]/cell_size[1]);
		coords_out[2]=0;
	}

}
