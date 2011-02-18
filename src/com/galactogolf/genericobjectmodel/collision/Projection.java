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

package com.galactogolf.genericobjectmodel.collision;

/**
 * A projection of a shape on to a 1d line segment
 * 
 */
public class Projection {

	private float _min;
	private float _max;

	public Projection(float min, float max) {
		_min = min;
		_max = max;

	}

	public float getMin() {
		return _min;
	}

	public float getMax() {
		return _max;
	}

	/**
	 * returns whether or not self and other intersect
	 * 
	 * @param other
	 * @return
	 */
	public boolean intersects(Projection other) {

		return _max > other.getMin() && other.getMax() > _min;
	}
}
