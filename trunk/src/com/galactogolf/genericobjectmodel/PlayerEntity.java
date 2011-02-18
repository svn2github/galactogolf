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

import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;

/**
 * Represents a generic player entity 
 *
 */
public abstract class PlayerEntity extends GameEntity{

	

	@Override
	public CollisionModes getCollisionMode() {
		return null;
	}

	@Override
	protected void updateAnimation(long timeSinceLastFrame) {
		
	}

	public PlayerEntity(GameWorld world) {
		super("Player",world);
	}

	@Override
	public void HandleCollision(GameEntity ent) {
		super.HandleCollision(ent);
		
		// any collisions, start again
		_world.PlayerDied();
	}

	@Override
	public void RunPhysics(long timeSinceLastFrame) {
		
	}

	public void reset() {
		this.setScaleX(1.0f);
		this.setScaleY(1.0f);
		
	}



}
