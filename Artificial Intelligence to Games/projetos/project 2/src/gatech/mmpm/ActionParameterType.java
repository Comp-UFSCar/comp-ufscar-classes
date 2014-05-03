/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm;

import java.lang.reflect.Constructor;


/**
 * Different types allowed in parameters to actions, in the
 * evaluation of sensors and as basic types in the built-in
 * language for the XML domain files. 
 * 
 * In the built-in language, the allowed operators between
 * types are:
 * 
 * <table border=1 cellspacing=0 cellpadding=0>
<tr>
  <td>ENTITY_TYPE</td>
  <td>==</td> <!-- ENTITY_TYPE -->
  <td style='background:black'></td> <!-- ENTITY_ID -->
  <td style='background:black'></td> <!-- PLAYER -->
  <td style='background:black'></td> <!-- COORDINATE -->
  <td style='background:black'>&nbsp;</td> <!-- DIRECTION -->
  <td style='background:black'>&nbsp;</td> <!-- INTEGER -->
  <td style='background:black'>&nbsp;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>ENTITY_ID</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td>==</td> <!-- ENTITY_ID -->
  <td style='background:black'>&nbsp;</td> <!-- PLAYER -->
  <td style='background:black'>&nbsp;</td> <!-- COORDINATE -->
  <td style='background:black'>&nbsp;</td> <!-- DIRECTION -->
  <td style='background:black'>&nbsp;</td> <!-- INTEGER -->
  <td style='background:black'>&nbsp;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>PLAYER</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td>==</td> <!-- PLAYER -->
  <td style='background:black'>&nbsp;</td> <!-- COORDINATE -->
  <td style='background:black'>&nbsp;</td> <!-- DIRECTION -->
  <td style='background:black'>&nbsp;</td> <!-- INTEGER -->
  <td style='background:black'>&nbsp;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>COORDINATE</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td style='background:gray'>&nbsp;</td> <!-- PLAYER -->
  <td>==, +, -</td> <!-- COORDINATE -->
  <td style='background:black'>&nbsp;</td> <!-- DIRECTION -->
  <td style='background:black'>&nbsp;</td> <!-- INTEGER -->
  <td style='background:black'>&nbsp;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>DIRECTION</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td style='background:gray'>&nbsp;</td> <!-- PLAYER -->
  <td style='background:gray'>&nbsp;</td> <!-- COORDINATE -->
  <td>==</td> <!-- DIRECTION -->
  <td style='background:black'>&nbsp;</td> <!-- INTEGER -->
  <td style='background:black'>&nbsp;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>INTEGER</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td style='background:gray'>&nbsp;</td> <!-- PLAYER -->
  <td>*, / (coordinate)</td> <!-- COORDINATE -->
  <td>*, / (coordinate)</td> <!-- DIRECTION -->
  <td>==, &lt;, arithm</td> <!-- INTEGER -->
  <td style='background:black'>&nbsp;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>STRING</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td style='background:gray'>&nbsp;</td> <!-- PLAYER -->
  <td style='background:gray'>&nbsp;</td> <!-- COORDINATE -->
  <td style='background:gray'>&nbsp;</td> <!-- DIRECTION -->
  <td style='background:gray'>&nbsp;</td> <!-- INTEGER -->
  <td>==, &lt;</td> <!-- STRING  -->
  <td style='background:black'>&nbsp;</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>BOOLEAN</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td style='background:gray'>&nbsp;</td> <!-- PLAYER -->
  <td style='background:gray'>&nbsp;</td> <!-- COORDINATE -->
  <td style='background:gray'>&nbsp;</td> <!-- DIRECTION -->
  <td style='background:gray'>&nbsp;</td> <!-- INTEGER -->
  <td style='background:gray'>&nbsp;</td> <!-- STRING  -->
  <td>==</td> <!-- BOOLEAN -->
  <td style='background:black'>&nbsp;</td> <!-- FLOAT -->
</tr>
<tr>
  <td>FLOAT</td>
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_TYPE -->
  <td style='background:gray'>&nbsp;</td> <!-- ENTITY_ID -->
  <td style='background:gray'>&nbsp;</td> <!-- PLAYER -->
  <td>*, / (coordinate)</td> <!-- COORDINATE -->
  <td>*, / (coordinate)</td> <!-- DIRECTION -->
  <td>==, &lt;, arithm</td> <!-- INTEGER -->
  <td style='background:gray'>&nbsp;</td> <!-- STRING  -->
  <td style='background:gray'>&nbsp;</td> <!-- BOOLEAN -->
  <td>==, &lt;, arithm</td> <!-- FLOAT -->
</tr>
<th>
  <td>ENTITY_TYPE</td>
  <td>ENTITY_ID</td>
  <td>PLAYER</td>
  <td>COORDINATE</td>
  <td>DIRECTION</td>
  <td>INTEGER</td>
  <td>STRING</td>
  <td>BOOLEAN</td>
  <td>FLOAT</td>
</th>

</table>
 *
 * When <tt>==</tt> is specified, also <tt>!=</tt> is allowed.
 * In the same way, <tt>&lt;</tt> represents all the relational
 * operators (<tt>&gt;</tt>, <tt>&lt;=</tt> and <tt>&gt;=</tt>). 
 * 
 * This means, for example two coordinates can be compared,
 * added and subtracted, but not multiplied. On the other hand,
 * a Direction can be multiplied (becoming a coordinate). 
 * 
 * @note As an implementation note, if any of these combinations
 * want to be changed, both ExpressionBuilder and Value classes
 * must be updated.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August-September, 2009
 */
public enum ActionParameterType {

	// You should NOT change these names.
	// They are used as such in the XML domain files.
	
	// If you change the underlying Java types, then you should
	// revise gatech.mmpm.sensor.composite.Value.
	ENTITY_TYPE {
		public Class<?> getJavaType() {
			// return Class<? extends gatech.mmpm.Entity>.class;
			return Class.class;
		}
		public String getJavaTypeDeclaration() {
			return "Class<? extends gatech.mmpm.Entity>";
		}
		public Object fromString(String s) {
			Class<?> c;
			Class<? extends gatech.mmpm.Entity> result;
			try {
				c = Class.forName(s);
			}
			catch (Exception e) {
				return null;
			}
			try {
				result = c.asSubclass(gatech.mmpm.Entity.class);
			} catch (java.lang.ClassCastException e) {
				return null;
			}
			return result;
		} // fromString
		public String toString(Object v) {
			if (v.getClass() != getJavaType())
				throw new RuntimeException("Type mismatch");
			Class<?> c;
			Class<? extends gatech.mmpm.Entity> result;
			try {
				c = (Class<?>) v;
			}
			catch (Exception e) {
				return null;
			}
			try {
			result = 
				c.asSubclass(gatech.mmpm.Entity.class);
			} catch (java.lang.ClassCastException e) {
				return null;
			}
			return result.getName();
		}
	}, 
	ENTITY_ID {
		// ENTITY_ID is not an ID at all, but, in fact,
		// an entity :)
		public Class<?> getJavaType() {
			return gatech.mmpm.Entity.class;
		}
		public String getJavaTypeDeclaration() {
			return "gatech.mmpm.Entity";
		}
		public String toString(Object v) {
			Entity e = (Entity)v;
			String out = e.getClass().getName() + ":[";				
			for(String f:e.listOfFeatures()) 
				out += f + "=" + e.featureValue(f) + ",";

			String tmp = out.substring(0, out.length()-1) + "]";
//			System.out.println("A: " + out);
//			System.out.println("B: " + tmp);
			return tmp;
		}
		public Object fromString(String s) {
			String aux = s.trim();
			String[] pieces = aux.split(":");
			Entity e;
			try {
				Class<? extends Entity> type = 
					((Class<?>) ENTITY_TYPE.fromString(pieces[0])).asSubclass(Entity.class);
				Constructor<? extends Entity> c = type.getConstructor(String.class,String.class);
				e = c.newInstance(null,null);
//				e = type.newInstance();
			} catch (Exception ex){
				System.err.println("Cannot parse ENTITY_ID: " + s);
				ex.printStackTrace();
				return null;
			}
			
			if (!pieces[1].startsWith("[") ||
			    !pieces[1].endsWith("]"))
				return null;
			aux = pieces[1].substring(1, pieces[1].length() - 1);
			String[] parameters = aux.split(",");
			String[] nameValue;
			for(String param:parameters)
			{
				nameValue = param.split("=");
				if (nameValue.length>1) e.setFeatureValue(nameValue[0], nameValue[1]);
			}
			return e;
		} // fromString
	},
	PLAYER {
		public Class<?> getJavaType() {
			return String.class;
		}
		public String getJavaTypeDeclaration() {
			return "String";
		}
		public Object fromString(String s) {
			return s;
		}
	},
	COORDINATE {
		public Class<?> getJavaType() {
			return float[].class;
		}
		public String getJavaTypeDeclaration() {
			return "float[]";
		}
		public String toString(Object v) {
			if (v.getClass() != getJavaType())
				throw new RuntimeException("Type mismatch");
			float[] coord = (float[]) v;
			return "{" + coord[0] + ", " +
			             coord[1] + ", " +
			             coord[2] + "}";
		}
		public Object fromString(String s) {
			String aux = s.trim();
			if (!aux.startsWith("{") ||
			    !aux.endsWith("}"))
				return null;
			aux = aux.substring(1, s.length() - 1);
			String[] pieces;
			pieces = aux.split(",");
			if (pieces.length > 3)
				return null;
			float[] coord = new float[3];
			try {
				if (pieces.length > 0)
					coord[0] = Float.parseFloat(pieces[0]);
				if (pieces.length > 1)
					coord[1] = Float.parseFloat(pieces[1]);
				if (pieces.length > 2)
					coord[2] = Float.parseFloat(pieces[2]);
			}
			catch (NumberFormatException e) {
				return null;
			}
			return coord;
		}
	},
	DIRECTION {
		// TODO: What about a enum for this?
		public Class<?> getJavaType() {
			return Integer.class;
		}
		public String getJavaTypeDeclaration() {
			return "Integer";
		}
		public Object fromString(String s) {
			try {
				return new Integer(s);
			}
			catch (NumberFormatException e) {
				return null;
			}
		}
	},
	INTEGER {
		public Class<?> getJavaType() {
			return Integer.class;
		}
		public String getJavaTypeDeclaration() {
			return "Integer";
		}
		public Object fromString(String s) {
			try {
				return new Integer(s);
			}
			catch (NumberFormatException e) {
				return null;
			}
		}
	},
	STRING {
		public Class<?> getJavaType() {
			return String.class;
		}
		public String getJavaTypeDeclaration() {
			return "String";
		}
		public Object fromString(String s) {
			return s;
		}
	},
	BOOLEAN {
		public Class<?> getJavaType() {
			return Float.class;
		}
		public String getJavaTypeDeclaration() {
			return "Float";
		}
		public Object fromString(String s) {
			try {
				return new Float(s);
			}
			catch (NumberFormatException e) {
				return null;
			}
		}
	},
	FLOAT {
		public Class<?> getJavaType() {
			return Float.class;
		}
		public String getJavaTypeDeclaration() {
			return "Float";
		}
		public Object fromString(String s) {
			try {
				return new Float(s);
			}
			catch (NumberFormatException e) {
				return null;
			}
		}
	};

	/**
	 * It returns the real Java type for this MMPM type.
	 * For example, this is the type of the Object returned
	 * by gatech.mmpm.sensor.Sensor that return this
	 * MMPM type.
	 * 
	 * @return Java type which the MMPM type is map on.
	 */
	public abstract Class<?> getJavaType();
	
	/**
	 * It returns a String with the declaration of
	 * the real Java type for this MMPM type. For
	 * example, it returns <tt>"String"</tt> for
	 * PLAYER.
	 * 
	 * @return Declaration of the associated Java
	 * type for the MMPM type.
	 */
	public abstract String getJavaTypeDeclaration();

	/**
	 * It returns a String with the representation
	 * of a value.
	 * 
	 * @param v Object to be serialized. It must be
	 * an object of the class returned by
	 * getJavaType().
	 * @return String representing the value.
	 * 
	 * @note This method is provided to let complex
	 * MMPM types to serialize in a different way
	 * to the toString() method of the real Java
	 * type (as in ActionParameterType.COORDINATE).
	 */
	public String toString(Object v) {
		if (v.getClass() != getJavaType())
			throw new RuntimeException("Type mismatch");
		return v.toString();
	}

	/**
	 * Parses a string with the value of this MMPM type,
	 * and returns a Java object of the getJavaType()
	 * class with that value.
	 * 
	 * @param s String to be deserialized.
	 *  
	 * @return An object with the value, or null if the
	 * string does not represent a valid object.
	 * 
	 * @note ENTITY_ID becomes an exception for this
	 * method. It cannot return the entity associated
	 * with the string, because it would need the
	 * current world state. Instead of that, it throws
	 * an exception.
	 */
	public abstract Object fromString(String s);

} // enum ActionParameterType
