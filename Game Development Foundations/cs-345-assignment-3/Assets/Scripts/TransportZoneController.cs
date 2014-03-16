using UnityEngine;
using System.Collections;

public class TransportZoneController : MonoBehaviour {

	int current;
	Vector3 transportPosition;

	void Start()
	{
		// each transport zone get the other's position
		GameObject[] transportZones = GameObject.FindGameObjectsWithTag("transportzones");

		transportPosition
			= transportZones[0].transform.position.x != transform.position.x
			? transportZones[0].transform.position
			: transportZones[1].transform.position
			;

		if ( transportPosition.x > 0 ) {
			transportPosition.x -= 1;
		} else {
			transportPosition.x += 1;
		}
	}

	void OnTriggerEnter( Collider c )
	{
		if (c.tag == "Player" || c.tag == "enemy") {
			c.transform.position = transportPosition;
		}
	}
}
