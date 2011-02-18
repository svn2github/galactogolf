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

import com.galactogolf.genericobjectmodel.GameEntity;

/**
 * CLass that holds the graphical representation of a Game Entity
 *
 */
public class GameEntityRenderable {
	
	protected GameEntity _entity;
	private int _width;
	private int _height;
	
	public GameEntityRenderable(int[][] frameSetIds) {
		_frameSets = new RenderableFrameSet[frameSetIds.length];
		for(int i=0;i<_frameSets.length;i++) {
			_frameSets[i] = new RenderableFrameSet(frameSetIds[i]);
		}
	}
	

	protected RenderableFrameSet[] _frameSets;
	
	
	public void render(GL10 gl, float x,float y,float angle,float scaleX,float scaleY,boolean inViewSpace, int currentFrameSet,int currentFrame) {
		_frameSets[currentFrameSet].render(gl, x, y, angle,scaleX,scaleY,inViewSpace, currentFrame);
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public void initGL(GL10 gl, Context context) {
		for(int i=0;i<_frameSets.length;i++) {
			_frameSets[i].initGL(gl, context);
		}
		_width = _frameSets[0].getWidth();
		_height = _frameSets[0].getHeight();

	}
	
	


}