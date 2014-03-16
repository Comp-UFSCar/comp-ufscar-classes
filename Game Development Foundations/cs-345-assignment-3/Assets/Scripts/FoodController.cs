using UnityEngine;
using System.Collections;

public class FoodController : MonoBehaviour {

	public enum FoodType {
		regular, boss
	}

	public FoodType type;

	// Use this for initialization
	void Start () {
		GetComponent<Animator>().SetBool("IsRegularFood", type == FoodType.regular);
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void OnTriggerEnter(Collider c) {
		if (c.name == "Pacman") {
			c.SendMessage(
				type == FoodType.regular
				? "EatRegularFood"
				: "EatBossFood"
			);
			Destroy(gameObject);
		}
	}
}
