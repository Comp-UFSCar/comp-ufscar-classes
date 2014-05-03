/*******************************************************************
 *                       MACHINE GENERATED CODE                    *
 *                            DO NOT EDIT                          *
 *        FIX THE SOURCE XML INSTEAD, AND REGENERATE IT AGAIN      *
 *                                                                 *
 *                                                                 *
 * Tool: gatech.mmpm.tools.DomainGenerator                         *
 *                                                                 *
 * Organization: Georgia Institute of Technology                   *
 *               Cognitive Computing Lab (CCL)                     *
 * Authors:      Pedro Pablo Gomez Martin                          *
 *               Marco Antonio Gomez Martin                        *
 * Based on previous work of:                                      *
 *               Jai Rad                                           *
 *               Prafulla Mahindrakar                              *
 *               Santi Onta��n                                     *
 *******************************************************************/


package s3.mmpm.entities;

import gatech.mmpm.Action;


import java.util.List;

/**
 * Class that represents a particular entity type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Entity for more information.
 */
public class WUnit extends gatech.mmpm.PhysicalEntity {

	public WUnit(String entityID, String owner) {
	
		super(entityID, owner);
		_cycle_created = 0;
		_cycle_last_attacked = -1;
		_creator = "";
		
	} // Constructor

	//---------------------------------------------------------------

	public WUnit(WUnit rhs) {

		super(rhs);
		_max_hitpoints = rhs._max_hitpoints;
		_current_hitpoints = rhs._current_hitpoints;
		_status = rhs._status;
		_cost_gold = rhs._cost_gold;
		_cost_wood = rhs._cost_wood;
		_cost_oil = rhs._cost_oil;
		_attack = rhs._attack;
		_range = rhs._range;
		_cycle_created = rhs._cycle_created;
		_cycle_last_attacked = rhs._cycle_last_attacked;
		_creator = rhs._creator;

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		WUnit e = new WUnit(this);
		return e;

	} // clone

	//---------------------------------------------------------------

	public char instanceShortName() {
	
		return '\0';
	
	} // instanceShortName

	//---------------------------------------------------------------
	
	public List<String> listOfFeatures() {
	
		// Overwritten in each entity class to return the
		// class static attribute. 
		return _listOfFeatures;

	} // listOfFeatures
	
	//---------------------------------------------------------------
	
	public List<gatech.mmpm.Action> listOfActions() {
	
		// Overwritten in each entity class to return the
		// class static attribute. 
		return _listOfActions;

	} // listOfActions

	//---------------------------------------------------------------

	public boolean isActive() {

		return false;

	} // isActive

	//---------------------------------------------------------------
	//                       Getter & setter
	//---------------------------------------------------------------


	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        max_hitpoints feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getMax_hitpoints() {

		return _max_hitpoints;

	} // getMax_hitpoints

	public void setMax_hitpoints(int rhs) {

		_max_hitpoints = rhs;

	} // setMax_hitpoints

	public void setMax_hitpoints(String rhs) {

		_max_hitpoints = Integer.parseInt(rhs);

	} // setMax_hitpoints(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        current_hitpoints feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCurrent_hitpoints() {

		return _current_hitpoints;

	} // getCurrent_hitpoints

	public void setCurrent_hitpoints(int rhs) {

		_current_hitpoints = rhs;

	} // setCurrent_hitpoints

	public void setCurrent_hitpoints(String rhs) {

		_current_hitpoints = Integer.parseInt(rhs);

	} // setCurrent_hitpoints(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        status feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public String getStatus() {

		return _status;

	} // getStatus

	public void setStatus(String rhs) {

		_status = rhs;

	} // setStatus

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        cost_gold feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCost_gold() {

		return _cost_gold;

	} // getCost_gold

	public void setCost_gold(int rhs) {

		_cost_gold = rhs;

	} // setCost_gold

	public void setCost_gold(String rhs) {

		_cost_gold = Integer.parseInt(rhs);

	} // setCost_gold(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        cost_wood feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCost_wood() {

		return _cost_wood;

	} // getCost_wood

	public void setCost_wood(int rhs) {

		_cost_wood = rhs;

	} // setCost_wood

	public void setCost_wood(String rhs) {

		_cost_wood = Integer.parseInt(rhs);

	} // setCost_wood(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        cost_oil feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCost_oil() {

		return _cost_oil;

	} // getCost_oil

	public void setCost_oil(int rhs) {

		_cost_oil = rhs;

	} // setCost_oil

	public void setCost_oil(String rhs) {

		_cost_oil = Integer.parseInt(rhs);

	} // setCost_oil(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        attack feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getAttack() {

		return _attack;

	} // getAttack

	public void setAttack(int rhs) {

		_attack = rhs;

	} // setAttack

	public void setAttack(String rhs) {

		_attack = Integer.parseInt(rhs);

	} // setAttack(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        range feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getRange() {

		return _range;

	} // getRange

	public void setRange(int rhs) {

		_range = rhs;

	} // setRange

	public void setRange(String rhs) {

		_range = Integer.parseInt(rhs);

	} // setRange(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        cycle_created feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCycle_created() {

		return _cycle_created;

	} // getCycle_created

	public void setCycle_created(int rhs) {

		_cycle_created = rhs;

	} // setCycle_created

	public void setCycle_created(String rhs) {

		_cycle_created = Integer.parseInt(rhs);

	} // setCycle_created(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        cycle_last_attacked feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCycle_last_attacked() {

		return _cycle_last_attacked;

	} // getCycle_last_attacked

	public void setCycle_last_attacked(int rhs) {

		_cycle_last_attacked = rhs;

	} // setCycle_last_attacked

	public void setCycle_last_attacked(String rhs) {

		_cycle_last_attacked = Integer.parseInt(rhs);

	} // setCycle_last_attacked(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        creator feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public String getCreator() {

		return _creator;

	} // getCreator

	public void setCreator(String rhs) {

		_creator = rhs;

	} // setCreator


	//---------------------------------------------------------------
	//                 Generic getter and setter
	//---------------------------------------------------------------

	public Object featureValue(String feature) {

		if (feature.compareTo("max_hitpoints") == 0)
			return getMax_hitpoints();
		else 
		if (feature.compareTo("current_hitpoints") == 0)
			return getCurrent_hitpoints();
		else 
		if (feature.compareTo("status") == 0)
			return getStatus();
		else 
		if (feature.compareTo("cost_gold") == 0)
			return getCost_gold();
		else 
		if (feature.compareTo("cost_wood") == 0)
			return getCost_wood();
		else 
		if (feature.compareTo("cost_oil") == 0)
			return getCost_oil();
		else 
		if (feature.compareTo("attack") == 0)
			return getAttack();
		else 
		if (feature.compareTo("range") == 0)
			return getRange();
		else 
		if (feature.compareTo("cycle_created") == 0)
			return getCycle_created();
		else 
		if (feature.compareTo("cycle_last_attacked") == 0)
			return getCycle_last_attacked();
		else 
		if (feature.compareTo("creator") == 0)
			return getCreator();
		else 
			return super.featureValue(feature);
	
	} // featureValue

	//---------------------------------------------------------------

	public void setFeatureValue(String feature, String value) {

		if (feature.compareTo("max_hitpoints") == 0)
			setMax_hitpoints(value);
		else 
		if (feature.compareTo("current_hitpoints") == 0)
			setCurrent_hitpoints(value);
		else 
		if (feature.compareTo("status") == 0)
			setStatus(value);
		else 
		if (feature.compareTo("cost_gold") == 0)
			setCost_gold(value);
		else 
		if (feature.compareTo("cost_wood") == 0)
			setCost_wood(value);
		else 
		if (feature.compareTo("cost_oil") == 0)
			setCost_oil(value);
		else 
		if (feature.compareTo("attack") == 0)
			setAttack(value);
		else 
		if (feature.compareTo("range") == 0)
			setRange(value);
		else 
		if (feature.compareTo("cycle_created") == 0)
			setCycle_created(value);
		else 
		if (feature.compareTo("cycle_last_attacked") == 0)
			setCycle_last_attacked(value);
		else 
		if (feature.compareTo("creator") == 0)
			setCreator(value);
		else 
			super.setFeatureValue(feature, value);
	
	} // setFeatureValue

	//---------------------------------------------------------------
	//                       Static methods
	//---------------------------------------------------------------
	
	public static char shortName() {

		return '\0';

	} // shortName

	//---------------------------------------------------------------

	public static List<String> staticListOfFeatures() {

		return _listOfFeatures;

	}

	//---------------------------------------------------------------

	public static List<gatech.mmpm.Action> staticListOfActions() {

		return _listOfActions;

	}

	//---------------------------------------------------------------
	//                       Protected fields
	//---------------------------------------------------------------
	
	protected int _max_hitpoints;

	protected int _current_hitpoints;

	protected String _status;

	protected int _cost_gold;

	protected int _cost_wood;

	protected int _cost_oil;

	protected int _attack;

	protected int _range;

	protected int _cycle_created;

	protected int _cycle_last_attacked;

	protected String _creator;



	static java.util.List<String> _listOfFeatures;

	static java.util.List<Action> _listOfActions;

	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add features to _listOfFeatures.
		_listOfFeatures = new java.util.LinkedList<String>(gatech.mmpm.PhysicalEntity.staticListOfFeatures());
		_listOfFeatures.add("max_hitpoints");
		_listOfFeatures.add("current_hitpoints");
		_listOfFeatures.add("status");
		_listOfFeatures.add("cost_gold");
		_listOfFeatures.add("cost_wood");
		_listOfFeatures.add("cost_oil");
		_listOfFeatures.add("attack");
		_listOfFeatures.add("range");
		_listOfFeatures.add("cycle_created");
		_listOfFeatures.add("cycle_last_attacked");
		_listOfFeatures.add("creator");

		// Add valid actions to _listOfActions.
		_listOfActions = new java.util.LinkedList<Action>(gatech.mmpm.PhysicalEntity.staticListOfActions());

	} // static initializer

} // class WUnit

/*******************************************************************
 *                       MACHINE GENERATED CODE                    *
 *                            DO NOT EDIT                          *
 *        FIX THE SOURCE XML INSTEAD, AND REGENERATE IT AGAIN      *
 *                                                                 *
 *                                                                 *
 * Tool: gatech.mmpm.tools.DomainGenerator                         *
 *                                                                 *
 * Organization: Georgia Institute of Technology                   *
 *               Cognitive Computing Lab (CCL)                     *
 * Authors:      Pedro Pablo Gomez Martin                          *
 *               Marco Antonio Gomez Martin                        *
 * Based on previous work of:                                      *
 *               Jai Rad                                           *
 *               Prafulla Mahindrakar                              *
 *               Santi Onta��n                                     *
 *******************************************************************/

