using UnityEngine;
using System.Collections;

public class GameController : MonoBehaviour {

	GameObject pacman;
	GameObject[] enemies;

	GameController()
	{
		pacman  = null;
		enemies = null;
	}

	public GameObject getPacman()
	{
		if ( pacman == null ) {
			pacman = GameObject.Find ("Pacman");
		}

		return pacman;
	}

	public GameObject[] getEnemies()
	{
		if ( enemies == null ) {
			enemies = GameObject.FindGameObjectsWithTag("enemy");
		}

		return enemies;
	}
}
