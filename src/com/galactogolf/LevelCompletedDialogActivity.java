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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

/**
 * Shows a dialog offering options when the user completes a level
 */
public class LevelCompletedDialogActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_completed_dialog);

		Button nextLevelButton = (Button) findViewById(R.id.next_level_button);

		nextLevelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(UIConstants.REPLAY_LEVEL, false);
				setResult(RESULT_OK, i);
				finish();
			}
		});

		/*
		 * Button replayLevelButton = (Button)
		 * findViewById(R.id.replay_level_button);
		 * 
		 * replayLevelButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * public void onClick(View v) { Intent i = new Intent();
		 * i.putExtra(UIConstants.REPLAY_LEVEL, true); setResult(RESULT_OK,i);
		 * finish(); } });
		 */
		TextView scoreTotal = (TextView) findViewById(R.id.level_set_score_total);
		int total = 0;
		for (LevelDefinition d : ((GalactoGolfWorld) GameActivity.currentWorld)
				.getCurrentLevelSet().getAllLevels()) {
			if (d.getResult() != null) {
				total += ((GalactoGolfLevelResult) d.getResult()).score;
			}
		}
		scoreTotal.setText("" + total);
		TableLayout scoreTable = (TableLayout) findViewById(R.id.level_completed_score_table);
		populateScores((GalactoGolfWorld) GameActivity.currentWorld,
				scoreTable, this);
	}

	public static void populateScores(GalactoGolfWorld world,
			TableLayout scoreTable, Context context) {
		scoreTable.setStretchAllColumns(true);
		int maxTableWidth = 9;
		int currentTableWidth = 0;
		TableRow currentHoleRow = null;
		TableRow currentParRow = null;
		TableRow currentScoreRow = null;
		TableRow currentStarRow = null;
		TableLayout.LayoutParams rowLayout = new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rowLayout.setMargins(1,1,1,1);
		
		int currentHole = 1;
		
		TableRow.LayoutParams noBorderLayout = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		noBorderLayout.setMargins(0, 0, 0, 0);
		TableLayout.LayoutParams noBorderTableLayout = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		noBorderTableLayout.setMargins(0, 0, 0, 0);
		scoreTable.setLayoutParams(noBorderTableLayout);

		TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(1, 1, 1, 1);
		int backColor = Color.argb(255, 0, 0, 0);
		int rowBorderColor = Color.argb(255, 0, 0, 0);
		for (LevelDefinition d : ((GalactoGolfWorld) world)
				.getCurrentLevelSet().getAllLevels()) {
			if (currentTableWidth == maxTableWidth) { // we have overrun the end
				// of the row
				currentTableWidth = 0;
			}
			if (currentTableWidth == 0) { // we are starting a new row
				currentHoleRow = new TableRow(context);
				currentHoleRow.setBackgroundColor(rowBorderColor);
				currentParRow = new TableRow(context);
				currentParRow
						.setBackgroundColor(rowBorderColor);
				currentScoreRow = new TableRow(context);
				currentScoreRow.setBackgroundColor(rowBorderColor);
				currentStarRow = new TableRow(context);
				currentStarRow.setBackgroundColor(rowBorderColor);
				scoreTable.addView(currentHoleRow, rowLayout);
				scoreTable.addView(currentParRow, rowLayout);
				scoreTable.addView(currentScoreRow, rowLayout);
				scoreTable.addView(currentStarRow,rowLayout);
				TextView holeLabel = new TextView(context);
				TextView parLabel = new TextView(context);
				TextView scoreLabel = new TextView(context);
				TextView starLabel = new TextView(context);

				holeLabel.setText("Hole:");
				holeLabel.setBackgroundColor(backColor);
				holeLabel.setTextColor(Color.argb(255,0xED,0xB9,0x58));
				parLabel.setText("Par:");
				parLabel.setBackgroundColor(backColor);
				scoreLabel.setText("Score:");
				scoreLabel.setBackgroundColor(backColor);
				starLabel.setText("Stars:");
				starLabel.setBackgroundColor(backColor);
				currentHoleRow.addView(holeLabel, params);
				currentParRow.addView(parLabel, params);
				currentScoreRow.addView(scoreLabel, params);
				currentStarRow.addView(starLabel, params);
			}
			TableRow.LayoutParams cellLayout = params;
			TextView hole = new TextView(context);
			hole.setBackgroundColor(backColor);
			hole.setTextColor(Color.argb(255,0xED,0xB9,0x58));
			TextView par = new TextView(context);
			par.setBackgroundColor(backColor);
			TextView score = new TextView(context);
			score.setBackgroundColor(backColor);
			hole.setText(" " + currentHole);
			par.setText(" " + d.getPar());
			GalactoGolfLevelResult result = ((GalactoGolfLevelResult) d
					.getResult());
			View starView;
			if (result != null) {
				if (result.bonus > 0) {
					starView = new ImageView(context);
					((ImageView)starView).setImageResource(R.drawable.bonus_small_star);

				} else {
					starView = new TextView(context);
					((TextView)starView).setText(" ");
				}

				score.setText(" "
						+ ((GalactoGolfLevelResult) d.getResult()).score);
			} else {
				score.setText(" ");
				score.setText(" -");
				starView = new TextView(context);
				((TextView)starView).setText(" ");
			}
			starView.setBackgroundColor(backColor);

			if(currentTableWidth < maxTableWidth-1) {

				currentHoleRow.addView(hole, cellLayout);
				currentParRow.addView(par, cellLayout);
				currentStarRow.addView(starView,cellLayout);
				currentScoreRow.addView(score, cellLayout);	
			}
			else {
				currentHoleRow.addView(hole,noBorderLayout);
				currentParRow.addView(par,noBorderLayout);
				currentScoreRow.addView(score,noBorderLayout);	
				currentStarRow.addView(starView,noBorderLayout);
			
			}
			currentHole++;
			currentTableWidth++;
		}
		for (int i = currentTableWidth; i < maxTableWidth; i++) {
			TextView emptyCell = new TextView(context);
			emptyCell.setText(" ");
			emptyCell.setBackgroundColor(backColor);
			TextView emptyCell2 = new TextView(context);
			emptyCell2.setText(" ");
			emptyCell2.setBackgroundColor(backColor);
			TextView emptyCell3 = new TextView(context);
			emptyCell3.setText(" ");
			emptyCell3.setBackgroundColor(backColor);
			TextView emptyCell4 = new TextView(context);
			emptyCell4.setText(" ");
			emptyCell4.setBackgroundColor(backColor);
			TableRow.LayoutParams cellLayout = params;
			if(i < maxTableWidth-1) {
			
			currentHoleRow.addView(emptyCell4, cellLayout);
			currentParRow.addView(emptyCell, cellLayout);
			currentScoreRow.addView(emptyCell2, cellLayout);
			currentStarRow.addView(emptyCell3, cellLayout);
			}
			else {
				currentHoleRow.addView(emptyCell4, cellLayout);
				currentParRow.addView(emptyCell,noBorderLayout);
				currentScoreRow.addView(emptyCell2,noBorderLayout);
				currentStarRow.addView(emptyCell3,noBorderLayout);
		
			}
		}

	}
	
	@Override
	public void onBackPressed() {
	/*	Intent i = new Intent();
		i.putExtra(UIConstants.REPLAY_LEVEL, false);
		setResult(RESULT_OK, i);

	return;*/
	}
}
