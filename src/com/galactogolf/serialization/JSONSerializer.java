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

package com.galactogolf.serialization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.levelloader.EntityDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelSet;

/**
 * handles serialization of file to and from JSON format
 * 
 */
public class JSONSerializer {
	
	public class JSONTags {
		public static final String LEVEL_SET = "level_set";
		public static final String LEVEL_SET_NAME = "name";
		public static final String LEVEL_SET_DESCRIPTION = "description";
		public static final String LEVEL_SET_ID = "id";
		public static final String LEVEL_SET_LEVELS = "levels";
		
		public static final String LEVEL_DEFINITION = "level";
		public static final String LEVEL_DEFINITION_NAME = "name";
		public static final String LEVEL_DEFINITION_DESCRIPTION = "description";
		public static final String LEVEL_DEFINITION_PAR = "par";
		public static final String LEVEL_DEFINITION_NPCS = "npcs";
		public static final String LEVEL_DEFINITION_PLAYER = "player";

		public static final String ENTITY_DEFINITION = "entity";
		public static final String ENTITY_DEFINITION_NAME = "name";
		public static final String ENTITY_DEFINITION_TYPE = "type";
		public static final String ENTITY_DEFINITION_POSITION = "position";

		public static final String POSITION_X = "x";
		public static final String POSITION_Y = "y";

	};
	

	public static JSONObject toJSON(LevelSet levelSet) throws JSONException {
	
			
			JSONObject levelSetJSON = new JSONObject();
			levelSetJSON.put(JSONTags.LEVEL_SET_NAME, levelSet.getName());
			levelSetJSON.put(JSONTags.LEVEL_SET_DESCRIPTION, levelSet.getDescription());
			levelSetJSON.put(JSONTags.LEVEL_SET_ID, levelSet.getId());

			Iterator<LevelDefinition> iter = levelSet.getAllLevels().iterator();

			
			boolean first = true;			
			while(iter.hasNext()) {
				LevelDefinition level = iter.next();
				JSONObject levelJSON = JSONSerializer.toJSON(level);
				if(first) {
					JSONArray array = new JSONArray();
					array.put(levelJSON);
					levelSetJSON.put(JSONTags.LEVEL_SET_LEVELS , array);
					first = false;
				}
				else {
					levelSetJSON.accumulate(JSONTags.LEVEL_SET_LEVELS,  levelJSON);
				}

			}
			
			
		return levelSetJSON;
	}

	private static JSONObject toJSON(LevelDefinition level) throws JSONException {
		
		JSONObject levelJSON = new JSONObject();
		levelJSON.put(JSONTags.LEVEL_DEFINITION_NAME, level.getName());
		levelJSON.put(JSONTags.LEVEL_DEFINITION_PAR, level.getPar());
		levelJSON.put(JSONTags.LEVEL_DEFINITION_DESCRIPTION, level.getDescription());

		Iterator<EntityDefinition> iter = level.GetNPCDefinitions().iterator();
		
		boolean first = true;
		while(iter.hasNext()) {
			EntityDefinition entity = iter.next();
			JSONObject npcJSON = JSONSerializer.toJSON(entity);
			if(first) {
				JSONArray array = new JSONArray();
				array.put(npcJSON);
				levelJSON.put(JSONTags.LEVEL_DEFINITION_NPCS , array);
				first = false;
			}
			else {
				levelJSON.accumulate(JSONTags.LEVEL_DEFINITION_NPCS,  npcJSON);
			}
		}
		
		levelJSON.put(JSONTags.LEVEL_DEFINITION_PLAYER, JSONSerializer.toJSON(level.GetPlayerDefinition()) );

		
		return levelJSON;
	}

	private static JSONObject toJSON(EntityDefinition entity) throws JSONException {
		JSONObject entityJSON = new JSONObject();
		entityJSON.put(JSONTags.ENTITY_DEFINITION_NAME, entity.name);
		entityJSON.put(JSONTags.ENTITY_DEFINITION_TYPE, entity.type);

		JSONArray array = new JSONArray();
		array.put(JSONSerializer.toJSON(new float[] {entity.p1.x,entity.p1.y}));
		entityJSON.put(JSONTags.ENTITY_DEFINITION_POSITION , array);
		if(entity.p2 != null) {
			entityJSON.accumulate(JSONTags.ENTITY_DEFINITION_POSITION, JSONSerializer.toJSON(new float[] {entity.p2.x,entity.p2.y}));
			
		}
		
		
		return entityJSON;
	}

	private static JSONArray toJSON(float[] fs) throws JSONException {
		JSONArray floatArray = new JSONArray();
		for(int i=0;i<fs.length;i++) {
			floatArray.put(i,fs[i]);
		}
		return floatArray;
	}
	
	
	
	public static LevelSet fromLevelSetJSON(JSONObject levelSetJSON) throws JSONException {
		
		LevelSet levelSet = new LevelSet(levelSetJSON.getString(JSONTags.LEVEL_SET_ID),levelSetJSON.getString(JSONTags.LEVEL_SET_NAME));
		levelSet.setDescription(levelSetJSON.optString(JSONTags.LEVEL_SET_DESCRIPTION));
		JSONArray levelsJSON=null;
		try {
			levelsJSON = levelSetJSON.getJSONArray(JSONTags.LEVEL_SET_LEVELS);
		}
		catch(JSONException ex) {
			Log.e("Level Loading Exception",ex.getMessage());
			levelSet.add(JSONSerializer.fromLevelDefinitionJSON(levelSetJSON.getJSONObject(JSONTags.LEVEL_SET_LEVELS)));
			return levelSet;
		}
		for(int i=0;i<levelsJSON.length();i++) {
			levelSet.add(JSONSerializer.fromLevelDefinitionJSON(levelsJSON.getJSONObject(i)));
		}
		
		return levelSet;
	}

private static LevelDefinition fromLevelDefinitionJSON(JSONObject levelDefnJSON) throws JSONException {
	LevelDefinition levelDefn = new LevelDefinition(levelDefnJSON.getString(JSONTags.LEVEL_DEFINITION_NAME));
	levelDefn.setDescription(levelDefnJSON.optString(JSONTags.LEVEL_DEFINITION_DESCRIPTION));
	levelDefn.setPar(levelDefnJSON.optInt(JSONTags.LEVEL_DEFINITION_PAR));

	JSONArray npcsJSON = levelDefnJSON.optJSONArray(JSONTags.LEVEL_DEFINITION_NPCS);
	if(npcsJSON != null) {
	for(int i=0;i<npcsJSON.length();i++) {
		levelDefn.GetNPCDefinitions().add(JSONSerializer.fromEntityDefinitionJSON(npcsJSON.getJSONObject(i)));
	}
	}
	levelDefn.setPlayerDefinition(JSONSerializer.fromEntityDefinitionJSON(levelDefnJSON.getJSONObject(JSONTags.LEVEL_DEFINITION_PLAYER)) );

	return levelDefn;
	
}

private static EntityDefinition fromEntityDefinitionJSON(JSONObject entityDefnJSON) throws JSONException {
	JSONArray positionsJSON = entityDefnJSON.getJSONArray(JSONTags.ENTITY_DEFINITION_POSITION);
	float x = (float) positionsJSON.getJSONArray(0).getDouble(0);
	float y = (float) positionsJSON.getJSONArray(0).getDouble(1);
	EntityDefinition entityDefn = new EntityDefinition(entityDefnJSON.getString(JSONTags.ENTITY_DEFINITION_NAME), 
			entityDefnJSON.getString(JSONTags.ENTITY_DEFINITION_TYPE),x,y );
	
	if(positionsJSON.length()>1) {
		entityDefn.p2 = new Vector2D((float) positionsJSON.getJSONArray(1).getDouble(0),
									(float) positionsJSON.getJSONArray(1).getDouble(1));
	}
		
	
	
	return entityDefn;
}

private static float[] fromFloatArrayJSON(JSONArray floatArrayJSON) throws JSONException {
	float[] floatArray = new float[floatArrayJSON.length()];
	for(int i=0;i<floatArrayJSON.length();i++) {
		floatArray[i] = (float) floatArrayJSON.getDouble(i);
	}
	return floatArray;
}

public static List<LevelSet> fromLevelSetJSON(JSONArray jsonArray) throws JSONException {
	ArrayList<LevelSet> levelSets= new ArrayList<LevelSet>();
	for(int i=0;i<jsonArray.length();i++) {
		levelSets.add(JSONSerializer.fromLevelSetJSON(jsonArray.getJSONObject(i)));
	}
	
	return levelSets;
}
}
