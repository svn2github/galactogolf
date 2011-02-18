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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.galactogolf.database.DatabaseAdapter;
import com.galactogolf.database.DatabaseException;
import com.galactogolf.serialization.JSONSerializer;
import com.galactogolf.specificobjectmodel.GalactoGolfLevelResult;
import com.galactogolf.specificobjectmodel.GalactoGolfLevelSetResult;
import com.galactogolf.specificobjectmodel.GameConstants;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * represents a pack of levels
 * 
 */
public class LevelSet {
	private ArrayList<LevelDefinition> _levels;

	private int _currentLevel;
	private String _name;
	private String _description;
	private Integer _fileResourceId=null; // when it is loaded from built in resources, this is populated

	private UUID _id;
	private String _filename;

	public LevelSet(String name) {
		_levels = new ArrayList<LevelDefinition>();
		_currentLevel = -1;
		_name = name;
		_id = UUID.randomUUID();
	}

	public LevelSet(String levelSetId, String name) {
		_id = UUID.fromString(levelSetId);
		_name = name;
		_levels = new ArrayList<LevelDefinition>();
		_currentLevel = -1;
	}

	public void add(LevelDefinition level) {
		_levels.add(level);
	}

	public LevelDefinition getCurrentLevel() throws LevelLoadingException {
		if (_currentLevel == -1) {
			throw new LevelLoadingException(
					"Have not selected first level yet!");
		}
		return _levels.get(_currentLevel);
	}

	public LevelDefinition getNextLevel() throws LevelLoadingException {
		_currentLevel++;
		if (_currentLevel > _levels.size() - 1) {
			throw new LevelLoadingException("Level not found");
		}
		return getCurrentLevel();
	}

	public boolean OnLastLevel() {
		if (_currentLevel == _levels.size() - 1) {
			return true;
		} else {
			return false;
		}
	}

	public LevelDefinition getPrevLevel() throws LevelLoadingException {
		_currentLevel--;
		if (_currentLevel < 0) {
			throw new LevelLoadingException("Level not found");
		}
		return getCurrentLevel();
	}



	
	public void saveToInternalStorage(Context context)
			throws LevelSavingException {
		File dir = context.getDir(GameConstants.LOCATION_OF_LEVELS_CREATED_BY_USER,
				Context.MODE_PRIVATE);
		File f = new File(dir, this.getFilename());
		this.saveToFile(f);

	}

	private void saveToFile(File f) throws LevelSavingException {
		OutputStream output;
		try {
			output = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			Log.e("File saving error", e.getMessage());
			throw new LevelSavingException(e.getMessage());
		}
		OutputStreamWriter writer = new OutputStreamWriter(output);

		String data;
		try {
			data = JSONSerializer.toJSON(this).toString(2);
		} catch (JSONException e) {
			Log.e("File saving error", e.getMessage());
			throw new LevelSavingException(e.getMessage());
		}

		try {
			writer.write(data);
			writer.flush();
			writer.close();

			output.close();

		} catch (IOException e) {
			Log.e("Exception",e.getMessage());
		}		
	}

	public String getFilename() {
		if (_filename == null) {
			// generate the filename
			_filename = _name;
			
			String valid_chars = "[^(abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789)]";
			_filename = _filename.replaceAll(valid_chars, "_") + "." + getId().toString() + ".level.json";

		}
		
		return _filename;
	}


	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;

	}

	public Object getDescription() {
		return _description;
	}

	public ArrayList<LevelDefinition> getAllLevels() {
		return _levels;
	}

	public static ArrayList<LevelSet> loadAllLevelSetsFromInternalStorage(
			Context context) throws LevelLoadingException {
		
		
		File levelsDir = context.getDir(
				GameConstants.LOCATION_OF_LEVELS_CREATED_BY_USER,
				Context.MODE_PRIVATE);
		File[] levelSetFiles = levelsDir.listFiles();
		ArrayList<LevelSet> levelSets = new ArrayList<LevelSet>();
		for (int i = 0; i < levelSetFiles.length; i++) {
			InputStream input;
			try {
				input = new FileInputStream(levelSetFiles[i]);
			} catch (FileNotFoundException e) {
				Log.e("File loading error", e.getMessage());
				throw new LevelLoadingException(e.getMessage());
			}
			StringBuilder sb = new StringBuilder();
			final char[] buffer = new char[500];
			Reader in = new InputStreamReader(input);
			int read;
			try {
				do {
					read = in.read(buffer, 0, buffer.length);
					if (read > 0) {
						sb.append(buffer, 0, read);
					}
				} while (read >= 0);
			} catch (IOException e) {
				Log.e("File loading error", e.getMessage());
				throw new LevelLoadingException(e.getMessage());
			}
			String data = sb.toString();
			try {
				levelSets.add(JSONSerializer.fromLevelSetJSON(new JSONObject(
						data)));
			} catch (JSONException e) {
				Log.e("File loading error", e.getMessage());
				throw new LevelLoadingException(e.getMessage());
			}

		}
		return levelSets;

	}
	
	public static LevelSet loadLevelSetFromInternalStorage(
			String levelFilename,Context context) throws LevelLoadingException {
			File levelsDir = context.getDir(
				GameConstants.LOCATION_OF_LEVELS_CREATED_BY_USER,
				Context.MODE_PRIVATE);
			LevelSet levelSet;
			File f = null;
			try {
				File[] files = levelsDir.listFiles();
				for(int i=0;i<files.length;i++) {
					if(files[i].getName().equals(levelFilename)) {
						f = files[i];
					}
				}
				if(f==null) {
					throw new LevelLoadingException("Level not found!");
				}
			} 
			catch(Exception ex) {
				Log.e("General exception","");
				return null;
			}
			FileInputStream input=null;
			try {
				input = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				Log.e("File loading error", e.getMessage());
				throw new LevelLoadingException(e.getMessage());
			}
			levelSet = LoadLevelSetFromStream(input);
			// if this is a file with the id in the name, then check it matches the internal id
			UUID levelId = null;
			String[] fileNameParts = f.getName().split("[.]");
			if(fileNameParts.length==4) {
				levelId = UUID.fromString(fileNameParts[1]);
			}
			if(levelId!=null && !levelSet.getId().equals(levelId)) {
				throw new LevelLoadingException("Id within filename and id within file do not agree");
			}
			
			// manually set the file name to the correct one so prevent conflicts
			levelSet.setFileName(f.getName());
			return levelSet;
	}

	private static LevelSet LoadLevelSetFromStream(InputStream input) throws LevelLoadingException {
		LevelSet levelSet;

		StringBuilder sb = new StringBuilder();
		final char[] buffer = new char[500];
		
		Reader in = new InputStreamReader(input);
		int read;
		try {
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					sb.append(buffer, 0, read);
				}
			} while (read >= 0);
		} catch (IOException e) {
			Log.e("File loading error", e.getMessage());
			throw new LevelLoadingException(e.getMessage());
		}
		String data = sb.toString();
		try {
			levelSet =(JSONSerializer.fromLevelSetJSON(new JSONObject(
					data)));
		} catch (JSONException e) {
			Log.e("File loading error", e.getMessage());
			throw new LevelLoadingException(e.getMessage());
		}
		
	return levelSet;
	}

	private void setFileName(String levelFilename) {
		_filename = levelFilename;
		
	}

	public void setCurrentLevel(LevelDefinition levelDefinition) {
		_levels.set(_currentLevel,levelDefinition);
		
	}

	public UUID getId() {
		return _id;
	}

	public void setDescription(String string) {
		_description = string;
		
	}

	public int getCurrentLevelNumber() {
		return _currentLevel + 1;
	}

	public void saveScores(Context ctx) throws LevelSavingException {
		int levelNumber = 1;
		DatabaseAdapter adapter = new DatabaseAdapter(ctx);
		adapter.open();
		SQLiteDatabase db;
		try {
			db = adapter.getOpenDB();
		} catch (DatabaseException ex) {
			throw new LevelSavingException("Could not access database",ex);
		}
		int roundNumber = GalactoGolfLevelResult.getLastRoundNumber(db, this.getId()) + 1;
		for(LevelDefinition level:_levels) {
			level.saveScore(db,roundNumber,levelNumber);
		
		}
	}

	public void saveToExternalStorage(
			Context context) throws LevelSavingException {
		File filesDir = context.getExternalFilesDir(null);
		File f = new File(filesDir, this.getFilename());
		this.saveToFile(f);
	}

	public static LevelSet loadLevelSetFromRaw(Context ctx,int fileResource) throws NotFoundException, LevelLoadingException {
		LevelSet levelSet =  LoadLevelSetFromStream(ctx.getResources().openRawResource(fileResource));
		levelSet.setFileResourceId(fileResource);
		return levelSet;
	}

	public void setFileResourceId(int fileResourceId) {
		this._fileResourceId = new Integer(fileResourceId);
	}

	public Integer getFileResourceId() {
		return _fileResourceId;
	}

	public int getCoursePar() {
		int coursePar = 0;
		for(LevelDefinition level:_levels) {
			coursePar += level.getPar();
		}
		return coursePar;
	}

	public boolean isCompleted(Context ctx) throws  DatabaseException {
		DatabaseAdapter adapter = new DatabaseAdapter(ctx);
		adapter.open();
		SQLiteDatabase db;
		
			db = adapter.getOpenDB();
		
		
		ArrayList<GalactoGolfLevelSetResult> res = GalactoGolfLevelSetResult
		.loadStarsFromDatabase(
				adapter.getOpenDB(), this.getId());
		adapter.close();
		if(res.size()>0 && (res.get(0).bonus>=6 || this._id.equals(UUID.fromString("ed308e23-5cde-4647-baa6-e262e2fe9241")))) {
			return true;
		}
		else {
			return false;
		}
	}



}
