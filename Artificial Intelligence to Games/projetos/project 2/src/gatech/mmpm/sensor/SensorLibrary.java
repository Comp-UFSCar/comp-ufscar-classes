/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.sensor;



import gatech.mmpm.ActionParameterType;

import java.util.ArrayList;
import java.util.List;

public class SensorLibrary {
	static ArrayList<Sensor> m_sensors = new ArrayList<Sensor>();
	static ArrayList<Sensor> m_conditions = new ArrayList<Sensor>();
	static ArrayList<Sensor> m_goals = new ArrayList<Sensor>();

	public SensorLibrary()
	{
		
	}
	public static void registerSensor(Sensor s) {
		m_sensors.add(s);
	}

	public static Sensor getSensor(int index) {
		return m_sensors.get(index);
	}

	public static void registerCondition(Sensor c) {
		m_conditions.add(c);
	}

	public static Sensor getCondition(int index) {
		return m_conditions.get(index);
	}

	public static void registerGoal(Sensor c) {
		m_goals.add(c);
	}

	public static Sensor getGoal(int index) {
		return m_goals.get(index);
	}

	
	public static int getSensorCount() {
		return m_sensors.size();
	}
	
	public static int getConditionCount() {
		return m_conditions.size();
	}

	public static int getGoalCount() {
		return m_goals.size();
	}
	
	public static List<Sensor> getSensors() {
		return m_sensors;
	}
	
	public static List<Sensor> getSensorsByType(ActionParameterType ... types) {
		List<Sensor> sensorsByType = new ArrayList<Sensor>();
		for(Sensor s: m_sensors) {
			for(ActionParameterType type: types) 
				if(s.getType() == type)
					sensorsByType.add(s);
		}
		return sensorsByType;
	}
	
	public static List<Sensor> getConditions() {
		return m_conditions;
	}
	
	public static List<Sensor> getGoals() {
		return m_goals;
	}
	
}
