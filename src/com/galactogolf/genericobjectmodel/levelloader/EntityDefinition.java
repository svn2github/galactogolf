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

package com.galactogolf.genericobjectmodel.levelloader;

import java.util.ArrayList;

import com.galactogolf.genericobjectmodel.Vector2D;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Defines an entity in preparation for serialisation/deserialisation
 * 
 */
public class EntityDefinition {
	public String name;
	public String type;
	public Vector2D p1;
	public Vector2D p2;

	/**
	 * Called when not loaded from the database
	 * 
	 * @param name
	 * @param type
	 * @param x
	 * @param y
	 */
	public EntityDefinition(String name, String type, float x, float y) {
		this.name = name;
		this.type = type;
		this.p1 = new Vector2D(x, y);
	}

	public EntityDefinition(String name, String type, float p1x, float p1y,
			float p2x, float p2y) {
		this.name = name;
		this.type = type;
		this.p1 = new Vector2D(p1x, p1y);
		this.p2 = new Vector2D(p2x, p2y);
	}

}
