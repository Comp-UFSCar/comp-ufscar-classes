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
import s3.mmpm.actions.Build;
import s3.mmpm.actions.Move;
import s3.mmpm.actions.Repair;
import s3.mmpm.actions.ResourceLocation;
import s3.mmpm.actions.Stop;


import java.util.List;

/**
 * Class that represents a particular entity type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Entity for more information.
 */
public class WPeasant extends WTroop {

	public WPeasant(String entityID, String owner) {
	
		super(entityID, owner);
		_max_hitpoints = 30;
		_width = 1;
		_length = 1;
		_cost_gold = 400;
		_cost_wood = 0;
		_cost_oil = 0;
		
	} // Constructor

	//---------------------------------------------------------------

	public WPeasant(WPeasant rhs) {

		super(rhs);
		_carriedGold = rhs._carriedGold;
		_carriedWood = rhs._carriedWood;

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		WPeasant e = new WPeasant(this);
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


	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        carriedGold feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCarriedGold() {

		return _carriedGold;

	} // getCarriedGold

	public void setCarriedGold(int rhs) {

		_carriedGold = rhs;

	} // setCarriedGold

	public void setCarriedGold(String rhs) {

		_carriedGold = Integer.parseInt(rhs);

	} // setCarriedGold(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        carriedWood feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getCarriedWood() {

		return _carriedWood;

	} // getCarriedWood

	public void setCarriedWood(int rhs) {

		_carriedWood = rhs;

	} // setCarriedWood

	public void setCarriedWood(String rhs) {

		_carriedWood = Integer.parseInt(rhs);

	} // setCarriedWood(String)


	//---------------------------------------------------------------
	//                 Generic getter and setter
	//---------------------------------------------------------------

	public Object featureValue(String feature) {

		if (feature.compareTo("carriedGold") == 0)
			return getCarriedGold();
		else 
		if (feature.compareTo("carriedWood") == 0)
			return getCarriedWood();
		else 
			return super.featureValue(feature);
	
	} // featureValue

	//---------------------------------------------------------------

	public void setFeatureValue(String feature, String value) {

		if (feature.compareTo("carriedGold") == 0)
			setCarriedGold(value);
		else 
		if (feature.compareTo("carriedWood") == 0)
			setCarriedWood(value);
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
	
	protected int _carriedGold;

	protected int _carriedWood;



	static java.util.List<String> _listOfFeatures;

	static java.util.List<Action> _listOfActions;

	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add features to _listOfFeatures.
		_listOfFeatures = new java.util.LinkedList<String>(WTroop.staticListOfFeatures());
		_listOfFeatures.add("carriedGold");
		_listOfFeatures.add("carriedWood");

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

		a = new Build(null, null);
		_listOfActions.add(a);

		a = new Repair(null, null);
		_listOfActions.add(a);

		a = new ResourceLocation(null, null);
		_listOfActions.add(a);


	} // static initializer

} // class WPeasant

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

