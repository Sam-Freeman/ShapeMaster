/** 
 * Created by Samuel Freeman
 */
package com.freeman.samuel.shapemaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu {
	// Controls the main menu, including the credits and how to play
	
	Stage stage;
	
	Texture menu;
	Button play;
	Button howTo;
	Button credits;
	
	Button back;
	
	Button buttons[] = new Button[3];
	
	public void load() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		menu = Assets.main_menu;
		
		play = new Button();
		howTo = new Button();
		credits = new Button();
		
		back = new Button();
		back.setSize(540, 80);
		back.setPosition(40,  720-450);
		
		play.setSize(540, 80);
		howTo.setSize(540, 80);
		credits.setSize(540, 80);
		play.setPosition(40, 720 - 250);
		howTo.setPosition(40, 720 - 350);
		credits.setPosition(40, 720 - 450);
		buttons[0] = play;
		buttons[1] = howTo;
		buttons[2] = credits;
		
		for (Button b1 : buttons) {
			stage.addActor(b1);
		}
		if (Game_Main.game_state == Game_Main.MAIN_MENU) {
			for (int i = 0; i < buttons.length; i++) {
				final int j = i;
				buttons[i].addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						super.clicked(event, x, y);
						justClicked(j);
					}
				});
			}
		} 
		if (Game_Main.game_state == Game_Main.CREDITS || Game_Main.game_state == Game_Main.HOW_TO_PLAY) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
				Game_Main.game_state = Game_Main.MAIN_MENU;
				load();
			}
		}
	}
	
	public void justClicked(int n) {
		System.out.println(n);
		if (Game_Main.game_state == Game_Main.MAIN_MENU) {
			switch (n) {
			case 0:
				Game_Main.game_state = Game_Main.PLAYING;
				break;
			case 1:
				Game_Main.game_state = Game_Main.HOW_TO_PLAY;
				break;
			case 2:
				Game_Main.game_state = Game_Main.CREDITS;
				break;
			default:
				break;
			}
			stage.dispose();
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(menu, 0, 1015, 1080, 720, 0, 0, 1080, 720, false, true);
		batch.draw(Assets.pcr_standing, 900, 1500, 64, 64, 128, 128, 1, 1, 180);
		batch.draw(Assets.psl_standing, 775, 1500, 64, 64, 128, 128, 1, 1, 180);
		batch.draw(Assets.ptr_standing, 640, 1500, 64, 64, 128, 128, 1, 1, 180);		
		batch.end();
		
		if (Game_Main.game_state == Game_Main.CREDITS) {
			load();
			batch.begin();
			batch.draw(Assets.credits, 0, 1015, 1080, 720, 0, 0, 1080, 720, false, true);
			batch.end();
		}
		
		if (Game_Main.game_state == Game_Main.HOW_TO_PLAY) {
			load();
			batch.begin();
			batch.draw(Assets.howTo, 0, 1015, 1080, 720, 0, 0, 1080, 720, false, true);
			batch.end();
		}
		
		stage.act();
	}
}
