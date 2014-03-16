using UnityEngine;
using System.Collections;

public class CharacterController : MonoBehaviour {

	public int speed;
	bool grounded;

	void Update () {
		if ( grounded )
		{
			Vector3 movement = new Vector3();

			movement.x = speed * Input.GetAxis ("Horizontal");
			movement.z = speed * Input.GetAxis ("Vertical");

			if ( Input.GetButtonDown("Jump") )
			{
				movement.y = speed;
			}

			GetComponent<Rigidbody>().AddForce(movement * Time.deltaTime);
		}
	}

	void OnCollisionEnter(Collision c)
	{
		if ( c.gameObject.name == "track" )
		{
			grounded = true;
		}
	}

	void OnCollisionExit( Collision c )
	{
		if ( c.gameObject.name == "track" )
		{
			grounded = false;
		}
	}
}
