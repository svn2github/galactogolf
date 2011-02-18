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

import com.galactogolf.genericobjectmodel.Vector2D;

/**
 * A pop up that can appear on screen
 * TODO - this isn't used yet
 */
public class GameNotification {
	private Vector2D _bottomRight;
	private Vector2D _topLeft;
	private StringBuilder _message; 
	
	public GameNotification(StringBuilder message,Vector2D topLeft,Vector2D bottomRight) {
		_topLeft = topLeft;
		_bottomRight = bottomRight;
		_message = message;
	}
	
	
	public void render(RenderInstructionBuffer instructionBuffer,long timeSinceLastFrame) {
		instructionBuffer.AddRenderTextInViewSpace(_message,_bottomRight,_topLeft);
	}
}
