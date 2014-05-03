/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

import gatech.mmpm.util.XMLWriter;

import java.util.List;

/**
 * Superclass of all MMPM entities.
 * 
 * @author Redesign by Pedro Pablo Gomez Martin, August, 2009
 * based in previous work of other people.
 */
public abstract class Entity implements Cloneable {

    public Entity(String iEntityID, String iOwner) {
        entityID = iEntityID;
        owner = iOwner;
    }

    //---------------------------------------------------------------
    public Entity() {
    }

    //---------------------------------------------------------------
    /**
     * Copy constructor
     *
     * @param incoming
     */
    public Entity(Entity incoming) {
        this.entityID = incoming.entityID;
        this.owner = incoming.owner;
    }

    public abstract Object clone();

    //---------------------------------------------------------------
    // You must also implements
    // public static char shortName() although Java
    // do not provide a way to force you to do it!
    /**
     * Short name of the entity (used for serialization).
     * Subclasses must implement this method.
     * @return Short name of the entity (used for serialization).
     *
     * @note Apart from this method, subclasses <em>must</em>
     * also implement the method:
     *
     *  <tt>public static char shortName()</tt>
     *
     * which will return exactly the same value of this one.
     * Keep in mind that Java do not provide a way to force you
     * to do it, so we trust on you!
     */
    public abstract char instanceShortName();

    //---------------------------------------------------------------
    /**
     * Returns a list with the names of the features that
     * this entity has.
     *
     * @return List of the entity features.
     *
     * @note This method must be overwritten in each
     * subclass.
     */
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
    public String getowner() {
        return owner;
    }

    public void setowner(String iOwner) {
        owner = iOwner;
    }

    public String getentityID() {
        return entityID;
    }

    public void setentityID(String iEntityID) {
        entityID = iEntityID;
    }

    //---------------------------------------------------------------
    //                 Generic getter and setter
    //---------------------------------------------------------------
    /**
     * Returns a feature value by its name.
     *
     * @param feature Feature name which value want to be recovered.
     * As special values, if <tt>type</tt> is provided, the class name will
     * be returned. On the other hand, <tt>id</tt> is an alias for
     * <tt>entityID</tt>.
     * @return Feature value, or null if it do not exist.
     *
     * @note This method MUST BE overwritten in subclasses if more
     * features are added.
     */
    public Object featureValue(String feature) {

        // D2 did not consider "type" and "id" as common features
        // (reported by listOfFeatures) but, for some reason, it let
        // get them using featureValue... We will not change this.

        if (feature == null) {
            return null;
        } else if (feature.compareTo("type") == 0) {
            return getClass().getSimpleName();
        } else if (feature.compareTo("id") == 0) {
            return entityID;
        }

        // "Real" features.
        if (feature.compareTo("owner") == 0) {
            return getowner();
        } else if (feature.compareTo("entityID") == 0) {
            return getentityID();
        } else {
            return null;
        }

    } // featureValue

    //---------------------------------------------------------------
    /**
     * Set a feature value by its name.
     *
     * @param feature Feature name which value want to be set.
     * @param value New value. It will be converted to the real
     * data type.
     *
     * @note This method MUST BE overwritten in subclasses if more
     * features are added.
     */
    public void setFeatureValue(String feature, String value) {

        if (feature.compareTo("owner") == 0) {
            setowner(value);
        } else if (feature.compareTo("entityID") == 0) {
            setentityID(value);
        }

    } // setFeatureValue

    //---------------------------------------------------------------
    //                 Support methods
    //---------------------------------------------------------------
    public boolean hasFeature(String feature) {

        if (feature == null) {
            return false;
        }

        return listOfFeatures().contains(feature);

    } // hasFeature

    public boolean equivalents(Entity e) {
        if (!getClass().equals(e.getClass())) {
            return false;
        }
        for (String f : listOfFeatures()) {

            // We require them to be the same in all features except the ID:
            if (!f.equals("entityID")) {
                Object v = featureValue(f);
                if (v == null) {
                    if (e.featureValue(f) != null) {
                        return false;
                    }
                } else {
                    if (!v.equals(e.featureValue(f))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String toString() {
        String out = "Entity(" + entityID + "): " + getClass().getSimpleName() + " [ ";
        for (String f : listOfFeatures()) {
            out += "(" + f + " = " + featureValue(f) + ") ";
        }

        return out + "]";
    }

    /**
     * Writes the entity info to an XMLWriter object
     * @param w The XMLWriter object
     */
    public void writeToXML(XMLWriter w) {
        w.tagWithAttributes("entity", "id=\"" + entityID + "\"");
        w.tag("type", getClass().getSimpleName());

        for (String f : listOfFeatures()) {
            if (featureValue(f) != null) {
                try {
                    Entity e = this.getClass().newInstance();
                    if (!featureValue(f).equals(e.featureValue(f))) {
                        w.tag(f, featureValue(f));
                    } // if
                } catch (Exception e) {
                    w.tag(f, featureValue(f));
                }
            }
        }
        w.tag("/entity");
    }

    /**
     * Writes the difference (if any) between the two entities to an XMLWriter object
     * If there are no differences, then only the Entity ID is written
     * @param w The XMLWriter object
     * @param prevStateEntity The previous entity to compare
     */
    public void writeDifferenceToXML(XMLWriter w, Entity prevStateEntity) {
        /*
         * Santi: I'm commenting this out, since it has exactly the same effect as the code underneath, and at a higher
         * computational cost.
        if (prevStateEntity != null) {
        if (this.toString().equals(prevStateEntity.toString())) {
        w.rawXML("<entity id=\"" + entityID + "\"></entity>");
        return;
        }
        }
         */
        w.tagWithAttributes("entity", "id=\"" + entityID + "\"");
        if (prevStateEntity == null) {
            w.tag("type", getClass().getSimpleName());
        }
        for (String f : listOfFeatures()) {
            if (featureValue(f) != null) {
                /*
                 * 				Santi: I'm commenting this out, since it is not correct. Notice this situation:
                 * 					   - an entity has a default value for a feature 'f' of 'x'
                 * 					   - the prevStateEntity has a value for 'f' different than 'x', say 'y'
                 * 					   - the current entity has value for 'f' exactly 'x'
                 * 					   - because of this code, that value won't be saved. But it HAS to be saved, otherwise
                 * 					     the loading code will assume that the value for 'f' is 'y'. Which is incorrect
                 */

//				try {
//					Entity e = this.getClass().newInstance();
//					if (!featureValue(f).equals(e.featureValue(f))) {
//						w.tag(f,featureValue(f));
//					} // if
//				} catch(Exception e) {
                if (prevStateEntity != null) {
                    if (!featureValue(f).equals(prevStateEntity.featureValue(f))) {
                        w.tag(f, featureValue(f));
                    }
                } else {
                    w.tag(f, featureValue(f));
                }
//				}
            }
        }
        w.tag("/entity");
    }

    public boolean canExecute(Action a) {

        List<Action> la = listOfActions();
        boolean match = true;

        /*
        System.out.println("Actions for type " + this.getClass().getName());
        for(Action a_pattern:la) {
            System.out.println(a_pattern);
        }
        */

        for (Action a_pattern : la) {
            match = true;
            if (a_pattern.getClass().equals(a.getClass())) {
//				System.out.println("pattern: " + a_pattern); 
                for (ActionParameter ap : a_pattern.listOfParameters()) {
                    // We don't want to check actionID of actions because every
                    // action have its own actionID, even actions of the same
                    // class have different actionIDs.
                    if (ap.m_name != "actionID") {
                        String v = a_pattern.parameterStringValue(ap.m_name);
                        //					System.out.println("pattern: feature " + ap.m_name + " = "+ v);
                        if (v != null) {
                            String v2 = a.parameterStringValue(ap.m_name);

                            if (!v.equals(v2)) {
                                //							System.out.println("match fails because of " + ap.m_name);
                                match = false;
                            }
                        }
                    }
                }

//				System.out.println("match: " + match);
                if (match) {
                    return true;
                }
            }
        }
        return false;
        /*


        Class<?> c = this.getClass();

        while(c!=Entity.class) {
        List<Action> la = executableActions.get(c.getSimpleName());
        boolean match = true;

        //			System.out.println("canExecute: " + this.getClass().getName() + " -> " + a);

        if (la==null) return false;
        for(Action a_pattern:la) {
        match = true;
        if (a_pattern.getClass().equals(a.getClass())) {
        //					System.out.println("pattern: " + a_pattern);
        for(ActionParameter ap:a_pattern.getParameters()) {
        String v = a_pattern.parameterValue(ap.m_name);
        //					System.out.println("pattern: feature " + ap.m_name + " = "+ v);
        if (v!=null) {
        String v2 = a.parameterValue(ap.m_name);

        if (!v.equals(v2)) {
        //								System.out.println("match fails because of " + ap.m_name);
        match = false;
        }
        }
        }

        //					System.out.println("match: " + match);
        if (match) return true;
        }
        }

        c = c.getSuperclass();
        }

        return false;
         */
    } // canExecute

    //---------------------------------------------------------------
    //                       Static methods
    //---------------------------------------------------------------
    /**
     * Short name of the entity (used for serialization).
     * Subclasses must "overwrite" this method.
     * @return Short name of the entity (used for serialization).
     *
     * @note Keep in mind that this method is static so it cannot
     * be really overwritten. Subclasses must implement it anyway.
     */
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
    protected String entityID = null;
    protected String owner = null;
    static java.util.List<String> _listOfFeatures = new java.util.LinkedList<String>();
    static java.util.List<Action> _listOfActions = new java.util.LinkedList<Action>();

    //---------------------------------------------------------------
    //                       Static initializers
    //---------------------------------------------------------------
    static {

        // Add features to _listOfFeatures.
        _listOfFeatures = new java.util.LinkedList<String>(gatech.mmpm.Entity.staticListOfFeatures());
        _listOfFeatures.add("owner");
        _listOfFeatures.add("entityID");

    } // static initializer
} // class Entity

