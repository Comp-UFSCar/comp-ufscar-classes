using UnityEngine;
using System.Collections;

public class CameraController : MonoBehaviour {

	GameObject target;

	// Use this for initialization
	void Start () {
		target = GameObject.Find("player");
	}
	
	// Update is called once per frame
	void Update () {
		transform.position = new Vector3 ( 
			target.transform.position.x,
			target.transform.position.y +3,
	        target.transform.position.z -4
  		);

	}
}
