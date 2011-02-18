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

/**
 * Iterates over a RenderInstructionBuffer
 * 
 */
public class RenderInstructionBufferIterator implements
		Iterator<RenderInstruction> {
	RenderInstructionBuffer _buffer;
	int _currentIndex = 0;

	protected RenderInstructionBufferIterator(RenderInstructionBuffer buffer) {
		_buffer = buffer;
	}

	public boolean hasNext() {
		return _currentIndex <= _buffer.size() - 1;
	}

	public RenderInstruction next() {
		RenderInstruction instruction = _buffer.get(_currentIndex);
		_currentIndex++;
		return instruction;
	}

	public void remove() {
		// TODO Auto-generated method stub

	}

}
