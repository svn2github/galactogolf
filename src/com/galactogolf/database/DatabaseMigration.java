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

/* represents a particular step in the evolution of the DB schema
 * When the app is upgraded, any up applied migrations are applied in order
 * This is abased on a similar idea in Ruby on Rails
 */
public class DatabaseMigration {
	private int _versionNumber;
	private String[] _commands;

	public DatabaseMigration(int versionNum, String[] commands) {
		_versionNumber = versionNum;
		_commands = commands;
	}

	public int getVersionNumber() {
		return _versionNumber;
	}

	public String[] getCommands() {
		return _commands;
	}
}
