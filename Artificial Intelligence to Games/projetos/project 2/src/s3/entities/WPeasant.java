/**
 * *******************************************************************************
 * Organization : Georgia Institute of Technology Cognitive Computing Lab (CCL)
 * Authors	: Jai Rad Santi Ontanon
 ***************************************************************************
 */
package s3.entities;

import gatech.mmpm.Entity;
import java.util.ArrayList;
import java.util.List;

import s3.base.PlayerInput;
import s3.base.S3;
import s3.base.S3Action;
import s3.util.Pair;

public class WPeasant extends WTroop {

    /**
     * which buildings can be built
     */
    private List<String> allowedUnits = new ArrayList<String>();
    private int carriedGold = 0;
    private int carriedWood = 0;

    public WPeasant() {
        setConstants();
    }

    public WPeasant(WPeasant incoming) {
        super(incoming);
        setConstants();
    }

    private void setConstants() {
        attack = 3;
        max_hitpoints = 30;
        width = 1;
        length = 1;
        cost_gold = 400;
        cost_wood = 0;
        this.spriteName = "peasant";

        // Add actions to the list
        actionList.add(S3Action.ACTION_BUILD);
        actionList.add(S3Action.ACTION_HARVEST);
        actionList.add(S3Action.ACTION_REPAIR);

        allowedUnits.add(WTownhall.class.getSimpleName());
        allowedUnits.add(WWall.class.getSimpleName());
    }

    public Object clone() {
        WUnit e = new WPeasant(this);
        return e;
    }

    public static boolean isActive() {
        return true;
    }

    protected void cleanup(S3 game) {
        super.cleanup(game);
        clearConstruction(game);
    }

    /**
     * checks the status of the entity to see if there are any pending actions
     * to be performed
     */
    public void cycle(int a_cycle, S3 a_game, List<S3Action> failedActions) {
        super.cycle(a_cycle, a_game, failedActions);

        if (progressTimer > 0) {
            progressTimer--;
        }

        if (a_cycle % speed == 0) {

            setAllowed(a_game);

            if (status != null) {
                switch (status.m_action) {
                    case S3Action.ACTION_REPAIR:
                        doRepair(a_game);
                        break;
                    case S3Action.ACTION_HARVEST:
                        doHarvest(a_game);
                        break;
                    case S3Action.ACTION_BUILD:
                        if (!actionList.contains(S3Action.ACTION_CANCEL)) {
                            actionList.add(S3Action.ACTION_CANCEL);
                        }
                        doBuild(a_cycle, a_game, failedActions);
                        break;
                    case S3Action.ACTION_CANCEL:
                        doCancel(a_game);
                        break;
                    default:
                        actionList.remove((Integer) S3Action.ACTION_CANCEL);
                }
            }
        }
    }

    private void doCancel(S3 game) {
        String type = (String) status.m_parameters.get(0);
        int xLoc = (Integer) status.m_parameters.get(1);
        int yLoc = (Integer) status.m_parameters.get(2);
        WBuilding building = (WBuilding) newEntity(type);
        WPlayer player = getPlayer(game);

        // remove construction tiles:
        for (int i = 0; i < building.getWidth(); i++) {
            for (int j = 0; j < building.getLength(); j++) {
                game.clearMapEntity(xLoc + i, yLoc + j);
            }
        }

        player.setGold(player.getGold() + building.getCost_gold());
        player.setWood(player.getWood() + building.getCost_wood());
        status = null;

    }

    private void addAllowed(String unit) {
        if (!allowedUnits.contains(unit)) {
            allowedUnits.add(unit);
        }
    }

    /**
     * changes which buildings can be built
     *
     * @param m_game
     */
    private void setAllowed(S3 m_game) {
        WPlayer player = m_game.getPlayer(owner);
        if (null == m_game.getUnitType(player, WTownhall.class)) {
            allowedUnits.remove(WBarracks.class.getSimpleName());
            allowedUnits.remove(WLumberMill.class.getSimpleName());
            allowedUnits.remove(WBlacksmith.class.getSimpleName());
        } else {
            addAllowed(WBarracks.class.getSimpleName());
            addAllowed(WLumberMill.class.getSimpleName());
            addAllowed(WBlacksmith.class.getSimpleName());
        }

        if (null == m_game.getUnitType(player, WLumberMill.class)) {
            allowedUnits.remove(WTower.class.getSimpleName());
        } else {
            addAllowed(WTower.class.getSimpleName());
        }

        if (null == m_game.getUnitType(player, WBarracks.class)
                || null == m_game.getUnitType(player, WLumberMill.class)
                || null == m_game.getUnitType(player, WBlacksmith.class)) {
            allowedUnits.remove(WFortress.class.getSimpleName());
        } else {
            addAllowed(WFortress.class.getSimpleName());
        }

        if (null == m_game.getUnitType(player, WFortress.class)) {
            allowedUnits.remove(WStable.class.getSimpleName());
        } else {
            addAllowed(WStable.class.getSimpleName());
        }

    }

    public void clearConstruction(S3 game) {
        if (status != null && status.m_action == S3Action.ACTION_BUILD && progressTimerMax != 0) {
            String type = (String) status.m_parameters.get(0);
            int xLoc = (Integer) status.m_parameters.get(1);
            int yLoc = (Integer) status.m_parameters.get(2);
            WBuilding building = (WBuilding) newEntity(type);
            // remove construction tiles:
            for (int i = 0; i < building.getWidth(); i++) {
                for (int j = 0; j < building.getLength(); j++) {
                    game.clearMapEntity(xLoc + i, yLoc + j);
                }
            }
        }
    }

    private void doBuild(int cycle, S3 game, List<S3Action> failedActions) {
        String type = (String) status.m_parameters.get(0);
        int xLoc = (Integer) status.m_parameters.get(1);
        int yLoc = (Integer) status.m_parameters.get(2);

        if (inRange(xLoc, yLoc)) {

//            System.out.println("doBuild: inRange, progressTimer: " + progressTimer);

            if (progressTimerMax == 0) {
                WBuilding building = (WBuilding) newEntity(type);
                WPlayer player = getPlayer(game);
                building.setX(xLoc);
                building.setY(yLoc);

                // Temporary remove the peasant:
                game.removeUnit(this);

                S3Entity e = game.anyLevelCollision(building);
                if (e != null) {
                    game.addUnit(this); // Add the peasant again

                    // can't build here d00dz
                    if (player.getInputType() == PlayerInput.INPUT_MOUSE) {
                        game.setMessage("Building location needs to be cleared.");
                    }

                    System.out.println("Can't build '" + type + "' at " + xLoc + "," + yLoc + " because of collision with " + e);

                    if (e instanceof WBuilding) {
                        // cancel the action
                        status = null;
                        progressTimer = progressTimerMax = 0;
                    }
                    return;
                }

                if (player.getGold() < building.getCost_gold()
                        || player.getWood() < building.getCost_wood()) {
                    game.addUnit(this); // Add the peasant again

                    // can't afford building, stop
                    if (player.getInputType() == PlayerInput.INPUT_MOUSE) {
                        game.setMessage("Can't afford that building; Cost is "
                                + building.getCost_gold() + " gold and " + building.getCost_wood()
                                + " wood.");
                    }
                } else {

                    player.setGold(player.getGold() - building.getCost_gold());
                    player.setWood(player.getWood() - building.getCost_wood());

                    // Add construction terrain:
                    for (int i = 0; i < building.getWidth(); i++) {
                        for (int j = 0; j < building.getLength(); j++) {
                            game.setMapEntity(xLoc + i, yLoc + j, new WOConstruction());
                        }
                    }

                    game.addUnit(this); // Add the peasant again
                    progressTimer = progressTimerMax = building.cost_gold;
                }
            } else {
                if (progressTimer <= 0) {
                    WBuilding building = (WBuilding) newEntity(type);
                    WPlayer player = getPlayer(game);

                    building.setCreator(this);
                    building.setCreatedCycle(cycle);

                    building.setX(xLoc);
                    building.setY(yLoc);

                    building.setCurrent_hitpoints(building.getMax_hitpoints());
                    building.setOwner(player.owner);
                    building.setEntityID(game.nextID());
                    building.setColor(player.getColor());

                    // System.out.println(building);
                    game.addUnit(building);
                    status = null;
                    progressTimer = progressTimerMax = 0;

                    // remove construction tiles:
                    for (int i = 0; i < building.getWidth(); i++) {
                        for (int j = 0; j < building.getLength(); j++) {
                            game.clearMapEntity(xLoc + i, yLoc + j);
                        }
                    }

                    // Find a free square around the new building:
                    Pair<Integer, Integer> loc = game.findFreeSpace(getX(), getY(), 1);
                    setX(loc.m_a);
                    setY(loc.m_b);
                }
            }
        } else {
            moveTowardsTarget(game, xLoc, yLoc);
        }
    }

    /**
     * @param game
     */
    private void doRepair(S3 game) {
        int unitID = (Integer) status.m_parameters.get(0);
        WPlayer player = getPlayer(game);
        WUnit target = game.getUnit(unitID);
        if (target == null || target.getCurrent_hitpoints() <= 0
                || target.getCurrent_hitpoints() == target.getMax_hitpoints()) {
            if (player.getInputType() == PlayerInput.INPUT_MOUSE) {
                game.setMessage("Repairs finished.");
            }

            status = null;
        } else {
            if (inRange(target)) {
                target.setCurrent_hitpoints(target.getCurrent_hitpoints() + attack);
            } else {
                moveTowardsTarget(game, target);
            }
        }
    }

    /**
     * executes the harvest action.
     *
     * @param game
     */
    private void doHarvest(S3 game) {

        if (status.m_parameters.size() == 1) {
            int unitID = (Integer) status.m_parameters.get(0);
            WUnit target = game.getUnit(unitID);

            if (target != null && target instanceof WGoldMine) {
                mine((WGoldMine) target, game);
            }
        } else {
            chop((Integer) status.m_parameters.get(0), (Integer) status.m_parameters.get(1), game);
        }

    }

    /**
     * harvest wood from location x, y
     *
     * @param xTarget
     * @param yTarget
     * @param game
     */
    private void chop(int xTarget, int yTarget, S3 game) {
        if (carriedWood == 100) {
            WPlayer player = getPlayer(game);
            WTownhall townhall = (WTownhall) game.getUnitType(player, WTownhall.class);
            if (inRange(townhall)) {
                player.setWood(player.getWood() + 100);
                carriedWood = 0;
                cleanup(game);
            } else {
                // move towards townhall
                moveTowardsTarget(game, townhall);
            }
        } else {
            if (inRange(xTarget, yTarget)) {
                S3PhysicalEntity wood = game.mapEntityAt(xTarget, yTarget);
                if (!(wood instanceof WOTree)) {
                    WPlayer player = getPlayer(game);
                    // already harvested

                    WTownhall townhall = (WTownhall) game.getUnitType(player, WTownhall.class);
                    S3PhysicalEntity nextWood = game.locateNearestMapEntity(xTarget, yTarget,
                            WOTree.class, townhall);

                    if (nextWood != null) {
                        status.m_parameters.set(0, nextWood.getX());
                        status.m_parameters.set(1, nextWood.getY());
                        return;
                    }
                    if (player.getInputType() == PlayerInput.INPUT_MOUSE) {
                        game.setMessage("There is no wood at that location.");
                    }
                    status = null;
                    return;
                }
                if (progressTimerMax == 0) {
                    progressTimerMax = progressTimer = 200;
                } else {
                    if (progressTimer <= 0) {
                        game.clearMapEntity(xTarget, yTarget);
                        carriedWood = 100;
                        cleanup(game);
                        progressTimerMax = progressTimer = 0;
                    }
                }
            } else {
                // move towards wood
                Pair<Integer, Integer> loc = rangedLoc(xTarget, yTarget, game);
                moveTowardsTarget(game, loc.m_a, loc.m_b);
            }
        }
    }

    /**
     * harvest gold from the target mine
     *
     * @param target
     * @param game
     */
    private void mine(WGoldMine target, S3 game) {
        if (target == null || target.getRemaining_gold() <= 0) {
            status = null;
        } else {
            if (carriedGold == 100) {
                WPlayer player = getPlayer(game);
                WTownhall townhall = (WTownhall) game.getUnitType(player, WTownhall.class);
                if (inRange(townhall)) {
                    player.setGold(player.getGold() + 100);
                    carriedGold = 0;
                    cleanup(game);
                } else {
                    // move towards townhall
                    moveTowardsTarget(game, townhall);
                }
            } else {
                if (inRange(target)) {
                    if (progressTimerMax == 0) {
                        progressTimerMax = progressTimer = 100;
                    } else {
                        if (progressTimer <= 0) {
                            progressTimerMax = progressTimer = 0;
                            target.setRemaining_gold(target.getRemaining_gold() - 100);
                            carriedGold = 100;
                            cleanup(game);
                        }
                    }
                } else {
                    // move towards mine
                    moveTowardsTarget(game, target);
                }
            }
        }
    }

    public List<String> getAllowedUnits() {
        return allowedUnits;
    }

    public Entity toD2Entity() {
        s3.mmpm.entities.WPeasant ret;
        ret = new s3.mmpm.entities.WPeasant("" + entityID, owner);
        ret.setx(x);
        ret.sety(y);
        ret.setCurrent_hitpoints(current_hitpoints);
        ret.setCycle_created(cycle_created);
        ret.setCycle_last_attacked(cycle_last_attacked);
        ret.setRange(range);
        ret.setAttack(attack);
        if (creator == null) {
            ret.setCreator("");
        } else {
            ret.setCreator(creator.getEntityID() + "");
        }
        if (status == null) {
            ret.setStatus("0");
        } else {
            ret.setStatus("" + status.m_action);
        }
        return ret;
    }
}