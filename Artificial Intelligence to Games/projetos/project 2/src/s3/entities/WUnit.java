/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;


public abstract class WUnit extends S3PhysicalEntity implements Comparable<WUnit>{	
	protected int range;
	protected int attack;

	protected int max_hitpoints;
	protected int current_hitpoints;
	protected S3Action status = null;
	protected int cost_gold;
	protected int cost_wood;
	protected int speed = 25;

	protected int target_x = -1;
	protected int target_y = -1;

	protected int cycle_created = 0;
	protected int cycle_last_attacked = -1;
	protected WUnit creator = null;
	
	// progress bars:
	protected int progressTimer = 0, progressTimerMax = 0;

	public List <Integer> actionList = new ArrayList<Integer>();

	public List<Integer> getActionList() {
		return actionList;
	}
	public WUnit()
	{
		status = null;
	}
	
	public WUnit( WUnit incoming )
	{
		super(incoming);
		this.max_hitpoints = incoming.max_hitpoints;
		this.current_hitpoints = incoming.current_hitpoints;
		if (incoming.status==null) this.status = null;
							  else this.status = new S3Action(incoming.status);
		this.cost_gold = incoming.cost_gold;
		this.cost_wood = incoming.cost_wood;
	}

	public static boolean isActive() 
	{
		return true;
	}
	
	public void performAction(S3Action a) {
        if (status!=null && a!=null && status.equals(a)) return;
		status = a;
		progressTimer = progressTimerMax = 0;
	}

	public int getMax_hitpoints() {
		return max_hitpoints;
	}
	public int getCurrent_hitpoints() {
		return current_hitpoints;
	}
	public void setCurrent_hitpoints(int current_hitpoints) {
		if (this.current_hitpoints > current_hitpoints)
			hit_timer =10;
		this.current_hitpoints = current_hitpoints;
	}
	public S3Action getStatus() {
		return status;
	}
	
	public int getCost_gold() {
		return cost_gold;
	}
	public int getCost_wood() {
		return cost_wood;
	}
	/**
	 * gets the current player from the game state.
	 * 
	 * @param game
	 *            the current game.
	 * @return the player that owns this unit.
	 */
	protected WPlayer getPlayer(S3 game) {
		WPlayer player = null;
		for (S3Entity entity : game.getPlayers()) {
			if (entity instanceof WPlayer) {
				if (((WPlayer) entity).owner.equals(owner)) {
					player = (WPlayer) entity;
					break;
				}
			}
		}
		return player;
	}

	public void draw(Graphics2D g, int x_offset, int y_offset) {
		super.draw(g, x_offset, y_offset);
		//check to see if the status has ATTACK or not
		if (target_x >=0 && target_y >=0) {
			g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND, 1f, new float[] { 5f }, 1.0f));

			g.setColor(Color.RED);
			g.drawLine((x+getWidth()/2) * CELL_SIZE - x_offset, 
					(y+getLength()/2)* CELL_SIZE - y_offset, 
					target_x* CELL_SIZE - x_offset, 
					target_y* CELL_SIZE - y_offset);
			g.setStroke(new BasicStroke());
		}
		//draw progress bar:
		if (progressTimerMax!=0) drawProgressBar(g,progressTimer,progressTimerMax, x_offset, y_offset);
	}
	
	public void drawProgressBar(Graphics2D g,int current,int max, int x_offset,int y_offset) {
		float w = getLength()*CELL_SIZE;
		float f = (float)current/(float)max;
		g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 1f, null, 1.0f));
		g.setColor(Color.YELLOW);
		g.drawLine(x * CELL_SIZE - x_offset, 
				  (y+getLength())* CELL_SIZE - y_offset, 
				  (int)(x * CELL_SIZE - x_offset + w), 
				  (y+getLength())* CELL_SIZE - y_offset);
		g.setColor(Color.GREEN);
		g.drawLine(x * CELL_SIZE - x_offset, 
				  (y+getLength())* CELL_SIZE - y_offset, 
				  (int)(x * CELL_SIZE - x_offset + (1-f)*w), 
				  (y+getLength())* CELL_SIZE - y_offset); 
		g.setStroke(new BasicStroke());
	}
	
	public int compareTo(WUnit u) {
		return entityID - u.entityID;
	}


	public void setCreator(WUnit c) {
		creator = c;
	}

	public void setCreatedCycle(int cycle) {
		cycle_created = cycle;
	}

	public void setLastAttackCycle(int cycle) {
		cycle_last_attacked = cycle;
	}
}