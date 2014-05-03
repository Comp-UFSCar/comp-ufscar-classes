/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor;

import gatech.mmpm.Context;
import gatech.mmpm.Entity;
import gatech.mmpm.GameState;
import gatech.mmpm.ActionParameterType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import org.jdom.Element;

import gatech.mmpm.util.Pair;
import gatech.mmpm.util.XMLWriter;

public abstract class Sensor implements Cloneable {
	
	public static final float BOOLEAN_TRUE_THRESHOLD = 1.0f;

	public Sensor() {
	}
	
	public Sensor(Sensor rhs) {
	}
	
	public Object evaluate(int cycle, GameState gs, String player) {
		return evaluate(cycle, gs, player, null);
	}
	
	public abstract Object evaluate(int cycle, GameState gs, String player, Context parameters);

	public abstract Object clone();

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
	public abstract ActionParameterType getType();


	/**
	 * Writes the Sensor into an xml file.
	 * @param w XMLWriter to eases the process.
	 */
	public void writeToXML(XMLWriter w) {
		w.rawXML(" <Sensor type = \"" + this.getClass().getName() + "\">");
		w.tag("/Sensor");
	}


	/**
	 * Creates a Sensor from an xml Element. It calls to the loadFromXMLInternal
	 * method simulating inheritance because it's an static method.
	 * @param xml Element for creating the Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXML(Element xml) {
		try {
			Class<?> askedClass;
			askedClass = Class.forName(xml.getAttributeValue("type"));

			Class<? extends Sensor> baseClass = null;
			
			Method method = null;
			// Looking for the loadFromXMLInternal method simulating inheritance.
			while(method == null)
			{
				//Ensure that base class inherits from Sensor.
				try {
					baseClass = askedClass.asSubclass(Sensor.class);
				} catch (java.lang.ClassCastException e) {
					System.out.println(askedClass.getName() + 
							" does not extend Sensor class.");
					e.printStackTrace();
				}
				// Iterating over the methods.
				for(Method m: baseClass.getMethods())
				{
					// Checking if it is the method we are looking for.
					if( m.getName().equals("loadFromXMLInternal") &&
							m.getParameterTypes().length == 1 &&
							m.getParameterTypes()[0].equals(Element.class))
					{
						method = m;
						break;
					}
				}
				// If actual class has not the the method, we simulate inheritance
				// by looking for into the super class.
				askedClass = baseClass.getSuperclass();
			}
			return (Sensor) method.invoke(baseClass, xml);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Creates a Sensor from an xml Element.
	 * @param xml Element for creating the Sensor.
	 * @return Created Sensor.
	 */
	public static Sensor loadFromXMLInternal(Element xml) {		
		try {
			Class<?> c;
			c = Class.forName(xml.getAttributeValue("type"));

			Class<? extends Sensor> baseClass = c.asSubclass(Sensor.class);
			Sensor sensor = baseClass.newInstance();
		/*	for(Object o:xml.getChildren("parameter")) {
				Element e = (Element)o;
				sensor.setParameterValue(e.getAttributeValue("name"), e.getText());
			}
			*/
			return sensor;
		} catch (Exception e) {
			System.out.println("System crashes when loading "+ xml.getAttributeValue("type") + " sensor.");
			e.printStackTrace();
		}
					
		return null;
	}

	/**
	 * Returns true if the current object and the sensor of the
	 * parameter are equivalents (equals).
	 * 
	 * The implementation of this method checks if both this one
	 * and the parameter sensor are instances of the same class.
	 * If they are not, false is returned. In other case,
	 * internalEquivalents() method is called. Subclasses must
	 * override that method instead of this one to decide if
	 * two sensors are equivalents.
	 *
	 * @param s Sensor to compare the current one with.
	 * @return True if both sensors are equivalents.
	 */
    final public boolean equivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {

		if (s2 == null)
			return false;
		else if (!(s2.getClass().equals(getClass())))
			return false;
		else
			return internalEquivalents(parameters1, cycle1, gs1, player1, s2, parameters2, cycle2, gs2, player2);

	} // equivalents

    /**
	 * Protected method called from equivalents to compare
	 * two sensors. Subclasses should override this method to
	 * decide if a sensor of the current class is equivalent
	 * to the current sensor. By default it returns false.
	 * 
	 * @param s Sensor to compare the current one with.
	 * It will always be an instance of the current class.
	 * @return True if both sensors are equivalents.
	 * 
	 * @note This method should never be externally called.
	 * Use equivalents() instead.
	 */
	protected boolean internalEquivalents(Context parameters1, int cycle1, GameState gs1, String player1, Sensor s2, Context parameters2, int cycle2, GameState gs2, String player2) {

		if (!s2.getClass().equals(getClass())) return false;

        for(Pair<String,ActionParameterType> p:getNeededParameters()) {
            Object v1 = getParam(parameters1,p._a);
            Object v2 = getParam(parameters2,p._a);
            if (v1==null && v2!=null) return false;
            if (v2==null && v1!=null) return false;
            if (v1!=null && v2!=null &&
                !v1.equals(v2)) return false;
        }

        return true;
	} // internalEquivalents

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

		return new java.util.LinkedList<Pair<String,ActionParameterType>>();
	
	} // getNeededParameters
	
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

		return new java.util.LinkedList<Pair<String,ActionParameterType>>();
	
	} // getStaticNeededParameters
	
	/**
	 * Returns a String taken from 'parameters' context with 
	 * the 'param' key.
	 * @param parameters Context with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted string.
	 */
	public static String getStringParam(Context parameters, 
										String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		return (String)o;
	}
	
	/**
	 * Returns an Integer taken from 'parameters' context with 
	 * the 'param' key.
	 * @param parameters Hash map with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted Integer.
	 */
	public static Integer getIntParam(Context parameters, 
									  String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		return (Integer)o;
	}
	
	/**
	 * Returns a Float taken from 'parameters' context with 
	 * the 'param' key.
	 * @param parameters Hash map with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted Float.
	 */
	public static Float getFloatParam(Context parameters, 
									  String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		return (Float)o;
	}
	
	/**
	 * Returns a Boolean taken from 'parameters' context with 
	 * the 'param' key.
	 * @param parameters Hash map with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted Boolean.
	 */
	public static Boolean getBoolParam (Context parameters, 
									  String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		return (Boolean)o;
	}
	
	/**
	 * Returns a Coordinate (float[]) taken from 'parameters' 
	 * context with the 'param' key.
	 * @param parameters Context with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted Coordinate (float[]).
	 */
	public static float[] getCoorParam(Context parameters, 
									  String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		return (float[])o;
	}
	
	/**
	 * Returns a Class taken from 'parameters' context with 
	 * the 'param' key.
	 * @param parameters Context with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted Class.
	 */
	public static Class<? extends Entity> getTypeParam(Context parameters, 
									  String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		
		try {
			Class<?> c = (Class<?>)o;
			Class<? extends Entity> result = c.asSubclass(gatech.mmpm.Entity.class);
			return result;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns an Entity taken from 'parameters' context with 
	 * the 'param' key.
	 * @param parameters Context with the parameters.
	 * @param param The parameter key to extract from the map.
	 * @return The extracted Entity.
	 */
	public static gatech.mmpm.Entity getEntityParam(Context parameters,
									  String param) 
	{
		Object o = parameters.get(param);
		if(o == null)
			return null;
		return (gatech.mmpm.Entity)o;
	}
	
	public static Object getParam(Context parameters, String param)
	{
		Object o = parameters.get(param);
		return o;
	}

} // class Sensor
