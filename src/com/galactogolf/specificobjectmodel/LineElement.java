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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.galactogolf.genericobjectmodel.GameEntity;
import com.galactogolf.genericobjectmodel.Vector2D;

/**
 * Represents a line
 * 
 */
public class LineElement {

	private Vector2D _start;
	private Vector2D _end;

	public LineElement() {
		_start = new Vector2D();
		_end = new Vector2D();
	}

	public void render(GL10 gl) {
		float line[] = { _start.x, _start.y, // point A
				_end.x, _end.y, // point B
		};
		// Our vertex buffer.
		FloatBuffer vertexBuffer;

		gl.glDisable(GL10.GL_TEXTURE_2D);
		// gl.glColor4f(1.0f,0.0f,0.0f,1.0f);
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(line.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(line);
		vertexBuffer.position(0);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDrawArrays(GL10.GL_LINES, 0, 2);
	}

	public Vector2D getStart() {
		return _start;
	}

	public Vector2D getEnd() {
		return _end;
	}

}
