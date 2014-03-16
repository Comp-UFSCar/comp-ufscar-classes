using UnityEngine;
using System.Collections;
using System.Collections.Generic;

[RequireComponent (typeof (EnemyType))]
[RequireComponent (typeof (EnemyState))]
public class EnemyController : MonoBehaviour {

	public EnemyType type;
	public EnemyState state;
	public float speed;
	public float sightDistance;
	public float waitingTimeInInitialMode;

	public float randomWalkFactor;
	public Vector3 defaultSeekPosition;

	Animator anim;
	GameController game;
	Vector3 position;

	Vector3 target;
	List<float> previous;

	void Start ()
	{
		position = transform.position;

		previous = new List<float>();
		target = Vector3.zero;

		state = EnemyState.initial;

		anim = GetComponent<Animator>();
		game = Camera.main.GetComponent<GameController>();

		anim.SetInteger("type", (int) type);

		StartCoroutine (Restart(EnemyState.idle));
	}

	IEnumerator Restart( EnemyState previousState )
	{
		transform.position = position;

		state = EnemyState.initial;

		yield return new WaitForSeconds(waitingTimeInInitialMode);

		state = previousState;
	}

	void FixedUpdate()
	{
		// initial mode, do not do anything, just wait...
		if ( state == EnemyState.initial )
		{
			return;
		}

		// Decide to which node the enemy should move next,
		// it will follow the player or walk its defined path
		if ( state == EnemyState.idle )
		{
			Vector3 next;

			// if enemy is on visionsight
			if ( Vector3.Distance(transform.position, game.getPacman().transform.position) < sightDistance ) {
				next = NextNode( game.getPacman().transform.position );
			}
			// standard walking
			else {
				next = NextNode ( defaultSeekPosition );
			}

			if ( next != Vector3.zero ) {
				target = next;
				state = EnemyState.walking;
			}
		}

		// Decide to which node the enemy should move next,
		// it will run from the player or walk its defined path
		if ( state == EnemyState.frightened )
		{
			Vector3 next;

			// if pacman is on visionsight, flee from it
			if ( Vector3.Distance(transform.position, game.getPacman().transform.position) < sightDistance ) {
				next = NextNode( - game.getPacman().transform.position );
			}
			// standard walking
			else {
				next = NextNode ( defaultSeekPosition );
			}

			if ( next != Vector3.zero ) {
				target = next;
				state = EnemyState.fleeing;
			}
		}

		// it is moving to a path node
		if ( state == EnemyState.walking || state == EnemyState.fleeing )
		{
			rigidbody.AddForce(
				speed * Vector3.Normalize(target - transform.position)
			);
		}
	}

	Vector3 NextNode(Vector3 goal)
	{
		Vector3 current = Vector3.zero;
		Collider[] hits = Physics.OverlapSphere(transform.position, 1f);
		
		previous.Add( AbsPosition(target) );
		
		if (previous.Count > 10) {
			previous.RemoveAt(0);
		}
		
		foreach (Collider hit in hits) {
			// if the hit is not a node or if it was already visited, ignore it
			if ( hit.name != "node"
			    || previous.Contains( AbsPosition(hit.transform.position) ) ) {
				continue;
			}
			
			// verify if a wall is in the way, if true, ignore node
			bool possible = true;
			RaycastHit[] obstacles = Physics.RaycastAll(transform.position, Vector3.Normalize(hit.transform.position - transform.position), .5f);
			
			foreach (RaycastHit obstacle in obstacles ) {
				if ( obstacle.transform.name == "maze" || previous.Contains( AbsPosition(obstacle.transform.position)) ) {
					possible = false;
				}
			}
			
			if ( ! possible ) {
				continue;
			}
			
			// target is a node and it is reachable
			if ( Vector3.Distance(goal, current) > Vector3.Distance(goal, hit.transform.position)
			    || Random.value > .5f
			    || current == Vector3.zero ) {
				current = hit.transform.position;
			}
		}

		return current;
	}

	float AbsPosition(Vector3 position) {
		return position.x * 100 + position.y * 10 + position.z;
	}

	void OnPacmanFurious()
	{
		if ( state != EnemyState.frightened && state != EnemyState.fleeing ) {
			state = EnemyState.frightened;
		} else {
			state = EnemyState.idle;
		}

		anim.SetTrigger("furious");
	}

	void OnTriggerEnter ( Collider c )
	{
		if ( c.tag == "Player" ) {
			// pacman is in furious mode, ghost die
			if ( state == EnemyState.frightened || state == EnemyState.fleeing ) {
				StartCoroutine (Restart(EnemyState.frightened));
			}

			// it collides anyway
			c.SendMessage("OnCollisionWithGhost");
		}
		else if ( c.tag == "pathnode" && c.transform.position == target) {
			state
				= state == EnemyState.walking
				? EnemyState.idle
				: EnemyState.frightened
				;
		}
	}

}
