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
import s3.mmpm.actions.Attack;
import s3.mmpm.actions.AttackLocation;
import s3.mmpm.actions.Move;
import s3.mmpm.actions.Stop;


import java.util.List;

/**
 * Class that represents a particular entity type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Entity for more information.
 */
public class WKnight extends WTroop {

	public WKnight(String entityID, String owner) {
	
		super(entityID, owner);
		_max_hitpoints = 90;
		_width = 1;
		_length = 1;
		_cost_gold = 800;
		_cost_wood = 100;
		_cost_oil = 0;
		
	} // Constructor

	//---------------------------------------------------------------

	public WKnight(WKnight rhs) {

		super(rhs);

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		WKnight e = new WKnight(this);
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

		return true;

	} // isActive

	//---------------------------------------------------------------
	//                       Getter & setter
	//---------------------------------------------------------------



	//---------------------------------------------------------------
	//                 Generic getter and setter
	//---------------------------------------------------------------

	public Object featureValue(String feature) {

			return super.featureValue(feature);
	
	} // featureValue

	//---------------------------------------------------------------

	public void setFeatureValue(String feature, String value) {

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
	


	static java.util.List<String> _listOfFeatures;

	static java.util.List<Action> _listOfActions;

	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add features to _listOfFeatures.
		_listOfFeatures = new java.util.LinkedList<String>(WTroop.staticListOfFeatures());

		// Add valid actions to _listOfActions.
		_listOfActions = new java.util.LinkedList<Action>(WTroop.staticListOfActions());
		Action a;
		a = new Move(null, null);
		_listOfActions.add(a);

		a = new Attack(null, null);
		_listOfActions.add(a);

		a = new AttackLocation(null, null);
		_listOfActions.add(a);

		a = new Stop(null, null);
		_listOfActions.add(a);


	} // static initializer

} // class WKnight

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

