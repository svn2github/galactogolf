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
import com.galactogolf.database.DatabaseAdapter;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * Used to edit level set details, when creating and modifying levels
 */
public class LevelSetDetailsEditActivity extends Activity {

	private LevelSet _levelSet;
	private EditText levelSetTitle;
	private EditText levelSetDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String levelSetFilename = extras != null ? extras
				.getString(GameActivity.LEVEL_SET_FILENAME) : null;
		if (levelSetFilename != null) {

			try {
				_levelSet = LevelSet.loadLevelSetFromInternalStorage(
						levelSetFilename, this);
			} catch (LevelLoadingException ex) {

			}
		} else {
			_levelSet = new LevelSet("");
			LevelDefinition level = new LevelDefinition("1");

			_levelSet.add(level);
		}

		setContentView(R.layout.level_set_edit_layout);
		levelSetTitle = (EditText) findViewById(R.id.level_set_edit_name);
		levelSetDescription = (EditText) findViewById(R.id.level_set_edit_description);
		Button saveButton = (Button) findViewById(R.id.level_set_edit_save_button);

		if (_levelSet != null) {
			levelSetTitle.setText(_levelSet.getName());
		} else {
			levelSetTitle.setText("");
			levelSetTitle.setText("");
		}

		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				saveLevelSet();

			}

		});
	}

	private void saveLevelSet() {
		readFields();
		try {
			_levelSet.saveToInternalStorage(this);
		} catch (LevelSavingException e) {
			Log.e("Could not save level set", e.getMessage());
			return;
		}

		Intent i = new Intent(this, LevelSetDetailsActivity.class);
		if (_levelSet.getFileResourceId() != null) {
			i.putExtra(GameActivity.LEVEL_SET_RESOURCE_ID,
					_levelSet.getFileResourceId());

		} else {
			i.putExtra(GameActivity.LEVEL_SET_FILENAME, _levelSet.getFilename());
		}

	}

	private void readFields() {
		_levelSet.setName(levelSetTitle.getText().toString());

	}
}
