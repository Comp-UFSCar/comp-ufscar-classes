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
public class WPlayer extends gatech.mmpm.Entity {

	public WPlayer(String entityID, String owner) {
	
		super(entityID, owner);
		
	} // Constructor

	//---------------------------------------------------------------

	public WPlayer(WPlayer rhs) {

		super(rhs);
		_race = rhs._race;
		_gold = rhs._gold;
		_wood = rhs._wood;
		_oil = rhs._oil;
		_research = rhs._research;

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		WPlayer e = new WPlayer(this);
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
	//        race feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public String getRace() {

		return _race;

	} // getRace

	public void setRace(String rhs) {

		_race = rhs;

	} // setRace

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        gold feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getGold() {

		return _gold;

	} // getGold

	public void setGold(int rhs) {

		_gold = rhs;

	} // setGold

	public void setGold(String rhs) {

		_gold = Integer.parseInt(rhs);

	} // setGold(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        wood feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getWood() {

		return _wood;

	} // getWood

	public void setWood(int rhs) {

		_wood = rhs;

	} // setWood

	public void setWood(String rhs) {

		_wood = Integer.parseInt(rhs);

	} // setWood(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        oil feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public int getOil() {

		return _oil;

	} // getOil

	public void setOil(int rhs) {

		_oil = rhs;

	} // setOil

	public void setOil(String rhs) {

		_oil = Integer.parseInt(rhs);

	} // setOil(String)

	//- - - - - - - - - - - - - - - - - - - - - - - -
	//        research feature 
	//- - - - - - - - - - - - - - - - - - - - - - - - 

	public String getResearch() {

		return _research;

	} // getResearch

	public void setResearch(String rhs) {

		_research = rhs;

	} // setResearch


	//---------------------------------------------------------------
	//                 Generic getter and setter
	//---------------------------------------------------------------

	public Object featureValue(String feature) {

		if (feature.compareTo("race") == 0)
			return getRace();
		else 
		if (feature.compareTo("gold") == 0)
			return getGold();
		else 
		if (feature.compareTo("wood") == 0)
			return getWood();
		else 
		if (feature.compareTo("oil") == 0)
			return getOil();
		else 
		if (feature.compareTo("research") == 0)
			return getResearch();
		else 
			return super.featureValue(feature);
	
	} // featureValue

	//---------------------------------------------------------------

	public void setFeatureValue(String feature, String value) {

		if (feature.compareTo("race") == 0)
			setRace(value);
		else 
		if (feature.compareTo("gold") == 0)
			setGold(value);
		else 
		if (feature.compareTo("wood") == 0)
			setWood(value);
		else 
		if (feature.compareTo("oil") == 0)
			setOil(value);
		else 
		if (feature.compareTo("research") == 0)
			setResearch(value);
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
	
	protected String _race;

	protected int _gold;

	protected int _wood;

	protected int _oil;

	protected String _research;



	static java.util.List<String> _listOfFeatures;

	static java.util.List<Action> _listOfActions;

	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add features to _listOfFeatures.
		_listOfFeatures = new java.util.LinkedList<String>(gatech.mmpm.Entity.staticListOfFeatures());
		_listOfFeatures.add("race");
		_listOfFeatures.add("gold");
		_listOfFeatures.add("wood");
		_listOfFeatures.add("oil");
		_listOfFeatures.add("research");

		// Add valid actions to _listOfActions.
		_listOfActions = new java.util.LinkedList<Action>(gatech.mmpm.Entity.staticListOfActions());

	} // static initializer

} // class WPlayer

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

