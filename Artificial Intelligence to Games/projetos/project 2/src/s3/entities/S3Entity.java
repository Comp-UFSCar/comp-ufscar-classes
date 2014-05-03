package s3.entities;
//import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;


public abstract class S3Entity 
{

	public int entityID;
	public String owner;
	
	public S3Entity(int iEntityID, String iOwner)
	{
		entityID = iEntityID;
		owner = iOwner;
	}
	
	public S3Entity()
	{
	}
	


	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getEntityID() {
		return entityID;
	}

	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}

	public S3Entity( S3Entity incoming)
	{
		this.entityID = incoming.entityID;
		this.owner = incoming.owner;
	}
	
	public abstract Object clone();
	
	public boolean greater(S3Entity incoming)
	{
		boolean returnFlag = true;
		//System.out.println(this.getClass());
		//System.out.println(incoming.getClass());
		try
		{
		if ( this.getClass().equals(incoming.getClass()) )
			{
				//System.out.println("Classes are equal: Can Compare");
				Field[] fx = this.getClass().getFields();
				
				for ( Field a : fx)
				{
					String fieldType = a.getType().toString(); 
					//System.out.println(" ---->" + fieldType);
					
					if ( fieldType.equals("class java.lang.String") )
						//do nothing u MORON!!!
						continue;
					
					if ( fieldType.equals("boolean") )
						//U tell me how to make true > false, and I'l give u the Nobel prize for PEACE!
						continue;
					
					if ( fieldType.equals("char") )
					{
						if ( a.getChar(this) <= a.getChar(incoming))
							returnFlag = returnFlag && false;
						continue;
					}
					
					//System.out.println("Is " + a.getDouble(this) + " > " + a.getDouble(incoming));
					
					if ( a.getDouble(this) <= a.getDouble(incoming) )
					{
						//System.out.println("lesser than satisfied...");
						returnFlag = returnFlag && false;
						//still wanna run this thru, as after one touch here, you have to break out
						//as it makes no sense in continuing on
						//break;
					}
					
					
				}
				
					
			}
		
		return returnFlag;
		}
		catch ( Exception e )
		{
			System.out.println("Now you're screwed! " + e);
		}
		return true;
	}

	public boolean lesser(S3Entity incoming)
	{
		boolean returnFlag = true;
		//System.out.println(this.getClass());
		//System.out.println(incoming.getClass());
		try
		{
		if ( this.getClass().equals(incoming.getClass()) )
			{
				//System.out.println("Classes are equal: Can Compare");
				Field[] fx = this.getClass().getFields();
				
				for ( Field a : fx)
				{
					String fieldType = a.getType().toString(); 
					//System.out.println(" ---->" + fieldType);
					
					if ( fieldType.equals("class java.lang.String") )
						//do nothing u MORON!!!
						continue;
					
					if ( fieldType.equals("boolean") )
						//U tell me how to make true > false, and I'l give u the Nobel prize for PEACE!
						continue;
					
					if ( fieldType.equals("char") )
					{
						if ( a.getChar(this) >= a.getChar(incoming))
							returnFlag = returnFlag && false;
						continue;
					}
					
					//System.out.println("Is " + a.getDouble(this) + " > " + a.getDouble(incoming));
					
					if ( a.getDouble(this) >= a.getDouble(incoming) )
					{
						//System.out.println("lesser than satisfied...");
						returnFlag = returnFlag && false;
						//still wanna run this thru, as after one touch here, you have to break out
						//as it makes no sense in continuing on
						//break;
					}
					
					
				}
				
					
			}
		
		return returnFlag;
		}
		catch ( Exception e )
		{
			System.out.println("Now you're screwed! " + e);
		}
		return true;
	}

	public boolean equals(S3Entity incoming)
	{
		boolean returnFlag = true;
		//System.out.println(this.getClass());
		//System.out.println(incoming.getClass());
		try
		{
		if ( this.getClass().equals(incoming.getClass()) )
			{
				//System.out.println("Classes are equal: Can Compare");
				Field[] fx = this.getClass().getFields();
				
				for ( Field a : fx)
				{
					String fieldType = a.getType().toString(); 
					//System.out.println(" ---->" + fieldType);
					
					if ( fieldType.equals("class java.lang.String") )
					{
						if (!a.get(this).equals(a.get(incoming)))
							returnFlag = returnFlag && false;
						
						continue;
					}
					
					if ( fieldType.equals("boolean") )
						//U tell me how to make true > false, and I'l give u the Nobel prize for PEACE!
						continue;
					
					if ( fieldType.equals("char") )
					{
						if ( a.getChar(this) != a.getChar(incoming))
							returnFlag = returnFlag && false;
						continue;
					}
					
					//System.out.println("Is " + a.getDouble(this) + " > " + a.getDouble(incoming));
					
					if ( a.getDouble(this) != a.getDouble(incoming) )
					{
						//System.out.println("lesser than satisfied...");
						returnFlag = returnFlag && false;
						//still wanna run this thru, as after one touch here, you have to break out
						//as it makes no sense in continuing on
						//break;
					}
					
					
				}
				
					
			}
		
		return returnFlag;
		}
		catch ( Exception e )
		{
			System.out.println("Now you're screwed! " + e);
		}
		return true;
	}
	
	static private HashMap<String,List<String>> m_listOfFeaturesHash = new HashMap<String,List<String>>();
	
	@SuppressWarnings("unchecked")
	public List<String> listOfFeatures() {
		Class c = getClass();
		String c_name = c.getName();
		List<String> features;
		
		features = m_listOfFeaturesHash.get(c_name);
		
		if (features==null) {
			features = new LinkedList<String>();
			do {
				for(Method m:c.getDeclaredMethods()) {
					if (m.getName().startsWith("get") && m.getParameterTypes().length==0 &&
						!m.getName().equals("getAllowedUnits") &&
						!m.getName().equals("getActionList")) {
						features.add(m.getName().substring(3));
						
//						System.out.println("found feature '" + m.getName().substring(3) + "' for " + c_name);
					}
				}
				c = c.getSuperclass();
			}while(c!=null && !c.getSimpleName().equals("Object"));
			m_listOfFeaturesHash.put(c_name, features);			
		}		
		return features;		
	}
	
	public Object featureValue(String feature) {
		
		if (feature.equals("type")) return getClass().getSimpleName();
		if (feature.equals("id")) return entityID;
		
		Method m;
		try {
			feature = feature.substring(0, 1).toUpperCase() + feature.substring(1);
			m = getClass().getMethod("get"+feature, (Class[])null);
			if (m!=null) return m.invoke(this, (Object[])null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public void setfeatureValue(String feature,String value) {
		Method m;
		try {
			m = getClass().getMethod("set"+feature, String.class);
			m.invoke(this, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean equivalents(S3Entity e) {
		if (!getClass().equals(e.getClass())) return false;
		for(String f:listOfFeatures()) {
			
			// We require them to be the same in all features except the ID:
			if (!f.equals("entityID")) {
				Object v = featureValue(f);
				if (v==null) {
					if (e.featureValue(f)!=null) return false;
				} else {
					if (!v.equals(e.featureValue(f))) return false;
				}
			}
		}
		return true;
	}
	
	public String toString() {
		String out = "Entity(" + entityID +"): " + getClass().getSimpleName() + " [ ";				
		for(String f:listOfFeatures()) 
			out += "(" + f + " = " + featureValue(f) + ") ";
			
		return out + "]";
	}
	
	public void cycle(int m_cycle, S3 m_game, List<S3Action> failedActions) {
	}
	
	public S3Entity newEntity(String type) {
		S3Entity ent = null;
		
		try {
			Class c = Class.forName("s3.entities." + type);
			if (c==null) return null;
			ent = (S3Entity) c.newInstance();
		} catch(Exception e) {
			
		}
		return ent;
	}

	public abstract gatech.mmpm.Entity toD2Entity();

}
