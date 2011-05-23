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

package com.galactogolf.views;

import com.galactogolf.controllers.GameController;
import com.galactogolf.controllers.GameGestureListener;
import com.galactogolf.genericobjectmodel.GameWorld;

import android.content.Context;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Main game view, implementing GLSurfaceView
 * 
 */
public class GameView extends GLSurfaceView implements OnTouchListener {

	private GameRenderer _renderer;
	private ScaleGestureDetector scaleGestureDetector;
	private GestureDetector gestureScanner;
	private GameGestureListener gameGestureListener;


	private Context _context;

	public GameView(Context context, GameController controller, GameWorld world) {
		super(context);
		this._context = context;
		boolean bMupportMultiTouch = context
				.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		_renderer = world.GetRenderer();
		setRenderer(_renderer);

		this.SetController(controller);
		setFocusable(true);
		setFocusableInTouchMode(true);

		this.setOnTouchListener(this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;

	}

	public boolean onTouch(View v, MotionEvent event) {

		scaleGestureDetector.onTouchEvent(event);
		gestureScanner.onTouchEvent(event);
		gameGestureListener.onTouch(event);
		return true;
	}

	public void SetController(GameController controller) {
		gameGestureListener = new GameGestureListener(controller);
		scaleGestureDetector = new ScaleGestureDetector(_context,
				gameGestureListener);

		gestureScanner = new GestureDetector(gameGestureListener);

	}

}
