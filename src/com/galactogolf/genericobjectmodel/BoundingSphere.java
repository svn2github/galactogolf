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

import com.galactogolf.genericobjectmodel.collision.Projection;

/*
 * Represents a bounding sphere around an entity (nb because we are in 2d, this is really a circle
 *
 */
public class BoundingSphere {
	public Vector2D position;
	public float radius;

	public BoundingSphere() {
		position = new Vector2D();
	}

	public Projection projectToAxis(Vector2D axis) {
		// project the centre onto this line (which is equivalent to doing the
		// dot product
		float projectedCentre = Vector2D.dotProduct(position, axis);

		return new Projection(projectedCentre - radius, projectedCentre
				+ radius);
	}
}
