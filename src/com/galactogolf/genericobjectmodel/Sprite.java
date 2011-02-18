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

import com.galactogolf.views.GameEntityRenderable;

/**
 * An object that should be displayed in the game world when it is rendered, 
 * but has no interactions with the world itself, e.g. background images or overlays
 */
public abstract class Sprite {
	protected Vector2D _position;
	protected GameEntityRenderable _renderable;
	protected float _scaleX = 1.0f;
	protected float _scaleY = 1.0f;
	private float _angle;
	

	public Sprite() {
		_position = new Vector2D();
	}
	
	public Vector2D getPosition() {
		return _position;
	}

	public void setPosition(Vector2D position) {
		_position = position;
	}

	
	public float getAngle() {
		return _angle;
	}
	
	public void setAngle(float angle) {
		_angle = angle;
	}
	
	public GameEntityRenderable GetRenderable()
	{
		return _renderable;
	}
	
	
	public void SetRenderable(GameEntityRenderable renderable)
	{
		_renderable = renderable;
	}

	public boolean visible(float cameraMinX,float cameraMinY,float cameraMaxX,float cameraMaxY) {
		float entityMinX = this._position.x - this._renderable.getWidth()/2;
		float entityMinY = this._position.y - this._renderable.getHeight()/2;
		float entityMaxX = this._position.x + this._renderable.getWidth()/2;
		float entityMaxY = this._position.y + this._renderable.getHeight()/2;
		if(entityMaxX < cameraMinX) {
			return false;
		}
		if(entityMinX > cameraMaxX) {
			return false;
		}
		if(entityMaxY < cameraMinY) {
			return false;
		}
		if(entityMinY > cameraMaxY) {
			return false;
		}
		
		return true;
	}
	
	abstract public int GetCurrentFrame();

	abstract public int GetCurrentFrameSet();

	public float GetScaleX() {
		return _scaleX;
	}

	public float GetScaleY() {
		return _scaleY;
	}

}
