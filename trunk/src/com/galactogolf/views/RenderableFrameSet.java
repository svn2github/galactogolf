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

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * A set of renderable frames,in time order
 * 
 */
public class RenderableFrameSet {

	private RenderableFrame[] _frames;

	public RenderableFrameSet(int[] frameIds) {
		_frames = new RenderableFrame[frameIds.length];
		for (int i = 0; i < _frames.length; i++) {
			_frames[i] = new RenderableFrame(frameIds[i]);
		}
	}

	public void render(GL10 gl, float x, float y, float angle, float scaleX,
			float scaleY, boolean inViewSpace, int currentFrame) {
		_frames[currentFrame].render(gl, x, y, angle, scaleX, scaleY,
				inViewSpace);
	}

	public int getWidth() {
		return _frames[0].getWidth();
	}

	public int getHeight() {
		return _frames[0].getHeight();
	}

	public void initGL(GL10 gl, Context context) {
		for (int i = 0; i < _frames.length; i++) {
			_frames[i].initGL(gl, context);
		}

	}

}
