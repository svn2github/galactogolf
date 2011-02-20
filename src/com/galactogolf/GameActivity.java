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

import com.galactogolf.controllers.ExternalEventProcessor;
import com.galactogolf.controllers.GalacticPoolController;
import com.galactogolf.controllers.GalacticPoolEditorController;
import com.galactogolf.controllers.GalacticPoolEditorExternalEventProcessor;
import com.galactogolf.controllers.GalacticPoolExternalEventProcessor;
import com.galactogolf.controllers.GameController;
import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSet;
import com.galactogolf.specificobjectmodel.GalactoGolfWorld;
import com.galactogolf.specificobjectmodel.TestLevels;
import com.galactogolf.views.GameView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

/*
 * The main Activity for the actual Game 
 */
public class GameActivity extends Activity {

	public static final String LEVEL_SET_FILENAME = "LEVEL_SET_FILENAME";
	public static final String LEVEL_SET_RESOURCE_ID = "LEVEL_SET_RESOURCE_ID";

	public static final String EDIT_LEVEL_FLAG = "EDIT_LEVEL_FLAG";

	public static GameWorld currentWorld;

	private GameView _view;

	private GalactoGolfWorld _world;
	private GameController _controller;
	private ExternalEventProcessor _eventProcessor;

	private Game _game;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserPreferences.init(this.getApplicationContext());

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int defaultWidth = 480;
		int defaultHeight = 320;
		if (dm.widthPixels != defaultWidth) {
			float ratio = ((float) dm.widthPixels) / dm.heightPixels;
			defaultWidth = (int) (defaultHeight * ratio);
		}
		Bundle extras = getIntent().getExtras();

		_world = new GalactoGolfWorld(this);
		_eventProcessor = new GalacticPoolExternalEventProcessor(_world);
		_controller = new GalacticPoolController(this, _eventProcessor);

		_game = new Game(_world);
		_game.setup(this, dm.widthPixels, dm.heightPixels, defaultWidth,
				defaultHeight);
		_view = new GameView(this, _controller, _world);
		setContentView(_view);
		_view.requestFocus();

		String _levelSetFilename = extras != null ? extras
				.getString(LEVEL_SET_FILENAME) : null;
		Integer _levelSetResourceId = extras != null ? extras
				.getInt(LEVEL_SET_RESOURCE_ID) : null;

		try {
			if (_levelSetFilename != null) {
				LevelSet levelSet = LevelSet.loadLevelSetFromInternalStorage(
						_levelSetFilename, this);
				_world.LoadLevelSet(levelSet);
			} else if (_levelSetResourceId != 0) {
				LevelSet levelSet = LevelSet.loadLevelSetFromRaw(this,
						_levelSetResourceId);
				_world.LoadLevelSet(levelSet);
			} else {
				_world.LoadLevelSet(TestLevels.GetLevelSet());
			}

			if (extras != null && extras.getBoolean(EDIT_LEVEL_FLAG)) {

				EnableEditor();
			}

			_world.LoadFirstLevel();

		} catch (LevelLoadingException ex) {
			Log.e("Exception", ex.getMessage());
		}

		if (UserPreferences.MethodTracingEnabled()) {
			Debug.startMethodTracing("galacto");
		}
		_game.start();
		currentWorld = _world;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		_controller.onPrepareOptionsMenu(menu);

		return true;
	}

	@Override
	protected void onDestroy() {
		if (UserPreferences.MethodTracingEnabled()) {
			Debug.stopMethodTracing();
		}
		super.onDestroy();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		_controller.onMenusItemSelected(item.getItemId());
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Replaces the current controller, used when switching to level editor
	 * 
	 * @param controller
	 */
	public void SetController(GameController controller) {
		_controller = controller;
		_view.SetController(controller);
	}

	public void EnableEditor() {
		_eventProcessor = new GalacticPoolEditorExternalEventProcessor(_world);
		SetController(new GalacticPoolEditorController(this, _eventProcessor));
		_world.setIsEditing(true);

	}

	public void ShowMessagePopup(String title, String body) {
		Intent i = new Intent(this, MessagePopupActivity.class);
		i.putExtra(UIConstants.PARAM_MESSAGE_TITLE, title);
		i.putExtra(UIConstants.PARAM_MESSAGE_BODY, body);
		startActivityForResult(i, UIConstants.MESSAGE_POPUP_ACTIVITY);
	}

	public void ShowLevelCompleteDialog() {
		Intent i = new Intent(this, LevelCompletedDialogActivity.class);
		startActivityForResult(i, UIConstants.LEVEL_COMPLETED_ACTIVITY);

		// return i.getBooleanExtra(UIConstants.REPLAY_LEVEL, false);

	}

	public void ShowLevelSetCompleteDialog() {
		Intent i = new Intent(this, LevelSetCompletedDialogActivity.class);
		startActivityForResult(i, UIConstants.LEVEL_SET_COMPLETED_ACTIVITY);

		// return i.getBooleanExtra(UIConstants.REPLAY_LEVEL, false);

	}

	public void EnablePlayLevel() {
		try {
			_world.setSelectedEntity(null);
			_world.saveLevelToMemory();
		} catch (LevelSavingException e) {
			Log.e("Exception",e.getMessage());
		}
		_eventProcessor = new GalacticPoolExternalEventProcessor(_world);
		SetController(new GalacticPoolController(this, _eventProcessor));
		_world.setIsEditing(false);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UIConstants.LEVEL_COMPLETED_ACTIVITY) {
			if (data.getBooleanExtra(UIConstants.REPLAY_LEVEL, false)) {
				((GalacticPoolController) _controller).onReplayLevel();
			} else {
				((GalacticPoolController) _controller).onNextLevel();

			}
		} else if (requestCode == UIConstants.LEVEL_SET_COMPLETED_ACTIVITY) {
			Intent i = new Intent();
			i.putExtra(UIConstants.LEVEL_SET_COMPLETED, true);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	public void exitToMenu() {
		Intent i = new Intent();
		i.putExtra(UIConstants.EXIT_TO_MENU, true);
		setResult(RESULT_OK, i);
		finish();

	}

}