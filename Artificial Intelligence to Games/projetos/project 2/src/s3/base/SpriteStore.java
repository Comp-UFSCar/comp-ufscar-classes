package s3.base;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
public class SpriteStore {
	/** The single instance of this class */
	private static SpriteStore single = new SpriteStore();
	
	/**
	 * Get the single instance of this class 
	 * 
	 * @return The single instance of this class
	 */
	public static SpriteStore get() {
		return single;
	}
	
	/** The cached sprite map, from reference to sprite instance */
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	/**
	 * Retrieve a sprite from the store
	 * 
	 * @param ref The reference to the image to use for the sprite
	 * @return A sprite instance containing an accelerate image of the request reference
	 */
	public Sprite getSprite(String ref) {
		// if we've already got the sprite in the cache
		// then just return the existing version
		if (sprites.get(ref) != null) {
			return sprites.get(ref);
		}
		
		// otherwise, go away and grab the sprite from the resource
		// loader
		BufferedImage sourceImage = null;
		
		try {
			// When the image is in a JAR file:
			InputStream f = Object.class.getResourceAsStream("/graphics/" + ref + ".png");
			sourceImage = ImageIO.read(f);
		} catch (Exception e) {
			// When it's not:
			try {
				File f = new File("graphics/" + ref + ".png");
				sourceImage = ImageIO.read(f);
			} catch (Exception e2) {
				e2.printStackTrace();
				fail("Failed to load: "+ref);
			}
		}
		
		if (null == sourceImage) {
			return null;
		}
		
		// create an accelerated image of the right size to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage,0,0,null);
		
		// create a sprite, add it the cache then return it
		Sprite sprite = new Sprite(image);
		sprites.put(ref,sprite);
		
		return sprite;
	}
	
	/**
	 * Utility method to handle resource loading failure
	 * 
	 * @param message The message to display on failure
	 */
	private void fail(String message) {
		// we're pretty dramatic here, if a resource isn't available
		// we dump the message and exit the game
		System.err.println(message);
		System.exit(0);
	}
}