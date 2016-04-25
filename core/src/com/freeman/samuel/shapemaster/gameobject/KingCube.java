/**
 *  Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.freeman.samuel.shapemaster.Assets;

public class KingCube extends GameObject {
	// Currently only animates a cube up and down
	// Intended to need protecting, as attacked by shapes 
	
	//private int health;
	float elapsedTime;
	
	public KingCube(Vector2 position, int width, int height) {
		super(position, width, height);
		//health = 100;
		elapsedTime = 0;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		elapsedTime += Gdx.graphics.getDeltaTime();
		batch.draw(Assets.king_updown.getKeyFrame(elapsedTime, true), position.x, position.y, width / 2, height / 2, width, height, 1, 1, 180);
	}
}
