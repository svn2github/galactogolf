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

package com.galactogolf.views;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

import com.galactogolf.utils.Util;

/**
 * contains one frame of an animation
 * 
 * 
 */
public class RenderableFrame {
	private float _vertices[] = { 0.0f, 1.0f, 0.0f, // 0, Top Left
			0.0f, 0.0f, 0.0f, // 1, Bottom Left
			1.0f, 0.0f, 0.0f, // 2, Bottom Right
			1.0f, 1.0f, 0.0f, // 3, Top Right
	};

	private short[] _indices = { 0, 1, 2, 0, 2, 3 };

	private float[] _textureCoords = { // front
	0, 1, 0, 0, 1, 0, 1, 1 };

	// Our vertex buffer.
	private FloatBuffer _vertexBuffer;

	// Our index buffer.
	private ShortBuffer _indexBuffer;

	private static FloatBuffer _textureCoordsBuffer;

	private IntBuffer _texturesBuffer;

	private int _textureResource;

	protected int _width;

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	protected int _height;

	public RenderableFrame(int textureResource) {
		_textureResource = textureResource;
	}

	public void initGL(GL10 gl, Context context) {

		Bitmap texture = Util.getTextureFromBitmapResource(context,
				_textureResource);

		_width = texture.getWidth();
		_height = texture.getHeight();

		// scale the vertex buffer coords
		for (int i = 0; i < _vertices.length; i += 3) {
			_vertices[i] *= _width;
			_vertices[i + 1] *= _height;
		}
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(_vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		_vertexBuffer = vbb.asFloatBuffer();
		_vertexBuffer.put(_vertices);
		_vertexBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(_indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		_indexBuffer = ibb.asShortBuffer();
		_indexBuffer.put(_indices);
		_indexBuffer.position(0);

		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer tbb = ByteBuffer.allocateDirect(_textureCoords.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		_textureCoordsBuffer = tbb.asFloatBuffer();
		_textureCoordsBuffer.put(_textureCoords);
		_textureCoordsBuffer.position(0);

		// create texture
		gl.glEnable(GL10.GL_TEXTURE_2D);
		_texturesBuffer = IntBuffer.allocate(1);
		gl.glGenTextures(1, _texturesBuffer);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, _texturesBuffer.get(0));

		// set the texture
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, texture, 0);

		texture.recycle();
	}

	public void render(GL10 gl, float x, float y, float angle, float scaleX,
			float scaleY) {
		render(gl, x, y, angle, scaleX, scaleY, false);

	}

	public void render(GL10 gl, float x, float y, float angle, float scaleX,
			float scaleY, boolean inViewSpace) {
		if (inViewSpace) {
			gl.glPushMatrix();// save off the modelview
			gl.glLoadIdentity();// reset modelview

		}

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glTranslatef(x, y, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		gl.glScalef(scaleX, scaleY, 0.0f);
		gl.glTranslatef(-_width / 2, -_height / 2, 0.0f);

		// Counter-clockwise winding.
		// gl.glFrontFace(GL10.GL_CCW); // OpenGL docs
		// Enable face culling.
		// gl.glEnable(GL10.GL_CULL_FACE); // OpenGL docs
		// What faces to remove with the face culling.
		// gl.glCullFace(GL10.GL_FRONT); // OpenGL docs

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.

		gl.glBindTexture(GL10.GL_TEXTURE_2D, _texturesBuffer.get(0));
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, // OpenGL docs
				_vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, _textureCoordsBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, _indices.length,// OpenGL docs
				GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE); // OpenGL docs
		if (inViewSpace) {
			gl.glPopMatrix();
		}

	}
}
