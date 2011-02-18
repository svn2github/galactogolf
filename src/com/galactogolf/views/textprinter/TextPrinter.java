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

package com.galactogolf.views.textprinter;

import android.content.Context;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;

import com.galactogolf.R;
import com.galactogolf.utils.Util;

/**
 * Prints text to an openGL surface, using a texture containing all the
 * printable ASCII characters as the source for the character bitmaps
 * 
 */
public class TextPrinter {
	private FloatBuffer _vertexBuffer;
	private FloatBuffer _textureBuffers[];
	private ShortBuffer _indexBuffer;

	private int[] _textureBuffer = new int[1];

	private float _vertices[] = { 0.0f, 1.0f, 0.0f, // 0, Top Left
			0.0f, 0.0f, 0.0f, // 1, Bottom Left
			1.0f, 0.0f, 0.0f, // 2, Bottom Right
			1.0f, 1.0f, 0.0f, // 3, Top Right
	};
	private short[] _indices = { 0, 1, 2, 0, 2, 3 };

	float[][] _textureCoords;

	private int _textureSize;
	private int _characterSize;
	private int _characterMargin;

	public TextPrinter(GL10 gl, Context context) {
		// Load the big font texture
		Bitmap bitmap = Util.getTextureFromBitmapResource(context,
				R.drawable.font_texture);

		_textureSize = bitmap.getWidth();
		// each character takes up 16 pixels
		_characterSize = _textureSize / 16;
		_characterMargin = _characterSize / 3; // they are spaced by 1/4 of a
												// cell on each side

		// scale the vertex buffer coords
		for (int i = 0; i < _vertices.length; i += 3) {
			_vertices[i] *= _characterSize;
			_vertices[i + 1] *= _characterSize;
		}

		_textureCoords = new float[16 * 16][4 * 2]; // font grid is 16*16 and
													// we have the tex coords of
													// all four corners

		// initialise the texture coords for each character on the grid
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				_textureCoords[(x) + (y * 16)][0] = ((float) x) / 16.0f; // top
																			// left
				_textureCoords[(x) + (y * 16)][1] = ((float) y + 1.0f) / 16.0f;
				_textureCoords[(x) + (y * 16)][2] = ((float) x) / 16.0f; // bottom
																			// left
				_textureCoords[(x) + (y * 16)][3] = ((float) y) / 16.0f;
				_textureCoords[(x) + (y * 16)][4] = ((float) x + 1.0f) / 16.0f; // bottom
																				// right
				_textureCoords[(x) + (y * 16)][5] = ((float) y) / 16.0f;
				_textureCoords[(x) + (y * 16)][6] = ((float) x + 1) / 16.0f; // top
																				// right
				_textureCoords[(x) + (y * 16)][7] = ((float) y + 1) / 16.0f;
			}
		}
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(_vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		_vertexBuffer = byteBuf.asFloatBuffer();
		_vertexBuffer.put(_vertices);
		_vertexBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(_indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		_indexBuffer = ibb.asShortBuffer();
		_indexBuffer.put(_indices);
		_indexBuffer.position(0);

		// create texture
		gl.glGenTextures(1, _textureBuffer, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureBuffer[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// allocate a texture buffer for each character
		_textureBuffers = new FloatBuffer[256];
		for (int i = 0; i < 256; i++) {
			byteBuf = ByteBuffer.allocateDirect(_textureCoords[i].length * 4);
			byteBuf.order(ByteOrder.nativeOrder());

			_textureBuffers[i] = byteBuf.asFloatBuffer();
			_textureBuffers[i].put(_textureCoords[i]);
			_textureBuffers[i].position(0);

		}

	}

	public void drawTextInWorldSpace(GL10 gl, float x, float y,
			StringBuilder text) {
		drawText(gl, x, y, text, false);

	}

	public void drawTextInViewSpace(GL10 gl, float x, float y,
			StringBuilder text) {
		drawText(gl, x, y, text, true);
	}

	protected void drawText(GL10 gl, float x, float y, StringBuilder text,
			boolean inViewSpace) {
		if (inViewSpace) {
			gl.glPushMatrix();// save off the modelview
			gl.glLoadIdentity();// reset modelview

		}
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Bind our only previously generated texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, _textureBuffer[0]);

		float posX = x;
		float posY = y;
		gl.glPushMatrix();
		gl.glTranslatef(posX, posY, 0.0f);

		for (int i = 0; i < text.length(); i++) {
			int ascii = text.codePointAt(i);
			_textureBuffers[ascii].position(0);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, _textureBuffers[ascii]);
			gl.glDrawElements(GL10.GL_TRIANGLES, _indices.length,
					GL10.GL_UNSIGNED_SHORT, _indexBuffer);
			// move along by the character width minus the extra margin that
			// appears on each char
			gl.glTranslatef(_characterSize - _characterMargin * 2, 0, 0.0f);

		}
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		if (inViewSpace) {
			gl.glPopMatrix();
		}
	}

}
