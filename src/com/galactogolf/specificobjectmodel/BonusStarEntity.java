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
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;

/**
 * Star that gives the user points when they hit it
 * 
 */
public class BonusStarEntity extends NonPlayerEntity {

	protected Vector2D _velocity;

	protected NonPlayerEntity _inOrbitAround;

	public BonusStarEntity(String name, GameWorld world,
			NonPlayerEntity inOrbitAround) {
		super(name, world);
		this._boundingSphereRadius = 10;
		this._inOrbitAround = inOrbitAround;
		this._velocity = new Vector2D(0.0f, 0.0f);
	}

	@Override
	public void RunPhysics(long timeSinceLastFrame) {

	}

	public Vector2D getVelocity() {
		return _velocity;
	}

	public void HitByPlayer() {
		_visible = false;
	}

	@Override
	protected void updateAnimation(long timeSinceLastFrame) {
		float angle = getAngle();
		angle = angle + 200.0f * timeSinceLastFrame / 1000;
		if (angle > 360) {
			setAngle((float) (angle - 360));
		} else {
			setAngle(angle);
		}

	}

	@Override
	public CollisionModes getCollisionMode() {
		return CollisionModes.BOUNDING_SPHERE;
	}

	public void setVisible(boolean visible) {
		_visible = visible;

	}

}
