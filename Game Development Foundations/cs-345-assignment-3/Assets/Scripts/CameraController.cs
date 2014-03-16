using UnityEngine;
using System.Collections;

public class CameraController : MonoBehaviour {

	const float MIN_X = -2;
	const float MAX_X = 2;
	const float MIN_Y = -3;
	const float MAX_Y = 3;
	
	Transform target;
	
	public void Start()
	{
		// Camera will always look for the character.
		target = GameObject.Find ("Pacman").transform;
	}
	
	void LateUpdate()
	{
		// perform smooth transitions to the character.
		// the idea is quite simple: if the character is far from the camera focus, it will 
		// move fast and, as it's becomes near, it becomes to slow down.
		float camX = transform.position.x;
		float camY = transform.position.y;
		float targetX;
		float targetY;
		
		targetX = target.position.x;
		
		if ( targetX < MIN_X )
			targetX = MIN_X;
		else if ( targetX > MAX_X )
			targetX = MAX_X;
		
		targetY = target.position.y;
		
		if ( target.position.y < MIN_Y )
			targetY = MIN_Y;
		else if ( target.position.y > MAX_Y )
			targetY = MAX_Y;
		
		float x = IncrementTowards(camX, targetX, Mathf.Abs(targetX - camX) * 2f);
		float y = IncrementTowards(camY, targetY, Mathf.Abs(targetY - camY) * 2f);
		
		transform.position = new Vector3(x, y, transform.position.z);
	}

	public static float IncrementTowards(float current, float target, float acceleration)
	{
		float dir = Mathf.Sign(target - current);
		current += dir * acceleration * Time.deltaTime;
		
		return
			0 < (target - current) * dir
				? current
				: target
				;
	}

}
