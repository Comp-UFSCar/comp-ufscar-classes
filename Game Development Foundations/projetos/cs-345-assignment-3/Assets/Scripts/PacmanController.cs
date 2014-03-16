using UnityEngine;
using System.Collections;

public class PacmanController : MonoBehaviour {

	public float speed;
	public float furiousStateDuration;
	public bool furious;

	public int food;

	Vector3 movement;
	Animator anim;
	GameController game;

	void Start()
	{
		food = 0;
		movement = new Vector3();
		anim = GetComponent<Animator>();
		game = Camera.main.GetComponent<GameController>();
	}
	
	void FixedUpdate ()
	{
		// moving character
		movement.x = Input.GetAxis ("Horizontal");
		movement.y = Input.GetAxis ("Vertical");

		rigidbody.AddForce(movement * speed);

		// defining which direction character is going and rotating it properly.
//		if ( Mathf.Abs(movement.z) <= 0.1f ) {
//			transform.localRotation = Quaternion.AngleAxis(0, Vector3.up);
//		} else if ( movement.z > 0 ) {
//			transform.localRotation = Quaternion.AngleAxis(270, Vector3.up);
//		} else {
//			transform.localRotation = Quaternion.AngleAxis(90, Vector3.up);
//		}
		float essencial
			= Mathf.Abs(movement.x) > Mathf.Abs (movement.y)
			? Mathf.Abs (movement.x)
			: Mathf.Abs (movement.y)
			;

		if ( Mathf.Abs(movement.x) > Mathf.Abs(movement.y) ) {
			if ( Mathf.Abs(movement.x) > 0.1f ) {
				transform.localEulerAngles = (movement.x  > 0 ? 0 : 180) * Vector3.forward;
			}
		} else if ( Mathf.Abs(movement.y) > 0.1f ) {
			transform.localEulerAngles = (movement.y  > 0 ? 90 : -90) * Vector3.forward;
		}

		anim.SetFloat("speed", essencial);
	}

	IEnumerator GetFurious()
	{
		furious = true;
		anim.SetTrigger("furious");

		foreach (GameObject enemy in game.getEnemies()) {
			enemy.SendMessage("OnPacmanFurious");
		}

		yield return new WaitForSeconds(furiousStateDuration);

		foreach (GameObject enemy in game.getEnemies()) {
			enemy.SendMessage("OnPacmanFurious");
		}

		anim.SetTrigger("furious");
		furious = false;
	}

	void EatRegularFood()
	{
		food++;

		if ( GameObject.Find ("food") ) {
			Application.LoadLevel("stage-1");
		}
	}

	void EatBossFood()
	{
		if ( ! furious ) {
			StartCoroutine(GetFurious());
		}
	}

	void OnCollisionWithGhost()
	{
		if ( ! furious ) {
			Application.LoadLevel("stage-1");
		}
	}
}
