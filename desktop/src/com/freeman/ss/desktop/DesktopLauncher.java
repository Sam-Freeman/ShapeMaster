package com.freeman.ss.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.freeman.samuel.shapemaster.Game_Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game_Main(), config);
		config.width = 1080;
		config.height = 720;
		config.resizable = false;
		config.title = "Shape Master";
	}
}
