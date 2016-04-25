/**
 * Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CollisionAnimation {
	// Class to play effect on collision
	// Created in case needed for more than just player hit by enemy
	
	Vector2 position;
	Animation animation;
	int width, height;
	static float elapsedTime;
	public boolean isComplete;
	
	public CollisionAnimation(Vector2 position, Animation animation, int width, int height) {
		this.position = position;
		this.animation = animation;
		this.width = width;
		this.height = height;
		elapsedTime = 0;
		isComplete = false;
	}
	
	public void update(float deltaTime) {
		elapsedTime += deltaTime;
	}
	
	public void Animation(SpriteBatch batch, float deltaTime) {
		update(deltaTime);
		batch.begin();
		batch.draw(animation.getKeyFrame(elapsedTime, false), position.x, position.y, width / 2, height / 2, width, height, 1, 1, 180);
		if (animation.isAnimationFinished(elapsedTime)) isComplete = true;
		batch.end();
	}
}
