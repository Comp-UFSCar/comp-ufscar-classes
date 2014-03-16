using UnityEngine;
using System.Collections;

public class ShotController : MonoBehaviour {

	public int speed;
	public float lifetime;

	void Start()
	{
		Destroy(gameObject, lifetime);
	}

	void FixedUpdate()
	{	
		rigidbody.AddForce(Vector3.forward * speed);
	}

	void OnTriggerEnter ( Collider c )
	{
		if ( c.gameObject.tag == "Enemy" )
		{
			c.SendMessage("ShotCollision");
			Destroy(gameObject);
		}
	}
}
