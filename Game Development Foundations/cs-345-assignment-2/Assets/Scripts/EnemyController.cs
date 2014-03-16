using UnityEngine;
using System.Collections;

public class EnemyController : MonoBehaviour {

	public GameObject [] explosionsEffects;
	public AudioClip  [] explosionsAudios;

	public int speed, tumble;

	private AudioSource audioSource;

	void Start()
	{
		rigidbody.angularVelocity = Random.insideUnitSphere * tumble;
		audioSource = GetComponent<AudioSource>().audio;
	}

	void FixedUpdate()
	{
		rigidbody.AddForce(Vector3.back * speed);
	}

	public void ShotCollision()
	{
		Instantiate(
			explosionsEffects[ (int) (Random.value * explosionsEffects.Length) ],
			transform.position,
			Quaternion.identity
		);

		audioSource.clip = explosionsAudios[ (int) (Random.value * explosionsAudios.Length) ];

		audio.Play();

		Destroy (gameObject);
	}
}
