/**
 *  Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster.gameobject;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.freeman.samuel.shapemaster.Assets;

public class Player extends GameObject {

	public int health;
	
	// Player's Projectiles
	ArrayList<Projectiles> projectiles;
	private final int MAX_NUMBER_PROJECTILES = 5;
	
	// Player State
	public int current_player_state;
	public final int PLAYER_STATE_WIZARD = 0;
	public final int PLAYER_STATE_SQUARE = 1;
	public final int PLAYER_STATE_TRIANGLE = 2;
	
	
	// Texture and animation holders
	private TextureRegion standing_texture;
	private TextureRegion standing_right;
	private TextureRegion standing_left;
	private Animation current_animation;
	private Animation right_animation;
	private Animation left_animation;
	
	// Determine whether moving
	private boolean isMoving;
	// Last Direction --> true = right, false = left
	private boolean lastDirection;
	
	// Elapsed Time for animation
	float elapsedTime;
	
	// Jumping
	boolean hasJumped;
	float startingY;
	float gravity;
	
	// Tilemap, for tile collision
	private TiledMapTileLayer collisionLayer;
	float tileWidth, tileHeight, tileScale;
	
	// Getting click location
	private Vector3 touched = new Vector3();
		
	public Player(TiledMapTileLayer collisionLayer, float tileScale, Vector2 position, Vector2 velocity, int width, int height, ArrayList<Projectiles> projectiles) {
		super(position, velocity, width, height);
		this.projectiles = projectiles;
		this.collisionLayer = collisionLayer;
		this.tileScale = tileScale;
		
		tileWidth = collisionLayer.getTileWidth() * tileScale;
		tileHeight = collisionLayer.getTileHeight() * tileScale;
		
		right_animation = Assets.p_c_r;
		left_animation = Assets.p_c_l;
		standing_right = Assets.pcr_standing;
		standing_left = Assets.pcl_standing;
		isMoving = false;
		
		health = 100; // Set's players starting and max health
		
		elapsedTime = 0f;
	}
	
	@Override
	public void init() {
		super.init();
		// Initialise player
		hasJumped = false;
		gravity = 0.5f;
		
		current_player_state = PLAYER_STATE_WIZARD;
		standing_texture = standing_right;
		current_animation = right_animation;
		
		lastDirection = true;
	}

	public void jump() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (!hasJumped) {
				gravity = 0.5f;
				startingY = position.y;
				velocity.y = -11.0f;
				hasJumped = true;
			}
		}
	}
	
	public void checkXPlayBoundaries() {
		// Check left, assumption that boundary is 0x
		if (position.x <= 0) {
			position.x = 0;
		}
		// Check right
		if (position.x >= (collisionLayer.getWidth() * tileScale) * collisionLayer.getTileWidth() - width) {
			position.x = (collisionLayer.getWidth() * tileScale) * collisionLayer.getTileWidth() - width;
		}
	}
	
	private void collisionDetection_x(float oldX) {
		
		boolean collisionX = false;
		
		// Moving Left
		if (velocity.x < 0) {
			// Top
			collisionX = collisionLayer.getCell((int)(getX() / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Middle
			if (!collisionX) // If yet to be a collision: 
				collisionX = collisionLayer.getCell((int)(getX() / tileWidth), (int)((getY() + height / 2) / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Bottom
			if (!collisionX) // If still no collision: 
				collisionX = collisionLayer.getCell((int)(getX() / tileWidth), (int)((getY() + height) / tileHeight)).getTile().getProperties().containsKey("Collidable");			
			
		} 
		
		// Moving right
		else if (velocity.x > 0) {
			// Top
			collisionX = collisionLayer.getCell((int)((getX() + width / 2) / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Middle
			if (!collisionX) 
				collisionX = collisionLayer.getCell((int)((getX() + width / 2) / tileWidth), (int)((getY() + height / 2) / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Bottom
			if (!collisionX) 
				collisionX = collisionLayer.getCell((int)((getX() + width / 2) / tileWidth), (int)((getY() + height) / tileHeight)).getTile().getProperties().containsKey("Collidable");
		}
		
		if (collisionX) {
			position.x = oldX;
			velocity.x = 0;
		}
	}
	
	private void collisionDetection_y(float oldY) {
		boolean collisionY = false; 
		// Moving up
		if (velocity.y < 0) {
			// Left
			collisionY = collisionLayer.getCell((int)(getX() / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Middle
			if (!collisionY) 
				collisionY = collisionLayer.getCell((int)((getX() + width / 2) / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Right
			if (!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + width) / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey("Collidable");
		} 
		
		// Moving down
		else if (velocity.y > 0) {
			// Left
			collisionY = collisionLayer.getCell((int)(getX() / tileWidth), (int)((getY() + height) / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Middle
			if (!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + width / 2) / tileWidth), (int)((getY() + height) / tileHeight)).getTile().getProperties().containsKey("Collidable");
			// Right
			if (!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + width / 2) / tileWidth), (int)((getY() + height) / tileHeight)).getTile().getProperties().containsKey("Collidable");
		}
				
		if (collisionY) {
			position.y = oldY;
			velocity.y = 0;
			hasJumped = false;
		} 
				
	}
	
	private void movement(int speed) {
		checkXPlayBoundaries();		
		jump();
		
		if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			velocity.x = speed;
			updateSprite();
			isMoving = true;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			velocity.x = speed * -1;
			updateSprite();
			isMoving = true;
		} else {
			isMoving = false;
		}
		
		// Stores X in case of collision
		float oldX = getX();
		// Update X
		position.x += velocity.x;
		// Check for collision
		collisionDetection_x(oldX);
		// Don't move when no input
		velocity.x = 0;

		// Stores Y in case of collision
        float oldY = getY();
		// Update Y
		velocity.y += gravity;
        position.y += velocity.y;
        // Check collision
		collisionDetection_y(oldY);
	}
	
	public void updateSprite() {
		// Checks for direction, updates sprite accordingly
		if (velocity.x > 0) {
			current_animation = right_animation;
			standing_texture = standing_right;
			lastDirection = true;
		}
		if (velocity.x < 0) {
			current_animation = left_animation;
			standing_texture = standing_left;
			lastDirection = false;
		}
		if (velocity.x == 0) {
			if (lastDirection) {
				current_animation = right_animation;
				standing_texture = standing_right;
			} else {
				current_animation = left_animation;
				standing_texture = standing_left;
			}
		}
		
	}
	
	public void shoot(float x, float y) {
		if (projectiles.size() == MAX_NUMBER_PROJECTILES) return;
		projectiles.add(new Projectiles(new Vector2(getX(), getY()), 32, 32, x, y, current_player_state));
	}
	
	public void shapeShift() {
		// Allows user to change between wizards
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			current_player_state++;
			if (current_player_state > 2) current_player_state = 0;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			current_player_state--;
			if (current_player_state < 0) current_player_state = 2;
		}
		checkShapeShift();
	}
	
	public void checkShapeShift() {
		// Checks current player state, sets animations and textures accordingly
		switch (current_player_state) {
		case PLAYER_STATE_WIZARD:
			right_animation = Assets.p_c_r;
			left_animation = Assets.p_c_l;
			standing_right = Assets.pcr_standing;
			standing_left = Assets.pcl_standing;
			updateSprite();
			break;
		case PLAYER_STATE_SQUARE:
			right_animation = Assets.p_s_r;
			left_animation = Assets.p_s_l;
			standing_right = Assets.psr_standing;
			standing_left = Assets.psl_standing;		
			updateSprite();
			break;
		case PLAYER_STATE_TRIANGLE:
			right_animation = Assets.p_t_r;
			left_animation = Assets.p_t_l;
			standing_right = Assets.ptr_standing;
			standing_left = Assets.ptl_standing;		
			updateSprite();
			break;
		default:
			break;
		}
	}
	
	public void update(OrthographicCamera camera) {
		// Player's Update method
		super.update();
		shapeShift();
		movement(5);
		if (Gdx.input.justTouched()) {
			camera.unproject(touched.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			shoot(touched.x, touched.y);
		}
		
		// Update Projectiles
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(Gdx.graphics.getDeltaTime());
			if (projectiles.get(i).shouldRemove()) {
				projectiles.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		// Selects if standing or moving
		if (isMoving) {
			batch.draw(current_animation.getKeyFrame(elapsedTime, true), position.x, position.y, width / 2, height / 2, width * 1.5f, height * 1.5f, 1, 1, 180);
		} else {
			batch.draw(standing_texture, position.x, position.y, width / 2, height / 2, width * 1.5f, height * 1.5f, 1, 1, 180);
		}
		
		// Projectiles
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).draw(batch, elapsedTime);
		}
	}
}
