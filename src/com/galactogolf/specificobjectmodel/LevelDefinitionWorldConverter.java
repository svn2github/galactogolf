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

import java.util.Iterator;
import java.util.Vector;

import com.galactogolf.genericobjectmodel.GameEntity;
import com.galactogolf.genericobjectmodel.NonPlayerEntity;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.levelloader.EntityDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelDefinition;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;

/**
 * Handles the conversion of Level definitions to and from live game state
 * 
 */
public class LevelDefinitionWorldConverter {
	public static class EntityType {
		public static final String Player = "Player";
		public static final String Barrier = "Barrier";
		public static final String BounceBarrier = "BounceBarrier";
		public static final String Star = "Star";
		public static String Moon = "Moon";
		public static String Wormhole = "Wormhole";
		public static String Planet = "Planet";
		public static String Sun = "Sun";
		public static String Saturn = "Saturn";
		public static String ScoreBonus = "ScoreBonus";
	}

	/**
	 * Loads a level definition into a world, assumes that the world is empty
	 * 
	 * @param level
	 * @param world
	 * @throws LevelLoadingException
	 */
	public static void LoadLevelDefinitionIntoWorld(LevelDefinition level,
			GalactoGolfWorld world) throws LevelLoadingException {
		Iterator<EntityDefinition> iter = level.GetNPCDefinitions().iterator();
		while (iter.hasNext()) {
			EntityDefinition npcDef = iter.next();
			NonPlayerEntity p;
			if (npcDef.type.equals(EntityType.Moon)) {
				p = new MoonEntity(npcDef.name, world, null);
			} else if (npcDef.type.equals(EntityType.Planet)) {
				p = new PlanetEntity(npcDef.name, world, null);
			} else if (npcDef.type.equals(EntityType.Wormhole)) {
				p = new WormholeEntity(npcDef.name, world);
			} else if (npcDef.type.equals(EntityType.Star)) {
				p = new BonusStarEntity(npcDef.name, world, null);
			} else if (npcDef.type.equals(EntityType.Sun)) {
				p = new SunEntity(npcDef.name, world);
			} else if (npcDef.type.equals(EntityType.Saturn)) {
				p = new SaturnEntity(npcDef.name, world);
			} else if (npcDef.type.equals(EntityType.BounceBarrier)) {
				p = new BounceBarrierEntity(npcDef.name, world);
				Vector2D p1 = new Vector2D(npcDef.p1.x, npcDef.p1.y);
				Vector2D p2 = new Vector2D(npcDef.p2.x, npcDef.p2.y);
				((BounceBarrierEntity) p).setPosition1(p1);
				((BounceBarrierEntity) p).setPosition2(p2);
			} else if (npcDef.type.equals(EntityType.Barrier)) {
				p = new BarrierEntity(npcDef.name, world);
				Vector2D p1 = new Vector2D(npcDef.p1.x, npcDef.p1.y);
				Vector2D p2 = new Vector2D(npcDef.p2.x, npcDef.p2.y);
				((BarrierEntity) p).setPosition1(p1);
				((BarrierEntity) p).setPosition2(p2);
			} else {
				throw new LevelLoadingException("Unknown entity type");
			}
			p.getPosition().x = npcDef.p1.x;
			p.getPosition().y = npcDef.p1.y;
			world.addNpc(p);

		}
		// load player
		EntityDefinition playerDef = level.GetPlayerDefinition();
		world.GetPlayer().getPosition().x = playerDef.p1.x;
		world.GetPlayer().getPosition().y = playerDef.p1.y;
	}

	/**
	 * Loads a level definition into a world, assumes that the world is empty
	 * 
	 * @param world
	 * @throws LevelSavingException
	 */
	public static LevelDefinition WorldIntoLevelDefinition(
			GalactoGolfWorld world) throws LevelSavingException {
		Iterator<NonPlayerEntity> iter = world.getNpcs().iterator();
		LevelDefinition level = new LevelDefinition("");
		while (iter.hasNext()) {
			NonPlayerEntity npc = iter.next();
			String npcType;
			Vector2D p1 = npc.getPosition();
			Vector2D p2 = null;
			if (npc instanceof MoonEntity) {
				npcType = EntityType.Moon;
			} else if (npc instanceof PlanetEntity) {
				npcType = EntityType.Planet;
			} else if (npc instanceof WormholeEntity) {
				npcType = EntityType.Wormhole;
			} else if (npc instanceof BonusStarEntity) {
				npcType = EntityType.Star;
			} else if (npc instanceof SunEntity) {
				npcType = EntityType.Sun;
			} else if (npc instanceof SaturnEntity) {
				npcType = EntityType.Saturn;
			} else if (npc instanceof BounceBarrierEntity) {
				npcType = EntityType.BounceBarrier;
				// barrier has different position data
				p1 = ((BounceBarrierEntity) npc).getPosition1();
				p2 = ((BounceBarrierEntity) npc).getPosition2();
			} else if (npc instanceof BarrierEntity) {
				npcType = EntityType.Barrier;
				// barrier has different position data
				p1 = ((BarrierEntity) npc).getPosition1();
				p2 = ((BarrierEntity) npc).getPosition2();
			} else {
				throw new LevelSavingException("Unknown entity type");
			}
			EntityDefinition npcDef = new EntityDefinition(npc.getName(),
					npcType, p1.x, p1.y);

			npcDef.p2 = p2;

			level.GetNPCDefinitions().add(npcDef);
		}
		// load player
		level.GetPlayerDefinition().p1.x = world.GetPlayer().getPosition().x;
		level.GetPlayerDefinition().p1.y = world.GetPlayer().getPosition().y;

		return level;
	}
}
