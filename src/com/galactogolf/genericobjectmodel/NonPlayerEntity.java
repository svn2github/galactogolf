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
 * Represents an entity not controlled by the player
 */
public abstract class NonPlayerEntity extends GameEntity {

	public NonPlayerEntity(String name, GameWorld world) {
		super(name, world);
	}

	@Override
	public void HandleCollision(GameEntity ent) {
		super.HandleCollision(ent);

	}

	public void SetRenderable(GameEntityRenderable gameEntityRenderable) {
		_renderable = gameEntityRenderable;

	}

	@Override
	public void RunPhysics(long timeSinceLastFrame) {
	}

}
