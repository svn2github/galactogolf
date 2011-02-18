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

package com.galactogolf.specificobjectmodel;

import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.NonPlayerEntity;
import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;

/**
 * Base class of all objects that have a gravitational field
 * 
 */
public abstract class GravitationalEntity extends NonPlayerEntity {

	public GravitationalEntity(String name, GameWorld world) {
		super(name, world);
	}

	public abstract double GetMass();

	@Override
	public CollisionModes getCollisionMode() {
		return CollisionModes.BOUNDING_SPHERE;
	}
}
