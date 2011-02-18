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

import android.util.Log;

import com.galactogolf.genericobjectmodel.Vector2D;

/**
 * Represents a render instruction which is passed to the renderer for drawing
 * 
 */
public class RenderInstruction {

	public class InstructionTypes {
		public static final int RENDER_SIMPLE_SPRITE = 1;
		public static final int RENDER_TEXT_IN_VIEW_SPACE = 3;
		public static final int RENDER_TEXT_IN_WORLD_SPACE = 4;
	}

	public int InstructionType;

	public GameEntityRenderable Renderable;

	public Vector2D Position;
	public float Angle;
	public StringBuilder Text;
	public int CurrentFrameSet;
	public int CurrentFrame;
	public Vector2D Position2;
	public Vector2D Scale;

	public boolean InViewSpace;

	public RenderInstruction() {
		Position = new Vector2D();
		Position2 = new Vector2D();
		Scale = new Vector2D();
	}

	public void render(GameRenderer renderer) {
		switch (InstructionType) {
		case InstructionTypes.RENDER_SIMPLE_SPRITE:
			renderSimpleSprite(renderer);
			break;
		case InstructionTypes.RENDER_TEXT_IN_VIEW_SPACE:
			renderTextInViewSpace(renderer);
			break;
		case InstructionTypes.RENDER_TEXT_IN_WORLD_SPACE:
			renderTextInWorldSpace(renderer);
			break;

		default:
			Log.e("rendering error",
					"Found an instruction that we didn't know how to process");
			break;
		}

	}

	private void renderTextInWorldSpace(GameRenderer renderer) {
		renderer.GetTextRenderer().drawTextInWorldSpace(renderer.GetGL(),
				Position.x, Position.y, Text);

	}

	private void renderTextInViewSpace(GameRenderer renderer) {
		renderer.GetTextRenderer().drawTextInViewSpace(renderer.GetGL(),
				Position.x, Position.y, Text);

	}

	private void renderSimpleSprite(GameRenderer renderer) {
		Renderable.render(renderer.GetGL(), Position.x, Position.y, Angle,
				Scale.x, Scale.y, InViewSpace, CurrentFrameSet, CurrentFrame);

	}

}
