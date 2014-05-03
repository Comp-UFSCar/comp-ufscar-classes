/* Copyright 2010 Santiago Ontanon and Ashwin Ram */

package gatech.mmpm.learningengine;

import java.io.OutputStream;
import java.util.List;


/**
 * Every learner engine must provide a class implementing this
 * interface to create MEs using game traces.
 * 
 * @see IMEExecutor
 * 
 * @author Pedro Pablo Gomez-Martin and David Llanso
 * @date August, 2009
 */
public interface IMETrainer {

	/**
	 * Main function of the interface. ME trainers must create
	 * a new ME using the traces provided.
	 * 
	 * @param traces Traces to be used in the ME generation. 
	 * 
	 * @param playerNames Each player name is linked in order 
	 * with each trace. Each one specifies the player, of the 
	 * trace, used to learn from his actions.
	 * 
	 * @param me Output stream with the new ME. Make Me Play Me
	 * will consider the ME as a black box. The ME format must
	 * be synchronized with the format expected in the
	 * IMEExecutor interface for the same learning engine.
	 *  
	 * @param domain Java class that contains the information
	 * of the game domain.
	 * 
	 * @param config Extra configuration parameters. There are
	 * extracted from the command line arguments. Make Me Play
	 * Me don't process them, they are specific from the
	 * concrete learner engine.
	 * 
	 * @return True if the ME was generated, and false in
	 * other case. Any partial information sent to the
	 * output stream will be discarded.
	 */
	public AbstractMEExecutor train(List<gatech.mmpm.Trace> traces,
	                     List<String> playerNames,
	                     gatech.mmpm.IDomain domain,
	                     java.util.Properties config,
	                     OutputStream serializedMEStream) 
	                     throws Exception ;

} // IMETrainer
