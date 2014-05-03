/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

//*******************************************************//
// VERY IMPORTANT NOTE:
// THIS INTERFACE CANNOT BE CHANGED.
// Automatic tools generate Java code of classes that
// implement this interface.
// If you change this file, then you MUST also update
// those tools!
//*******************************************************//

/**
 * Interface with the methods that D2 uses to get all the information
 * it needs about the current domain (game).
 * <p>
 * Game developers should create a class of this interface for
 * every new game they create.
 *  
 * @author Marco Antonio Gomez Martin
 *
 * @note This class is intended to overwrite the behaviour of the
 * domain.[game] package, and other functionality that were
 * spread on the D2 constructor.
 */
public interface IDomain {

	/**
	 * Returns the name of the domain (usually the game name).
	 * @return Name of the domain.
	 */
	String getName();
	
	/**
	 * Return the world model of the game.
	 * @return World model or null if there is no one.
	 */
    // This was to be a D2 hack. MMPM will not provide that.
	// Game XML should be changed to removed this element.
	//worldmodel.WorldModel getWorldModel();
	
	/**
	 * Return a list of the entities defined by the domain.
	 * All of them must inherit from domain.Entity (D2 class)
	 * @return The list of classes that conforms the domain.
	 * All the classes should inherit from domain.Entity.
	 */
	Class<? extends gatech.mmpm.Entity>[] getEntities();

    /**
    * Method that return a new entity generated from its short name class.
    * @return New instance of the specified entity.
    */
    public gatech.mmpm.Entity getEntityByShortName(char shortName, String entityId, String owner);

    /**
    * Method that return a new entity generated from its name class.
    * @return New instance of the specified entity.
    */
    public gatech.mmpm.Entity getEntityByName(String name, String entityId, String owner);

	/**
	 * Return a list of the actions defined by the domain.
	 * All of them must inherit from goals.Action (D2 class).
	 * @return The list of classes that represent all the
	 * possible actions in the game.
	 */
	Class[] getActions();
	
	/**
	 * Return a list of all the sensors the game has.
	 * @return List of all the sensors the game has. The sensors
	 * will be added to the sensors library in the same order.
	 */
	gatech.mmpm.sensor.Sensor[] getSensors();
	
	/**
	 * Return a list of all the goals the game has.
	 * @return List of all the goals. The goals will be aded
	 * to the sensors library in the same order.
	 */
	gatech.mmpm.sensor.Sensor[] getGoals();
	
	/**
	 * Return the WinGameGoal of the domain.
	 * @return The ultimate goal that D2 has to pursuit.
	 */
	gatech.mmpm.sensor.Sensor getWinGoal();

	/**
	 * Returns the name of the rules file.
	 */
    // This was to be a D2 hack. MMPM will not provide that.
	// Game XML should be changed to removed this element.
	//String getRulesFile();
	// Someone included this method and did not
	// update the domain generation tool.
	// A hack has been done: the generation tool
	// implements this method returning always null.
	// If this is important, then the XML with the
	// game domain should include the value to be
	// returned here, and the tool should be updated.

}

//*******************************************************//
//VERY IMPORTANT NOTE:
//THIS INTERFACE CANNOT BE CHANGED.
//Automatic tools generate Java code of classes that
//implement this interface.
//If you change this file, then you MUST also update
//those tools!
//*******************************************************//

