/**
 *  Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// Base class for in-game objects
public class GameObject {
	
	protected Vector2 position;
	protected Vector2 velocity;
	
	protected Rectangle boundingBox;
	
	protected int width;
	protected int height;
	
	// Constructor for object with no movement
	public GameObject(Vector2 position, int width, int height) {
		this.position = position;
		this.width = width;
		this.height = height;
	}
	// Constructor for object with movement
	public GameObject(Vector2 position, Vector2 velocity, int width, int height) {
		this.position = position;
		this.velocity = velocity;
		this.width = width;
		this.height = height;
	}
	
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	
	// Creates initial bounding box for collisions
	public void init() {
		boundingBox = new Rectangle(position.x, position.y, width, height);
	}
	// Updates bounding box based on movement of object
	public void update() {
		boundingBox = new Rectangle(position.x, position.y, width, height);		
	}
	// Currently no base draw needed, here just in case.
	public void draw(SpriteBatch batch) {
		
	}
}
