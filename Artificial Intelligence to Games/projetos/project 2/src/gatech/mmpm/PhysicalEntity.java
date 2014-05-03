/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;


import java.util.List;

public class PhysicalEntity extends Entity {


	/**
	 * Constructor
	 * 
	 * @param a_entityID
	 * @param a_owner
	 */
	public PhysicalEntity(String a_entityID,String a_owner)
	{
		super(a_entityID,a_owner);
	}

	//---------------------------------------------------------------
	
	/**
	 * Copy constructor
	 * 
	 * @param incoming
	 */
	public PhysicalEntity(PhysicalEntity incoming) {
		super(incoming);
		this.coords[0] = incoming.coords[0];
		this.coords[1] = incoming.coords[1];
		this.coords[2] = incoming.coords[2];
		this._width = incoming._width;
		this._length = incoming._length;
		this._height = incoming._height;
	}

	//---------------------------------------------------------------

	public Object clone() {
		PhysicalEntity e = new PhysicalEntity(this);
		return e;
	}

	//---------------------------------------------------------------
	
	public char instanceShortName() {
		return '\0';
	}
	
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
	//                       Getter & setter
	//---------------------------------------------------------------

	public static final String coordNames[] = {"x","y","z"};
	
	protected float coords[] = {0,0,0};

	protected float _width = 1, _length = 1, _height = 1;
	
	public float getx()
	{
		return coords[0];
	}

	public void setx(float a_x)
	{
		coords[0] = a_x;
	}

	public void setx(String a_x)
	{
		coords[0] = Float.parseFloat(a_x);
	}

	public float gety()
	{
		return coords[1];
	}

	public void sety(float a_y)
	{
		coords[1] = a_y;
	}

	public void sety(String a_y)
	{
		coords[1] = Float.parseFloat(a_y);
	}
	
	public float getz()
	{
		return coords[2];
	}

	public void setz(float a_z)
	{
		coords[2] = a_z;
	}

	public void setz(String a_z)
	{
		coords[2] = Float.parseFloat(a_z);
	}
	
	public float []get_Coords() {		// Added the "_" in between get and Coords, so that the method "listOfFeatures" does not get this a =s a feature
		return coords;
	}
	
	public void set_Coords(float []c) {
		coords = c;
	}
	
	public float getwidth()
	{
		return _width;
	}

	public void setwidth(float a_width)
	{
		_width = a_width;
	}

	public void setwidth(String a_width)
	{
		_width = Float.parseFloat(a_width);
	}

	public float getlength()
	{
		return _length;
	}

	public void setlength(float a_length)
	{
		_length = a_length;
	}

	public void setlength(String a_length)
	{
		_length = Float.parseFloat(a_length);
	}
	
	
	public float getheight()
	{
		return _height;
	}

	public void setheight(float a_height)
	{
		_height = a_height;
	}

	public void setheight(String a_height)
	{
		_height = Float.parseFloat(a_height);
	}
	
	//---------------------------------------------------------------
	//                 Generic getter and setter
	//---------------------------------------------------------------

	public Object featureValue(String feature) {

		if (feature.compareTo("height") == 0)
			return getheight();
		else 
		if (feature.compareTo("width") == 0)
			return getwidth();
		else 
		if (feature.compareTo("length") == 0)
			return getlength();
		else 
		if (feature.compareTo("x") == 0)
			return getx();
		else 
		if (feature.compareTo("y") == 0)
			return gety();
		else 
		if (feature.compareTo("z") == 0)
			return getz();
		else
		if (feature.compareTo("coords") == 0)
			return get_Coords();
		else 
			return super.featureValue(feature);
	
	} // featureValue

	//---------------------------------------------------------------

	public void setFeatureValue(String feature, String value) {

		if (feature.compareTo("height") == 0)
			setheight(value);
		else 
		if (feature.compareTo("width") == 0)
			setwidth(value);
		else 
		if (feature.compareTo("length") == 0)
			setlength(value);
		else 
		if (feature.compareTo("x") == 0)
			setx(value);
		else 
		if (feature.compareTo("y") == 0)
			sety(value);
		else 
		if (feature.compareTo("z") == 0)
			setz(value);
		else 
			super.setFeatureValue(feature, value);
	
	} // setFeatureValue

	//---------------------------------------------------------------
	//                       Collision detection
	//---------------------------------------------------------------

	public boolean collision(float coords[]) {
		if (getx() > coords[0] ||
			getx()+getwidth() <= coords[0] ||
			gety() > coords[1] ||
			gety()+getlength() <= coords[1] ||
			getz() > coords[2] ||
			getz()+getheight() <= coords[2]) {
			return false;
		}
		return true;
	}

	
	public boolean collision(PhysicalEntity e) {
		if (getx() >= e.getx()+e.getwidth() ||
			getx()+getwidth() <= e.getx() ||
			gety() >= e.gety()+e.getlength() ||
			gety()+getlength() <= e.gety() ||
			getz() >= e.getz()+e.getheight() ||
			getz()+getheight() <= e.getz()) {
			return false;
		}
		return true;
	}
	
	/*
	 *  This method is like the previous one, but assumes that "coords" are the coordinaes at which
	 *  the entity "e" is located right now.
	 */
	public boolean collision(PhysicalEntity e,float coords[]) {
		if (getx() >= coords[0]+e.getwidth() ||
			getx()+getwidth() <= coords[0] ||
			gety() >= coords[1]+e.getlength() ||
			gety()+getlength() <= coords[1] ||
			getz() >= coords[2]+e.getheight() ||
			getz()+getheight() <= coords[2]) {
			return false;
		}
		return true;
	}
	
	public double collisionSoft(PhysicalEntity e) {
		if (getx() >= e.getx()+e.getwidth() ||
			getx()+getwidth() <= e.getx() ||
			gety() >= e.gety()+e.getlength() ||
			gety()+getlength() <= e.gety() ||
			getz() >= e.getz()+e.getheight() ||
			getz()+getheight() <= e.getz()) {
			return 0;
		}
		return 1;
	}

	public double collisionSoft(PhysicalEntity e,float coords[]) {
		if (getx() >= coords[0]+e.getwidth() ||
			getx()+getwidth() <= coords[0] ||
			gety() >= coords[1]+e.getlength() ||
			gety()+getlength() <= coords[1] ||
			getz() >= coords[2]+e.getheight() ||
			getz()+getheight() <= coords[2]) {
			return 0;
		}
		return 1;
	}
	
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
		_listOfFeatures = new java.util.LinkedList<String>(gatech.mmpm.Entity.staticListOfFeatures());
		_listOfFeatures.add("x");
		_listOfFeatures.add("y");
		_listOfFeatures.add("z");
		_listOfFeatures.add("width");
		_listOfFeatures.add("height");
		_listOfFeatures.add("length");

		// Add valid actions to _listOfActions.
		_listOfActions = new java.util.LinkedList<Action>(gatech.mmpm.Entity.staticListOfActions());

	} // static initializer

} // class PhysicalEntity
