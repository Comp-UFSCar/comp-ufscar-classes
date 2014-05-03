/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor.composite;

import java.util.LinkedList;
import org.jdom.Element;

import gatech.mmpm.ActionParameterType;
import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.Pair;
import gatech.mmpm.util.XMLWriter;

/**
 * Sensor that resolves an invocation of an evaluate method of 
 * another sensor in which its parameters are given in form of 
 * sensors.
 * 
 * @author David Llanso
 * @date November, 2009
 */

public class Invocation extends Sensor {
	
	public Invocation()
	{		
		_sensor = null;
		_parameters = null;
		
	} // Invocation
	
	//---------------------------------------------------------------
	
	public Invocation(Sensor sensor, Pair<String, Sensor> ... parameters)
	{		
		_sensor = sensor;
		_parameters = parameters;
		
	} // Invocation
	
	//---------------------------------------------------------------
	
	public Invocation(Invocation i)
	{		
		this((Sensor) i._sensor.clone(), i._parameters);
		
	} // Invocation
	
	//---------------------------------------------------------------


	public Object clone() 
	{
		return new Invocation(_sensor, _parameters);
		
	} // clone
	
	//---------------------------------------------------------------


	public Object evaluate(int cycle, GameState gs, String player,
	                       Context parameters) 
	{
		// While there aren't collisions, sensorParameters = parameters.
		Context sensorParameters = parameters;
		
		for (Pair<String, Sensor> p:_parameters) 
		{
			// In case there is a name collision a new map is created.
			if(parameters == null || parameters.containsKey(p.getFirst()))
			{
				sensorParameters = new Context();
				break;
			}
		} // for
		
		// The parameters needed by the sensor are added.
		for (Pair<String, Sensor> p:_parameters) 
		{
			Object o = null;
			if(p.getSecond() != null)
				o = p.getSecond().evaluate(cycle, gs, player, parameters);
			sensorParameters.put(p.getFirst(), o);
		} // for
		
		return _sensor.evaluate(cycle, gs, player, sensorParameters);
		
	} // evaluate


    //---------------------------------------------------------------


    /*
     * This method evaluates the parameters of the sensor, and returns their specific value:
     */
	public Context evaluateParameters(int cycle, GameState gs, String player,Context parameters)
	{
		// While there aren't collisions, sensorParameters = parameters.
		Context sensorParameters = parameters;

		for (Pair<String, Sensor> p:_parameters)
		{
			// In case there is a name collision a new map is created.
			if(parameters == null || parameters.containsKey(p.getFirst()))
			{
				sensorParameters = new Context();
				break;
			}
		} // for

		// The parameters needed by the sensor are added.
		for (Pair<String, Sensor> p:_parameters)
		{
			Object o = null;
			if(p.getSecond() != null)
				o = p.getSecond().evaluate(cycle, gs, player, parameters);
			sensorParameters.put(p.getFirst(), o);
		} // for

		return sensorParameters;

	} // evaluateParameters
	
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
		return _sensor.getType();
		
	} // get Type


    public Sensor getSensor() {
        return _sensor;
    }
	
	//---------------------------------------------------------------
	
	/**
	 * Writes the LogicalOperator Sensor to an XMLWriter object 
	 * @param w The XMLWriter object
	 */
	public void writeToXML(XMLWriter w) {
		w.tagWithAttributes("Sensor", "type = '" + this.getClass().getName() + "'");
		_sensor.writeToXML(w);
		for(Pair<String, Sensor> p:_parameters)
		{
			w.tagWithAttributes("Parameter", "name = '" + p.getFirst() + "'");
			if(p.getSecond() != null)
				p.getSecond().writeToXML(w);
			w.tag("/Parameter");
		}
		w.tag("/Sensor");
	}
	
	//---------------------------------------------------------------

	/**
	 * Creates a Sensor from an xml Element.
	 * @param xml Element for creating the Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {	
		try {
			Class<?> askedClass;
			askedClass = Class.forName(xml.getAttributeValue("type"));

			Class<? extends Invocation> baseClass = askedClass.asSubclass(Invocation.class);
			Invocation ret = baseClass.newInstance();
			
			Element s_xml = (Element)xml.getChild("Sensor");
			ret._sensor = Sensor.loadFromXML(s_xml);

			LinkedList<Pair<String, Sensor>> list = new LinkedList<Pair<String, Sensor>>();

			for(Object o:xml.getChildren("Parameter")) {
				Element a_xml = (Element)o;
				String name = a_xml.getAttributeValue("name");
				s_xml = (Element)a_xml.getChild("Sensor");
				Sensor s = null;
				if(s_xml != null)
					s = Sensor.loadFromXML(s_xml);
				list.add(new Pair<String, Sensor>(name,s));
			}

			Pair<String, Sensor> b[] = new Pair[1];
			ret._parameters = list.toArray(b);

			return ret;
		} catch (Exception e) {
			System.out.println("System crashes when loading "+ xml.getAttributeValue("type") + " sensor.");
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString() {
		String ret = this.getClass().getSimpleName() + "(" + _sensor.getClass().getSimpleName();
		for(Pair<String, Sensor> p:_parameters) {
			ret += ", " + p.getFirst() + " = " + p.getSecond();
		}
		ret+=")";
		return ret;
	}

 
	protected boolean internalEquivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {
		if (!s2.getClass().equals(getClass())) return false;

        Sensor is1, is2;
        is1 = _sensor;
        is2 = ((Invocation)s2)._sensor;

        if (!is1.getClass().equals(is2.getClass())) return false;

        // compute contexts:
		// While there aren't collisions, sensorParameters = parameters.
		Context sensorParameters1 = parameters1;
		Context sensorParameters2 = parameters2;

		for (Pair<String, Sensor> p:_parameters)
		{
			// In case there is a name collision a new map is created.
			if(parameters1 == null || parameters1.containsKey(p.getFirst()))
			{
				sensorParameters1 = new Context();
				break;
			}
		} // for
        // The parameters needed by the sensor are added.
		for (Pair<String, Sensor> p:_parameters)
		{
			Object o = null;
			if(p.getSecond() != null)
				o = p.getSecond().evaluate(cycle1, gs1, player1, sensorParameters1);
			sensorParameters1.put(p.getFirst(), o);
		} // for
		
        for (Pair<String, Sensor> p:_parameters)
		{
			// In case there is a name collision a new map is created.
			if(parameters2 == null || parameters2.containsKey(p.getFirst()))
			{
				sensorParameters2 = new Context();
				break;
			}
		} // for

        // The parameters needed by the sensor are added.
        for (Pair<String, Sensor> p:((Invocation)s2)._parameters)
		{
			Object o = null;
			if(p.getSecond() != null)
				o = p.getSecond().evaluate(cycle1, gs2, player2, sensorParameters2);
			sensorParameters2.put(p.getFirst(), o);
		} // for

        return is1.equivalents(sensorParameters1, cycle1, gs1, player1, is2, sensorParameters2, cycle2, gs1, player2);
	} // internalEquivalents


	
	//---------------------------------------------------------------
	// Parameters
	//---------------------------------------------------------------
	
	/**
	 * The Sensor to invoke.
	 */
	Sensor _sensor;
	
	//---------------------------------------------------------------
	
	/**
	 * Parameters that the sensor needs to be evaluated.
	 */
	Pair<String, Sensor>[] _parameters;

} // class Invocation
