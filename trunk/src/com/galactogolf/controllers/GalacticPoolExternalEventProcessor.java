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
package com.galactogolf.controllers;

import com.galactogolf.genericobjectmodel.ExternalGameEvent;
import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.Vector3D;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.specificobjectmodel.GalactoGolfPlayerEntity;
import com.galactogolf.specificobjectmodel.GalactoGolfWorld;
import com.galactogolf.specificobjectmodel.GameConstants;

/*
 * Processed events that happen when the player is playing the game
 */
public class GalacticPoolExternalEventProcessor extends ExternalEventProcessor {

	Vector2D _touchLocation;

	boolean _touchingPlayer = false;
	boolean _zooming = false;
	boolean _ableToEdit = true;

	public GalacticPoolExternalEventProcessor(GameWorld world) {
		super(world);
	}

	@Override
	protected void onMenuItemSelected(int menuId) {
		switch (menuId) {
		case GameConstants.MENU_RETRY_LEVEL:
			reset(); // player is still playing, we need to incrememt the score
						// etc.
			break;
		case GameConstants.MENU_REPLAY_LEVEL:
			_world.reset(); // player has completed level, we just wanty to
							// replay
			break;
		case GameConstants.MENU_NEXT_LEVEL:
			try {
				((GalactoGolfWorld) _world).LoadNextLevel();
			} catch (LevelLoadingException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onDown(float x, float y) {
		if(((GalactoGolfWorld)_world).isRunningPhysics()) {
		// first determine if user is pressing the reset button
		float distFromResetButton = (new Vector2D(x - 420, y - (_world.GetScreenHeight()-32)))
				.magnitude();
		if (distFromResetButton < 40 && !(((GalactoGolfWorld)_world).isLevelCompleted())) {
			reset(); // player is still playing, we need to incrememt the score
						// etc.

		} 
		} 
		else {
			// determine if we are touching the player
			_touchLocation = getTouchLocation(x, y);
			Vector2D pullHandleLocation = new Vector2D(_world.GetPlayer().getPosition().x,_world.GetPlayer().getPosition().y);

			if (Vector2D.Sub(pullHandleLocation, _touchLocation)
					.magnitude() < 100 * _globalScaleFactor) {
				_touchingPlayer = true;
				((GalactoGolfWorld) _world).setUserIsSettingPower(true);
				((GalactoGolfWorld) _world).getProbePowerLine().getStart().x = _world
						.GetPlayer().getPosition().x;
				((GalactoGolfWorld) _world).getProbePowerLine().getStart().y = _world
						.GetPlayer().getPosition().y;

				((GalactoGolfWorld) _world).getProbePowerLine().getEnd().x = _world
						.GetPlayer().getPosition().x;
				((GalactoGolfWorld) _world).getProbePowerLine().getEnd().y = _world
						.GetPlayer().getPosition().y;

			} else {
				_touchingPlayer = false;
				((GalactoGolfWorld) _world).setUserIsSettingPower(false);
			}
		}
		}
	


	@Override
	public void onScroll(float distanceX, float distanceY) {
		if (!_zooming) {
			if (!_touchingPlayer) {

				_world.setCameraScrolling(distanceX,distanceY);

			} else if(!((GalactoGolfWorld)_world).isRunningPhysics()){
				// average out over two frames to smooth the input
				float newX = ((GalactoGolfWorld) _world).getProbePowerLine()
						.getEnd().x
						- distanceX;
				float newY = ((GalactoGolfWorld) _world).getProbePowerLine()
						.getEnd().y
						- distanceY;
				((GalactoGolfWorld) _world).getProbePowerLine().getEnd().x = newX;
				((GalactoGolfWorld) _world).getProbePowerLine().getEnd().y = newY;
				((GalactoGolfPlayerEntity) _world.GetPlayer()).getVelocity().x = (((GalactoGolfWorld) _world)
						.getProbePowerLine().getStart().x - ((GalactoGolfWorld) _world)
						.getProbePowerLine().getEnd().x);
				((GalactoGolfPlayerEntity) _world.GetPlayer()).getVelocity().y = (((GalactoGolfWorld) _world)
						.getProbePowerLine().getStart().y - ((GalactoGolfWorld) _world)
						.getProbePowerLine().getEnd().y);

			}
		} else {
			_zooming = false;
		}
	}

	@Override
	public void onTouchUp(float x, float y) {
		if (_touchingPlayer&&!((GalactoGolfWorld)_world).isRunningPhysics()) {
			// user has released player, set them moving!
			_touchingPlayer = false;
			_touchLocation = getTouchLocation(x, y);

			Vector2D playerSpeed = new Vector2D(( ((GalactoGolfWorld) _world).getProbePowerLine()
					.getEnd().x- ((GalactoGolfWorld) _world).getProbePowerLine()
					.getStart().x), 
					( ((GalactoGolfWorld) _world).getProbePowerLine()
							.getEnd().y- ((GalactoGolfWorld) _world).getProbePowerLine()
							.getStart().y)
						);

			float playerSpeedMag = playerSpeed.magnitude();
			float powerMag = playerSpeedMag;
			// remove the first 8 pixels of a player dragging the arrow
			// backwards, to account for my big fingers
			if (powerMag > 8) {
				powerMag = powerMag - 8;
				float powerScale = (powerMag / 100) / _globalScaleFactor;
				playerSpeed.x = (playerSpeed.x /playerSpeedMag) * powerScale;
				playerSpeed.y = (playerSpeed.y /playerSpeedMag) * powerScale;
			} else {
				playerSpeed.x = 0;
				playerSpeed.y = 0;
			}
	
			((GalactoGolfPlayerEntity) _world.GetPlayer()).getVelocity().x = -playerSpeed.x;
			((GalactoGolfPlayerEntity) _world.GetPlayer()).getVelocity().y = -playerSpeed.y;
			((GalactoGolfPlayerEntity) _world.GetPlayer()).getVelocity().x = -playerSpeed.x;
			((GalactoGolfWorld) _world).setUserIsSettingPower(false);

			((GalactoGolfWorld) _world).startPhysics();
		}
		else {
			_world.setUserScrolling(false);
		}

	}

	@Override
	public void onSingleTapUp(float screenX, float screenY) {

	}

	@Override
	public void onLongPress(float x, float y) {
		
		reset();
	}
	private void reset() {
		if (((GalactoGolfWorld) _world).runningPhysics()) {
//			_world.PlayerDied();
			((GalactoGolfPlayerEntity) _world.GetPlayer()).explode(null);
//			_world.reset();
		}

	}

	@Override
	public void onScale(float newScaleFactor) {
		_zooming = true;
		_globalScaleFactor = _globalScaleFactor / newScaleFactor;
		if (_globalScaleFactor < 0.5) {
			_globalScaleFactor = 0.5f;
		}
		else if(_globalScaleFactor > 2.0f) {
			_globalScaleFactor = 2.0f;
		}
			
			Vector3D currPos = _world.getCameraLocation();
		_world.setCameraLocation(new Vector3D(currPos.x, currPos.y,
				1 / _globalScaleFactor));

		

	
	}
	

}
