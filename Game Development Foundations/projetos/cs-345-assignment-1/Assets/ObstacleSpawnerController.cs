using UnityEngine;
using System.Collections;

public class ObstacleSpawnerController : MonoBehaviour {

	public float rate;
	public GameObject obstacle;

	void Start () {
		StartCoroutine(SpawnObstacle());
	}

	IEnumerator SpawnObstacle()
	{
		while ( true )
		{
			yield return new WaitForSeconds( (float) 1.0 / rate);

			GameObject instance = Instantiate(obstacle, transform.position, Quaternion.identity) as GameObject;
			instance.transform.position = new Vector3(
				(Random.value > 0.5 ? Random.value : -Random.value) * 8,
				instance.transform.position.y,
				instance.transform.position.z
			);
		}
	}
}
