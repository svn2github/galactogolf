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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.galactogolf.genericobjectmodel.levelloader.LevelResult;
import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;

/**
 * Represents the result of successfully completing a level, van be saved to a
 * DB
 * 
 * @author andyds
 * 
 */
public class GalactoGolfLevelResult extends LevelResult {

	public static final String TABLE_NAME = "level_scores";

	public final static String KEY_ROWID = "_id";
	public final static String COL_LEVEL_SET_ID = "level_set_id";
	public final static String COL_LEVEL_NUMBER = "level_number";
	public final static String COL_LEVEL_SCORE = "level_score";
	public final static String COL_LEVEL_BONUS = "level_bonus";
	public final static String COL_PLAYER_POWER = "player_power";
	public final static String COL_PLAYER_ANGLE = "player_angle";
	public final static String COL_ATTEMPT_NUMBER = "attempt_number"; // we
																		// record
																		// every
																		// time
																		// the
																		// player
																		// completes
																		// a
																		// round

	public Long id;
	public UUID levelSetId;
	public int score;
	public int bonus;
	public float playerAngle;
	public float playerPower;

	public int levelNumber;

	private int attemptNumber;

	

	public GalactoGolfLevelResult(UUID levelSetId, int levelNumber, int bonus,
			int score, float playerPower, float playerAngle, int attemptNumber) {
		this.id = null;
		this.levelSetId = levelSetId;
		this.levelNumber = levelNumber;
		this.score = score;
		this.bonus = bonus;
		this.playerPower = playerPower;
		this.playerAngle = playerAngle;
		this.attemptNumber = attemptNumber;
	}

	public GalactoGolfLevelResult(long id, UUID levelSetId, int levelNumber,
			int score, int bonus, float playerPower, float playerAngle,
			int attemptNumber) {
		this.id = new Long(id);
		this.levelSetId = levelSetId;
		this.levelNumber = levelNumber;
		this.score = score;
		this.bonus = bonus;
		this.playerPower = playerPower;
		this.playerAngle = playerAngle;
		this.attemptNumber = attemptNumber;
	}

	public static ArrayList<GalactoGolfLevelResult> loadDefinitionsFromDatabase(
			SQLiteDatabase db, UUID levelSetId) {
		Cursor cur = db.query(TABLE_NAME, new String[] { KEY_ROWID,
				COL_LEVEL_SET_ID, COL_LEVEL_NUMBER, COL_LEVEL_SCORE,
				COL_LEVEL_BONUS, COL_PLAYER_POWER, COL_PLAYER_ANGLE,
				COL_ATTEMPT_NUMBER },
				COL_LEVEL_SET_ID + "='" + levelSetId.toString() + "'", null,
				null, null, COL_ATTEMPT_NUMBER);

		ArrayList<GalactoGolfLevelResult> definitions = new ArrayList<GalactoGolfLevelResult>();
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			definitions.add(new GalactoGolfLevelResult(cur.getLong(0), /* KEY_ROWID */
			UUID.fromString(cur.getString(1)), /* COL_LEVEL_SET_ID */
			cur.getInt(2), /* COL_LEVEL_NUMBER */
			cur.getInt(3),/* COL_LEVEL_SCORE */
			cur.getInt(4),/* COL_LEVEL_BONUS */
			cur.getFloat(5),/* COL_PLAYER_POWER */
			cur.getFloat(6),/* COL_PLAYER_ANGLE */
			cur.getInt(7)/* COL_ATTEMPT_NUMBER */
			));
			cur.moveToNext();
		}
		cur.close();

		return definitions;
	}

	public void saveToDatabase(SQLiteDatabase db, int roundNumber)
			throws LevelSavingException {
		ContentValues args = new ContentValues();
		args.put(COL_LEVEL_SET_ID, this.levelSetId.toString());
		args.put(COL_LEVEL_NUMBER, this.levelNumber);
		args.put(COL_LEVEL_SCORE, this.score);
		args.put(COL_LEVEL_BONUS, this.bonus);
		args.put(COL_PLAYER_POWER, this.playerPower);
		args.put(COL_PLAYER_ANGLE, this.playerAngle);
		args.put(COL_ATTEMPT_NUMBER, roundNumber);
		if (id == null) {
			try {
				id = db.insertOrThrow(TABLE_NAME, null, args);
			} catch (SQLException ex) {
				throw new LevelSavingException(
						"Could not save level set result id: " + this.id, ex);
			}
		} else {
			db.update(TABLE_NAME, args, KEY_ROWID + "=" + id, null);
		}
	}

	public static int getLastRoundNumber(SQLiteDatabase db, UUID levelSetId) {
		Cursor cur;
		try {
			cur = db.rawQuery("SELECT MAX(" + COL_ATTEMPT_NUMBER + ") from "
					+ TABLE_NAME + " where " + COL_LEVEL_SET_ID + " = '"
					+ levelSetId.toString() + "'", null);
		} catch (SQLiteException ex) {
			Log.e("SQLite error", ex.getMessage());
			return -1;
		}
		cur.moveToFirst();
		int roundNum = cur.getInt(0);
		cur.close();
		return roundNum;
	}
}
