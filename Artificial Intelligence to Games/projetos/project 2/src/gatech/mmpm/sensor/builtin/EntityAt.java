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
 * This condition tests whether there is an entity with the specified 
 * ID in the specified coordinates.
 *  
 * @author Santi Ontanon Villar and David Llanso
 * @organization: Georgia Institute of Technology
 * @date 29-April-2008
 * 
 * @note Updated according to the new gatech.mmpm.Sensor class. 04-November-2009
 * 
 */
public class EntityAt extends Sensor 
{
	
	public EntityAt() 
	{
	} // Constructor

	//---------------------------------------------------------------

	public EntityAt(EntityAt ea) 
	{
		super(ea);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new EntityAt();
		
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
		Entity entity = getEntityParam(parameters,"entity");
		float[] coor = getCoorParam(parameters,"coor");

		if(coor == null)
			return 0.0f;

		// If the entity isn't specified we only want to know if there is
		// any entity in the specified coordinates. So we search in the map.
		if(entity == null)
		{
			Entity e = gs.getMap().getCellLocation(coor);
			if (e == null) 
				return 0.0f;
			return 1.0f;
		}
		
		if(!(entity instanceof PhysicalEntity))
			return 0.0f;
		
		// Both parameter are specified. We only check if entity coordinates
		// fit with the given coordinates.
		int[] cellCoor = gs.getMap().toCellCoords(coor);
		int[] entityCellCoor = gs.getMap().toCellCoords(((PhysicalEntity)entity).get_Coords());
		for(int i=0;i<cellCoor.length;i++) {
			if (cellCoor[i] != entityCellCoor[i]) 
				return 0.0f;
		}
		return 1.0f;
		
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("entity", ActionParameterType.ENTITY_ID));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("coor", ActionParameterType.COORDINATE));
		
	} // static initializer

	//---------------------------------------------------------------

	/* Old code
	 * 
	 * Protected method called from equivalents to compare
	 * two sensors. Subclasses must override this method to
	 * decide if a sensor of the current class is equivalent
	 * to the current sensor.
	 * 
	 * @param s Sensor to compare the current one with.
	 * It will always be an instance of the current class.
	 * @return True if both sensors are equivalents.
	 * 
	 * @note This method should never be externally called.
	 * Use equivalents() instead.
	 *
	public boolean internalEquivalents(Sensor c) 
	{
	 	if (getClass().equals(c.getClass())) {
			EntityAt cc = (EntityAt)c;
			
			if (!m_entityID.equals(cc.m_entityID)) return false;
			if (m_coords.length!=cc.m_coords.length) return false;
			for(int i=0;i<m_coords.length;i++) 
				if (m_coords[i]!=cc.m_coords[i]) return false;
			
			return true;
		}
		
		return false;
		
	} // internalEquivalents

	//---------------------------------------------------------------

	/* Old code
	 * 
	 * Writes the ENTITY AT Sensor to an XMLWriter object
	 * Writes the co-ordinates of the Entity to XML 
	 * @param w The XMLWriter object
	 *
	public void writeToXML(XMLWriter w) {
		w.rawXML("<Sensor type = \"" + this.getClass().getName() + "\">");
		w.tag("coordinates");
		String coords = new String();
		for(int i=0;i<m_coords.length;i++) {
			coords = coords + m_coords[i]+ " ";
		}
		w.rawXML(coords);
		w.tag("/coordinates");
		w.tag("entityID",m_entityID);
		w.rawXML("</Condition>");
	}
	
	public static Sensor loadFromXMLInternal(Element xml) {
		String entityID = xml.getChildText("entityID");
		int coords[]={0,0,0};
		
		String coordsString = xml.getChildText("coordinates").trim();
		System.out.println(coordsString);
		StringTokenizer st = new StringTokenizer(coordsString," ");
		{
			int i = 0;
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				coords[i++] = Integer.parseInt(token);
			}
		}
		
		return new EntityAt(entityID,coords);
	}*/

} // class EntityAt
