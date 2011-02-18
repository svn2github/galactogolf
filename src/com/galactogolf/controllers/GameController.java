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

import java.util.HashMap;

import android.view.Menu;

import com.galactogolf.GameActivity;
import com.galactogolf.genericobjectmodel.ExternalGameEvent;
import com.galactogolf.genericobjectmodel.ExternalGameEvent.EventTypes;

/* These controller classes form one half of the input event processing pipeline. 
 * Their job is to package up any external event in to an ExternalGameEvent objects
 * that are then put on a queue to be processed by the game world
 */
public abstract class GameController {

	private HashMap<Integer, MenuItemSelectedListener> _menuActions;
	protected ExternalEventProcessor _eventProcessor;
	protected GameActivity _parentActivity;

	public GameController(GameActivity _parentActivity,
			ExternalEventProcessor eventProcessor) {
		_menuActions = new HashMap<Integer, MenuItemSelectedListener>();
		_eventProcessor = eventProcessor;
	}

	public void addMenuAction(int id, MenuItemSelectedListener listener) {
		_menuActions.put(new Integer(id), listener);
	}

	public void onScroll(float distanceX, float distanceY) {

	}

	public void onScale(float scaleFactor) {

	}

	public void onSingleTapUp(float screenX, float screenY) {

	}

	public void onDown(float x, float y) {

	}

	public void onTouchUp(float x, float y) {

	}

	public void onLongPress(float x, float y) {

	}

	public void onMenusItemSelected(int id) {
		if (_menuActions.containsKey(new Integer(id))) {
			_menuActions.get(new Integer(id)).onMenuItemSelected();
		}
	}

	public abstract void onPrepareOptionsMenu(Menu menu);

	public void AddExternalEvent(EventTypes eventType) {
		_eventProcessor.submitEvent(new ExternalGameEvent(eventType));
	}

	public void AddExternalEvent(EventTypes eventType, int number) {
		_eventProcessor.submitEvent(new ExternalGameEvent(eventType, number));
	}

	protected void AddExternalEvent(EventTypes eventType, float distanceX,
			float distanceY) {
		_eventProcessor.submitEvent(new ExternalGameEvent(eventType, distanceX,
				distanceY));

	}

	protected void AddExternalEvent(EventTypes eventType, float magnitude) {
		_eventProcessor
				.submitEvent(new ExternalGameEvent(eventType, magnitude));

	}

}
