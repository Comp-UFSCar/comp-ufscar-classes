using UnityEngine;
using System.Collections;

public class AsteroidSpawnerController : MonoBehaviour {

	public GameObject[] EnemyPrefabs;
	public float interval;

	void Start()
	{
		StartCoroutine(SpawnAsteroid());
	}

	IEnumerator SpawnAsteroid()
	{
		GameObject element;

		if ( interval > 0)
		{
			while ( true )
			{
				element = EnemyPrefabs[ (int) (EnemyPrefabs.Length * Random.value) ];

				yield return new WaitForSeconds(interval);

				Instantiate(
					element,
					transform.position + new Vector3(1, 0, 0) * Random.value * 8 * (Random.value > 0.5f ? 1 : -1),
					Quaternion.identity
				);
			}
		}
	}
}
