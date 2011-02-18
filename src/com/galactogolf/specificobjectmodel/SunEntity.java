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
import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;

/**
 * A large sun entity
 * 
 */
public class SunEntity extends GravitationalEntity {

	public SunEntity(String name, GameWorld world) {
		super(name, world);
	}

	@Override
	public float getBoundingSphereRadius() {
		return 60.0f;
	}

	@Override
	public double GetMass() {
		return 1.5d;
	}

	@Override
	protected void updateAnimation(long timeSinceLastFrame) {
		// TODO Auto-generated method stub

	}
}
