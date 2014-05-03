/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.builtin;


import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.Entity;
import gatech.mmpm.GameState;
import gatech.mmpm.Map;
import gatech.mmpm.PhysicalEntity;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;

/**
 * This condition tests whether there is an entity with the specified 
 * ID in the specified coordinates.
 * 
 * @author Santi Onta��n Villar and David Llanso
 * @organization: Georgia Institute of Technology
 * @date 29-April-2008
 * 
 * @note Updated according to the new gatech.mmpm.Sensor class. 04-November-2009
 * 
 */
public class EntityTypeExists extends Sensor 
{

	public EntityTypeExists() 
	{
	} // Constructor

	//---------------------------------------------------------------

	public EntityTypeExists(EntityTypeExists ete) 
	{
		super(ete);

	} // Copy constructor 

	//---------------------------------------------------------------

	public Object clone() 
	{
		return new EntityTypeExists();
		
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

	// TODO Check what is what we receive when the parameter is not
	// 		specified: null? an empty string?.
	public Object evaluate(int cycle, GameState gs, String player, Context parameters) 
	{
		Class<? extends Entity> type = getTypeParam(parameters,"type");
		String owner = getStringParam(parameters,"owner");
	
		// First check the map
		Map m = gs.getMap();
		for(int i = 0;i<m.size();i++) 
		{
			PhysicalEntity e = m.get(i);
            if (e!=null) {
                if( ((type == null) || type == e.getClass()) &&
                    ((owner == null) || (e.getowner()!=null && e.getowner().equals(owner))) )
                    return 1.0f;
            }
		}

		// If not, then check unit by unit:
		for(Entity e:gs.getAllEntities()) 
		{
			if( ((type == null) || type == e.getClass()) && 
				((owner == null) || (e.getowner()!=null && e.getowner().equals(owner))) ) 
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
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("owner", ActionParameterType.PLAYER));
		
	} // static initializer
	
	//---------------------------------------------------------------
	
	/* Old code
	public boolean internalEquivalents(Sensor c) {
		if (getClass().equals(c.getClass())) {
			EntityTypeExists cc = (EntityTypeExists) c;

			if (!m_entityType.equals(cc.m_entityType))
				return false;
			return true;
		}

		return false;
	}

	public void writeToXML(XMLWriter w) {
		w.rawXML("<Sensor type = \"" + this.getClass().getName() + "\">");
		w.tag("entityType", m_entityType);
		if (m_player!=null) w.tag("player", m_player);
		w.rawXML("</Condition>");
	}

	public static Sensor loadFromXMLInternal(Element xml) {
		String entityType = xml.getChildText("entityType");
		String player = xml.getChildText("player");
		return new EntityTypeExists(entityType,player);
	}
	*/

} // class EntityTypeExists
