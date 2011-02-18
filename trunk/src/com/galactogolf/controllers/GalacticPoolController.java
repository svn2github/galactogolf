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

import android.view.Menu;

import com.galactogolf.GameActivity;
import com.galactogolf.UserPreferences;
import com.galactogolf.genericobjectmodel.ExternalGameEvent;
import com.galactogolf.genericobjectmodel.ExternalGameEvent.EventTypes;
import com.galactogolf.specificobjectmodel.GameConstants;

/*
 * Controller that processed external events for the regular game mode, e.g.
 * when the user is playing
 */
public class GalacticPoolController extends GalacticPoolBaseController {

	protected static final int MENU_RETRY_LEVEL = 1;
	protected static final int MENU_EDIT_LEVEL = 2;
	protected static final int MENU_CONTINUE_LEVEL = 4;
	protected static final int MENU_EXIT = 3;

	public GalacticPoolController(GameActivity parentActivity,
			ExternalEventProcessor eventProcessor) {
		super(parentActivity, eventProcessor);
		_parentActivity = parentActivity;
		addMenuAction(MENU_RETRY_LEVEL, new MenuItemSelectedListener() {
			public void onMenuItemSelected() {
				AddExternalEvent(
						ExternalGameEvent.EventTypes.MENU_ITEM_SELECTED,
						GameConstants.MENU_RETRY_LEVEL);
			}

		});
		addMenuAction(MENU_CONTINUE_LEVEL, new MenuItemSelectedListener() {
			public void onMenuItemSelected() {
			}

		});

		addMenuAction(MENU_EXIT, new MenuItemSelectedListener() {
			public void onMenuItemSelected() {
				_parentActivity.exitToMenu();
			}

		});

		if (UserPreferences.EditorEnabled) {
			addMenuAction(MENU_EDIT_LEVEL, new MenuItemSelectedListener() {
				// now editing level, swap in the editor controller
				public void onMenuItemSelected() {
					_parentActivity.EnableEditor();
				}
			});
		}
	}

	@Override
	public void onScroll(float distanceX, float distanceY) {
		AddExternalEvent(EventTypes.SCROLLED, distanceX, distanceY);
	}

	public void onNextLevel() {
		AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
				GameConstants.MENU_NEXT_LEVEL);
	}

	public void onReplayLevel() {
		AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
				GameConstants.MENU_REPLAY_LEVEL);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.add(0, MENU_RETRY_LEVEL, 1, "Self destruct");
		menu.add(0, MENU_CONTINUE_LEVEL, 3, "Continue Level");
		if (UserPreferences.EditorEnabled) {
			menu.add(0, MENU_EDIT_LEVEL, 2, "Edit Level");
		}
		menu.add(0, MENU_EXIT, 3, "Exit to menu");
	}

	@Override
	public void onScale(float newScaleFactor) {
		AddExternalEvent(EventTypes.SCALED, newScaleFactor);

	}

	@Override
	public void onSingleTapUp(float screenX, float screenY) {
		AddExternalEvent(EventTypes.SINGLE_TAP_UP, screenX, screenY);
	}

	@Override
	public void onDown(float x, float y) {
		AddExternalEvent(EventTypes.TOUCH_DOWN, x, y);
	}

	@Override
	public void onTouchUp(float x, float y) {
		AddExternalEvent(EventTypes.TOUCH_UP, x, y);

	}

	@Override
	public void onLongPress(float x, float y) {
		AddExternalEvent(EventTypes.LONG_PRESS, x, y);

	}

}
