/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.builtin;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.Entity;
import gatech.mmpm.GameState;
import gatech.mmpm.PhysicalEntity;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;



/**
 * This condition tests whether there is an entity of the specified type 
 * in the specified coordinates.
 * 
 * @author Santi Ontanon Villar and David Llanso
 * @organization: Georgia Institute of Technology
 * @date 29-April-2008
 * 
 * @note Updated according to the new gatech.mmpm.Sensor class. 04-November-2009
 * 
 */
public class EntityTypeAt extends Sensor 
{

	public EntityTypeAt() 
	{
	} // Constructor

	//---------------------------------------------------------------

	public EntityTypeAt(EntityTypeAt eta) 
	{
		super(eta);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new EntityTypeAt();
	} // clone

	//---------------------------------------------------------------

	/**
	 * Return the type of the sensor.
	 * 
	 * Keep in mind that this is <em>not</em> the real Java type,
	 * but the MMPM type. See the 
	 * gatech.mmpm.ActionParameterType.getJavaType() method
	 * for more information.
	 * 
	 * @return Type of the sensor. 
	 */
	public ActionParameterType getType() 
	{
		return ActionParameterType.BOOLEAN;
	} // getType

	//---------------------------------------------------------------

	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		Class<? extends Entity> type = getTypeParam(parameters,"type");
		float[] coor = getCoorParam(parameters,"coor");
		String owner = getStringParam(parameters,"owner");
		
		if(coor == null)
			return 0.0f;
		
		// First check the map:
		Entity e = gs.getMap().getCellLocation(coor);
		if (e!=null)
			if ( ((type == null) || type == e.getClass()) && 
				 ((owner == null) || e.getowner().equals(owner)) )
				return 1.0f;	
		
		// If not, then check unit by unit:
		for(Entity e2:gs.getAllEntities()) {
			if ( ((type == null) || type == e2.getClass()) && 
				 ((owner == null) || e2.getowner().equals(owner)) &&
				 e2 instanceof PhysicalEntity && 
				 ((PhysicalEntity) e2).collision(coor)  )
				
				return 1.0f;
		}
		
		return 0.0f;
		
	} // evaluate

	//---------------------------------------------------------------
	
	/**
	 * Public method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
	public java.util.List<Pair<String,ActionParameterType>> getNeededParameters() {

		return _listOfNeededParameters;
	
	} // getNeededParameters

	//---------------------------------------------------------------
	
	/**
	 * Public static method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
	public static java.util.List<Pair<String,ActionParameterType>> getStaticNeededParameters() {

		return _listOfNeededParameters;
	
	} // getStaticNeededParameters
	
	//---------------------------------------------------------------
	//                       Static fields
	//---------------------------------------------------------------
	
	static java.util.List<Pair<String,ActionParameterType>> _listOfNeededParameters;
	
	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add parameters to _listOfNeededParameters.
		_listOfNeededParameters = new java.util.LinkedList<Pair<String,ActionParameterType>>(gatech.mmpm.sensor.Sensor.getStaticNeededParameters());
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("type", ActionParameterType.ENTITY_TYPE));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("coor", ActionParameterType.COORDINATE));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("owner", ActionParameterType.PLAYER));
		
	} // static initializer

	//---------------------------------------------------------------

	/* Old code
	public boolean internalEquivalents(Sensor c) 
	{
		if (getClass().equals(c.getClass())) {
			EntityTypeAt cc = (EntityTypeAt)c;
			
			if (!m_entityType.equals(cc.m_entityType)) return false;
			if (m_cellCoords.length!=cc.m_cellCoords.length) return false;
			for(int i=0;i<m_cellCoords.length;i++) 
				if (m_cellCoords[i]!=cc.m_cellCoords[i]) return false;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Writes the ENTITY TYPE AT Sensor to an XMLWriter object
	 * Writes the co-ordinates of the Entity to XML 
	 * @param w The XMLWriter object
	 *
	public void writeToXML(XMLWriter w) {
		w.rawXML("<Sensor type = \"" + this.getClass().getName() + "\">");
		String coords = new String();
		for(int i=0;i<m_cellCoords.length;i++) {
			coords = coords + m_cellCoords[i]+ " ";
		}
		w.tag("coordinates",coords);
		w.tag("entityType",m_entityType);
		w.rawXML("</Condition>");
	}

	
	public static Sensor loadFromXMLInternal(Element xml) {
		String entityType = xml.getChildText("entityType");
		int coords[]={0,0,0};
		
		String coordsString = xml.getChildText("coordinates");
		StringTokenizer st = new StringTokenizer(coordsString," ");
		{
			int i = 0;
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				coords[i++] = Integer.parseInt(token);
			}
		}
		
		return new EntityTypeAt(entityType,coords);
	}

	public String toString() {
		if (m_cellCoords.length==1) {
			return "EntityTypeAtCondition(" + m_entityType + ", " + m_cellCoords[0] + ")";
		} else if (m_cellCoords.length==2) {
			return "EntityTypeAtCondition(" + m_entityType + ", " + m_cellCoords[0] + ", " + m_cellCoords[1] + ")";
		} else if (m_cellCoords.length==3) {
			return "EntityTypeAtCondition(" + m_entityType + ", " + m_cellCoords[0] + ", " + m_cellCoords[1] + ", " + m_cellCoords[2] + ")";			
		} else {
			return "EntityTypeAtCondition(" + m_entityType + ", " + m_cellCoords + ")";			
		}
	}
	*/

} // class EntityTypeAt
