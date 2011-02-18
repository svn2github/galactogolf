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
import java.util.UUID;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Represents the result of completing a levelset, potentially saved to the DB
 * 
 */
public class GalactoGolfLevelSetResult {

	public int attemptNumber;
	public int score;
	public int bonus;
	


	public GalactoGolfLevelSetResult(int attemptNumber, int score, int bonus) {
		this.attemptNumber = attemptNumber;
		this.score = score;
		this.bonus = bonus;
	}

	


	public static ArrayList<GalactoGolfLevelSetResult> loadScoresFromDatabase(
			SQLiteDatabase db, UUID levelSetId) {
		try {
			Cursor cur = db.rawQuery("select "
					+ GalactoGolfLevelResult.COL_ATTEMPT_NUMBER + ", sum("
					+ GalactoGolfLevelResult.COL_LEVEL_SCORE + "), sum("
					+ GalactoGolfLevelResult.COL_LEVEL_BONUS + ") from "
					+ GalactoGolfLevelResult.TABLE_NAME
					+ " where level_set_id = ? group by "
					+ GalactoGolfLevelResult.COL_ATTEMPT_NUMBER
					+ " order by sum(" + GalactoGolfLevelResult.COL_LEVEL_SCORE
					+ ") limit 5", new String[] { levelSetId.toString() });

			ArrayList<GalactoGolfLevelSetResult> definitions = new ArrayList<GalactoGolfLevelSetResult>();
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				definitions.add(new GalactoGolfLevelSetResult(cur.getInt(0), /* COL_ATTEMPT_NUMBER */
				cur.getInt(1) /* COL_LEVEL_SCORE */,
				cur.getInt(2) /* COL_LEVEL_BONUS */
				));
				cur.moveToNext();
			}
			cur.close();
			return definitions;
		} catch (SQLiteException ex) {
			Log.e("SQL error", ex.getMessage());
			return null;
		}

	}

	/**
	 * Load the 5 best star scores from the db
	 * @param db
	 * @param levelSetId
	 * @return
	 */
	public static ArrayList<GalactoGolfLevelSetResult> loadStarsFromDatabase(
			SQLiteDatabase db, UUID levelSetId) {
		try {
			Cursor cur = db.rawQuery("select "
					+ GalactoGolfLevelResult.COL_ATTEMPT_NUMBER + ", sum("
					+ GalactoGolfLevelResult.COL_LEVEL_SCORE + "), sum("
					+ GalactoGolfLevelResult.COL_LEVEL_BONUS + ") from "
					+ GalactoGolfLevelResult.TABLE_NAME
					+ " where level_set_id = ? group by "
					+ GalactoGolfLevelResult.COL_ATTEMPT_NUMBER
					+ " order by sum(" + GalactoGolfLevelResult.COL_LEVEL_BONUS
					+ ") desc limit 5", new String[] { levelSetId.toString() });

			ArrayList<GalactoGolfLevelSetResult> definitions = new ArrayList<GalactoGolfLevelSetResult>();
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				definitions.add(new GalactoGolfLevelSetResult(cur.getInt(0), /* COL_ATTEMPT_NUMBER */
				cur.getInt(1) /* COL_LEVEL_SCORE */,
				cur.getInt(2) /* COL_LEVEL_BONUS */
				));
				cur.moveToNext();
			}
			cur.close();
			return definitions;
		} catch (SQLiteException ex) {
			Log.e("SQL error", ex.getMessage());
			return null;
		}

	}

}
