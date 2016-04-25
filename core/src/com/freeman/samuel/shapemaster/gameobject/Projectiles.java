/**
 *  Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster.gameobject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.freeman.samuel.shapemaster.Assets;

// Base projectile class --> only have one kind of projectile at the moment though.
public class Projectiles extends GameObject {

	// Floats to determine lives of projectiles
	private float max_life;
	private float alive_timer;
	// Whether the projectile should be alive
	private boolean remove;
	
	float dx, dy;
	
	int type;
	
	// Damage constant at the moment, 
	int damage;
	
	public Projectiles(Vector2 position, int width, int height, float touchedX, float touchedY, int type) {
		super(position, width, height);
		// Type assigned 
		this.type = type;
		
		// Set alive time
		max_life = 1.5f;
		alive_timer = 0;
		
		// Calculate distance between touch location and player position.
		dx = touchedX - position.x;
		dy = touchedY - position.y;
		double dl = Math.sqrt(dx * dx + dy * dy);
		// Normalize vectors
		dx /= dl;
		dy /= dl;	
		
		// Set damage --> constant for now.
		damage = 50;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update(float deltaTime) {
		super.update();
		// Update projectile position
		float speed = 600;
		position.x += dx * speed * deltaTime;
		position.y += dy * speed * deltaTime;
		
		// Determine whether the projectile should be alive, if not --> remove
		alive_timer += deltaTime;
		if (alive_timer > max_life) {
			setRemove(true);
		}
	}
	
	public void draw(SpriteBatch batch, float elapsedTime) {
		// Animates the projectile based upon its type
		Animation animation = null;
		switch (type) {
		case 0:
			animation = Assets.projectile_circle;
			break;
		case 1: 
			animation = Assets.projectile_square;
			break;
		case 2:
			animation = Assets.projectile_triangle;
			break;
		default:
			break;
		}
		batch.draw(animation.getKeyFrame(elapsedTime, true), position.x, position.y, width / 2, height / 2, width * 1.5f, height * 1.5f, 1, 1, 180);		
	}
}
