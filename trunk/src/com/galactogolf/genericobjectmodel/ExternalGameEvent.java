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

package com.galactogolf.genericobjectmodel;

/**
 * Represents an event that has happened outside the Game world that might
 * affect game state
 * 
 */
public class ExternalGameEvent {
	public EventTypes eventType;
	public Vector2D position; // some events have a position
	public float magnitude; // some events have magnitude
	public int number; // some events have an number/amount

	public ExternalGameEvent(EventTypes type) {
		eventType = type;
	}

	public ExternalGameEvent(EventTypes type, int num) {
		eventType = type;
		number = num;
	}

	public ExternalGameEvent(EventTypes type, float distanceX, float distanceY) {
		eventType = type;
		position = new Vector2D(distanceX, distanceY);
	}

	public ExternalGameEvent(EventTypes type, float magnitude) {
		eventType = type;
		this.magnitude = magnitude;
	}

	public enum EventTypes {
		MENU_ITEM_SELECTED, SCROLLED, ZOOMED, TOUCH_DOWN, TOUCH_UP, LONG_PRESS, SINGLE_TAP_UP, SCALED
	}
}
