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

/***
 * Represents a bounding box, arbitrarily oriented, defined by 3 coordinates,
 * the origin, and the two axes
 * 
 */
public class BoundingBox {
	public Vector2D origin; // origin vector relative to world
	public Vector2D xAxis; // xAxis, normalised and relative to origin vector
	public Vector2D yAxis; // yAxis, normalised and relative to origin vector
	public float xExtent;
	public float yExtent;

	public BoundingBox() {
		origin = new Vector2D();
		xAxis = new Vector2D();
		yAxis = new Vector2D();
	}

	public BoundingBox(Vector2D origin, float xExtent, float yExtent,
			float angle) {
		this.setup(origin, xExtent, yExtent, angle);

	}

	public Projection projectToAxis(Vector2D axis) {
		// TODO write a proper projection function
		float f = 1 / 0f; // crash out if we reach here
		return null;
	}

	public void setup(Vector2D origin, float xExtent, float yExtent, float angle) {
		this.origin = origin;
		Vector2D unrotatedXAxis = new Vector2D(1.0f, 0.0f);
		Vector2D unrotatedYAxis = new Vector2D(0.0f, 1.0f);
		xAxis = Vector2D.rotate(unrotatedXAxis, angle);
		yAxis = Vector2D.rotate(unrotatedYAxis, angle);
		this.xExtent = xExtent;
		this.yExtent = yExtent;

	}

}
