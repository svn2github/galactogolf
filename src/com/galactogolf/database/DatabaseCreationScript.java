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

package com.galactogolf.database;

import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * Holds database creation code, including a simple version of migrations 
 * (see Rails for the inspiration)
 */
public class DatabaseCreationScript {
	public static final int DATABASE_VERSION = 2; // incremented when we release
													// a new version and we need
													// to apply a migration
	private static final DatabaseMigration[] DATABASE_MIGRATIONS = new DatabaseMigration[] { new DatabaseMigration(
			1,
			new String[] { "create table level_scores (_id  integer primary key autoincrement,"
					+ " level_set_id varchar(100) NOT NULL, "
					+ " level_number integer NOT NULL,"
					+ " level_score integer NOT NULL,"
					+ " level_bonus integer NOT NULL, "
					+ " player_power float NOT NULL, "
					+ " player_angle float NOT NULL, "
					+ " attempt_number integer NOT NULL )" }), };

	/**
	 * upgrades the database to the latest version by applying all DDL
	 * statements that haven't been applied yet
	 * 
	 * @throws LevelSavingException
	 * 
	 */
	public static void migrate(SQLiteDatabase db, int oldVersion, int newVersion)
			throws DatabaseException {
		try {
			for (int i = 0; i < DATABASE_MIGRATIONS.length; i++) {
				DatabaseMigration migration = DATABASE_MIGRATIONS[i];
				if (migration.getVersionNumber() > oldVersion) {
					if (migration.getVersionNumber() <= newVersion) {
						for (int j = 0; j < migration.getCommands().length; j++) {
							db.execSQL(migration.getCommands()[j]);
						}
					}
				}
			}
		} catch (Exception ex) {
			Log.e("Error", ex.getMessage());
			throw new DatabaseException(
					"could not migrate database to from version " + oldVersion
							+ " to version " + newVersion, ex);
		}
	}

	public static void createDb(SQLiteDatabase db) throws DatabaseException {
		migrate(db, -1, DATABASE_VERSION);
	}
}
