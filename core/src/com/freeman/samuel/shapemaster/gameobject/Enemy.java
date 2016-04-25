/** 
 * Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster.gameobject;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.freeman.samuel.shapemaster.Assets;
import com.freeman.samuel.shapemaster.CollisionAnimation;
import com.freeman.samuel.shapemaster.Game_Main;

public class Enemy extends GameObject {

	// Creates array of textures for the different enemy types
	static public TextureRegion enemyTextures[] = new TextureRegion[3];
	static public void initTex() {
		enemyTextures[0] = Assets.enemy_circle;
		enemyTextures[1] = Assets.enemy_square;
		enemyTextures[2] = Assets.enemy_triangle;
	}
	// Texture for current enemy, int to store enemy type
	private TextureRegion current_enemy;
	private int current_type;
	
	// Variables for max life
	private float max_lifeSpan;
	private float time_alive;
	
	// Basic enemy needs
	private int health;
	private boolean isAlive;
	private int damage;
	
	// Allows for access to required variables
	ArrayList<Projectiles> projectiles;
	Player player;
	
	// AI
	Rectangle detectionRectangle_large;
	Rectangle detectionRectangle_small;
	Rectangle movementRectangle;
	
	boolean hasChecked;
	boolean hasDashed;
	float player_x, player_y;
	
	// Rendering
	float elapsedTime = 0;
	boolean finishedAnimation;
	public boolean render;	
	CollisionAnimation ca;
	
	public Enemy(Vector2 position, Vector2 velocity, int width, int height, ArrayList<Projectiles> projectiles, Player player) {
		super(position, velocity, width, height);
		
		this.projectiles = projectiles;
		this.player = player;
		
		// Sets max life
		max_lifeSpan = 20;
		time_alive = 0;
		
		// Controls alive
		isAlive = true;
		health = 100;	
		
		// Sets type, randomly selected
		current_type = Game_Main.random.nextInt(3);
		current_enemy = enemyTextures[current_type];
		
		// AI
		detectionRectangle_large = new Rectangle(position.x - (500 - player.width) / 2 , position.y - (500 - player.width) / 2, 500, 500);
		detectionRectangle_small = new Rectangle(position.x - (100 - player.width) / 2, position.y - (100 - player.width) / 2, 100, 100);
		movementRectangle = new Rectangle(position.x - (500 - width) / 2, position.y - (500 - player.width) / 2, 500, 500);
		
		hasChecked = false;
		hasDashed = false;
		
		// Randomly assigns attack damage --> 0 to 10
		damage = Game_Main.random.nextInt(11);
		
		// Rendering
		elapsedTime = 0;
		finishedAnimation = false;
		
		render = true;
		
		ca = new CollisionAnimation(position, Assets.sparkly, width, height);
	}
	
	public boolean hitByPlayer(Projectiles p) {
		// checks if the projectile is the same type as the enemy --> yes - true, no - false
		if (p.type == current_type)	{
			Game_Main.score++;
			isAlive = false;
			return true;
		}
		else return false;
	}
	
	public boolean dead() { return isAlive; }
	
	@Override
	public void init() {
		super.init();
	}

	public void calculatePlayerPosition(boolean attack) {
		player_x = player.getX();
		player_y = player.getY();
		if (attack) hasChecked = true;
	}
	
	public void resetMovement() {
		// resets the movement rectangle of the enemy
		movementRectangle = new Rectangle(position.x - (500 - width) / 2, position.y - (500 - player.width) / 2, 500, 500);
	}
	
	public void locatePlayer(SpriteBatch batch, float deltaTime) {
		// Updates small and large rectangles, searching for player
		detectionRectangle_large = new Rectangle(position.x - (500 - player.width) / 2 , position.y - (500 - player.width) / 2, 500, 500);
		detectionRectangle_small = new Rectangle(position.x - (100 - player.width) / 2, position.y - (100 - player.width) / 2, 100, 100);
		
		// If player is in largest rectangle
		if (detectionRectangle_large.overlaps(player.boundingBox)) {
			followPlayer(deltaTime);
			resetMovement();
			// If player is in smallest rectangle, attacks player
			if (detectionRectangle_small.overlaps(player.boundingBox)) {
				dashAttackPlayer(batch, deltaTime);
				if (hasDashed) followPlayer(deltaTime);
			}
		} else { 
			// Moves enemy based on their movement rectangle, and checks whether edge of screen
			if (position.x <= movementRectangle.x) velocity.x *= -1;
			if (position.x >= movementRectangle.x + movementRectangle.width) velocity.x *= -1;
			if (position.x <= 0) velocity.x *= -1;
			if (position.x >= 4080 - width) velocity.x *= -1;
			position.x += velocity.x;
		}
	}
	
	public void followPlayer(float deltaTime) {
		// Checks for player position, moves towards it, same math as projectile movement
		calculatePlayerPosition(false);
		float dx, dy;
		dx = player_x - position.x;
		dy = player_y - position.y;
		
		double dl = Math.sqrt(dx * dx + dy * dy);
		dx /= dl;
		dy /= dl;
		
		position.x += dx * 200 * deltaTime;
		position.y += dy * 200 * deltaTime;
	}
	
	public void dashAttackPlayer(SpriteBatch batch, float deltaTime) {
		// Re-checks player position, increases speed (dashes) to player, attacking.
		if (!hasChecked) {
			calculatePlayerPosition(true);
		}
		float dx, dy;
		dx = player_x - position.x;
		dy = player_y - position.y;
	
		double dl = Math.sqrt(dx * dx + dy * dy);
		dx /= dl;
		dy /= dl;
		
		position.x += dx * 400 * deltaTime;
		position.y += dy * 400 * deltaTime;
		hasDashed = true;
		
		// If hits the player
		if (this.boundingBox.overlaps(player.boundingBox)) {
			hitPlayer(batch, deltaTime);
			// enemy is still technically there, just not drawn or attacking (enables full animation to play)
			render = false;
			playCollisionAnimation(batch, deltaTime);			
			if (finishedAnimation) {
				isAlive = false;
			}
		}
	}
	
	private void hitPlayer(SpriteBatch batch, float deltaTime) {
		if (render) {
			player.health -= damage;
			if (player.health < 0) player.health = 0;
		}
	}
	
	private void playCollisionAnimation(SpriteBatch batch, float deltaTime) {
		// Plays collision animation effect
		ca.Animation(batch, deltaTime);
		if (ca.isComplete) finishedAnimation = true;
	}
	
	public void update(SpriteBatch batch, float deltaTime) {
		super.update();
		locatePlayer(batch, deltaTime);
		
		// Alive logic
		time_alive += deltaTime;
		if (time_alive > max_lifeSpan) {
			isAlive = false;
		}
		if (health <= 0) {
			isAlive = false;
		}
		
		// Checks for collision with projectiles --> passes to function to check if same type
		for (Projectiles p : projectiles) {
			if (p.boundingBox.overlaps(boundingBox)) {
				if (hitByPlayer(p)) p.setRemove(true);
			}
		}
	}
	
	public void draw(SpriteBatch batch, OrthographicCamera camera) {
		super.draw(batch);
		// Checks if 'alive', then draws
		if (render) {
			batch.begin();
			batch.draw(current_enemy, position.x, position.y, width / 2, height / 2, width, height, 1, 1, 180);
			batch.end();
		}
	}

}
