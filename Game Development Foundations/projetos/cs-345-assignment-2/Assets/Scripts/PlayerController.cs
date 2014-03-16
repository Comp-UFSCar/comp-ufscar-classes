using UnityEngine;
using System.Collections;

public class PlayerController : MonoBehaviour
{
	public float speed;
	public float tilt;

	public GameObject shotPrefab;
	public float fireRate;
	private float nextFire = 0;

	private float xMin = -5, xMax = 5, zMin = -2, zMax = 4;

	void Update()
	{
		if ( Input.GetButton("Fire1") && Time.time > nextFire )
		{
			nextFire = Time.time + fireRate;
			Instantiate(shotPrefab, transform.position + Vector3.forward, Quaternion.Euler(new Vector3(90f, 0f, 0f)));
			audio.Play();
		}
	}
	
	void FixedUpdate ()
	{
		Vector3 movement = new Vector3 (
			Input.GetAxis ("Horizontal"),
			0.0f,
			Input.GetAxis ("Vertical")
		);

		rigidbody.velocity = movement * speed;
		
		rigidbody.position = new Vector3 
			(
				Mathf.Clamp (rigidbody.position.x, xMin, xMax), 
				0.0f, 
				Mathf.Clamp (rigidbody.position.z, zMin, zMax)
			);
		
		rigidbody.rotation = Quaternion.Euler (0.0f, 0.0f, rigidbody.velocity.x * -tilt);
	}

	void OnCollisionEnter( Collision c )
	{
		if ( c.gameObject.tag == "Enemy" )
		{
			Application.LoadLevel("stage-1");
		}
	}
}