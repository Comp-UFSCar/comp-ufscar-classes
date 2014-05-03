package s3.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import s3.entities.WArcher;
import s3.entities.WBuilding;
import s3.entities.WCatapult;
import s3.entities.WFootman;
import s3.entities.WFortress;
import s3.entities.WGoldMine;
import s3.entities.WBarracks;
import s3.entities.WBlacksmith;
import s3.entities.WKnight;
import s3.entities.WLumberMill;
import s3.entities.WPeasant;
import s3.entities.WPlayer;
import s3.entities.WStable;
import s3.entities.WTower;
import s3.entities.WTownhall;
import s3.entities.WUnit;
import s3.entities.WWall;
import s3.util.KeyInputHandler;
import s3.util.Pair;

public class HUD {
//	private static final int PARAM_Y_LOC = S3App.SCREEN_Y * 7 / 8;
	public static final int HUD_Y_LOC = S3App.SCREEN_Y - S3App.SCREEN_Y / 8;
	protected String owner = "player1";
	
	private S3 s2;
	// Each button is a pair "action" + "quick-key"
	List<Pair<S3Action,Character>> buttons1 = new LinkedList<Pair<S3Action,Character>>();
	List<Pair<S3Action,Character>> buttons2 = new LinkedList<Pair<S3Action,Character>>();

	// protected WPlayer player;
	protected Sprite gold;
	protected Sprite wood;
	private final int thickness = 3;
	private int x_margin1 = 120;
	private int y_margin1 = HUD_Y_LOC + 6 * thickness;

	public HUD(S3 game, String o) {
		s2 = game;
		owner = o;
		// player = game.getPlayers().get(0);
		// register sprite images for HUD
		gold = SpriteStore.get().getSprite("gold");
		wood = SpriteStore.get().getSprite("wood");
	}

	private void drawPlayerInfo(Graphics2D g, List<PlayerInput> pi_l) {
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(0, HUD_Y_LOC, S3App.SCREEN_X, S3App.SCREEN_Y / 8, 10, 10);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(0 + thickness, HUD_Y_LOC + thickness, S3App.SCREEN_X - 2 * thickness,
				S3App.SCREEN_Y / 8 - thickness, 10, 10);

		// Player1 information
		int x_margin_player = 5;
		int y_margin_player = HUD_Y_LOC + 6 * thickness;
		for (WPlayer player : s2.getPlayers()) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 12));

			// gold
			gold.draw(g, x_margin_player, y_margin_player - 10);
			g.drawString(player.getGold() + "", x_margin_player + 25, y_margin_player + 5);
			// wood
			wood.draw(g, x_margin_player, y_margin_player + 8);
			g.drawString(player.getWood() + "", x_margin_player + 25, y_margin_player + 25);

			{
				for (PlayerInput pi : pi_l) {
					if (pi.m_playerID.equals(player.getOwner())) {
						g.drawString(pi.m_playerName, x_margin_player + 25, y_margin_player + 50);
					}
				}
			}

			x_margin_player += 80;
		}

		g.drawString(s2.getCycle() + "", S3App.SCREEN_X - 50, y_margin_player + 50);
	}
	
	public void resetButtons() {
		buttons1.clear();
		buttons2.clear();
	}
	
	public S3Action cycle(WUnit selectedUnit, KeyInputHandler k) {
		for(Pair<S3Action,Character> b:buttons1) {
			int key = KeyInputHandler.keyCodeForLetter(b.m_b);
			if (k.m_keyboardStatus[key] && !k.m_old_keyboardStatus[key]) {
				// button pressed:
				return button1Click(selectedUnit,b.m_a);
			}
		}
		for(Pair<S3Action,Character> b:buttons2) {
			int key = KeyInputHandler.keyCodeForLetter(b.m_b);
			if (k.m_keyboardStatus[key]&& !k.m_old_keyboardStatus[key]) {
				return b.m_a;
			}
		}
		return null;
	}

	/**
	 * draw the HUD on the screen.
	 */
	public void draw(Graphics2D g, WUnit selected, S3Action action, List<PlayerInput> pi_l, WPlayer p) {
		drawMessage(g);
		drawPlayerInfo(g, pi_l);
		drawSelectedUnit(g, selected, action, p);
	}

	private void drawMessage(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString(s2.getMessage(), 10, HUD_Y_LOC - 10);
	}

	/**
	 * draw the unit image, name, hit points, and available actions.
	 */
	private void drawSelectedUnit(Graphics2D g, WUnit selected, S3Action action, WPlayer p) {
		
		//select sprite
		if (selected != null && selected.spriteName != null && selected.sprite == null)
			selected.setSprite();

		//display unit info
		if (selected != null && selected.getCurrent_hitpoints() > 0) {
			g.setColor(Color.RED);
			g.drawLine(x_margin1 + 40, y_margin1 - 5, x_margin1 + 40, y_margin1 + 50);

			// need to scale the unit image
			if (selected.getWidth() > 1) {
				selected.sprite.draw(g, x_margin1 + 45, y_margin1 - 5, 40, 40);
			} else {
				selected.sprite.draw(g, x_margin1 + 45, y_margin1 - 5);
			}
			g.drawString(selected.featureValue("type").toString(), x_margin1 + 88, y_margin1 + 5);
			g.drawString(selected.getCurrent_hitpoints() + " HP", x_margin1 + 88, y_margin1 + 25);
			if (selected.getOwner()!=null) {
				g.drawString(selected.getOwner(), x_margin1 + 88, y_margin1 + 45);
			} else {
				if (selected instanceof WGoldMine) {
					g.drawString("Gold: " + ((WGoldMine)selected).getRemaining_gold(), x_margin1 + 88, y_margin1 + 45);
				}
			}
		}

		// get list of actions allowed by selected unit
		if (selected!=null && selected.getOwner()!=null && owner!=null && selected.getOwner().equals(owner)) {
			if (buttons1.isEmpty()) {
				List<Integer> actions = selected.getActionList();
				for (int a : actions) {
					buttons1.add(new Pair<S3Action, Character>(new S3Action(selected.entityID, a),
							S3Action.quick_keys[a]));
				}
			}
	
			//display actions
			drawActions(g, action, p);
		}
		
	}
	
	/**
	 * Draws the available actions for the selected unit.
	 * 
	 * @param g
	 * @param action
	 * @param actions
	 * @param x_offset
	 * @return
	 */
	private int drawActions(Graphics2D g, S3Action action, WPlayer p) {
		String color = p.getColor();
		int x_offset = 0;
		List<Pair<S3Action,Character>> tmp1 = new LinkedList<Pair<S3Action,Character>>();
		tmp1.addAll(buttons1);
		for (Pair<S3Action,Character> b : tmp1) {
			if (b.m_a.m_action == S3Action.ACTION_MOVE) {
				SpriteStore.get().getSprite("actions/move").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_ATTACK) {
				SpriteStore.get().getSprite("actions/attack").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_HARVEST) {
				SpriteStore.get().getSprite("actions/harvest").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_STAND_GROUND) {
				SpriteStore.get().getSprite("actions/standGround").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_TRAIN) {
				SpriteStore.get().getSprite("actions/train").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_BUILD) {
				SpriteStore.get().getSprite("actions/build").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_REPAIR) {
				SpriteStore.get().getSprite("actions/repair").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (b.m_a.m_action == S3Action.ACTION_CANCEL) {
				SpriteStore.get().getSprite("actions/cancel").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));
			}
			if (action!=null){
				if (b.m_a.m_action == action.m_action) {
					g.setColor(Color.RED);
					g.setStroke(new BasicStroke(2));
					g.drawRect(x_margin1 + 155 + x_offset, (y_margin1 + 10), 32, 32);
				}
			}
			if (b.m_b!=0) {
				g.drawString("" + b.m_b, x_margin1 + 165 + x_offset, y_margin1 + 55);
			}
			x_offset += 40;
		}
		x_offset += 40;
		
		List<Pair<S3Action,Character>> tmp2 = new LinkedList<Pair<S3Action,Character>>();
		tmp2.addAll(buttons2);
		for (Pair<S3Action,Character> b : tmp2) {
			if (b.m_a.m_action == S3Action.ACTION_TRAIN) {
				if (((String)b.m_a.m_parameters.get(0)).equals(WPeasant.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "peasant").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WFootman.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "footman").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WArcher.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "archer").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WCatapult.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "catapult").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WKnight.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "knight").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10));					
				}
				if (action!=null && action.m_parameters.size()>0 && b.m_a.m_parameters.size()>0){
					if (b.m_a.m_parameters.get(0).equals(action.m_parameters.get(0))) {
						g.setColor(Color.RED);
						g.setStroke(new BasicStroke(2));
						g.drawRect(x_margin1 + 155 + x_offset, (y_margin1 + 10), 32, 32);
					}
				}
				if (b.m_b!=0) {
					g.drawString("" + b.m_b, x_margin1 + 165 + x_offset, y_margin1 + 55);
				}
			}
			if (b.m_a.m_action == S3Action.ACTION_BUILD) {				
				if (((String)b.m_a.m_parameters.get(0)).equals(WTownhall.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "townhall").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WBarracks.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "barracks").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WStable.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "stables").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WFortress.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "fortress").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WLumberMill.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "lumbermill").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WBlacksmith.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "blacksmith").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WTower.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "tower").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (((String)b.m_a.m_parameters.get(0)).equals(WWall.class.getSimpleName())) {
					SpriteStore.get().getSprite(color + "wall").draw(g, x_margin1 + 155 + x_offset, (y_margin1 + 10),32,32);					
				}
				if (action!=null && action.m_parameters.size()>0 && b.m_a.m_parameters.size()>0){
					if (b.m_a.m_parameters.get(0).equals(action.m_parameters.get(0))) {
						g.setColor(Color.RED);
						g.setStroke(new BasicStroke(2));
						g.drawRect(x_margin1 + 155 + x_offset, (y_margin1 + 10), 32, 32);
					}
				}
				if (b.m_b!=0) {
					g.drawString("" + b.m_b, x_margin1 + 165 + x_offset, y_margin1 + 55);
				}
			}			
			x_offset += 40;
		}

		return x_offset;
	}

	/**
	 * Method for any click on the HUD for ACTIONS only
	 * 
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 */
	public S3Action mouseClick(WUnit selectedUnit, int x, int y) {
		int x_offset = 0;
		for (Pair<S3Action,Character> b : buttons1) {
			if (x>=x_margin1 + 155 + x_offset && y>=y_margin1 + 10 &&
				x<x_margin1 + 155 + x_offset+32 && y<y_margin1 + 42) {
				return button1Click(selectedUnit, b.m_a);
			}
			x_offset+=40;
		}
		x_offset+=40;
		for (Pair<S3Action,Character> b : buttons2) {
			if (x>=x_margin1 + 155 + x_offset && y>=y_margin1 + 10 &&
				x<x_margin1 + 155 + x_offset+32 && y<y_margin1 + 42) {
				return b.m_a;
			}
			x_offset+=40;
		}
		resetButtons();
		return null;
	}
	
	private S3Action button1Click(WUnit selectedUnit, S3Action button) {
		buttons2.clear();

		// generate parameters:
		if (button.m_action == S3Action.ACTION_TRAIN) {
			if (selectedUnit instanceof WBuilding) {
				WBuilding building = (WBuilding) selectedUnit;
				char c = '1';
				for (String u : building.getAllowedUnits()) {
					buttons2.add(new Pair<S3Action, Character>(new S3Action(selectedUnit.entityID,
							S3Action.ACTION_TRAIN, u), c));
					c++;
				}
			}
		}
		if (button.m_action == S3Action.ACTION_BUILD) {
			if (selectedUnit instanceof WPeasant) {
				WPeasant p = (WPeasant) selectedUnit;
				char c = '1';
				for (String u : p.getAllowedUnits()) {
					buttons2.add(new Pair<S3Action, Character>(new S3Action(selectedUnit.entityID,
							S3Action.ACTION_BUILD, u), c));
					c++;
				}
			}
		}
		if (button.m_action == S3Action.ACTION_CANCEL) {
			if (selectedUnit instanceof WPeasant) {
				WPeasant p = (WPeasant) selectedUnit;
				S3Action cancel = new S3Action(selectedUnit.entityID, S3Action.ACTION_CANCEL, p
						.getStatus().m_parameters.get(0), p.getStatus().m_parameters.get(1), p
						.getStatus().m_parameters.get(2));
				return cancel;
			}
		}

		return button;
	}

}
