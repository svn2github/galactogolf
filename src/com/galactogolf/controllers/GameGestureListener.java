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

package com.galactogolf.controllers;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

/*
 * Registered by the game activiy to listen for gesture events and pass them to
 * the controller
 */
public class GameGestureListener implements OnGestureListener,
		OnScaleGestureListener {

	private GameController _controller;

	public GameGestureListener(GameController controller) {
		this._controller = controller;
	}

	/*
	 * ----- Single finger touch detection methods -----
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.
	 * view.MotionEvent)
	 */
	public boolean onSingleTapUp(MotionEvent e) {
		_controller.onSingleTapUp(e.getX(), e.getY());
		return true;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		_controller.onScroll(distanceX, distanceY);
		return true;
	}

	public void onLongPress(MotionEvent e) {
		_controller.onLongPress(e.getX(), e.getY());
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

	public boolean onDown(MotionEvent e) {
		_controller.onDown(e.getX(), e.getY());
		return true;
	}

	/*
	 * ------ Scale gesture detection methods ------
	 */

	/*
	 * onScale
	 * 
	 * @see
	 * android.view.ScaleGestureDetector.OnScaleGestureListener#onScale(android
	 * .view.ScaleGestureDetector)
	 */
	public boolean onScale(ScaleGestureDetector detector) {
		if (detector.isInProgress()) {
			_controller.onScale(detector.getScaleFactor());
		}
		return true;
	}

	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	public void onScaleEnd(ScaleGestureDetector detector) {
	}

	public void onTouch(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_UP) {
			_controller.onTouchUp(e.getX(), e.getY());
		}
	}
}
