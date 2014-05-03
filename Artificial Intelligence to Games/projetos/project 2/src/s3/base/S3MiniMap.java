package s3.base;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import s3.entities.S3PhysicalEntity;
import s3.entities.WBuilding;
import s3.entities.WGoldMine;
import s3.entities.WOGrass;
import s3.entities.WOMapEntity;
import s3.entities.WOTree;
import s3.entities.WOWater;
import s3.entities.WTroop;
import s3.util.Pair;

public class S3MiniMap {
	int m_width;
	int m_length;
	int m_miniMapScale = -1;
	public static final int MINI_MAP_MARGIN_X = 10;
	public static final int MINI_MAP_MARGIN_Y = 10;

	public static final int MINI_MAP_OUTLINE_MARGIN_X = 5;
	public static final int MINI_MAP_OUTLINE_MARGIN_Y = 5;
	
	double m_scale_x;
	double m_scale_y;

	Color treeColor = new Color(0,96,0);
	Color grassColor = new Color(0,32,0);
	Color waterColor = new Color(32,32,64);
	
	S3MiniMap ()
	{}
	
	S3MiniMap (int width, int length)
	{
		m_miniMapScale = S3PhysicalEntity.CELL_SIZE/10;
		if (width < S3App.SCREEN_X/10 && 
				length < S3App.SCREEN_Y/10)
		{
			//we're within the 10% bounds
			m_width = width*m_miniMapScale;
			m_length = length*m_miniMapScale;
			
		}
		else
		{
			if (width > length)
			{ 
				m_miniMapScale = 1;
				m_width  = S3App.SCREEN_X/10;
				m_length = length * m_width / width;
				m_width = width*m_miniMapScale;
				m_length = length*m_miniMapScale;

			}
			else
			{
				m_miniMapScale = 1;
				m_length = S3App.SCREEN_Y/10;
				m_width  = width * m_length / length;
				m_width = width*m_miniMapScale;
				m_length = length*m_miniMapScale;

			}
		}
		m_scale_x = (double)width*S3PhysicalEntity.CELL_SIZE/m_width;
		m_scale_y = (double)length*S3PhysicalEntity.CELL_SIZE/m_length;
	}

	public void drawMiniMapOutline(Graphics2D g, int x_offset, int y_offset) {
		g.setColor(Color.BLACK);
		//g.setComposite(AlphaComposite.SrcOver.derive(0.8f));
		g.drawRoundRect(MINI_MAP_MARGIN_X-MINI_MAP_OUTLINE_MARGIN_X, 
				        MINI_MAP_MARGIN_Y-MINI_MAP_OUTLINE_MARGIN_Y, 
				        m_width + MINI_MAP_OUTLINE_MARGIN_X*2, 
				        m_length + MINI_MAP_OUTLINE_MARGIN_Y*2,
				        MINI_MAP_OUTLINE_MARGIN_X*2,
				        MINI_MAP_OUTLINE_MARGIN_Y*2);
		
		g.setColor(Color.RED);
		g.drawRect(x_offset/(S3PhysicalEntity.CELL_SIZE/m_miniMapScale) + MINI_MAP_MARGIN_X, 
				   y_offset/(S3PhysicalEntity.CELL_SIZE/m_miniMapScale) + MINI_MAP_MARGIN_Y,
				   (S3App.SCREEN_X/S3PhysicalEntity.CELL_SIZE)*m_miniMapScale, 
				   (S3App.SCREEN_Y/S3PhysicalEntity.CELL_SIZE)*m_miniMapScale);
		//g.setComposite(AlphaComposite.SrcOver.derive(1.0f));
	}

	public void draw(Graphics2D g, S3PhysicalEntity entity) {

		if (entity instanceof WGoldMine) {
			g.setColor(Color.YELLOW);
		} else if (entity instanceof WBuilding ||
			entity instanceof WTroop) {
			if (entity.getPlayerColor().equals("red/")) {
				g.setColor(Color.RED);								
			} else if (entity.getPlayerColor().equals("blue/")) {
				g.setColor(Color.BLUE);				
			} else {
				g.setColor(Color.BLACK);				
			}
		} else if (entity instanceof WOGrass) {
			g.setColor(grassColor);
		} else if (entity instanceof WOTree) {
			g.setColor(treeColor);
		} else if (entity instanceof WOWater) {
			g.setColor(waterColor);
		} else if (entity instanceof WOMapEntity) {
			g.setColor(Color.GRAY);
		} else { 
			return;
		}
		g.setComposite(AlphaComposite.SrcOver.derive(0.8f));
		g.fillRect(entity.getX()*m_miniMapScale + MINI_MAP_MARGIN_X, 
				entity.getY()*m_miniMapScale + MINI_MAP_MARGIN_Y, 
				entity.getWidth()*m_miniMapScale, 
				entity.getLength()*m_miniMapScale);
		
		if (entity.getHitTimer() > 0)
		{
			g.setColor(Color.RED);
			//flash a red if ANY unit (friendly/enemy) is being attacked
			g.fillOval(entity.getX()*m_miniMapScale  + 
						(entity.getWidth()*m_miniMapScale)/4 + MINI_MAP_MARGIN_X,
					   entity.getY()*m_miniMapScale  + 
					   	(entity.getLength()*m_miniMapScale)/4 + MINI_MAP_MARGIN_Y,
					   entity.getWidth()*m_miniMapScale/2,
					   entity.getLength()*m_miniMapScale/2);
			
		}
		g.setComposite(AlphaComposite.SrcOver.derive(1.0f));
	}

	public Pair<Integer,Integer> isInMiniMap(int current_screen_x, int current_screen_y) {
		int modified_x = current_screen_x - MINI_MAP_OUTLINE_MARGIN_X;
		int modified_y = current_screen_y - MINI_MAP_OUTLINE_MARGIN_Y;
		
		if (modified_x <= (m_width + MINI_MAP_MARGIN_X) &&
			modified_x >= 0 &&
			modified_y <= (m_length + MINI_MAP_MARGIN_Y) &&
			modified_y >= 0)
		{
				return new Pair<Integer,Integer>((int)(modified_x*m_scale_x),
						                         (int)(modified_y*m_scale_y));
		}
		return null;
	}	
}
