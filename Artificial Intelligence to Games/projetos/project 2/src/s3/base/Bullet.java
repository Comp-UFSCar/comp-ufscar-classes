package s3.base;

/*
 * Note: this class is not considered an "Entity", since it is only an auxiliar class for drawing purposes.
 */

public class Bullet {
	public static final float speed = 8;
	public int current_x,current_y;
	public int target_x,target_y;
	
	public boolean cycle() {
		double vx = target_x - current_x;
		double vy = target_y - current_y;
		double d = Math.sqrt(vx*vx+vy*vy);
		if (d<=speed) {
			current_x = target_x;
			current_y = target_y;
			return true;
		} else {
			vx/=d;
			vy/=d;
			current_x += vx*speed;
			current_y += vy*speed;
			return false;
		}
	}
}
