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

import com.galactogolf.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/*
 * First activity that launches, shows a series of options to select from
 * , e.g. 'Play', 'Preferences' etc.
 */
public class MainMenuActivity extends Activity {
	private static final int ACTIVITY_PREFERENCES = 0;
	private static final int ACTIVITY_LEVEL_SELECT = 1;
	private static final int ACTIVITY_HOW_TO_PLAY = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_menu);

		Button preferencesButton = (Button) findViewById(R.id.PreferencesButton);

		preferencesButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				launchPreferences();
			}
		});
		Button selectLevelButton = (Button) findViewById(R.id.SelectLevelButton);

		selectLevelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				selectLevel();
			}
		});
		Button howToPlayButton = (Button) findViewById(R.id.HowToPlayButton);
		howToPlayButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				showHowToPlay();
			}

		});

		Button exitButton = (Button) findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				exitGame();
			}

		});

	}

	public void launchPreferences() {
		Intent i = new Intent(this, GalactoGolfPreferences.class);
		startActivityForResult(i, ACTIVITY_PREFERENCES);
	}

	public void selectLevel() {
		Intent i = new Intent(this, LevelSetSelectActivity.class);
		startActivityForResult(i, ACTIVITY_LEVEL_SELECT);
	}

	private void showHowToPlay() {
		Intent i = new Intent(this, HowToPlayActivity.class);
		startActivityForResult(i, ACTIVITY_HOW_TO_PLAY);

	}

	private void exitGame() {
		this.finish();
	}

}
