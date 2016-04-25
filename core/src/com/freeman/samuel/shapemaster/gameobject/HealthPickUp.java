/**
 * Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.freeman.samuel.shapemaster.Assets;
import com.freeman.samuel.shapemaster.Game_Main;

public class HealthPickUp extends GameObject {
// Randomly spawns a health pick up, and falls to ground
	
	private int healthAmount;
	private int gravity = 5;
	
	// Check for time alive
	private float max_life;
	private float alive_timer;
	private boolean remove;
	
	// Allows access to collision
	TiledMapTileLayer collision;
	// Allows access to player health
	Player player;
	
	public HealthPickUp(Vector2 position, int width, int height, TiledMapTileLayer collision, Player player) {
		super(position, width, height);
		
		this.collision = collision;
		this.player = player;
		
		// Sets time health is alive
		max_life = 20f;
		alive_timer = 0;
		
		// Randomly selects amount of health gained --> 0 - 10
		healthAmount = Game_Main.random.nextInt(11);
		
		remove = false;
	}
	
	public void pickUp() {
		// Adds health to player, accounts for max health
		player.health += healthAmount;
		if (player.health > 100) player.health = 100;
		remove = true;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void checkCollision(float oldY) {
		boolean collided = false;
		float tileWidth = collision.getTileWidth() * 1.5f, tileHeight = collision.getTileHeight() * 1.5f;
		position.y += gravity;
		collided = collision.getCell((int)(getX() / tileWidth), (int)((getY() + height) / tileHeight)).getTile().getProperties().containsKey("Collidable");
		if (collided) {
			position.y = oldY;
			gravity = 0;
		}
	}
	
	public void update(float deltaTime) {
		super.update();
		
		// Checks for collision
		float oldY = getY();
		checkCollision(oldY);
		
		// Check if should be alive --> if not, remove
		alive_timer += deltaTime;
		if (alive_timer > max_life) {
			remove = true;
		}
		
		// Check if picked up by player
		if (player.boundingBox.overlaps(this.boundingBox)) pickUp();		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		// Draws health pick up
		super.draw(batch);
		batch.begin();
		batch.draw(Assets.healthPickup, position.x, position.y, width / 2, height / 2, width, height, 1, 1, 180);
		batch.end();
	}

}
