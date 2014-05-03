/*********************************************************************************
 Organization 					: 				Georgia Institute of Technology
 Cognitive Computing Lab (CCL)
 Authors							: 				Jai Rad
 Santi Ontanon
 ****************************************************************************/
package s3.entities;

import java.awt.Graphics2D;
import java.util.List;

import s3.ai.AStar;
import s3.base.PlayerInput;
import s3.base.S3;
import s3.base.S3Action;
import s3.util.Pair;

public abstract class WTroop extends WUnit {
	protected AStar pathPlanner;

	protected List<Pair<Double, Double>> path;
	
	protected int previous_x = 0, previous_y = 0, previous_z = 0;
	protected int move_timmer;

	protected int pathIndex = -1;

	public WTroop() {
		setConstants();
	}

	private void setConstants() {
		width = 1;
		length = 1;
		range = 1;

		// Add actions to the list
		actionList.add(S3Action.ACTION_MOVE);
		actionList.add(S3Action.ACTION_ATTACK);
		actionList.add(S3Action.ACTION_STAND_GROUND);
	}

	public WTroop(WTroop incoming) {
		super(incoming);
		setConstants();
	}

	public static boolean isActive() {
		return true;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int incoming) {
		this.speed = incoming;
	}

	public void setSpeed(String incoming) {
		this.speed = Integer.parseInt(incoming);
	}
	
	public void setX(int x) {
		this.x = x;
		previous_x = x;
	}

	public void setY(int y) {
		this.y = y;
		previous_y = y;
	}


	/**
	 * checks the status of the entity to see if there are any pending actions
	 * to be performed
	 */
	public void cycle(int a_cycle, S3 a_game, List<S3Action> failedActions) {
		if (move_timmer>0) move_timmer--;
		super.cycle(a_cycle, a_game, failedActions);
		if (a_cycle % speed == 0) {
            if (status==null) {
                doIdleAction(a_game);
            } else {
                switch (status.m_action) {
                case S3Action.ACTION_MOVE:
                    moveTowardsTarget(a_game, (Integer)status.m_parameters.get(0), (Integer)status.m_parameters.get(1));
                    break;
                case S3Action.ACTION_ATTACK:
                    doAttack(a_cycle,a_game);
                    break;
                case S3Action.ACTION_STAND_GROUND:
                    doStandGround(a_game);
                    break;
                }
            }
		}
	}

	/**
	 * @param m_game
	 */
	private void doAttack(int a_cycle,S3 a_game) {
		int unitID = (Integer)status.m_parameters.get(0);
		WUnit target = a_game.getUnit(unitID);
		
		if (target == null || target.getCurrent_hitpoints() <= 0) {
			cleanup(a_game);
			status = null;
		} else {
			if (inRange(target)) {
//				System.out.println("  My target is in range, so I'm attacking for " + attack);
				target.setCurrent_hitpoints(target.getCurrent_hitpoints() - attack);
				target.setLastAttackCycle(a_cycle);
				if (range>1) {
					a_game.addBullet(getX()*CELL_SIZE + CELL_SIZE/2, getY()*CELL_SIZE + CELL_SIZE/2, 
							target.getX()*CELL_SIZE + (target.getWidth()*CELL_SIZE)/2, 
							target.getY()*CELL_SIZE + (target.getLength()*CELL_SIZE)/2);
				}
			} else {
//				System.out.println("  My target out of range");
				moveTowardsTargetToAttack(a_game, target);
			}
		}
	}

	/**
	 * @param m_game
	 */
	private void doStandGround(S3 m_game) {
		for (WUnit unit : m_game.getUnits()) {
			if (null == owner || null == unit.owner) {
				continue;
			}
			if (!unit.owner.equals(owner) && inRange(unit)) {
				unit.setCurrent_hitpoints(unit.getCurrent_hitpoints() - attack);
				return;
			}
		}
	}

	/**
	 * @param m_game
	 */
	private void doIdleAction(S3 m_game) {
		for (WUnit unit : m_game.getUnits()) {
			if (null == owner || null == unit.owner) {
				continue;
			}
			if (!unit.owner.equals(owner) && inRange(unit)) {
				performAction(new S3Action(entityID,S3Action.ACTION_ATTACK,unit.entityID));
				return;
			}
		}
	}

	/**
	 * plans and executes a path to a goal.
	 * 
	 * @param game
	 */
	protected void moveTowardsTarget(S3 game, int a_x, int a_y) {
		previous_x = x;
		previous_y = y;
		move_timmer = speed;
		if (x == a_x && y == a_y) {
			cleanup(game);
			status = null;
		} else {
			// init planner
			if (pathPlanner == null) {
				pathPlanner = new AStar(x, y, a_x, a_y, this, game);
				path = pathPlanner.computePath();
				pathIndex = 0;				
			}

			// check that a path exists
			if (null == path) {
				WPlayer player = getPlayer(game);
				if (player.getInputType() == PlayerInput.INPUT_MOUSE) {
					game.setMessage("Can't get to that location.");
				}
				cleanup(game);
				return;
			}

			if (path.size() > pathIndex) {
				x = path.get(pathIndex).m_a.intValue();
				y = path.get(pathIndex).m_b.intValue();
				pathIndex++;
			} else {
				cleanup(game);
				return;
			}

			// check for collision, replan if necessary
			if (game.anyLevelCollision(this)!=null) {
				pathPlanner = null;
				x = previous_x;
				y = previous_y;
				moveTowardsTarget(game, a_x, a_y);
			}
		}
	}

	/**
	 * cleans up unit items after finishing an action.
	 */
	protected void cleanup(S3 game) {
		pathPlanner = null;
		path = null;
		pathIndex = -1;
		target_x = -1;
		target_y = -1;
	}

	/**
	 * move towards the given unit.
	 * 
	 * @param m_game
	 * @param target
	 */
	protected void moveTowardsTargetToAttack(S3 m_game, WUnit target) {
		Pair<Integer, Integer> loc = rangedLoc(target, m_game);
//		System.out.println("  My target is in " + target.getX() + "," + target.getY() + " but I want to go to " + loc.m_a + "," + loc.m_b + " I am at " + getX() + "," + getY());
		moveTowardsTarget(m_game, loc.m_a, loc.m_b);
	}
	
	protected void moveTowardsTarget(S3 m_game, WUnit target) {
		Pair<Integer, Integer> loc = rangedLoc(target, m_game);
		moveTowardsTarget(m_game, loc.m_a, loc.m_b);
	}

	/**
	 * Decides if the target is within range of the unit's attack. Assumes units
	 * attack range is a square, not a circle.
	 * 
	 * @param target
	 *            the unit to be attacked.
	 * @return true if the unit is in range, false otherwise.
	 */
	protected boolean inRange(WUnit target) {
		if (target == null) {
			return false;
		}
		// check X
		if (x + range >= target.getX()
				&& x - range <= target.getX() + target.getWidth() - 1) {
			// check y
			if (y + range >= target.getY()
					&& y - range <= target.getY() + target.getLength() - 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Decides if the target location is within range of the unit's attack.
	 * Assumes units attack range is a square, not a circle.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return true if the unit is in range, false otherwise.
	 */
	protected boolean inRange(int xLoc, int yLoc) {
		// check X
		if (x + range >= xLoc && x - range <= xLoc) {
			// check y
			if (y + range >= yLoc && y - range <= yLoc) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Decides if the target is within range of the unit's attack. Assumes units
	 * attack range is a square, not a circle.
	 * 
	 * @param target
	 *            the unit to be attacked.
	 * @return true if the unit is in range, false otherwise.
	 */
	protected Pair<Integer, Integer> rangedLoc(WUnit target, S3 game) {
		if (target == null) {
			return new Pair<Integer, Integer>(-1, -1);
		}
		int bestx = -1;
		int besty = -1;
		double bestdistance = -1;

		for (int xLoc = target.getX() - range; xLoc < target.getX()
				+ target.getWidth() + range; xLoc++) {
			for (int yLoc = target.getY() - range; yLoc < target.getY()
					+ target.getLength() + range; yLoc++) {
				S3PhysicalEntity pe = game.getEntity(xLoc, yLoc);
				if (pe instanceof WOGrass) {
					double distance = Math.sqrt((xLoc - x) * (xLoc - x)
							+ (yLoc - y) * (yLoc - y));
					if (bestdistance == -1 || distance < bestdistance) {
						bestx = xLoc;
						besty = yLoc;
						bestdistance = distance;
					}
				}
			}

		}
		return new Pair<Integer, Integer>(bestx, besty);
	}

	/**
	 * Decides if the target is within range of the unit's attack. Assumes units
	 * attack range is a square, not a circle.
	 * 
	 * @param target
	 *            the unit to be attacked.
	 * @return true if the unit is in range, false otherwise.
	 */
	protected Pair<Integer, Integer> rangedLoc(int goalx, int goaly, S3 game) {
		int bestx = -1;
		int besty = -1;
		double bestdistance = -1;

		for (int xLoc = goalx - range; xLoc < goalx + 1 + range; xLoc++) {
			for (int yLoc = goaly - range; yLoc < goaly + 1 + range; yLoc++) {
				S3PhysicalEntity pe = game.getEntity(xLoc, yLoc);
				if (pe instanceof WOGrass) {
					double distance = Math.sqrt((xLoc - x) * (xLoc - x)
							+ (yLoc - y) * (yLoc - y));
					if (bestdistance == -1 || distance < bestdistance) {
						bestx = xLoc;
						besty = yLoc;
						bestdistance = distance;
					}
				}
			}
		}
		return new Pair<Integer, Integer>(bestx, besty);
	}
	
	public int distance(WUnit target, S3 game) {
		Pair<Integer,Integer> loc = rangedLoc(target,game);
		return AStar.pathDistance(x, y, loc.m_a,loc.m_b, this, game);
	}
	
	public int getActualX() {
		if (x==previous_x) return x*CELL_SIZE;
		float f = ((float)move_timmer)/speed;
		return (int)(x*CELL_SIZE*(1-f) + previous_x*CELL_SIZE*f);
	}

	public int getActualY() {
		if (y==previous_y) return y*CELL_SIZE;
		float f = ((float)move_timmer)/speed;
		return (int)(y*CELL_SIZE*(1-f) + previous_y*CELL_SIZE*f);
	}
	
	public void draw(Graphics2D g, int x_offset, int y_offset) {
//		if (this instanceof WPeasant) System.out.println(x + " - " + previous_x + " (" + move_timmer + ") -> " + getActualX());
		super.draw(g,x_offset + (x*CELL_SIZE - getActualX()) , y_offset + (y*CELL_SIZE - getActualY()));
	}
}