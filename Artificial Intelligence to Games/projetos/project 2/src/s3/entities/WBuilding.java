/*********************************************************************************
 Organization 					: 				Georgia Institute of Technology
 Cognitive Computing Lab (CCL)
 Authors							: 				Jai Rad
 Santi Ontanon
 ****************************************************************************/
package s3.entities;

import java.util.ArrayList;
import java.util.List;

import s3.base.PlayerInput;
import s3.base.S3;
import s3.base.S3Action;
import s3.util.Pair;

public abstract class WBuilding extends WUnit {

	/** which units this building can train */
	protected List<String> allowedUnits = new ArrayList<String>();

	public WBuilding() {
	}

	public WBuilding(WBuilding incoming) {
		super(incoming);
	}

	public static boolean isActive() {
		return true;
	}

	/**
	 * orders the unit to train the given unit
	 * 
	 * @param unit
	 *            the unit to train
	 */
	public void train(int unit, S3Action action) {
		status = action;
		progressTimerMax = progressTimer = 0;
	}
	
	protected void addAllowed(String unit) {
		if (!allowedUnits.contains(unit)) allowedUnits.add(unit);
	}

	public List<String> getAllowedUnits() {
		return allowedUnits;
	}

	/**
	 * checks the status of the entity to see if there are any pending actions
	 * to be performed
	 */
	public void cycle(int m_cycle, S3 m_game, List<S3Action> failedActions) {
		super.cycle(m_cycle, m_game, failedActions);
		if (progressTimer>0) progressTimer--;
		if (m_cycle % speed == 0) {
			if (status!=null) {
				switch (status.m_action) {
				case S3Action.ACTION_STAND_GROUND:
					doStandGround(m_game);
					break;
				case S3Action.ACTION_TRAIN:
					if (progressTimerMax == 0) {
						// check cost
						WUnit newUnit = (WUnit)newEntity((String)status.m_parameters.get(0));
						WPlayer player = getPlayer(m_game);
						if (player.getGold() < newUnit.getCost_gold()
								|| player.getWood() < newUnit.getCost_wood()) {
							// can't afford building, stop

							// Failed Action!!!
							failedActions.add(status);
							
							if (player.getInputType() == PlayerInput.INPUT_MOUSE) {
								m_game.setMessage("Can't afford that; Cost is "
										+ newUnit.getCost_gold() + " gold and "
										+ newUnit.getCost_wood() + " wood.");
							}
							status = null;
							return;
						}
						// subtract cost
						player.setGold(player.getGold() - newUnit.getCost_gold());
						player.setWood(player.getWood() - newUnit.getCost_wood());

						progressTimerMax = progressTimer = newUnit.getCost_gold();
					} else {
						if (progressTimer<=0) {
							WUnit newUnit = (WUnit)newEntity((String)status.m_parameters.get(0));
							newUnit.setCreator(this);
							newUnit.setCreatedCycle(m_cycle);
							findLocation(m_game, newUnit);

							WPlayer player = getPlayer(m_game);
							
							// set unit attributes
							newUnit.setCurrent_hitpoints(newUnit.getMax_hitpoints());
							newUnit.setOwner(player.owner);
							newUnit.setEntityID(m_game.nextID());
							newUnit.setColor(player.getColor());
							m_game.addUnit(newUnit);

							status = null;
							progressTimerMax = progressTimer = 0;
							break;
						}
					}	
				}
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
				unit.setCurrent_hitpoints(unit
						.getCurrent_hitpoints()
						- attack);
				m_game.addBullet((getX()+1)*CELL_SIZE, getY()*CELL_SIZE, 
								unit.getX()*CELL_SIZE + (unit.getWidth()*CELL_SIZE)/2, 
								unit.getY()*CELL_SIZE + (unit.getLength()*CELL_SIZE)/2);
//				target_x = unit.getX() + unit.getWidth()/2;
//				target_y = unit.getY() + unit.getLength()/2;

				return;
			}
		}
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
	 * finds a free spot for the unit
	 * 
	 * @param m_game
	 * @param newUnit
	 */
	private void findLocation(S3 m_game, WUnit newUnit) {
		Pair<Integer, Integer> loc = m_game.findFreeSpace(getX()
				+ (getWidth() / 2), getY() + (getHeight() / 2), 1);
		// location is next to the building
		newUnit.setX(loc.m_a);
		newUnit.setY(loc.m_b);

	}

}