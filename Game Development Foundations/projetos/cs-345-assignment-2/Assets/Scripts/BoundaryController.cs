using UnityEngine;
using System.Collections;

public class BoundaryController : MonoBehaviour {

	void OnTriggerExit( Collider c )
	{
		Destroy(c.gameObject);
	}
}
