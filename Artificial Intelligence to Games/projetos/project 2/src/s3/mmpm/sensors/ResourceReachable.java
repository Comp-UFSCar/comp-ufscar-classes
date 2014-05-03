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


package s3.mmpm.sensors;

import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.ActionParameterType;
import gatech.mmpm.util.Pair;




/**
 * Class that represents a particular sensor type
 * of the game. It contains machine generate code.
 * Go to gatech.mmpm.Sensor for more information.
 */
public class ResourceReachable extends Sensor {

	public ResourceReachable() {
	} // Constructor

	//---------------------------------------------------------------

	public ResourceReachable(ResourceReachable rhs) {

		super(rhs);

	} // Copy constructor 
	
	//---------------------------------------------------------------

	public Object clone() {

		ResourceReachable e = new ResourceReachable(this);
		return e;

	} // clone

	//---------------------------------------------------------------

	/**
	 * Return the type of the sensor.
	 * 
	 * Keep in mind that this is <em>not</em> the real Java type,
	 * but the MMPM type. See the 
	 * gatech.mmpm.ActionParameterType.getJavaType() method
	 * for more information.
	 * 
	 * @return Type of the sensor. 
	 */
	public ActionParameterType getType() {
	
		return ActionParameterType.BOOLEAN;

	} // getType

	//---------------------------------------------------------------
	
	public Object evaluate(int cycle, GameState gs, String player, Context parameters) {

		gatech.mmpm.PhysicalEntity peasant = (gatech.mmpm.PhysicalEntity)getEntityParam(parameters,"entity");
	      float validCoordinates[] = (float [])gs.getMetaData("ResourceReachableCondition" + peasant.getentityID());
  		
	      if (validCoordinates==null) {
		      // Create the metadata

		      gatech.mmpm.TwoDMap map = (gatech.mmpm.TwoDMap)gs.getMap();

		      validCoordinates = new float[map.size()];
		      for(int i = 0;i<map.size();i++) 
           validCoordinates[i]=0.0f;
  			
		      gs.addMetaData("ResourceReachableCondition" + peasant.getentityID(),validCoordinates);
  			
		      {
			      java.util.List<int[]> open = new java.util.LinkedList<int[]>();
			      java.util.List<int[]> closed = new java.util.LinkedList<int[]>();
  				
			      open.add(map.toCellCoords(peasant));
  				
			      while(!open.isEmpty()) {
				      int[] current = open.remove(0);
  					
				      gatech.mmpm.PhysicalEntity pe = gs.getEntityAt(map.toCoords(current));
  			
				      if (pe == peasant || pe == null ||
					      pe instanceof s3.mmpm.entities.WOGrass) {
					      closed.add(current);
  						
					      if (current[0]>0) {
						      int[] n = new int[]{current[0]-1,current[1],0};
					    	  boolean contained1 = false, contained2 = false;;
					    	  for(int []c:closed) if (java.util.Arrays.equals(c, n)) {contained1 = true;break;}
					    	  if (!contained1) for(int []c:open) if (java.util.Arrays.equals(c, n)) {contained2 = true;break;}
						      if (!contained1 && !contained2) open.add(n);
					      }
					      if (current[1]>0) {
						      int[] n = new int[]{current[0],current[1]-1,0};
					    	  boolean contained1 = false, contained2 = false;;
					    	  for(int []c:closed) if (java.util.Arrays.equals(c, n)) {contained1 = true;break;}
					    	  if (!contained1) for(int []c:open) if (java.util.Arrays.equals(c, n)) {contained2 = true;break;}
						      if (!contained1 && !contained2) open.add(n);
					      }
					      if (current[0]<map.getSizeInDimension(0)-1) {
						      int[] n = new int[]{current[0]+1,current[1],0};
					    	  boolean contained1 = false, contained2 = false;;
					    	  for(int []c:closed) if (java.util.Arrays.equals(c, n)) {contained1 = true;break;}
					    	  if (!contained1) for(int []c:open) if (java.util.Arrays.equals(c, n)) {contained2 = true;break;}
						      if (!contained1 && !contained2) open.add(n);
					      }
					      if (current[1]<map.getSizeInDimension(1)-1) {
						      int[] n = new int[]{current[0],current[1]+1,0};
					    	  boolean contained1 = false, contained2 = false;;
					    	  for(int []c:closed) if (java.util.Arrays.equals(c, n)) {contained1 = true;break;}
					    	  if (!contained1) for(int []c:open) if (java.util.Arrays.equals(c, n)) {contained2 = true;break;}
						      if (!contained1 && !contained2) open.add(n);
					      }
				      } 
            else if (pe instanceof s3.mmpm.entities.WOTree) 
            {
					      validCoordinates[map.toCell(current)]=1.0f;						
				      } 
            else if (pe instanceof s3.mmpm.entities.WGoldMine) 
            {
					      for(int i=0;i<3;i++) 
              {
						      for(int j=0;j<3;j++) 
                {
							      validCoordinates[map.toCell(new float[]{(pe.getx())+j,(pe.gety())+i,0})] = 1.0f;								
						      }
					      }
				      }
			      } // while(!open.isEmpty())
		      }
	      } // if (validCoordinates==null)
  		
	      return validCoordinates[gs.getMap().toCell(getCoorParam(parameters,"coor"))];
	
	} // evaluate
	
	//---------------------------------------------------------------
	
	/**
	 * Protected method called from equivalents to compare
	 * two sensors. Subclasses must override this method to
	 * decide if a sensor of the current class is equivalent
	 * to the current sensor.
	 * 
	 * @param s Sensor to compare the current one with.
	 * It will always be an instance of the current class.
	 * @return True if both sensors are equivalents.
	 * 
	 * @note This method should never be externally called.
	 * Use equivalents() instead.
	 */
	protected boolean internalEquivalents(Sensor s) {

		// Auto-generated sensors of the same class are
		// always equivalent in between.
		return true;

	} // internalEquivalents

	//---------------------------------------------------------------
	
	/**
	 * Public method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
	public java.util.List<Pair<String,ActionParameterType>> getNeededParameters() {

		return _listOfNeededParameters;
	
	} // getNeededParameters

	//---------------------------------------------------------------
	
	/**
	 * Public static method that provides the parameters that 
	 * this sensor uses to be evaluated. This method provides
	 * all the parameters that can be used in the evaluation, 
	 * nevertheless some sensor can be evaluated with only 
	 * some of them.
	 * 
	 * @return The list of needed parameters this sensor needs
	 * to be evaluated.
	 */
	public static java.util.List<Pair<String,ActionParameterType>> getStaticNeededParameters() {

		return _listOfNeededParameters;
	
	} // getStaticNeededParameters
	
	//---------------------------------------------------------------
	//                       Static fields
	//---------------------------------------------------------------
	
	static java.util.List<Pair<String,ActionParameterType>> _listOfNeededParameters;
	
	
	
	//---------------------------------------------------------------
	//                       Static initializers
	//---------------------------------------------------------------

	static {

		// Add parameters to _listOfNeededParameters.
		_listOfNeededParameters = new java.util.LinkedList<Pair<String,ActionParameterType>>(gatech.mmpm.sensor.Sensor.getStaticNeededParameters());
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("entity", ActionParameterType.ENTITY_ID));
		_listOfNeededParameters.add(new Pair<String,ActionParameterType>("coor", ActionParameterType.COORDINATE));
		

	} // static initializer

} // class ResourceReachable

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

