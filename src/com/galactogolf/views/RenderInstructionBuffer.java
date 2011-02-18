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

import java.util.Iterator;

import com.galactogolf.genericobjectmodel.Vector2D;

import android.util.Log;

/**
 * Represents a buffer of render instructions, which are created by the Game
 * World and processed by the game renderer. decoupling the game world
 * processing from the rendering thread
 * 
 */
public class RenderInstructionBuffer {
	private RenderInstruction[] _instructionPool;
	private int _currentPoolIndex;
	private int _currentPoolSize;
	private boolean _open;

	public RenderInstructionBuffer(int poolSize, boolean open) {
		_instructionPool = new RenderInstruction[poolSize];
		for (int i = 0; i < poolSize; i++) {
			_instructionPool[i] = new RenderInstruction();
		}
		clearPool();
		_open = open;
	}

	public void AddRenderSimpleSpriteInstruction(float x, float y, float angle,
			int currentFrameSet, int currentFrame,
			GameEntityRenderable renderable) {
		if (_currentPoolIndex >= _instructionPool.length) {
			Log.e("FIX NEEDED", "Instruction pool size exceeded");
			return;
		}

		AddRenderSimpleSpriteInstruction(x, y, angle, 1.0f, 1.0f,
				currentFrameSet, currentFrame, renderable);
	}

	public void AddRenderTextInViewSpace(StringBuilder message,
			Vector2D topRight, Vector2D bottomLeft) {
		if (_currentPoolIndex >= _instructionPool.length) {
			Log.e("FIX NEEDED", "Instruction pool size exceeded");
			return;
		}

		_instructionPool[_currentPoolIndex].Position.x = bottomLeft.x;
		_instructionPool[_currentPoolIndex].Position.y = bottomLeft.y;
		_instructionPool[_currentPoolIndex].Position2.x = topRight.x;
		_instructionPool[_currentPoolIndex].Position2.y = topRight.y;
		_instructionPool[_currentPoolIndex].InstructionType = RenderInstruction.InstructionTypes.RENDER_TEXT_IN_VIEW_SPACE;
		_instructionPool[_currentPoolIndex].Text = message;

		_currentPoolIndex++;
		_currentPoolSize++;

	}

	public void clearPool() {
		_currentPoolIndex = 0;
		_currentPoolSize = 0;
	}

	public void open() throws RenderException {
		if (_open) {
			throw new RenderException("Buffer is already open");
		}
		clearPool();
		_open = true;
	}

	public void close() throws RenderException {
		if (!_open) {
			throw new RenderException("Buffer is already closed");
		}
		_currentPoolIndex = 0;
		_open = false;
	}

	public RenderInstruction getNextRenderInstruction() throws RenderException {
		if (_open) {
			throw new RenderException("Trying to render closed buffer");
		}
		if (_currentPoolIndex >= _instructionPool.length - 1) {
			return null;
		} else {
			return _instructionPool[_currentPoolIndex];
		}
	}

	public boolean hasNext() {
		return _currentPoolIndex < _currentPoolSize;
	}

	public RenderInstruction next() {
		_currentPoolIndex++;
		return _instructionPool[_currentPoolIndex];
	}

	public Iterator<RenderInstruction> iterator() {
		return new RenderInstructionBufferIterator(this);
	}

	public boolean isEmpty() {
		return _currentPoolSize == 0;
	}

	public int size() {
		return _currentPoolSize;
	}

	public RenderInstruction get(int index) {
		return _instructionPool[index];
	}

	public void AddRenderSimpleSpriteInstruction(float x, float y, float angle,
			float scaleX, float scaleY, int currentFrameSet, int currentFrame,
			GameEntityRenderable renderable) {
		AddRenderSimpleSpriteInstruction(x, y, angle, scaleX, scaleY, false,
				currentFrameSet, currentFrame, renderable);
	}

	public void AddRenderSimpleSpriteInstruction(float x, float y, float angle,
			float scaleX, float scaleY, boolean inViewSpace,
			int currentFrameSet, int currentFrame,
			GameEntityRenderable renderable) {
		if (_currentPoolIndex >= _instructionPool.length) {
			Log.e("FIX NEEDED", "Instruction pool size exceeded");
			return;
		}

		_instructionPool[_currentPoolIndex].Position.x = x;
		_instructionPool[_currentPoolIndex].Position.y = y;
		_instructionPool[_currentPoolIndex].Scale.x = scaleX;
		_instructionPool[_currentPoolIndex].Scale.y = scaleY;
		_instructionPool[_currentPoolIndex].Angle = angle;
		_instructionPool[_currentPoolIndex].InViewSpace = inViewSpace;
		_instructionPool[_currentPoolIndex].CurrentFrameSet = currentFrameSet;
		_instructionPool[_currentPoolIndex].CurrentFrame = currentFrame;
		_instructionPool[_currentPoolIndex].InstructionType = RenderInstruction.InstructionTypes.RENDER_SIMPLE_SPRITE;
		_instructionPool[_currentPoolIndex].Renderable = renderable;

		_currentPoolIndex++;
		_currentPoolSize++;
	}

	public void AddRenderTextInWorldSpace(StringBuilder message,
			Vector2D bottomLeft, Vector2D topRight) {
		if (_currentPoolIndex >= _instructionPool.length) {
			Log.e("FIX NEEDED", "Instruction pool size exceeded");
			return;
		}

		_instructionPool[_currentPoolIndex].Position.x = bottomLeft.x;
		_instructionPool[_currentPoolIndex].Position.y = bottomLeft.y;
		_instructionPool[_currentPoolIndex].Position2.x = topRight.x;
		_instructionPool[_currentPoolIndex].Position2.y = topRight.y;
		_instructionPool[_currentPoolIndex].InstructionType = RenderInstruction.InstructionTypes.RENDER_TEXT_IN_WORLD_SPACE;
		_instructionPool[_currentPoolIndex].Text = message;

		_currentPoolIndex++;
		_currentPoolSize++;

	}

}
