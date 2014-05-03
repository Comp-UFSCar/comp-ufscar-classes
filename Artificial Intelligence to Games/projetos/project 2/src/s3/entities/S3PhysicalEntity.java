package s3.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import s3.base.Sprite;
import s3.base.SpriteStore;


public abstract class S3PhysicalEntity extends S3Entity {
	public static final String coordNames[] = { "x", "y", "z" };

	protected int x = 0, y = 0, z = 0;

	protected int width = 1, length = 1, height = 1;

	/** The sprite that represents this entity */
	public String spriteName = null;
	public Sprite sprite = null;


	/** The rectangle used for this entity during collisions resolution */
	private Rectangle me = new Rectangle();

	/** The rectangle used for other entities during collision resolution */
	private Rectangle him = new Rectangle();

	/** size of a cell side */
	public static int CELL_SIZE = 32;

	/** Leeway for mouse click */
	public static int MOUSE_CLICK_LEEWAY = 5;

	/** attack-hit timer */
	int hit_timer = 0;
	
	/** color of the current unit depending on the player*/
	protected String playerColor = "";
	
	public S3PhysicalEntity() {
	}

	public S3PhysicalEntity(S3PhysicalEntity incoming) {
		super(incoming);
		this.x = incoming.x;
		this.y = incoming.y;
		this.z = incoming.z;
		this.width = incoming.width;
		this.length = incoming.length;
		this.height = incoming.height;

	}
	
	public String getPlayerColor() {
		return playerColor;
	}

	public void setColor(String incomingColor) {
		playerColor = incomingColor;
	}

	public void setSprite() {
		this.sprite = SpriteStore.get().getSprite(playerColor+spriteName);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHitTimer() {
		return hit_timer;
	}

	public boolean collision(S3PhysicalEntity e) {
		if (getX() >= e.getX() + e.getWidth()
				|| getX() + getWidth() <= e.getX()
				|| getY() >= e.getY() + e.getLength()
				|| getY() + getLength() <= e.getY()
				|| getZ() >= e.getZ() + e.getHeight()
				|| getZ() + getHeight() <= e.getZ()) {
			return false;
		}
		return true;
	}

	/**
	 * This method is like the previous one, but assumes that "coords" are the
	 * coordinates at which the entity "e" is located right now.
	 */
	public boolean collision(S3PhysicalEntity e, int coords[]) {
		if (getX() >= coords[0] + e.getWidth()
				|| getX() + getWidth() <= coords[0]
				|| getY() >= coords[1] + e.getLength()
				|| getY() + getLength() <= coords[1]
				|| getZ() >= coords[2] + e.getHeight()
				|| getZ() + getHeight() <= coords[2]) {
			return false;
		}
		return true;
	}

	public double collisionSoft(S3PhysicalEntity e) {
		if (getX() >= e.getX() + e.getWidth()
				|| getX() + getWidth() <= e.getX()
				|| getY() >= e.getY() + e.getLength()
				|| getY() + getLength() <= e.getY()
				|| getZ() >= e.getZ() + e.getHeight()
				|| getZ() + getHeight() <= e.getZ()) {
			return 0;
		}
		return 1;
	}

	public double collisionSoft(S3PhysicalEntity e, int coords[]) {
		if (getX() >= coords[0] + e.getWidth()
				|| getX() + getWidth() <= coords[0]
				|| getY() >= coords[1] + e.getLength()
				|| getY() + getLength() <= coords[1]
				|| getZ() >= coords[2] + e.getHeight()
				|| getZ() + getHeight() <= coords[2]) {
			return 0;
		}
		return 1;
	}

	/**
	 * Method determines if the Physical Entity exists at given location.
	 * 
	 * @param mouse_x
	 *            X coordinate of entity
	 * @param mouse_y
	 *            Y coordinate of entity
	 * @return true if entity is at given x, y location
	 */
	public boolean isEntityAt(int xLoc, int yLoc) {
		if (x <= xLoc && (x + width) > xLoc && y <= yLoc
				&& (y + length) > yLoc)
			return true;
		return false;
	}

	public boolean collidesWith(S3PhysicalEntity other) {
		if (spriteName!=null && sprite==null) setSprite();

		me.setBounds(x, y, sprite.getWidth(), sprite.getHeight());
		him.setBounds(other.x, other.y, other.sprite.getWidth(),
				other.sprite.getHeight());
		return me.intersects(him);
	}

	public void draw(Graphics2D g, int x_offset, int y_offset) {

		if (spriteName!=null) {
			if (sprite==null) setSprite();

			sprite.draw(g, (x * CELL_SIZE - x_offset), (y
					* CELL_SIZE - y_offset));
			if (hit_timer > 0) {
				hit_timer--;
				g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND, 2f, new float[] { 5f }, 1.0f));

				g.setColor(Color.RED);
				g.drawArc((getX()*S3PhysicalEntity.CELL_SIZE)-x_offset,
						(getY()*S3PhysicalEntity.CELL_SIZE)-y_offset, 
						(getWidth() * S3PhysicalEntity.CELL_SIZE) ,
						(getLength() * S3PhysicalEntity.CELL_SIZE),
						-90,180);

				g.drawArc((getX()*S3PhysicalEntity.CELL_SIZE)-x_offset,
						(getY()*S3PhysicalEntity.CELL_SIZE)-y_offset, 
						(getWidth() * S3PhysicalEntity.CELL_SIZE) ,
						(getLength() * S3PhysicalEntity.CELL_SIZE),
						90,180);
				g.setStroke(new BasicStroke());
			}

		}

	}

}
