/** 
 * Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.freeman.samuel.shapemaster.gameobject.Enemy;
import com.freeman.samuel.shapemaster.gameobject.HealthPickUp;
import com.freeman.samuel.shapemaster.gameobject.KingCube;
import com.freeman.samuel.shapemaster.gameobject.Player;
import com.freeman.samuel.shapemaster.gameobject.Projectiles;

public class Game_Main extends ApplicationAdapter {
	
	// Important
	SpriteBatch batch;
	OrthographicCamera camera;
	public static Random random;
	public static int score;
	float elapsed;
	
	// Map
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	final int mapWidth = 4080;
	float tileScale;
	
	// Player controlled
	private Player player;
	private ArrayList<Projectiles> projectiles;
	
	// Enemy
	private ArrayList<Enemy> enemies;
	private final int MAX_ENEMIES = 20;
	float timeSinceSpawn;
	
	// Health pick up
	private ArrayList<HealthPickUp> health;
	private final int MAX_HEALTH = 2;
	float timeSinceLastHealth;
	
	// King Cube
	private KingCube king;
	
	// On screen text
	GlyphLayout layout;
	public static int heartSelector;
	Vector2 heartPosition;
	BitmapFont healthFont;
	BitmapFont scoreFont;
	
	// Game State
	public static int game_state;
	public static final int MAIN_MENU = 0;
	public static final int PLAYING = 1;
	public static final int GAME_OVER = 2;
	public static final int HOW_TO_PLAY = 3;
	public static final int CREDITS = 4;
	
	// Main Menu
	MainMenu menu;
	
	// Music
	int musicSelector;
	
	@Override
	public void create () {
		// Important
		batch = new SpriteBatch();
		random = new Random();
		Assets.Load();
		createGame();
	}

	public void createGame() {
		// Important
		score = 0;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		elapsed = 0;
		
		// Enemy
		Enemy.initTex();
		enemies = new ArrayList<Enemy>();
		timeSinceSpawn = 0;
		
		// Health pick up
		health = new ArrayList<HealthPickUp>();
		timeSinceLastHealth = 0;
		heartSelector = 9;
		
		// King Cube
		king = new KingCube(new Vector2(1632, 885), 128, 128);
		
		// Map
		Parameters parameters = new Parameters();
		parameters.flipY = false;
		tileScale = 1.5f;
		map = new TmxMapLoader().load("Main_Map.tmx", parameters);
		renderer = new OrthogonalTiledMapRenderer(map, tileScale);
		
		// Player controlled
		projectiles = new ArrayList<Projectiles>();
		player = new Player((TiledMapTileLayer) map.getLayers().get(0), tileScale, new Vector2(Gdx.graphics.getWidth() / 2, 1375), new Vector2(0,0), 64, 64, projectiles);
		player.init();
		
		// set initial camera position
		camera.position.set(new Vector3(player.getX(), player.getY(), 0));
		
		// Main Menu
		game_state = MAIN_MENU;
		menu = new MainMenu();
		if (game_state == MAIN_MENU) {
			menu.load();
		}
		
		// On screen text
		heartPosition = new Vector2(0, player.getY() - (Gdx.graphics.getHeight() / 2));
		layout = new GlyphLayout();
		healthFont = new BitmapFont(Gdx.files.internal("score_font.fnt"), true);	
		healthFont.getData().setScale(0.4f);
		layout.setText(healthFont, Integer.toString(player.health));
		scoreFont = new BitmapFont(Gdx.files.internal("score_font.fnt"), true);
		scoreFont.getData().setScale(1f);
		
		// Music
		musicSelector = random.nextInt(Assets.music.length);
	}
	
	// Method to draw texture flipped
	public static void DrawImageFlipped(SpriteBatch batchTex, Texture texture, float x, float y) {
		batchTex.draw(texture, x, y, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), false, true);
	}
	
	public void SpawnEnemy() {
		if (enemies.size() == MAX_ENEMIES) return;
		int randPosition;
		int randY;
		randY = 1000 + random.nextInt(1400 - 1000 + 1);
		randPosition = random.nextInt(mapWidth - 64);
		
		// Checks whether the spawn location collides with tiles --> if yes, reassigns 
		TiledMapTileLayer allow = (TiledMapTileLayer) map.getLayers().get(0);
		float tileWidth = allow.getTileWidth() * tileScale;
		boolean colliding = false;
		
		colliding = allow.getCell((int)(randPosition / tileWidth), (int)(randY / tileWidth)).getTile().getProperties().containsKey("notAllowed");
		
		while (colliding) {
			System.out.println("looped");
			randY = 1000 + random.nextInt(1400 - 1000 + 1);
			randPosition = random.nextInt(mapWidth - 64);
			colliding = allow.getCell((int)(randPosition / tileWidth), (int)(randY / tileWidth)).getTile().getProperties().containsKey("notAllowed");
		}
		enemies.add(new Enemy(new Vector2(randPosition, randY), new Vector2(3, 3), 50, 50, projectiles, player));
	}
	
	public void SpawnHealth() {
		// Spawns health randomly, no need to worry about already colliding, as above everything and will drop down
		if (health.size() == MAX_HEALTH) return;
		health.add(new HealthPickUp(new Vector2(random.nextInt(mapWidth), 900), 32, 32, (TiledMapTileLayer) map.getLayers().get(0), player));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		map.dispose();
		renderer.dispose();
		healthFont.dispose();
		scoreFont.dispose();
	}
	
	public void HeartSelector() {
		// Selects texture region based on how much health left
		if (player.health > 90) heartSelector = 9;
		if (player.health > 80 && player.health < 90) heartSelector = 8;
		if (player.health > 70 && player.health < 80) heartSelector = 7;
		if (player.health > 60 && player.health < 70) heartSelector = 6;
		if (player.health > 50 && player.health < 60) heartSelector = 5;
		if (player.health > 40 && player.health < 50) heartSelector = 4;
		if (player.health > 30 && player.health < 40) heartSelector = 3;
		if (player.health > 20 && player.health < 30) heartSelector = 2;
		if (player.health > 10 && player.health < 20) heartSelector = 1;
		if (player.health > 0 && player.health < 10) heartSelector = 0;
	}
	
	public void MusicSelector() {
		// Randomly selects music from the array
		musicSelector = random.nextInt(Assets.music.length);
	}
	
	// Split render into different methods, as easy to control
	public void render_playing() {
		// Player
		player.update(camera);
		
		if (player.health <= 0) {
			game_state = GAME_OVER;
		}
		// Camera control, following player
		if (player.getX() > Gdx.graphics.getWidth() / 2 && player.getX() < mapWidth - Gdx.graphics.getWidth() / 2) {
			camera.position.set(player.getX(), player.getY(), 0);
			heartPosition.x = player.getX() - Gdx.graphics.getWidth() / 2;
			heartPosition.y = player.getY() - Gdx.graphics.getHeight() / 2;
		} else if (player.getX() < Gdx.graphics.getWidth() / 2) {
			camera.position.set(Gdx.graphics.getWidth() / 2, player.getY(), 0);
			heartPosition.x = 0;
			heartPosition.y = player.getY() - Gdx.graphics.getHeight() / 2;
		} else if (player.getX() > mapWidth - Gdx.graphics.getWidth() / 2) {
			camera.position.set(mapWidth - Gdx.graphics.getWidth() / 2, player.getY(), 0);
			heartPosition.x = mapWidth - Gdx.graphics.getWidth();
			heartPosition.y = player.getY() - Gdx.graphics.getHeight() / 2;
		}
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		// Spawn Control
		timeSinceSpawn += Gdx.graphics.getDeltaTime();
		if (timeSinceSpawn >= 2) {
			SpawnEnemy();
			timeSinceSpawn = 0;
		}
		timeSinceLastHealth += Gdx.graphics.getDeltaTime();
		if (timeSinceLastHealth >= 18) {
			SpawnHealth();
			timeSinceLastHealth = 0;
		}
		
		// Drawing Background
		batch.begin();
		DrawImageFlipped(batch, Assets.background, heartPosition.x, heartPosition.y);
		batch.end();
		
		// Drawing Map
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		// draw heart/health and score
		HeartSelector();
		layout.setText(healthFont, Integer.toString(player.health));
		batch.draw(Assets.hearts[heartSelector], heartPosition.x, heartPosition.y, 32, 32, 64, 64, 1, 1, 180);
		healthFont.draw(batch, Integer.toString(player.health), heartPosition.x + 32 - (layout.width / 2), heartPosition.y + (32 - layout.height));
		layout.setText(scoreFont, "You have Killed " + score + " Shapes");
		scoreFont.draw(batch, "You have Killed " + score + " Shapes", heartPosition.x + 1080 - layout.width, heartPosition.y + 20);
		
		// Draw king and player
		king.draw(batch);
		player.draw(batch);
		
		batch.end();
		
		// Draw alive enemies
		for (int x = 0; x < enemies.size(); x++) {
			enemies.get(x).draw(batch, camera);
		}
		
		// Update alive enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(batch, Gdx.graphics.getDeltaTime());
			if (!enemies.get(i).dead()) {
				enemies.remove(i);
				i--;
			}
		}
		
		// Draw health pick ups
		for (int s = 0; s < health.size(); s++) {
			health.get(s).draw(batch);
		}
		
		// Update health pick ups
		for (int s = 0; s < health.size(); s++) {
			health.get(s).update(Gdx.graphics.getDeltaTime());
			if (health.get(s).shouldRemove()) {
				health.remove(s);
				s--;
			}
		}
	}

	public void render_gameOver() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			createGame();
			game_state = MAIN_MENU;
		}
		// Draws background
		batch.begin();
		DrawImageFlipped(batch, Assets.background, heartPosition.x, heartPosition.y);
		batch.end();
		
		// Map
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		// Hearts/health
		HeartSelector();
		layout.setText(healthFont, Integer.toString(player.health));
		batch.draw(Assets.hearts[heartSelector], heartPosition.x, heartPosition.y, 32, 32, 64, 64, 1, 1, 180);
		healthFont.draw(batch, Integer.toString(player.health), heartPosition.x + 32 - (layout.width / 2), heartPosition.y + (32 - layout.height));
				
		// draw player and king
		king.draw(batch);
		player.draw(batch);
		
		// Concluding view
		layout.setText(scoreFont, "You Killed: " + score + " Shapes");
		scoreFont.draw(batch, "You Killed: " + score + " Shapes", heartPosition.x + Gdx.graphics.getWidth() / 2 - (layout.width / 2), heartPosition.y + Gdx.graphics.getHeight() / 2 - (layout.height / 2));
		layout.setText(scoreFont, "Press Space To Continue");
		scoreFont.draw(batch, "Press Space to Continue", heartPosition.x + Gdx.graphics.getWidth() / 2 - (layout.width / 2), heartPosition.y + Gdx.graphics.getHeight() / 2 - (layout.height / 2) + 50);

		batch.end();
		
		// Draw alive enemies --> not moving
		for (int x = 0; x < enemies.size(); x++) {
			enemies.get(x).draw(batch, camera);
		}
		// Draw health pick ups
		for (int s = 0; s < health.size(); s++) {
			health.get(s).draw(batch);
		}
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Music control
		Assets.music[musicSelector].play();
		if (!Assets.music[musicSelector].isPlaying()) MusicSelector();
		
		// Game States
		// Menus
		if (game_state == MAIN_MENU) {
			menu.render(batch);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
		}
		
		if (game_state == CREDITS) {
			menu.render(batch);
		}
		
		if (game_state == HOW_TO_PLAY) {
			menu.render(batch);
		}
		// Game Play
		if (game_state == PLAYING) {
			render_playing();
		}
		
		if (game_state == GAME_OVER) {
			render_gameOver();
		}
	}
}
