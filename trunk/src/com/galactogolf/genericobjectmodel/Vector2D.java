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
 * A 2d Vector
 * 
 */
public class Vector2D {
	public float x;
	public float y;

	public Vector2D() {
		this.x = 0.0f;
		this.y = 0.0f;
	}

	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2D Sub(Vector2D p1, Vector2D p2) {
		return new Vector2D(p1.x - p2.x, p1.y - p2.y);
	}

	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public double GetAngle() {
		if (x > 0 && y >= 0) {
			return Math.atan(y / x);
		}
		if (x > 0 && y < 0) {
			return Math.atan(y / x) + 2 * Math.PI;
		}
		if (x < 0) {
			return Math.atan(y / x) + Math.PI;
		}
		if (x == 0 && y > 0) {
			return Math.PI / 2.0d;
		} else { // (x==0 && y<0) {
			return -Math.PI / 2.0d;
		}
	}

	public static float dotProduct(Vector2D v1, Vector2D v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public static Vector2D Add(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2D mult(Vector2D v, float f) {
		return new Vector2D(v.x * f, v.y * f);
	}

	public static Vector2D rotate(Vector2D v, float angle) {
		return new Vector2D((float) (v.x * Math.cos(angle) - v.y
				* Math.sin(angle)), (float) (v.x * Math.sin(angle) + v.y
				* Math.cos(angle)));
	}

	public void Add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;

	}

	public Vector2D normalize() {
		float z = this.magnitude();
		return new Vector2D(this.x / z, this.y / z);
	}

	public Vector2D mult(int f) {
		return new Vector2D(x*f,y*f);
	}
}
