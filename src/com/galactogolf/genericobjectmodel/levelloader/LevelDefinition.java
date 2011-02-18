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

import com.galactogolf.specificobjectmodel.LevelDefinitionWorldConverter;

import android.database.sqlite.SQLiteDatabase;

/**
 * Defines the layout of a particular level, e.g. entity types and positions
 * 
 */
public class LevelDefinition {
	private ArrayList<EntityDefinition> _npcDefinitions;
	private EntityDefinition _playerDefinition;
	private Long _id;
	private String _name;
	private String _description;
	private int _par;
	private LevelResult _result;

	public LevelDefinition(String name) {
		_npcDefinitions = new ArrayList<EntityDefinition>();
		_id = null;
		_name = name;
		_playerDefinition = new EntityDefinition("Player",
				LevelDefinitionWorldConverter.EntityType.Player, 0.0f, 0.0f);

		_par = 3;
	}

	public LevelDefinition(long id, String name) {
		_npcDefinitions = new ArrayList<EntityDefinition>();
		_id = new Long(id);
	}

	public Long getId() {
		return _id;
	}

	public ArrayList<EntityDefinition> GetNPCDefinitions() {
		return _npcDefinitions;
	}

	public EntityDefinition GetPlayerDefinition() {
		return _playerDefinition;
	}

	public String getName() {
		return _name;
	}

	public String getDescription() {
		return _description;
	}

	public void setPlayerDefinition(EntityDefinition playerDefn) {
		_playerDefinition = playerDefn;

	}

	public int getPar() {
		return _par;
	}

	public void Complete(LevelResult res) {
		this._result = res;
	}

	public void saveScore(SQLiteDatabase db, int roundNumber, int levelNumber)
			throws LevelSavingException {
		if (_result != null) {
			_result.saveToDatabase(db, roundNumber);
		}
	}

	public void setDescription(String description) {
		_description = description;

	}

	public LevelResult getResult() {
		return _result;
	}

	public void setPar(int p) {
		_par = p;

	}

}
