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
import android.view.SubMenu;

import com.galactogolf.GameActivity;
import com.galactogolf.genericobjectmodel.ExternalGameEvent.EventTypes;
import com.galactogolf.specificobjectmodel.GameConstants;

/*
 * Controller that processes events when the user is editing levels rather than
 * playing them.
 */
public class GalacticPoolEditorController extends GalacticPoolBaseController {


	public GalacticPoolEditorController(GameActivity parentActivity,
			ExternalEventProcessor eventProcessor) {
		super(parentActivity, eventProcessor);
		_parentActivity = parentActivity;
		((GalacticPoolEditorExternalEventProcessor) _eventProcessor)
				.setController(this);

		addMenuAction(GameConstants.MENU_PLAY_LEVEL,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {

						_parentActivity.EnablePlayLevel();

					}
				});
		addMenuAction(GameConstants.MENU_SAVE_LEVEL,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_SAVE_LEVEL);
					}
				});
		addMenuAction(GameConstants.MENU_RESET_LEVEL,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_RETRY_LEVEL);
					}
				});
		addMenuAction(GameConstants.MENU_PREV_LEVEL,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_PREV_LEVEL);
					}
				});
		addMenuAction(GameConstants.MENU_NEXT_LEVEL,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_NEXT_LEVEL);

					}
				});
		addMenuAction(GameConstants.MENU_NEW_LEVEL,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_NEW_LEVEL);

					}
				});

		addMenuAction(GameConstants.MENU_ADD_PLANET,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_PLANET);

					}
				});
		addMenuAction(GameConstants.MENU_ADD_MOON,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_MOON);

					}
				});
		addMenuAction(GameConstants.MENU_ADD_WORMHOLE,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_WORMHOLE);
					}
				});
		addMenuAction(GameConstants.MENU_ADD_BARRIER,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_BARRIER);
					}
				});
		addMenuAction(GameConstants.MENU_ADD_BOUNCE_BARRIER,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_BOUNCE_BARRIER);
					}
				});
		addMenuAction(GameConstants.MENU_ADD_STAR,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_STAR);

					}
				});
		addMenuAction(GameConstants.MENU_ADD_SUN,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_SUN);

					}
				});
		addMenuAction(GameConstants.MENU_ADD_SATURN,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_ADD_SATURN);

					}
				});
		addMenuAction(GameConstants.MENU_DELETE_SELECTED,
				new MenuItemSelectedListener() {
					public void onMenuItemSelected() {
						AddExternalEvent(EventTypes.MENU_ITEM_SELECTED,
								GameConstants.MENU_DELETE_SELECTED);

					}
				});
		addMenuAction(GameConstants.MENU_EXIT, new MenuItemSelectedListener() {
			public void onMenuItemSelected() {

			}
		});
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		SubMenu levelSubMenu = menu.addSubMenu("Level");
		levelSubMenu.add(0, GameConstants.MENU_PLAY_LEVEL, 1, "Play Level");
		levelSubMenu.add(0, GameConstants.MENU_SAVE_LEVEL, 2, "Save Level");
		levelSubMenu.add(0, GameConstants.MENU_RESET_LEVEL, 3, "Reset Level");
		levelSubMenu.add(0, GameConstants.MENU_PREV_LEVEL, 4, "Prev Level");
		if (((GalacticPoolEditorExternalEventProcessor) _eventProcessor)
				.isOnLastLevel()) {
			levelSubMenu.add(0, GameConstants.MENU_NEW_LEVEL, 5,
					"Add New Level");
		} else {
			levelSubMenu.add(0, GameConstants.MENU_NEXT_LEVEL, 5, "Next Level");
		}
		SubMenu addSubMenu = menu.addSubMenu("Add");
		addSubMenu.add(0, GameConstants.MENU_ADD_PLANET, 1, "Planet");
		addSubMenu.add(0, GameConstants.MENU_ADD_MOON, 2, "Moon");
		addSubMenu.add(0, GameConstants.MENU_ADD_WORMHOLE, 3, "Wormhole");
		addSubMenu.add(0, GameConstants.MENU_ADD_BARRIER, 3, "Barrier");
		addSubMenu.add(0, GameConstants.MENU_ADD_BOUNCE_BARRIER, 4,
				"Bounce Barrier");
		addSubMenu.add(0, GameConstants.MENU_ADD_STAR, 5, "Star");
		addSubMenu.add(0, GameConstants.MENU_ADD_SATURN, 6, "Saturn");
		addSubMenu.add(0, GameConstants.MENU_ADD_SUN, 7, "Sun");
		menu.add(0, GameConstants.MENU_DELETE_SELECTED, 4, "Delete Selected");

	}

	@Override
	public void onScroll(float distanceX, float distanceY) {
		AddExternalEvent(EventTypes.SCROLLED, distanceX, distanceY);
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
