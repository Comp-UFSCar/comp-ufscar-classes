using UnityEngine;
using System.Collections;

public class DeadZoneController : MonoBehaviour {
	void OnCollisionEnter(Collision c) {
		if ( c.gameObject.name == "player" )
		{
			Application.LoadLevel("stage-1");
		}
	}
}
