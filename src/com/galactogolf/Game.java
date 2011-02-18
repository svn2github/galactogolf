/* 
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.galactogolf;

import android.content.Context;

import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.views.GameRenderer;

/*
 * Main class that holds all the Game relevant objects together, e.g. the GameWorld domain model,
 * the GameRenderer that draws the graphics based onthe model, and the Game Thread that updates
 * the GameWorld
 */
public class Game {

	private Thread _thread;
	private boolean _running;
	private GameThread _gameThread;
	private GameRenderer _renderer;
	private GameWorld _world;

	public Game(GameWorld world) {
		_world = world;
	}

	public void setup(Context context, int viewWidth, int viewHeight,
			int gameWidth, int gameHeight) {
		_renderer = new GameRenderer(context, this, gameWidth, gameHeight);
		_gameThread = new GameThread(_world, _renderer);
	}

	/** Starts the game running. */
	public void start() {
		if (!_running) {
			// Now's a good time to run the GC.
			Runtime r = Runtime.getRuntime();
			r.gc();
			_thread = new Thread(_gameThread);
			_thread.setName("Game");
			_thread.start();
			_running = true;
		} else {
			// _gameThread.resumeGame();
		}
	}

	public GameWorld getWorld() {
		return _world;
	}

}
