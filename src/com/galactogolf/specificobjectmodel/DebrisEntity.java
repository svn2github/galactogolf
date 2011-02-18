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

import java.util.Random;

import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.NonPlayerEntity;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;

/**
 * Entity representing a bit of debris flying out when the player crashes
 */
public class DebrisEntity extends NonPlayerEntity {
	private float _rotationRate;
	private Vector2D _velocity;

	public DebrisEntity(String name, GameWorld world) {
		super(name, world);
		Random rnd = new Random();
		_rotationRate = ((rnd.nextFloat() - 0.5f) * 1000f);
		_velocity = new Vector2D();
	}

	@Override
	public void RunPhysics(long timeSinceLastFrame) {
		this._position.x += this._velocity.x * (GameConstants.TIME_SCALE)
				* (double) timeSinceLastFrame;
		this._position.y += this._velocity.y * (GameConstants.TIME_SCALE)
				* (double) timeSinceLastFrame;

		super.RunPhysics(timeSinceLastFrame);
	}

	@Override
	public CollisionModes getCollisionMode() {
		return CollisionModes.BOUNDING_SPHERE;
	}

	@Override
	protected void updateAnimation(long timeSinceLastFrame) {
		float angle = getAngle();
		angle = angle + _rotationRate * (timeSinceLastFrame / 1000.0f);
		if (angle > 360) {
			setAngle((float) (angle - 360));
		} else if (angle < -360) {
			setAngle((float) (angle + 360));
		} else {
			setAngle(angle);
		}

	}

	public Vector2D getVelocity() {
		return _velocity;
	}

}
