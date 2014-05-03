package s3.base;

public class PlayerInput {
	public static final int INPUT_NONE = -1;
	public static final int INPUT_MOUSE = 0;
	public static final int INPUT_AI = 1;

	public int m_inputType = INPUT_AI;
	public String m_playerID = "";
	public String m_playerName = "";
	public String AIType = "";
	public String ME = "";
}
