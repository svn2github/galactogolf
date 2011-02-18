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

import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;

/**
 * Class holding various collision detection methods for different objects.
 * 
 * For OBB interstions, the Seperating Axis Theorem method is used, e.g. we try
 * and find an axis which if we project the shapes down to a 1d line, there is
 * no intersection
 * 
 */
public class CollisionDetector {
	public static boolean isColliding(BoundingSphere sphere1,
			BoundingSphere sphere2) {
		Vector2D diffVect = Vector2D.Sub(sphere1.position, sphere2.position);

		float distBetweenPositions = diffVect.magnitude();
		if (distBetweenPositions < sphere1.radius + sphere2.radius) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tests if a sphere and an orientated bounding box intersect, by seeing how
	 * far away from the center the closest point on the box is
	 * 
	 * @param box
	 * @param box
	 * @return
	 */
	public static boolean isColliding(BoundingSphere sphere, BoundingBox box) {
		float distance = DistanceBetween(box, sphere.position);
		if (distance < sphere.radius) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tests if two orientated bounding boxes intersect, using the Separating
	 * Axis method
	 * 
	 * @param box
	 * @param box
	 * @return
	 */
	public static boolean isColliding(BoundingBox box1, BoundingBox box2) {
		/*
		 * // Create a list of both polygons' edges Vector2D edges = []
		 * edges.extend(self.edges) edges.extend(other.edges)
		 * 
		 * for edge in edges: axis = edge.normalize().perpendicular() # Create
		 * the separating axis (see diagrams)
		 * 
		 * # Project each to the axis self_projection =
		 * self.project_to_axis(axis) other_projection =
		 * other.project_to_axis(axis)
		 * 
		 * # If the projections don't intersect, the polygons don't intersect if
		 * not self_projection.intersects(other_projection): return False
		 * 
		 * # The projections intersect on all axes, so the polygons are
		 * intersecting return True
		 */
		return true;
	}

	/**
	 * Gets the squared distance betwen a point and a box
	 */
	public static float DistanceBetween(BoundingBox box, Vector2D point) {
		// Work in the box's coordinate system.
		Vector2D diff = Vector2D.Sub(point, box.origin);

		// Compute squared distance and closest point on box.
		float sqrDistance = 0;
		float delta;
		Vector2D closest = new Vector2D();
		int i;

		closest.x = Vector2D.dotProduct(diff, box.xAxis);
		if (closest.x < -box.xExtent) {
			delta = closest.x + box.xExtent;
			sqrDistance += delta * delta;
			closest.x = -box.xExtent;
		} else if (closest.x > box.xExtent) {
			delta = closest.x - box.xExtent;
			sqrDistance += delta * delta;
			closest.x = box.xExtent;
		}

		closest.y = Vector2D.dotProduct(diff, box.yAxis);
		if (closest.y < -box.yExtent) {
			delta = closest.y + box.yExtent;
			sqrDistance += delta * delta;
			closest.y = -box.yExtent;
		} else if (closest.y > box.yExtent) {
			delta = closest.y - box.yExtent;
			sqrDistance += delta * delta;
			closest.y = box.yExtent;
		}

		/*
		 * mClosestPoint0 = point; mClosestPoint1 = mBox->Center; for (i = 0; i
		 * < 2; ++i) { mClosestPoint1 += closest[i]*mBox->Axis[i]; }
		 */
		return (float) Math.sqrt(sqrDistance);
	}

	/**
	 * Gets the squared distance betwen a point and a sphere
	 */
	public static float DistanceBetween(BoundingSphere sphere, Vector2D point) {
		float distBetweenCenterAndPoint = Vector2D.Sub(sphere.position, point)
				.magnitude();
		if (distBetweenCenterAndPoint > sphere.radius) {
			return distBetweenCenterAndPoint - sphere.radius;
		} else {
			return 0.0f;
		}

	}

	/**
	 * Gets the squared distance betwen a point and a sphere
	 * 
	 * @throws CollisionException
	 */
	public static float DistanceBetween(GameEntity ent, Vector2D point)
			throws CollisionException {
		if (ent.getCollisionMode() == CollisionModes.BOUNDING_SPHERE) {
			return DistanceBetween(ent.getBoundingSphere(), point);
		} else if (ent.getCollisionMode() == CollisionModes.BOUNDING_BOX) {
			return DistanceBetween(ent.getBoundingBox(), point);
		} else {
			throw new CollisionException("Collision mode not found");
		}

	}
}
