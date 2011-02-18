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

package com.galactogolf.specificobjectmodel;

import java.util.ArrayList;
import java.util.Random;


import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.galactogolf.GameActivity;
import com.galactogolf.database.DatabaseAdapter;
import com.galactogolf.database.DatabaseException;
import com.galactogolf.genericobjectmodel.GameEntity;
import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.NonPlayerEntity;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSet;
import com.galactogolf.serialization.JSONSerializer;
import com.galactogolf.views.RenderInstructionBuffer;

/**
 * Main class for Galacto Golf game, includes specific logic for this game
 * 
 */
public class GalactoGolfWorld extends GameWorld {

	private InfoBarSprite _infoBar;
	
	public GalactoGolfWorld(GameActivity context) {
		super(context);
		_powerHandle = new PowerHandleSprite();
		_resetButton = new ResetButtonSprite();
		_infoBar = new InfoBarSprite();
	}

	@Override
	public void removeNpc(NonPlayerEntity ent) {
		
		super.removeNpc(ent);
		if(_gravityWells.contains(ent)) { 
			_gravityWells.remove(ent);
		}
	}

	private PowerHandleSprite _powerHandle;

	protected ArrayList<NonPlayerEntity> _gravityWells;
	protected ArrayList<StarSprite> _stars;

	@Override
	public void addNpc(NonPlayerEntity p) {
		super.addNpc(p);
		if ((p instanceof GravitationalEntity)) {
			_gravityWells.add(p);
		}
	}

	protected LevelSet _levelSet;
	protected ArrowSprite _powerArrow;
	protected ResetButtonSprite _resetButton;

	public ArrayList<StarSprite> getStars() {
		return _stars;
	}

	protected LineElement _probePowerLine;

	protected boolean _runningPhysics = false;
	private boolean _isEditing = false;
	private int _score;
	private boolean _completed = false;
	private GameEntity _selectedEntity;
	private int _bonus;
	private HazeSprite _hazeSprite;

	public void LoadFirstLevel() throws LevelLoadingException {
		this.loadLevelNextFrame(_levelSet.getNextLevel());
	}

	public boolean isRunningPhysics() {
		return _runningPhysics;
	}

	public void OnLevelCompleted() throws LevelLoadingException {
		_completed = true;
		_runningPhysics = false;
		DatabaseAdapter adapter = new DatabaseAdapter(_parentActivity);
		adapter.open();
		int attemptNumber;
		try {
			attemptNumber = GalactoGolfLevelResult.getLastRoundNumber(
					adapter.getOpenDB(), _levelSet.getId()) + 1;
		} catch (DatabaseException ex) {
			Log.e("Could not log score", ex.getMessage());
			return;
		}
		GalactoGolfLevelResult result = new GalactoGolfLevelResult(
				_levelSet.getId(), _levelSet.getCurrentLevelNumber(),
				getBonus(), GetScore(), _previousPowerLine,
				_previousPowerLineAngle, attemptNumber);
		_levelSet.getCurrentLevel().Complete(result);

		if (!_levelSet.OnLastLevel()) {
			_parentActivity.ShowLevelCompleteDialog();
		} else {
			_parentActivity.ShowLevelSetCompleteDialog();
			try {
				_levelSet.saveScores(_parentActivity);
			} catch (LevelSavingException ex) {
				Log.e("Level Score Saving", ex.getMessage());
			}

		}
		this.getCameraLocation().x = 0;
		this.getCameraLocation().y = 0;
		this.getCameraLocation().z = 1.0f;

	}

	public boolean userIsSettingPower() {
		return _userIsSettingPower;
	}

	public void setUserIsSettingPower(boolean value) {
		_userIsSettingPower = value;
	}

	private boolean _userIsSettingPower;

	@Override
	public void LoadLevel(LevelDefinition level) throws LevelLoadingException {
		_renderablesLoaded = false;
		_runningPhysics = false;
		_previousPowerLineSet = false;
		super.LoadLevel(level);

		_probePowerLine = new LineElement();

		this._player = new GalactoGolfPlayerEntity(this);
		((GalactoGolfPlayerEntity) this._player).getVelocity().x = 0.0f;
		((GalactoGolfPlayerEntity) this._player).getVelocity().y = 0.1f;
		this._player.getPosition().x = 600.0f;
		this._player.getPosition().y = 350.0f;

		_gravityWells = new ArrayList<NonPlayerEntity>();
		LevelDefinitionWorldConverter.LoadLevelDefinitionIntoWorld(level, this);

		// generate star field
		Random rnd = new Random();
		_stars = new ArrayList<StarSprite>();
		for (int i = 0; i < 200; i++) {
			StarSprite star;
			if(i%2==0) {
				star = new StarSprite();
			}
			else {
				star = new StarSprite2();
				
			}
			star.setPosition(new Vector2D(rnd.nextFloat() * 2000 - 1000, rnd
					.nextFloat() * 2000 - 1000));
			_stars.add(star);
		}

		_hazeSprite = new HazeSprite();

		_score = 0;
		_bonus = 0;
		_powerArrow = new ArrowSprite();
		_powerArrow.getPosition().x = this.GetPlayer().getPosition().x;
		_powerArrow.getPosition().y = this.GetPlayer().getPosition().y;

		if (level.getDescription() != null
				&& level.getDescription().length() > 0) {
			_parentActivity.ShowMessagePopup(level.getName(),
					level.getDescription());
		}
	}

	@Override
	public void PlayerDied() {
		super.PlayerDied();
	}

	@Override
	public void RunPhysics(long timeSinceLastFrame) {
		_externalEventProcessor.processExternalEvents();

		if (_completed) {
			_completed = false;
			stopPhysics();

		}
		if (_runningPhysics) {
			super.RunPhysics(timeSinceLastFrame);
		}
	}

	@Override
	public void Render(long timeSinceLastFrame) {
		super.Render(timeSinceLastFrame);
		RenderInstructionBuffer instructionList = _renderer
				.GetOpenRenderInstructionBuffer();

		// work out which tiles to draw based on the mininum x & y corner, then
		// draw four tiles to cover the other parts
		// of the screen. NB this only works if the screen is small enough so
		// four repeated tiles can cover
		// the whole screen
		float hazeScale = 2.0f;
		float tileWidth = _hazeSprite.GetRenderable().getWidth() * hazeScale;
		float tileHeight = _hazeSprite.GetRenderable().getHeight() * hazeScale;
		float minX = (float) (Math
				.floor((getCameraLocation().x - GetScreenWidth() / 2)
						/ tileWidth) * tileWidth);
		float minY = (float) (Math
				.floor((getCameraLocation().y - GetScreenHeight() / 2)
						/ tileHeight) * tileHeight);
		float halfX = (_hazeSprite.GetRenderable().getWidth()/ 2) * hazeScale;
		float halfY = (_hazeSprite.GetRenderable().getHeight() / 2) * hazeScale;

		// 0,0
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX, minY
				+ halfY, 0.0f, hazeScale, hazeScale,
				_hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());
		// 1,0
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX
				+ tileWidth, minY + halfY, 0.0f, hazeScale, hazeScale,
				_hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());
		// 0,1
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX, minY
				+ halfY + tileHeight, 0.0f, hazeScale, hazeScale,
				_hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());

		// 1,1
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX
				+ tileWidth, minY + halfY + tileHeight, 0.0f, hazeScale,
				hazeScale, _hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());

		// -1,0
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX
				- tileWidth, minY + halfY, 0.0f, hazeScale, hazeScale,
				_hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());
		// 0,-1
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX, minY
				+ halfY - tileHeight, 0.0f, hazeScale, hazeScale,
				_hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());

		// -1,-1
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX
				- tileWidth, minY + halfY - tileHeight, 0.0f, hazeScale,
				hazeScale, _hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());

		// -1,+1
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX
				- tileWidth, minY + halfY + tileHeight, 0.0f, hazeScale,
				hazeScale, _hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());

		// +1,-1
		instructionList.AddRenderSimpleSpriteInstruction(minX + halfX
				+ tileWidth, minY + halfY - tileHeight, 0.0f, hazeScale,
				hazeScale, _hazeSprite.GetCurrentFrameSet(),
				_hazeSprite.GetCurrentFrame(), _hazeSprite.GetRenderable());

		
		
		synchronized (getStars()) {

			for (StarSprite sprite : getStars()) {

				if (sprite.visible(getCameraLocation().x
						- (GetScreenWidth()/getCameraLocation().z / 2), getCameraLocation().y
						- (GetScreenHeight()/getCameraLocation().z / 2), getCameraLocation().x
						+ (GetScreenWidth()/getCameraLocation().z / 2), getCameraLocation().y
						+ (GetScreenHeight()/getCameraLocation().z / 2))) {

					instructionList.AddRenderSimpleSpriteInstruction(
							sprite.getPosition().x, sprite.getPosition().y,
							0.0f, sprite.GetCurrentFrameSet(),
							sprite.GetCurrentFrame(), sprite.GetRenderable());
				}
			}
		}

		for (NonPlayerEntity ent : getNpcs()) {
			if (ent.isVisibleAndInFrustrum(getCameraLocation().x
					- (GetScreenWidth()/getCameraLocation().z / 2), getCameraLocation().y
					- (GetScreenHeight()/getCameraLocation().z / 2), getCameraLocation().x
					+ (GetScreenWidth()/getCameraLocation().z / 2), getCameraLocation().y
					+ (GetScreenHeight()/getCameraLocation().z / 2)
			/*
			 * -_world.getCameraLocation().x, -_world.getCameraLocation().y,
			 * _world.GetScreenWidth() - _world.getCameraLocation().x ,
			 * _world.GetScreenHeight() - _world.getCameraLocation().y
			 */
			)) {
				ent.render(instructionList, timeSinceLastFrame);
			}
		}
		GetPlayer().render(instructionList, timeSinceLastFrame);
		instructionList.AddRenderSimpleSpriteInstruction(GetScreenWidth()/2, GetScreenHeight()-32,
				_infoBar.getAngle(), GetScreenWidth()/16.0f, 1.0f, true, 0, 0,
				_infoBar.GetRenderable());

		if (!_isEditing
				&& !((GalactoGolfPlayerEntity) GetPlayer())
						.HasPlayerHitWormhole()) {
			if (!runningPhysics()) {
				// draw the power handle and the reset button
				if (userIsSettingPower()) {
					_powerHandle.getPosition().x = _probePowerLine.getEnd().x;
					_powerHandle.getPosition().y = _probePowerLine.getEnd().y;
				} else {
					_powerHandle.getPosition().x = _player.getPosition().x;
					_powerHandle.getPosition().y = _player.getPosition().y + 100;
					instructionList.AddRenderSimpleSpriteInstruction(
							_powerHandle.getPosition().x,
							_powerHandle.getPosition().y,
							_powerHandle.getAngle(), 1.0f, 1.0f, 0, 0,
							_powerHandle.GetRenderable());
				}

			}

	
			instructionList.AddRenderSimpleSpriteInstruction(GetScreenWidth()-64, GetScreenHeight()-32,
					_resetButton.getAngle(), 1.0f, 1.0f, true, 0, 0,
					_resetButton.GetRenderable());

			

		}

		// pool specific, draw the power lines if needed
		if (userIsSettingPower()) {
			_currentPowerLine = Vector2D.Sub(_probePowerLine.getEnd(),
					_probePowerLine.getStart()).magnitude();
			// getProbePowerLine().render(gl);
			_currentPowerLine = _currentPowerLine / 50 + 0.1f;
			instructionList.AddRenderSimpleSpriteInstruction(
					_player.getPosition().x, _player.getPosition().y,
					_player.getAngle(), 1.0f, _currentPowerLine,
					GameConstants.POWER_ARROW_RED_FRAMESET, 0,
					_powerArrow.GetRenderable());
			// set the previous power line

		}

		if (_previousPowerLineSet) {
			instructionList.AddRenderSimpleSpriteInstruction(
					_previousPowerLineLocation.x, _previousPowerLineLocation.y,
					_previousPowerLineAngle, 1.0f, _previousPowerLine,
					GameConstants.POWER_ARROW_BLUE_FRAMESET,
					_powerArrow.GetCurrentFrame(), _powerArrow.GetRenderable());
		}

		if (isUserSelectingEntity()) {
			instructionList.AddRenderSimpleSpriteInstruction(
					_selectedEntity.getPosition().x,
					_selectedEntity.getPosition().y - 150, (float) 180, 1.0f,
					1.0f, _powerArrow.GetCurrentFrameSet(),
					_powerArrow.GetCurrentFrame(), _powerArrow.GetRenderable());

		}
	
	}

	private float _currentPowerLine;
	public boolean _previousPowerLineSet = false;
	private float _previousPowerLine;
	private float _previousPowerLineAngle;
	private Vector2D _previousPowerLineLocation;

	public void copyCurrentPowerLineToPrevious() {
		_previousPowerLineSet = true;
		_previousPowerLine = _currentPowerLine;
		_previousPowerLineAngle = _player.getAngle();
		_previousPowerLineLocation = new Vector2D(_player.getPosition().x,
				_player.getPosition().y);
	}

	public void setSelectedEntity(GameEntity ent) {
		_selectedEntity = ent;
	}

	private boolean isUserSelectingEntity() {
		if (_selectedEntity == null) {
			return false;
		} else {
			return true;
		}
	}

	public void startPhysics() {
		_runningPhysics = true;
		copyCurrentPowerLineToPrevious();
	}

	public boolean runningPhysics() {
		return _runningPhysics;
	}

	public void stopPhysics() {
		_runningPhysics = false;
	}

	@Override
	public void addNpcNextFrame(NonPlayerEntity p) {

		super.addNpcNextFrame(p);

	}

	public ArrayList<NonPlayerEntity> GetGravityWells() {
		return _gravityWells;
	}

	public LineElement getProbePowerLine() {
		return _probePowerLine;
	}

	@Override
	public void reset() {
		this.stopPhysics();
		super.reset();
		try {
			this.GetPlayer().getPosition().x = _levelSet.getCurrentLevel()
					.GetPlayerDefinition().p1.x;
			this.GetPlayer().getPosition().y = _levelSet.getCurrentLevel()
					.GetPlayerDefinition().p1.y;
			this.GetPlayer().reset();
			if(Math.abs(this.getCameraLocation().x-this.GetPlayer().getPosition().x)>_renderer.GetScreenWidth() ||
			   Math.abs(this.getCameraLocation().y-this.GetPlayer().getPosition().y)>_renderer.GetScreenHeight()) {
				this.getCameraLocation().x = 0.0f;
				this.getCameraLocation().y = 0.0f;
			}
			this.getCameraLocation().z = 1.0f;

		} catch (LevelLoadingException e) {
			Log.e("Exception", e.getMessage());
		}

		for (NonPlayerEntity star : this
				.getObjectsOfType(BonusStarEntity.class)) {
			((BonusStarEntity) star).setVisible(true);
		}

	}

	public int GetScore() {
		return _score;
	}

	public int getPar() {
		try {
			return _levelSet.getCurrentLevel().getPar();
		} catch (LevelLoadingException e) {
			Log.e("Exception Getting Level", "Could not get current level");
			return -1;
		}
	}

	public int getBonus() {
		return _bonus;
	}

	public void AssignRenderables() {
		// run NPC behaviour logic
		synchronized (_npcs) {

			for (NonPlayerEntity ent : _npcs) {
				ent.SetRenderable(GetRenderer().GetRenderables().get(
						ent.getClass().getName()));

			}
		}
		synchronized (getStars()) {

			for (StarSprite sprite : getStars()) {
				sprite.SetRenderable(GetRenderer().GetRenderables().get(
						sprite.getClass().getName()));

			}
		}
		_hazeSprite.SetRenderable(GetRenderer().GetRenderables().get(
				_hazeSprite.getClass().getName()));
		_player.SetRenderable(GetRenderer().GetRenderables().get(
				_player.getClass().getName()));
		_powerArrow.SetRenderable(GetRenderer().GetRenderables().get(
				_powerArrow.getClass().getName()));
		_powerHandle.SetRenderable(GetRenderer().GetRenderables().get(
				_powerHandle.getClass().getName()));
		_resetButton.SetRenderable(GetRenderer().GetRenderables().get(
				_resetButton.getClass().getName()));
		_infoBar.SetRenderable(GetRenderer().GetRenderables().get(
				_infoBar.getClass().getName()));

		_renderablesLoaded = true;
	}

	public void SetScore(int i) {
		_score = i;

	}

	/*
	 * Saves the level into the in memory level set, so it can be played. Saving
	 * permanently is done by the level set
	 */
	public void saveLevelToMemory() throws LevelSavingException {
		_levelSet.setCurrentLevel(LevelDefinitionWorldConverter
				.WorldIntoLevelDefinition(this));
	}

	public void LoadNextLevel() throws LevelLoadingException {
		loadLevelNextFrame(_levelSet.getNextLevel());
		GetPlayer().reset();
	}
	
	public int getCurrentLevelNumber() {
		return _levelSet.getCurrentLevelNumber();
	}

	public void LoadPrevLevel() throws LevelLoadingException {
		loadLevelNextFrame(_levelSet.getPrevLevel());

	}

	public void LoadLevelSet(LevelSet getLevelSet) {
		_levelSet = getLevelSet;
		String s;
		try {
			s = JSONSerializer.toJSON(_levelSet).toString();
			Log.d("Level JSON", s);
			LevelSet jsonSet = JSONSerializer
					.fromLevelSetJSON(new JSONObject(s));
			_levelSet = jsonSet;
		} catch (JSONException e) {
			Log.e("Exception", e.getMessage());
		}
	}

	public void setIsEditing(boolean isEditing) {
		_isEditing = isEditing;
	}

	public void CreateNewLevel() {
		_levelSet.add(new LevelDefinition("New Level"));

	}

	public boolean isAtLastLevel() {
		return _levelSet.OnLastLevel();
	}

	public void saveLevelSetToInternalStorage() throws LevelSavingException {
		saveLevelToMemory();
		_levelSet.saveToInternalStorage(_parentActivity);
	}

	public void setBonus(int i) {
		_bonus = i;

	}

	public LevelSet getCurrentLevelSet() {
		return _levelSet;
	}

	public boolean isLevelCompleted() {
		return _completed;
	}

}
