/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

import gatech.mmpm.util.XMLWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.jdom.Element;



public class GraphMap extends Map {
	
	public class GraphMapNode 
	{
		float coords[];
		Set<GraphMapNode> m_neighbors;
		PhysicalEntity m_entity;
		
		public GraphMapNode(float []a_coords) 
		{
			coords = a_coords;
			m_neighbors = new HashSet<GraphMapNode>();
			m_entity = null;
		}
		
		public void addLink(GraphMapNode n) 
		{
			m_neighbors.add(n);
			n.m_neighbors.add(this);
		}
		
		public boolean neighborsP(GraphMapNode n) 
		{
			if (m_neighbors.contains(n)) return true;
			return false;
		}
	}
	
	ArrayList<GraphMapNode> m_nodes = null;
	
	public int addGraphNode(float []a_coords) 
	{
		GraphMapNode gmn = new GraphMapNode(a_coords);
		m_nodes.add(gmn);
		return m_nodes.size()-1;
	}
	
	public void addLink(int n1,int n2) 
	{
		m_nodes.get(n1).addLink(m_nodes.get(n2));
	}
	
	public GraphMapNode closestNode(float coords[]) 
	{
		GraphMapNode found = null;
		double minDistance = 0, distance;
		
		for(GraphMapNode n:m_nodes) {
			distance = distance(coords,n.coords);
			if (found==null || distance<minDistance) {
				found = n;
				minDistance = distance;
			}
		}
		
		return found;
	}
	
	
	public GraphMap(int nDimensions)
	{
		super(nDimensions);
		m_nodes = new ArrayList<GraphMapNode>();
	}


	public void addEntity(PhysicalEntity pe) 
	{
		GraphMapNode found = closestNode(pe.get_Coords());
		if (found!=null) {
			pe.set_Coords(found.coords);
			found.m_entity = pe;
		} else {
			System.err.println("GraphMap.addEntity: cannot find any node to place the entity!");
		}
	}

	public Object clone() 
	{
		GraphMap m = new GraphMap(numberOfDimensions);
		
		for(GraphMapNode n:m_nodes) {
			float coords[];
			coords = new float[numberOfDimensions];
			for(int i = 0;i<numberOfDimensions;i++) coords[i] = n.coords[i];
			GraphMapNode new_n = new GraphMapNode(coords);
			if (n.m_entity!=null) new_n.m_entity = (PhysicalEntity)n.m_entity.clone();
			m.m_nodes.add(new_n);
		}
		
		for(int i = 0;i<m_nodes.size();i++) {
			for(GraphMapNode n2:m_nodes.get(i).m_neighbors) {
				m.m_nodes.get(i).addLink(m.m_nodes.get(m_nodes.indexOf(n2)));
			}
		}
		
		return m;
	}
	
	public Object cloneWithSameEntities()
	{
		GraphMap m = new GraphMap(numberOfDimensions);
		
		for(GraphMapNode n:m_nodes) {
			float coords[];
			coords = new float[numberOfDimensions];
			for(int i = 0;i<numberOfDimensions;i++) coords[i] = n.coords[i];
			GraphMapNode new_n = new GraphMapNode(coords);
			new_n.m_entity = n.m_entity;
			m.m_nodes.add(new_n);
		}
		
		for(int i = 0;i<m_nodes.size();i++) {
			for(GraphMapNode n2:m_nodes.get(i).m_neighbors) {
				m.m_nodes.get(i).addLink(m.m_nodes.get(m_nodes.indexOf(n2)));
			}
		}
		
		return m;		
	}

	public void deleteEntity(String id) 
	{
		for(GraphMapNode n:m_nodes) {
			if (n.m_entity!=null && n.m_entity.getentityID().equals(id)) {
				n.m_entity = null;
				return;
			}
		}
	}

	public void deleteEntity(PhysicalEntity e) 
	{
		for(GraphMapNode n:m_nodes) {
			if (n.m_entity!=null && n.m_entity==e) {
				n.m_entity = null;
				return;
			}
		}
	}
	
	public boolean areNeighbors(float coords1[],float coords2[]) 
	{
		GraphMapNode n1 = closestNode(coords1);
		GraphMapNode n2 = closestNode(coords2);
		
		if (n1==null || n2==null) return false;
		
		return n1.neighborsP(n2);
	}
	
	public boolean areNeighbors(int cellCoords1[],int cellCoords2[]) 
	{
		return areNeighbors(toCoords(cellCoords1), toCoords(cellCoords2));
	}

	public PhysicalEntity get(int i) {
		return m_nodes.get(i).m_entity;
	}

	public List<PhysicalEntity> getCollisionsOf(PhysicalEntity e) {
		GraphMapNode found = closestNode(e.get_Coords());

		if (distance(e.get_Coords(),found.coords)==0) {
			List<PhysicalEntity> l = new LinkedList<PhysicalEntity>();
			l.add(found.m_entity);
			return l;
		}
		return null;
	}

	public PhysicalEntity getCellLocation(float[] coords) 
	{
		GraphMapNode found = closestNode(coords);
		if (found!=null) return found.m_entity;
		return null;
	}

	public PhysicalEntity getCellLocation(int[] cellCoords) 
	{
		return getCellLocation(cellCoords);
	}

	public void printMap() 
	{
		int i = 0;
		System.out.println("GraphMap:");
		for(GraphMapNode n:m_nodes) {
			System.out.print( i + " - [ " );
			for(int j = 0;j<numberOfDimensions;j++) System.out.print(n.coords[j] + " ");
			System.out.println("] - " + (n.m_entity==null ? "null" : n.m_entity.toString()));
			i++;
		}
		
	}
	

	public int size() 
	{
		return m_nodes.size();
	}

	public boolean setCellLocation(char mapCharacter, int[] cellCoords, gatech.mmpm.IDomain idomain) 
	{		
		return setCellLocation(mapCharacter,closestNode(toCoords(cellCoords)),idomain);
	}

	public boolean setCellLocation(char mapCharacter, float[] coords, gatech.mmpm.IDomain idomain) 
	{		
		return setCellLocation(mapCharacter,closestNode(coords),idomain);
	}

	public boolean setCellLocation(char mapCharacter, GraphMapNode n, gatech.mmpm.IDomain idomain) {

		PhysicalEntity mapEntity = (PhysicalEntity) idomain.getEntityByShortName(mapCharacter, null, null);

		if(mapEntity != null)
			mapEntity.set_Coords(n.coords);

		n.m_entity = mapEntity;	

		return true;
	}

	public float squareDistance(int cellCoords1[],int cellCoords2[])
	{
		return squareDistance(toCoords(cellCoords1), toCoords(cellCoords2));
	}
	
	public float squareDistance(float[] coords1, float[] coords2) {
		int accum = 0;
		for(int i = 0;i<numberOfDimensions;i++) {
			accum +=(coords1[i]-coords2[i])*(coords1[i]-coords2[i]);
		}
		return accum;
	}

	public int toCell(float[] coords) {
		int i = 0;
		int found = -1;
		double minDistance = 0, distance;
		
		for(GraphMapNode n:m_nodes) {
			distance = distance(coords,n.coords);
			if (found==-1 || distance<minDistance) {
				found = i;
				minDistance = distance;
			}
			i++;
		}
		
		return found;
	}

	public int toCell(int[] cellCoords) 
	{
		return toCell(toCoords(cellCoords));
	}

	public int[] toCellCoords(int pos1) {
		return toCellCoords(m_nodes.get(pos1).coords);
	}

	public void toCellCoords(int pos1, int[] coords) {
		for(int i=0;i<numberOfDimensions;i++) 
			coords[i]=(int) m_nodes.get(pos1).coords[i];
	}

	public int[] toCellCoords(float[] coords) {
		int cellCoords[] = new int[numberOfDimensions];
		for(int i=0;i<numberOfDimensions;i++) 
			cellCoords[i] = (int) coords[i];
		return cellCoords;
	}

	public float[] toCoords(int[] cellcoords) 
	{
		float coords[] = new float[cellcoords.length];
		for(int i = 0; i<cellcoords.length; i++)
			coords[i] = cellcoords[i];
		return coords;
	}

	public float[] toCoords(int pos1) {
		return m_nodes.get(pos1).coords;
	}
	
	public double distance(int cellCoords1[],int cellCoords2[])
	{
		return distance(toCoords(cellCoords1), toCoords(cellCoords2));
	}
	
	public double distance(float coords1[],float coords2[]) {
		double accum = 0;
		for(int i = 0;i<numberOfDimensions;i++) {
			accum +=(coords1[i]-coords2[i])*(coords1[i]-coords2[i]);
		}
		return Math.sqrt(accum);
	}

	public String toString() {
		String out = "";
		int i = 0;
		out+="GraphMap:\n";
		for(GraphMapNode n:m_nodes) {
			out+= i + " - [ ";
			for(int j = 0;j<numberOfDimensions;j++) out+= n.coords[j] + " ";
			out+="] - " + (n.m_entity==null ? "null" : n.m_entity.toString()) + "\n";
			i++;
		}
		return out;
	}


	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("entity","id=\"0\"");
		w.tag("type","GraphMap");
		w.tag("numberOfDimensions",numberOfDimensions);
		w.tag("nodes");
		for(GraphMapNode n:m_nodes) {
			w.tag("node");
			{
				String coordsStr = "";
				for(int i=0;i<numberOfDimensions;i++) coordsStr += n.coords[i] + " ";
				w.tag("coords",coordsStr);
			}
			w.tag("neighbors");
			for(GraphMapNode n2:n.m_neighbors) w.tag("neighbor",m_nodes.indexOf(n2));
			w.tag("/neighbors");
			w.tag("entity",(n.m_entity!=null ? n.m_entity.instanceShortName()+"":"."));
			w.tag("/node");
		}
		w.tag("/nodes");
		w.tag("/entity");
	}
	
	public void writeDifferenceToXML(XMLWriter w, Map previousMap) {
		writeToXML(w);		
	}

	public static Map loadFromXML(Element map_e, gatech.mmpm.IDomain idomain) {
		int j;
		int nod = Integer.parseInt(map_e.getChildText("numberOfDimensions"));
		GraphMap map = new GraphMap(nod);
		
		Element nodes = map_e.getChild("nodes");
		for(Object o:nodes.getChildren("node")) {
			Element node_e = (Element)o;
			float coords[] = new float[nod];
			String coordsStr = node_e.getChildText("coords");
			StringTokenizer st = new StringTokenizer(coordsStr," ");
			{
				int i = 0;
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					coords[i++] = Integer.parseInt(token);
				}
			}
			GraphMapNode n = map.new GraphMapNode(coords);

			map.setCellLocation(node_e.getChildText("entity").charAt(0),n, idomain);
			
			map.m_nodes.add(n);
		}			

		j = 0;
		for(Object o:nodes.getChildren("node")) {
			Element node_e = (Element)o;
			Element neighbors_e = node_e.getChild("neighbors");
			
			for(Object o2:neighbors_e.getChildren("neighbor")) {
				Element n_e = (Element)o2;

				map.m_nodes.get(j).addLink(map.m_nodes.get(Integer.parseInt(n_e.getValue())));
			}
			j++;
		}		
		return map;
	}

	public void toCellCoords(float[] coords_in, int[] coords_out) 
	{
		for(int i = 0;i<coords_in.length;i++) 
			coords_out[i]=(int) coords_in[i];
	}

}
