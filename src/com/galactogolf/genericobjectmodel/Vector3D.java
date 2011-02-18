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

/**
 * a 3d Vector
 * 
 * @author andyds
 * 
 */
public class Vector3D {
	public float x;
	public float y;
	public float z;

	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vector3D Sub(Vector3D p1, Vector3D p2) {
		return new Vector3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
	}

	public static Vector3D Add(Vector3D p1, Vector3D p2) {
		return new Vector3D(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
	}

	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public void Add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;

	}
}
