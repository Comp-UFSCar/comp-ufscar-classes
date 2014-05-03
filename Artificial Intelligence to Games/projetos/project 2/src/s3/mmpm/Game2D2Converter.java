package s3.mmpm;

import gatech.mmpm.Action;
import gatech.mmpm.ActionParameterType;
import gatech.mmpm.GameState;
import gatech.mmpm.ParseLmxTrace;
import gatech.mmpm.PhysicalEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import s3.base.S3;
import s3.base.S3Action;

import s3.mmpm.actions.Attack;
import s3.mmpm.actions.AttackLocation;
import s3.mmpm.actions.Build;
import s3.mmpm.actions.Move;
import s3.mmpm.actions.Repair;
import s3.mmpm.actions.ResourceLocation;
import s3.mmpm.actions.Stop;
import s3.mmpm.actions.Train;
import s3.entities.S3PhysicalEntity;
import s3.entities.WGoldMine;
import s3.entities.WOTree;
import s3.entities.WUnit;

/**
 * Class used to convert elements between both domains: game and D2.
 *
 * @author Santi Ontanon
 */

public class Game2D2Converter {

	public static S3Action toGameAction(gatech.mmpm.Action d2Action, S3 game) {

		WUnit unit = game.getUnit(Integer.parseInt(d2Action.getEntityID()));
        if (unit==null) {
            System.err.println("toGameAction: unit " + d2Action.getEntityID() + " does not exist!!!");
            return null;
        }
		if (d2Action instanceof ResourceLocation) {
			S3PhysicalEntity pe = game.getEntity((int)((ResourceLocation) d2Action).getCoor()[0],
												 (int)((ResourceLocation) d2Action).getCoor()[1]);
			if (pe instanceof WGoldMine) {
				return new S3Action(unit.getEntityID(), S3Action.ACTION_HARVEST,pe.getEntityID());
			} else if (pe instanceof WOTree) {
				return new S3Action(unit.getEntityID(), S3Action.ACTION_HARVEST,pe.getX(),pe.getY());
			} else {
				System.err.println("Game2D2Converter.toGameAction: D2 is trying to harvest an entity that is not a goldmine nor a tree!");
			}
		} else if (d2Action instanceof Train) {
            return new S3Action(unit.getEntityID(), S3Action.ACTION_TRAIN, ((Train)d2Action).getType().getSimpleName());
		} else if (d2Action instanceof Build) {
            return new S3Action(unit.getEntityID(), S3Action.ACTION_BUILD, ((Build)d2Action).getType().getSimpleName(),
                                (int)((Build) d2Action).getCoor()[0],(int)((Build) d2Action).getCoor()[1]);
		} else if (d2Action instanceof Attack) {
            return new S3Action(unit.getEntityID(), S3Action.ACTION_ATTACK, (int)Integer.parseInt(((Attack) d2Action).getTarget().getentityID()));
		} else if (d2Action instanceof Move) {
			return new S3Action(unit.getEntityID(), S3Action.ACTION_MOVE,
                                (int)((Move) d2Action).getCoor()[0],
                                (int)((Move) d2Action).getCoor()[1]);
		} else if (d2Action instanceof Repair) {
			return new S3Action(unit.getEntityID(), S3Action.ACTION_REPAIR, (int)Integer.parseInt(((Repair) d2Action).getTarget().getentityID()));
		} else if (d2Action instanceof Stop) {
			return new S3Action(unit.getEntityID(), S3Action.ACTION_STAND_GROUND);
		}

		return null;
	}

	public static List<Action> toD2Action(S3Action action, GameState game, String playerID) {
		List<Action> list = new LinkedList<Action>();
		if (action.m_action == S3Action.ACTION_ATTACK) {
            Attack ret = new Attack("" + action.m_targetUnit, playerID);
            PhysicalEntity target = (PhysicalEntity)game.getEntity("" + ((Integer)action.m_parameters.get(0)));
            ret.setTarget(target);
            if (target!=null) {
                list.add(ret);
            } else {
                System.err.println("Null target in Attack action produced by D2, ignoring!");
            }
		} else if (action.m_action == S3Action.ACTION_BUILD) {
            Build ret = new Build("" + action.m_targetUnit, playerID);
            String type = "s3.mmpm.entities." + (String)action.m_parameters.get(0);
            if ((Class<? extends gatech.mmpm.Entity>)ActionParameterType.ENTITY_TYPE.fromString(type)==null) {
                System.err.println("error 2 translating entity: " + type);
            }
            ret.setType(type);
            ret.setCoor(new float[]{(Integer)action.m_parameters.get(1),
                                    (Integer)action.m_parameters.get(2),0});
            list.add(ret);
		} else if (action.m_action == S3Action.ACTION_MOVE) {
            Move ret = new Move("" + action.m_targetUnit, playerID);
            ret.setCoor(new float[]{(Integer)action.m_parameters.get(0),
                                    (Integer)action.m_parameters.get(1),0});
            list.add(ret);
		} else if (action.m_action == S3Action.ACTION_REPAIR) {
            Repair ret = new Repair("" + action.m_targetUnit, playerID);
            PhysicalEntity target = (PhysicalEntity)game.getEntity("" + ((Integer)action.m_parameters.get(0)));
            ret.setTarget(target);
            if (target!=null) {
                list.add(ret);
            } else {
                System.err.println("Null target in Repair action produced by D2, ignoring!");
            }
		} else if (action.m_action == S3Action.ACTION_HARVEST) {
            ResourceLocation ret = new ResourceLocation("" + action.m_targetUnit, playerID);
            PhysicalEntity target = null;
            if (action.m_parameters.size()==1) {
                target = (PhysicalEntity)game.getEntity("" + ((Integer)action.m_parameters.get(0)));
            } else {
                target = (PhysicalEntity)game.getEntityAt(new float[]{(Integer)action.m_parameters.get(0),(Integer)action.m_parameters.get(1),0});
            }
            if (target!=null) {
                ret.setCoor(new float[]{target.getx(),target.gety(),0});
                list.add(ret);
            } else {
                System.err.println("Null target in Harvest action produced by D2, ignoring!");
            }
		} else if (action.m_action == S3Action.ACTION_STAND_GROUND) {
            Stop ret = new Stop("" + action.m_targetUnit, playerID);
            list.add(ret);
		} else if (action.m_action == S3Action.ACTION_TRAIN) {
            Train ret = new Train("" + action.m_targetUnit, playerID);
            String type = "s3.mmpm.entities." + (String)action.m_parameters.get(0);
            if ((Class<? extends gatech.mmpm.Entity>)ActionParameterType.ENTITY_TYPE.fromString(type)==null) {
                System.err.println("error 2 translating entity: " + type);
            }
            ret.setType(type);
            list.add(ret);
		}
		return list;
	}

	/**
	 * @param game
	 *            Current game
	 * @return GameState in
	 */
	public static gatech.mmpm.GameState toGameState(s3.base.S3 game, gatech.mmpm.IDomain idomain) {
		gatech.mmpm.GameState ret = new gatech.mmpm.GameState();

		ret.addMap(game.getMap().toD2Map(idomain));

		// Add the entities stored in our TMap
		List<s3.entities.S3Entity> entities;

		entities = game.getAllUnits();
		for (s3.entities.S3Entity e : entities) {
			gatech.mmpm.Entity d2Entity = e.toD2Entity();
			if ((d2Entity != null))
				ret.addEntity(d2Entity);
		}

		// System.out.println(ret.toString());

		return ret;
	}

	static ParseLmxTrace parser = new ParseLmxTrace(S3Domain.getDomainName());
}
