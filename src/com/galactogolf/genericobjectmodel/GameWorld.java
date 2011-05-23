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

package com.galactogolf.genericobjectmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.galactogolf.GameActivity;
import com.galactogolf.controllers.ExternalEventProcessor;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.views.GameRenderer;

/**
 * The domain model of the Game World, holds all Game relevant state, e.g the
 * game entities and their interations
 * 
 */
public abstract class GameWorld {

	protected List<NonPlayerEntity> _npcs;
	protected List<NonPlayerEntity> _npcsToAddNextFrame;
	private List<NonPlayerEntity> _npcsToRemoveNextFrame;
	protected PlayerEntity _player;
	protected int _playerLives;
	protected GameRenderer _renderer;
	protected Vector3D _cameraLocation = new Vector3D(0.0f, 0.0f, 1.0f);
	private int _screenHeight;
	private int _screenWidth;
	protected boolean _renderablesLoaded = true;
	protected ExternalEventProcessor _externalEventProcessor;
	protected GameActivity _parentActivity;
	private LevelDefinition _levelToLoad;
	private Vector2D _cameraVelocity;
	private boolean _scrolling;

	public enum CollisionModes {
		BOUNDING_SPHERE, BOUNDING_BOX
	};

	public GameWorld(GameActivity context) {
		_playerLives = 3;
		_parentActivity = context;

		_npcsToAddNextFrame = new ArrayList<NonPlayerEntity>();
		_npcsToRemoveNextFrame = new ArrayList<NonPlayerEntity>();

		_cameraVelocity = new Vector2D();
		resetCameraLocation();

	}
	
	public void resetCameraLocation() {
	    this.getCameraLocation().x = 0;
		this.getCameraLocation().y = 0;
		this.getCameraLocation().z = 1.0f* (GetScreenHeight()/800.0f);
	}


	public GameRenderer GetRenderer() {
		return _renderer;
	}

	public Vector3D getCameraLocation() {
		return _cameraLocation;
	}

	public void setCameraLocation(Vector3D cameraLocation) {
		_cameraLocation = cameraLocation;
	}

	public List<NonPlayerEntity> getNpcs() {
		return _npcs;
	}

	/**
	 * Run world simulation
	 * 
	 * @param timeSinceLastFrame
	 */
	public void RunPhysics(long timeSinceLastFrame) {

		// did we add any npcs in the last frame?

		// process any input

		// move the player
		_player.RunPhysics(timeSinceLastFrame);

		// run NPC behaviour logic
		for (NonPlayerEntity ent : _npcs) {
			ent.RunPhysics(timeSinceLastFrame);
			// ent.getPosition().x += 0.01f;

		}

		// collide the NPCs with the player
		for (NonPlayerEntity ent : _npcs) {
			testForCollision(_player, ent);
			// ent.getPosition().x += 0.01f;

		}

	}

	/**
	 * Calculate collisions between entities, by a simple bounding sphere check
	 * 
	 * @param e1
	 * @param e2
	 */
	private void testForCollision(GameEntity e1, GameEntity e2) {

		boolean collided = false;

		// have the bounding sphere overlapped?
		if (CollisionDetector.isColliding(e1.getBoundingSphere(),
				e2.getBoundingSphere())) {
			if (e1.getCollisionMode() == CollisionModes.BOUNDING_SPHERE) {
				if (e2.getCollisionMode() == CollisionModes.BOUNDING_SPHERE) {
					collided = true;
				} else if (e2.getCollisionMode() == CollisionModes.BOUNDING_BOX) {
					collided = isColliding(e2.getBoundingBox(),
							e1.getBoundingSphere());
				}
			}
		}

		if (collided) {
			e1.HandleCollision(e2);
			e2.HandleCollision(e1);

		}
	}

	/***
	 * Tests intersection between OBB (Oriented Bounding box and sphere)
	 * 
	 * @param boundingBox
	 * @param boundingSphere
	 * @return
	 */
	private boolean isColliding(BoundingBox boundingBox,
			BoundingSphere boundingSphere) {
		return CollisionDetector.isColliding(boundingSphere, boundingBox);
	}

	/**
	 * Player has died, work out what to do
	 */
	public void PlayerDied() {

	}

	public void LoadLevel(LevelDefinition level) throws LevelLoadingException {
		_npcs = new Vector<NonPlayerEntity>();
		_npcsToAddNextFrame = new Vector<NonPlayerEntity>();
		_npcsToRemoveNextFrame = new Vector<NonPlayerEntity>();
		_renderablesLoaded = false;
	}

	/**
	 * Adds an npc, NB actually adds these to a list to add the next frame, to
	 * prevent synchronisation issues
	 * 
	 * @param p1
	 */
	public void addNpcNextFrame(NonPlayerEntity npc) {

		_npcsToAddNextFrame.add(npc);
		if (RenderablesLoaded()) {
			npc.SetRenderable(GetRenderer().GetRenderables().get(
					npc.getClass().getName()));
		}

	}

	public void addNpc(NonPlayerEntity npc) {

		_npcs.add(npc);

	}

	public void loadLevelNextFrame(LevelDefinition level) {
		_levelToLoad = level;
	}

	public void SetRenderer(GameRenderer gameRenderer) {
		_renderer = gameRenderer;

	}

	public PlayerEntity GetPlayer() {
		return _player;
	}

	public void reset() {

	}

	public void SetScreenWidth(int width) {
		_screenWidth = width;
	}

	public void SetScreenHeight(int height) {
		_screenHeight = height;
	}

	public int GetScreenWidth() {
		return _screenWidth;
	}

	public int GetScreenHeight() {
		return _screenHeight;
	}

	public boolean RenderablesLoaded() {
		return _renderablesLoaded;
	}

	public void Render(long timeSinceLastFrame) {
		// run scrolling

		_cameraLocation.Add(_cameraVelocity);

		if (_scrolling) {
			_cameraVelocity.x = 0;
			_cameraVelocity.y = 0;
		} else {
			// now damp the scroll
			_cameraVelocity.x = _cameraVelocity.x / 1.1f;
			_cameraVelocity.y = _cameraVelocity.y / 1.1f;
		}
	}

	public void RegisterExternalEventProcessor(
			ExternalEventProcessor externalEventProcessor) {
		_externalEventProcessor = externalEventProcessor;

	}

	public ArrayList<NonPlayerEntity> getObjectsOfType(Class entityClass) {
		ArrayList<NonPlayerEntity> entitiesOfCorrectType = new ArrayList<NonPlayerEntity>();
		for (NonPlayerEntity ent : getNpcs()) {
			if (entityClass.isInstance(ent)) {
				entitiesOfCorrectType.add(ent);

			}
		}
		return entitiesOfCorrectType;
	}

	/**
	 * does set up before running a frame
	 */
	public void doPreFrameSetup() {
		if (_npcsToAddNextFrame.size() > 0) {
			for (NonPlayerEntity ent : _npcsToAddNextFrame) {
				addNpc(ent);
			}
			_npcsToAddNextFrame.clear();
		}
		if (_npcsToRemoveNextFrame.size() > 0) {
			_npcs.removeAll(_npcsToRemoveNextFrame);
			_npcsToRemoveNextFrame.clear();
		}

		if (_levelToLoad != null) {
			try {
				LoadLevel(_levelToLoad);

			} catch (LevelLoadingException e) {
				Log.e("Exception",e.getMessage());
			}
			_levelToLoad = null;
			this.AssignRenderables();

		}

	}

	abstract protected void AssignRenderables();

	public void removeNpc(NonPlayerEntity ent) {
		_npcsToRemoveNextFrame.add(ent);

	}

	public void setUserScrolling(boolean scrolling) {
		_scrolling = scrolling;
	}

	public void setCameraScrolling(float distanceX, float distanceY) {
		_scrolling = true;
		_cameraVelocity.x += distanceX;
		_cameraVelocity.y += distanceY;

	}

}
