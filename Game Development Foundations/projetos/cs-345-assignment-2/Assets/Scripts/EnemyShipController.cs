using UnityEngine;
using System.Collections;

public class EnemyShipController : MonoBehaviour {

	string behavior;
	GameObject target;

	void Start ()
	{
		target = GameObject.Find("player");

		float chance = Random.value;

		if ( chance < 0.4 ) {
			behavior = "dummy";
		} else {
			behavior = "kamikaze";
		}
	}
	
	void FixedUpdate()
	{
		if ( behavior == "dummy" ) {
			// moves randomly
			rigidbody.AddForce(new Vector3(
				(Random.value > 0.5 ? 1 : -1) * GetComponent<EnemyController>().speed,
				0f,
				0f
			));
		} else if ( behavior == "kamikaze" ) {
			// always move towards the player
			rigidbody.AddForce(new Vector3(
				(target.transform.position.x > transform.position.x ? 1 : -1) * GetComponent<EnemyController>().speed,
				0f,
				0f
			));

		}
	}
}
