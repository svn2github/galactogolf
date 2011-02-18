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
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.specificobjectmodel.GalactoGolfLevelResult;
import com.galactogolf.specificobjectmodel.GalactoGolfWorld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

/*
 * Shown when a level set has been completed
 */
public class LevelSetCompletedDialogActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_set_completed_dialog);

		Button okButton = (Button) findViewById(R.id.level_set_completed_ok_button);

		okButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		Log.i("Scores", "scores");

		TextView scoreTotal = (TextView) findViewById(R.id.level_set_score_total);
		int total = 0;
		for (LevelDefinition d : ((GalactoGolfWorld) GameActivity.currentWorld)
				.getCurrentLevelSet().getAllLevels()) {
			if (d.getResult() != null) {
				total += ((GalactoGolfLevelResult) d.getResult()).score;
			}
		}
		scoreTotal.setText("" + total);

		TableLayout scoreTable = (TableLayout) findViewById(R.id.level_set_completed_score_table);
		LevelCompletedDialogActivity.populateScores((GalactoGolfWorld) GameActivity.currentWorld,scoreTable, this);

	}
	
	@Override
	public void onBackPressed() {
		/*Intent i = new Intent();
		i.putExtra(UIConstants.LEVEL_SET_COMPLETED_ACTIVITY, false);
		setResult(RESULT_OK, i);
*/
	return;
	}
}
