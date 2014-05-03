/*********************************************************************************
Organization 					: 				Georgia Institute of Technology
												Cognitive Computing Lab (CCL)
Authors							: 				Jai Rad
												Santi Ontanon
 ****************************************************************************/
package s3.entities;

import gatech.mmpm.Entity;



public class WPlayer extends S3Entity {
	public static String colors[]={"blue/","red/"};
	public static int playerCount = 0;

	private int gold;

	private int wood;
	
	private int inputType;
	
	private String playerColor;

	public int getInputType() {
		return inputType;
	}

	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	public WPlayer() {
		//set the playerColor
		playerColor = colors[playerCount++];
	}

	public WPlayer(WPlayer incoming) {
		super(incoming);
		this.gold = incoming.gold;
		this.wood = incoming.wood;
		this.playerColor = incoming.playerColor;
	}

	public Object clone() {
		WPlayer e = new WPlayer(this);
		return e;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}
	
	public boolean canAfford(Class<? extends WUnit> type) {
		
		return false;
	}

	public String getColor() {
		return playerColor;
	}

    public Entity toD2Entity() {
		s3.mmpm.entities.WPlayer ret;

		ret = new s3.mmpm.entities.WPlayer(owner, owner);
		ret.setGold(gold);
		ret.setWood(wood);
		return ret;
	}

}