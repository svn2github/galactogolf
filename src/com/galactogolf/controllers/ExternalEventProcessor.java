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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;

import com.galactogolf.genericobjectmodel.ExternalGameEvent;
import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.Vector3D;

/*
 * Handles the processing of external events on the world, holds a buffer of 
 * event commands, generally submitted to by the Controller classes. This is then processed
 * and potentially changes the world state.
 */
public abstract class ExternalEventProcessor {
	protected GameWorld _world;
	private BlockingQueue<ExternalGameEvent> _eventQueue;

	public ExternalEventProcessor(GameWorld world) {
		_world = world;
		_eventQueue = new LinkedBlockingQueue<ExternalGameEvent>();
		_world.RegisterExternalEventProcessor(this);
	}

	public void submitEvent(ExternalGameEvent event) {
		_eventQueue.add(event);
	}

	public void processExternalEvents() {
		while (_eventQueue.peek() != null) {
			try {
				processExternalEvent(_eventQueue.take());
			} catch (InterruptedException e) {
				Log.e("Exception",e.getMessage());
			}
		}
	}

	protected void processExternalEvent(ExternalGameEvent event) {

		switch (event.eventType) {
		case LONG_PRESS:
			onLongPress(event.position.x, event.position.y);
			break;
		case MENU_ITEM_SELECTED:
			onMenuItemSelected(event.number);
			break;
		case SCROLLED:
			onScroll(event.position.x, event.position.y);
			break;
		case TOUCH_DOWN:
			onDown(event.position.x, event.position.y);
			break;
		case TOUCH_UP:
			onTouchUp(event.position.x, event.position.y);
			break;
		case SCALED:
			onScale(event.magnitude);
			break;
		}
	}

	abstract protected void onMenuItemSelected(int menuId);

	abstract public void onTouchUp(float x, float y);

	abstract public void onDown(float x, float y);

	abstract public void onSingleTapUp(float screenX, float screenY);

	abstract public void onScale(float newScaleFactor);

	protected Vector2D getTouchLocation(float x, float y) {
		Vector3D camLocation = _world.getCameraLocation();
		return new Vector2D(camLocation.x
				- (_world.GetRenderer().GetScreenWidth() / 2)
				/ _world.getCameraLocation().z + x / _world.getCameraLocation().z, camLocation.y
				- (_world.GetRenderer().GetScreenHeight() / 2)
				/ _world.getCameraLocation().z + y / _world.getCameraLocation().z);
	}

	abstract public void onScroll(float x, float y);

	abstract public void onLongPress(float x, float y);
}
