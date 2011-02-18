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
package com.galactogolf;

import android.os.SystemClock;

import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.views.GameRenderer;

/***
 * Main game thread, uses same model as the replica island threading mechanism,
 * e.g. two main threads, this one and the rendering thread. This thread is
 * blocked while waitTillDrawingComplete runs because of the synchronised
 * section in onDrawframe in GameRenderer
 * 
 */
public class GameThread implements Runnable {

	private long _prevTime;

	private GameWorld _world;
	private GameRenderer _renderer;

	public GameThread(GameWorld world, GameRenderer renderer) {
		_world = world;
		_renderer = renderer;
		_prevTime = SystemClock.uptimeMillis();
	}

	public void run() {

		boolean finished = false;
		while (!_renderer.isSetupComplete()) {

		}

		while (!finished) {
			_renderer.waitTillDrawingComplete();
			_world.doPreFrameSetup();
			final long time = SystemClock.uptimeMillis();
			final long timeDelta = time - _prevTime;
			long finalDelta = timeDelta;
			if (timeDelta > 12) {
				_prevTime = time;
				_world.Render(timeDelta); // send instructions to the render
											// pool
				_world.RunPhysics(timeDelta);
				_renderer.SwapRenderInstructionPool();

				final long endTime = SystemClock.uptimeMillis();

				finalDelta = endTime - time;
			}
			if (finalDelta < 16) {
				try {
					Thread.sleep(16 - finalDelta);
				} catch (InterruptedException e) {
				}
			}

		}

	}

}
