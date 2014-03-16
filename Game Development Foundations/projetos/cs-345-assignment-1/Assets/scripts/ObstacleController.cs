using UnityEngine;
using System.Collections;

public class ObstacleController : MonoBehaviour {

	public int rotation;

	void Update () {
		GetComponent<Rigidbody>().AddTorque(new Vector3 (
			- rotation * Time.deltaTime, 0, 0
		));
	}

	void OnCollisionEnter( Collision c )
	{
		if ( c.gameObject.name == "player" )
		{
			Application.LoadLevel("stage-1");
		}
	}
}
