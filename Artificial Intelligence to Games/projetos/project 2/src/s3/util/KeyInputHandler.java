package s3.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInputHandler extends KeyAdapter {
	public boolean m_keyboardStatusBuffer[];
	public boolean m_keyboardStatus[];
	public boolean m_old_keyboardStatus[];

	public KeyInputHandler() {
		int i;
		m_keyboardStatusBuffer = new boolean[KeyEvent.KEY_LAST];
		m_keyboardStatus = new boolean[KeyEvent.KEY_LAST];
		m_old_keyboardStatus = new boolean[KeyEvent.KEY_LAST];
		
		for(i=0;i<KeyEvent.KEY_LAST;i++) {
			m_keyboardStatus[i]=false;	
			m_old_keyboardStatus[i]=false;
		}
	}
	
	public void cycle() {
		for(int i=0;i<KeyEvent.KEY_LAST;i++) {
			m_old_keyboardStatus[i] = m_keyboardStatus[i];
			m_keyboardStatus[i] = m_keyboardStatusBuffer[i];
		}		
	}
	
	
	/**
	 * Notification from AWT that a key has been pressed. Note that
	 * a key being pressed is equal to being pushed down but *NOT*
	 * released. Thats where keyTyped() comes in.
	 *
	 * @param e The details of the key that was pressed 
	 */
	public void keyPressed(KeyEvent e) {
		m_keyboardStatusBuffer[e.getKeyCode()]=true;
	} 

	/**
	 * Notification from AWT that a key has been released.
	 *
	 * @param e The details of the key that was released 
	 */
	public void keyReleased(KeyEvent e) {
		m_keyboardStatusBuffer[e.getKeyCode()]=false;
	}

	/**
	 * Notification from AWT that a key has been typed. Note that
	 * typing a key means to both press and then release it.
	 *
	 * @param e The details of the key that was typed. 
	 */
	public void keyTyped(KeyEvent e) {
	}
	
	public static int keyCodeForLetter(char l) {
		switch(l) {
		case '0':return java.awt.event.KeyEvent.VK_0;
		case '1':return java.awt.event.KeyEvent.VK_1;
		case '2':return java.awt.event.KeyEvent.VK_2;
		case '3':return java.awt.event.KeyEvent.VK_3;
		case '4':return java.awt.event.KeyEvent.VK_4;
		case '5':return java.awt.event.KeyEvent.VK_5;
		case '6':return java.awt.event.KeyEvent.VK_6;
		case '7':return java.awt.event.KeyEvent.VK_7;
		case '8':return java.awt.event.KeyEvent.VK_8;
		case '9':return java.awt.event.KeyEvent.VK_9;
		case 'A':return java.awt.event.KeyEvent.VK_A;
		case 'B':return java.awt.event.KeyEvent.VK_B;
		case 'C':return java.awt.event.KeyEvent.VK_C;
		case 'D':return java.awt.event.KeyEvent.VK_D;
		case 'E':return java.awt.event.KeyEvent.VK_E;
		case 'F':return java.awt.event.KeyEvent.VK_F;
		case 'G':return java.awt.event.KeyEvent.VK_G;
		case 'H':return java.awt.event.KeyEvent.VK_H;
		case 'I':return java.awt.event.KeyEvent.VK_I;
		case 'J':return java.awt.event.KeyEvent.VK_J;
		case 'K':return java.awt.event.KeyEvent.VK_K;
		case 'L':return java.awt.event.KeyEvent.VK_L;
		case 'M':return java.awt.event.KeyEvent.VK_M;
		case 'N':return java.awt.event.KeyEvent.VK_N;
		case 'O':return java.awt.event.KeyEvent.VK_O;
		case 'P':return java.awt.event.KeyEvent.VK_P;
		case 'Q':return java.awt.event.KeyEvent.VK_Q;
		case 'R':return java.awt.event.KeyEvent.VK_R;
		case 'S':return java.awt.event.KeyEvent.VK_S;
		case 'T':return java.awt.event.KeyEvent.VK_T;
		case 'U':return java.awt.event.KeyEvent.VK_U;
		case 'V':return java.awt.event.KeyEvent.VK_V;
		case 'W':return java.awt.event.KeyEvent.VK_W;
		case 'X':return java.awt.event.KeyEvent.VK_X;
		case 'Y':return java.awt.event.KeyEvent.VK_Y;
		case 'Z':return java.awt.event.KeyEvent.VK_Z;
		}
		return java.awt.event.KeyEvent.VK_UNDEFINED;
	}
}



