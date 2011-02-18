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

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Simple interface to SQLite DB.
 */
public class DatabaseAdapter {
	private static final String DATABASE_NAME = "data";
	private com.galactogolf.database.DatabaseAdapter.DatabaseHelper _DbHelper;
	private Context _Ctx;
	private SQLiteDatabase _Db;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null,
					DatabaseCreationScript.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				DatabaseCreationScript.createDb(db);
			} catch (DatabaseException ex) {
				Log.e("Database", ex.getMessage() + ex.getStackTrace());
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Database", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			try {
				DatabaseCreationScript.migrate(db, oldVersion, newVersion);
			} catch (DatabaseException ex) {
				Log.e("Database", ex.getMessage() + ex.getStackTrace());
			}

		}
	}

	public DatabaseAdapter(Context ctx) {
		this._Ctx = ctx;
	}

	public DatabaseAdapter open() throws SQLException {
		_DbHelper = new DatabaseHelper(_Ctx);
		_Db = _DbHelper.getWritableDatabase();
		return this;
	}

	public SQLiteDatabase getOpenDB() throws DatabaseException {
		if (_Db == null) {
			throw new DatabaseException("Database not opened", null);
		}
		return _Db;
	}

	public void close() {
		_DbHelper.close();
	}
}
