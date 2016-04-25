/** 
 * Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
// Class to load Assets
public class Assets {
	// Textures 
	public static Texture background;
	public static Texture player_spritesheet;
	public static Texture projectile_spritesheet;
	public static Texture king_cube_spritesheet;
	public static Texture collision_spritesheet;
	public static Texture sparkles;
	public static Texture items;
	public static Texture enemies;
	public static Texture main_menu;
	public static Texture credits;
	public static Texture howTo;
	
	// Texture regions
	public static TextureRegion pcr_standing;
	public static TextureRegion pcl_standing;
	public static TextureRegion psr_standing;
	public static TextureRegion psl_standing;
	public static TextureRegion ptr_standing;
	public static TextureRegion ptl_standing;
	
	public static TextureRegion[] hearts = new TextureRegion[10];
	public static TextureRegion healthPickup;
	
	public static TextureRegion enemy_circle;
	public static TextureRegion enemy_square;
	public static TextureRegion enemy_triangle;
	
	// Animations
	public static Animation p_c_r;
	public static Animation p_c_l;
	public static Animation p_s_r;
	public static Animation p_s_l;
	public static Animation p_t_r;
	public static Animation p_t_l;
	
	public static Animation projectile_circle;
	public static Animation projectile_square;
	public static Animation projectile_triangle;
	
	// Arrays of texture regions for animations
	public static Animation king_updown;
	static private TextureRegion kingTex[] = new TextureRegion[9];
	
	public static Animation collision;
	static private TextureRegion collisionTex[] = new TextureRegion[6];
	
	public static Animation sparkly;
	public static TextureRegion sparklesTex[] = new TextureRegion[12];
	
	// Music
	public static Music music[] = new Music[6];
	
	// Asset loaders
	private static Music loadMusic(String filePath) {
		return Gdx.audio.newMusic(Gdx.files.internal(filePath));
	}
	
	private static Texture loadTexture(String filePath) {
		return new Texture(Gdx.files.internal(filePath));
	}

	public static void Load() {	
		// Loads textures
		background = loadTexture("background.png");
		player_spritesheet = loadTexture("Player_SpriteSheet.png");
		projectile_spritesheet = loadTexture("Projectiles.png");
		king_cube_spritesheet = loadTexture("KingCubeSpriteSheet.png");
		collision_spritesheet = loadTexture("collisionAnimation.png");
		sparkles = loadTexture("Sparkles.png");
		items = loadTexture("SpriteSheet_Main.png");
		enemies = loadTexture("Enemies.png");
		main_menu = loadTexture("MainMenu.png");
		credits = loadTexture("Credits.png");
		howTo = loadTexture("HowToPlay.png");
		
		// Selects texture regions from textures
		pcl_standing = new TextureRegion(player_spritesheet, 0, 0, 32, 32);
		pcr_standing = new TextureRegion(player_spritesheet, 32, 0, 32, 32);
		psl_standing = new TextureRegion(player_spritesheet, 64, 0, 32, 32);
		psr_standing = new TextureRegion(player_spritesheet, 96, 0, 32, 32);
		ptl_standing = new TextureRegion(player_spritesheet, 128, 0, 32, 32);
		ptr_standing = new TextureRegion(player_spritesheet, 160, 0, 32, 32);
		
		enemy_circle = new TextureRegion(enemies, 0, 0, 32, 32);
		enemy_square = new TextureRegion(enemies, 32, 0, 32, 32);
		enemy_triangle = new TextureRegion(enemies, 64, 0, 32, 32);
		
		healthPickup = new TextureRegion(items, 0, 192, 32, 32);
		
		// Creates animations from texture regions
		p_c_l = new Animation(0.2f, pcl_standing, new TextureRegion(player_spritesheet, 0, 32, 32, 32),
				new TextureRegion(player_spritesheet, 0, 64, 32, 32));
		p_c_r = new Animation(0.2f, pcr_standing, new TextureRegion(player_spritesheet, 32, 32, 32, 32),
				new TextureRegion(player_spritesheet, 32, 64, 32, 32));
		p_s_l = new Animation(0.2f, psl_standing, new TextureRegion(player_spritesheet, 64, 32, 32, 32),
				new TextureRegion(player_spritesheet, 64, 64, 32, 32));
		p_s_r = new Animation(0.2f, psr_standing, new TextureRegion(player_spritesheet, 96, 32, 32, 32),
				new TextureRegion(player_spritesheet, 96, 64, 32, 32));
		p_t_l = new Animation(0.2f, ptl_standing, new TextureRegion(player_spritesheet, 128, 32, 32, 32),
				new TextureRegion(player_spritesheet, 128, 64, 32, 32));
		p_t_r = new Animation(0.2f, ptr_standing, new TextureRegion(player_spritesheet, 160, 32, 32, 32),
				new TextureRegion(player_spritesheet, 160, 64, 32, 32));
		
		projectile_circle = new Animation(0.2f, new TextureRegion(projectile_spritesheet, 0, 0, 32, 32),
				new TextureRegion(projectile_spritesheet, 32, 0, 32, 32));
		projectile_square = new Animation(0.2f, new TextureRegion(projectile_spritesheet, 0, 32, 32, 32),
				new TextureRegion(projectile_spritesheet, 32, 32, 32, 32));
		projectile_triangle = new Animation(0.2f, new TextureRegion(projectile_spritesheet, 0, 64, 32, 32),
				new TextureRegion(projectile_spritesheet, 32, 64, 32, 32));
		
		// Creates texture regions and animation in one step, using for loop
		for (int i = 0; i < kingTex.length; i++) {
			kingTex[i] = new TextureRegion(king_cube_spritesheet, i * 64, 0, 64, 64);
		}
		king_updown = new Animation(0.2f, kingTex);
		
		for (int i = 0; i < collisionTex.length; i++) {
			collisionTex[i] = new TextureRegion(collision_spritesheet, i * 32, 0, 32, 32);
		}
		collision = new Animation (50f, collisionTex);
		
		for (int i = 0; i < sparklesTex.length; i++) {
			sparklesTex[i] = new TextureRegion(sparkles, i * 32, 0, 32, 32);
		}
		sparkly = new Animation(0.1f, sparklesTex);
		
		for (int i = 0; i < hearts.length; i++) {
			hearts[i] = new TextureRegion(items, i * 32, 160, 32, 32);
		}
		
		// Creates array of music
		for (int i = 0; i < music.length; i++) {
			music[i] = loadMusic("music" + i + ".mp3");
		}
	}
}
