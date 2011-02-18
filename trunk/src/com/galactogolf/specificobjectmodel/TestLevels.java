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

import com.galactogolf.genericobjectmodel.levelloader.EntityDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelSet;

/**
 * Holds some test levels if we don't want to use the editor
 * 
 */
public class TestLevels {
	public static LevelSet GetLevelSet() {
		LevelSet levels = new LevelSet("Training");
		LevelDefinition level1 = new LevelDefinition("Hole 1");
		level1.GetNPCDefinitions().add(new EntityDefinition( "Moon 1",LevelDefinitionWorldConverter.EntityType.Moon,0,-300));
		level1.GetNPCDefinitions().add(new EntityDefinition( "Planet 1",LevelDefinitionWorldConverter.EntityType.Planet,0,-50));
		level1.GetNPCDefinitions().add(new EntityDefinition( "Planet 2",LevelDefinitionWorldConverter.EntityType.Planet,0,-120));
		level1.GetNPCDefinitions().add(new EntityDefinition("Wormhole",LevelDefinitionWorldConverter.EntityType.Wormhole,30,-200));
		level1.GetNPCDefinitions().add(new EntityDefinition( "Moon 2",LevelDefinitionWorldConverter.EntityType.Moon,200f,0f));
		level1.GetNPCDefinitions().add(new EntityDefinition( "Star 1",LevelDefinitionWorldConverter.EntityType.Star,100f,-50f));
		level1.GetNPCDefinitions().add(new EntityDefinition("Barrier",LevelDefinitionWorldConverter.EntityType.Barrier,60f,-150f,60f,150f));
		level1.GetPlayerDefinition().p1.x = 100f;
		level1.GetPlayerDefinition().p1.y = 150f;
		
		levels.add(level1);

		LevelDefinition level2 = new LevelDefinition("Hole 2");
		level2.GetNPCDefinitions().add(new EntityDefinition("Moon 1",LevelDefinitionWorldConverter.EntityType.Moon,0,600));
		level2.GetNPCDefinitions().add(new EntityDefinition("Moon 2",LevelDefinitionWorldConverter.EntityType.Moon,0,450));
		level2.GetNPCDefinitions().add(new EntityDefinition("Planet 1",LevelDefinitionWorldConverter.EntityType.Planet,0,300));
		level2.GetNPCDefinitions().add(new EntityDefinition("Planet 2",LevelDefinitionWorldConverter.EntityType.Planet,0,50));
		level2.GetNPCDefinitions().add(new EntityDefinition("Wormhole",LevelDefinitionWorldConverter.EntityType.Wormhole,0,150));
		level2.GetPlayerDefinition().p1.x = 200f;
		level2.GetPlayerDefinition().p1.y = 200f;
		levels.add(level2);
		
		return levels;
	}
}
