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

import java.util.HashMap;

import android.util.Log;

import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;
import com.galactogolf.views.GameEntityRenderable;
import com.galactogolf.views.RenderInstructionBuffer;

/**
 * Represents an entity within the game, eg. a player, an NPC or anything that
 * needs to respond to things happening in the world 
 *
 */
public abstract class GameEntity {
	
	protected String _name;
	
	protected boolean _visible=true;
	
	
	public String getName() {
		return _name; 
	}


	/**
	 * Position of center of object in 2d space
	 */
	protected Vector2D _position;
	
	public Vector2D getPosition() {
		return _position;
	}
	public void setPosition(Vector2D value) {
		_position = value;
	}
	
	protected float _angle;
	public float getAngle() {
		return _angle;
	}
	public void setAngle(float angle) {
		_angle = angle;
	}
	
	/**
	 * current frame to display
	 */
	protected int _currentFrame=0;

	
	/**
	 * The radius of the bounding sphere for collisions
	 */
	protected float _boundingSphereRadius;

	protected BoundingBox _boundingBox;

	private BoundingSphere _boundingSphere;
	public float getBoundingSphereRadius() {
		return _boundingSphereRadius;
	}
	public void setBoundingSphereRadius(float value) {
		_boundingSphereRadius = value;
	
	}
	

	public GameEntity(String name,GameWorld world)
	{
		_name = name;
		_world = world;
		_attributes = new HashMap<Integer,Object>();
		_position = new Vector2D(0.0f, 0.1f);
		_angle = 0.0f;
		  
	   _boundingBox = new BoundingBox();
	   _boundingSphere = new BoundingSphere();

	}
	
	protected GameWorld _world;
	protected GameEntityRenderable _renderable;

	public GameEntityRenderable GetRenderable()
	{
		return _renderable;
	}
	
	
	public void SetRenderable(GameEntityRenderable renderable)
	{
		_renderable = renderable;
		float value = renderable.getWidth();
		if(value==0.0f) {
			Log.d("zero radius!", "zero radius");
		}
		setBoundingSphereRadius(value/2);
	}
	
	private HashMap<Integer,Object> _attributes;

	private float _scaleX = 1.0f;

	private float _scaleY = 1.0f;

	protected int _currentFrameSet=0;
	
	public void setAttribute(int key,Object obj)
	{
		_attributes.put(key, obj);
	}
	
	public Object getAttribute(Integer key)
	{
		return _attributes.get(key);
	}
	
	/**
	 * handles a collision with another entity, this function will be twice for each collision,
	 * once for each entity
	 * @param ent
	 */
	public void HandleCollision(GameEntity ent)
	{
		
	}
	
	
	public abstract void RunPhysics(long timeSinceLastFrame);
	
	public boolean isVisibleAndInFrustrum(float cameraMinX,float cameraMinY,float cameraMaxX,float cameraMaxY) {
		if(!_visible) {
			return false;
		}
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
	
	public boolean visible() {
		return _visible;
	}
	
	
	
	public void render(RenderInstructionBuffer instructionList,long timeSinceLastFrame) {
		updateAnimation(timeSinceLastFrame);
		instructionList.AddRenderSimpleSpriteInstruction(getPosition().x,getPosition().y,getAngle(),getScaleX(),getScaleY(),GetCurrentFrameSet(),getCurrentFrame(), GetRenderable());
	}
	protected abstract void updateAnimation(long timeSinceLastFrame);
	
	
	public int getCurrentFrame() {
		return _currentFrame;
	}
	public int GetCurrentFrameSet() {
		return _currentFrameSet;
	}
	
	
	public void setScaleY(float f) {
		_scaleX = f;
	}

	public void setScaleX(float f) {
		_scaleY = f;
	}
	public float getScaleX() {
		return _scaleX;
	}
	
	public float getScaleY() {
		return _scaleY;
	}
	abstract public CollisionModes getCollisionMode();
	public BoundingBox getBoundingBox() {
		return _boundingBox;
	}
	public BoundingSphere getBoundingSphere() {
		_boundingSphere.position.x = _position.x;
		_boundingSphere.position.y = _position.y;
		_boundingSphere.radius = getBoundingSphereRadius();
		return _boundingSphere;
	}
}
