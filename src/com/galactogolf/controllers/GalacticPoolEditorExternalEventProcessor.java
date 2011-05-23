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
package com.galactogolf.controllers;

import java.util.Iterator;

import android.content.Context;
import android.util.Log;

import com.galactogolf.GameActivity;
import com.galactogolf.genericobjectmodel.CollisionDetector;
import com.galactogolf.genericobjectmodel.CollisionException;
import com.galactogolf.genericobjectmodel.GameEntity;
import com.galactogolf.genericobjectmodel.GameWorld;
import com.galactogolf.genericobjectmodel.NonPlayerEntity;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.Vector3D;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.genericobjectmodel.levelloader.LevelSavingException;
import com.galactogolf.specificobjectmodel.BarrierEntity;
import com.galactogolf.specificobjectmodel.BounceBarrierEntity;
import com.galactogolf.specificobjectmodel.GalactoGolfWorld;
import com.galactogolf.specificobjectmodel.GameConstants;
import com.galactogolf.specificobjectmodel.MoonEntity;
import com.galactogolf.specificobjectmodel.PlanetEntity;
import com.galactogolf.specificobjectmodel.SaturnEntity;
import com.galactogolf.specificobjectmodel.BonusStarEntity;
import com.galactogolf.specificobjectmodel.SunEntity;
import com.galactogolf.specificobjectmodel.WormholeEntity;

/*
 * Processes events that might affect the world when in editing mode
 */
public class GalacticPoolEditorExternalEventProcessor extends ExternalEventProcessor {
	@Override
	public void processExternalEvents() {
		
		super.processExternalEvents();
		checkIsAtLastLevel();
	}

	protected GameEntity _touchingEntity = null;
	protected Vector2D _touchLocation;
	protected GameActivity _parentActivity;
	protected GalacticPoolEditorController _controller;
	private boolean _isOnLastLevel = false;
	private boolean _isTouching=false;
	

	public GalacticPoolEditorExternalEventProcessor(GameWorld world) {
		super(world);
	}

	@Override
	public void onDown(float x, float y) {
		_touchLocation = getTouchLocation(x, y);
		_touchingEntity = null;
		float lastTouchDist = Float.MAX_VALUE;
		synchronized (_world.getNpcs()) {
			Iterator<NonPlayerEntity> iter = _world.getNpcs().iterator();
			while (iter.hasNext()) {
				NonPlayerEntity npc = iter.next();
				float distFromTouchLocation = Float.MAX_VALUE;
				try {
					distFromTouchLocation = CollisionDetector.DistanceBetween(npc, _touchLocation);				
				}
				catch(CollisionException cEx) {
					Log.e("onDown - Collision exception", "the entity had no collision mode");
				}
				// are we within touching distance and are we closer than any other object?
				if (distFromTouchLocation < 50/_world.getCameraLocation().z && distFromTouchLocation<lastTouchDist) {
					_touchingEntity = npc;
					_isTouching=true;
					((GalactoGolfWorld)_world).setSelectedEntity(_touchingEntity);
					lastTouchDist = distFromTouchLocation;
				} else {
					
				}
			}
		}
		
		float distFromTouchLocation = Vector2D.Sub(_world.GetPlayer().getPosition(),_touchLocation).magnitude() ;
		// are we within touching distance and are we closer than any other object?
		if (distFromTouchLocation < 50/_world.getCameraLocation().z && distFromTouchLocation<lastTouchDist) {
			_touchingEntity = _world.GetPlayer();
			_isTouching=true;
			((GalactoGolfWorld)_world).setSelectedEntity(_touchingEntity);
			lastTouchDist = distFromTouchLocation;
		} else {
			
		}
	}

	@Override
	public void onLongPress(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMenuItemSelected(int menuId) {
		switch(menuId) {
		case GameConstants.MENU_NEXT_LEVEL:	
			try {
				((GalactoGolfWorld) _world).LoadNextLevel();
			} catch (LevelLoadingException e) {
				e.printStackTrace();
			}
			break;
		case GameConstants.MENU_NEW_LEVEL:	
			try {
				((GalactoGolfWorld) _world).CreateNewLevel();
				
				((GalactoGolfWorld) _world).LoadNextLevel();
				
			} catch (LevelLoadingException e) {
				e.printStackTrace();
			}

			break;
		case GameConstants.MENU_PREV_LEVEL:
			try {
				((GalactoGolfWorld) _world).LoadPrevLevel();
			} catch (LevelLoadingException e) {
				e.printStackTrace();
			}
			break;
		case GameConstants.MENU_ADD_PLANET:
			PlanetEntity p = new PlanetEntity("Planet", _world,
					null);
			p.setPosition(new Vector2D(
					_world.getCameraLocation().x, _world
							.getCameraLocation().y));
			_world.addNpcNextFrame(p);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_MOON:
			MoonEntity m = new MoonEntity("Moon", _world,
					null);
			m.setPosition(new Vector2D(
					_world.getCameraLocation().x, _world
							.getCameraLocation().y));
			_world.addNpcNextFrame(m);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_STAR:
			BonusStarEntity s = new BonusStarEntity("Star", _world,
					null);
			s.setPosition(new Vector2D(
					_world.getCameraLocation().x, _world
							.getCameraLocation().y));
			_world.addNpcNextFrame(s);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_SUN:
			SunEntity sun = new SunEntity("Sun", _world);
			sun.setPosition(new Vector2D(
					_world.getCameraLocation().x, _world
							.getCameraLocation().y));
			_world.addNpcNextFrame(sun);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_SATURN:
			SaturnEntity sat = new SaturnEntity("Saturn", _world);
			sat.setPosition(new Vector2D(
					_world.getCameraLocation().x, _world
							.getCameraLocation().y));
			_world.addNpcNextFrame(sat);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_WORMHOLE:
			WormholeEntity w = new WormholeEntity("Wormhole", _world);
			w.setPosition(new Vector2D(
					_world.getCameraLocation().x, _world
							.getCameraLocation().y));
			_world.addNpcNextFrame(w);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_BARRIER:
			BarrierEntity b = new BarrierEntity("Barrier", _world);
			b.setPosition1(new Vector2D(
					_world.getCameraLocation().x-100, _world
							.getCameraLocation().y-100));
			b.setPosition2(new Vector2D(
					_world.getCameraLocation().x+100, _world
							.getCameraLocation().y+100));
			_world.addNpcNextFrame(b);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_ADD_BOUNCE_BARRIER:  
			BounceBarrierEntity bb = new BounceBarrierEntity("Bounce Barrier", _world);
			bb.setPosition1(new Vector2D(
					_world.getCameraLocation().x-100, _world
							.getCameraLocation().y-100));
			bb.setPosition2(new Vector2D(
					_world.getCameraLocation().x+100, _world
							.getCameraLocation().y+100));
			_world.addNpcNextFrame(bb);
			((GalactoGolfWorld) _world).AssignRenderables();
			break;
		case GameConstants.MENU_DELETE_SELECTED:
			if(_touchingEntity instanceof NonPlayerEntity) {
				_world.removeNpc((NonPlayerEntity) _touchingEntity);
				((GalactoGolfWorld)_world).setSelectedEntity(null);
			}
			break;
		case GameConstants.MENU_SAVE_LEVEL:
			try {
				((GalactoGolfWorld) _world).saveLevelSetToInternalStorage();
			} catch (LevelSavingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}	
		
	}

	private void checkIsAtLastLevel() {
		if(((GalactoGolfWorld) _world).isAtLastLevel()) {
			_isOnLastLevel  = true;
		}
		else {
			_isOnLastLevel = false;
		}
		
	}
	
	public boolean isOnLastLevel() {
		return _isOnLastLevel;
	}

	@Override
	public void onScroll(float x, float y) {
		if (!_isTouching) {
				Vector3D newLocation = _world.getCameraLocation();
				newLocation.x += x;
				newLocation.y += y;
				_world.setCameraLocation(newLocation);
			
			} else {
				if(_touchingEntity instanceof BarrierEntity) {
					// barriers are a special case, as there are two control points
					float distanceFromP1 = Vector2D.Sub(_touchLocation, ((BarrierEntity)_touchingEntity).getPosition1()).magnitude();
					float distanceFromP2 = Vector2D.Sub(_touchLocation, ((BarrierEntity)_touchingEntity).getPosition2()).magnitude();
					if(distanceFromP1<distanceFromP2) {
						((BarrierEntity)_touchingEntity).getPosition1().x -= x;
						((BarrierEntity)_touchingEntity).getPosition1().y -= y;
					}
					else {
						((BarrierEntity)_touchingEntity).getPosition2().x -= x;
						((BarrierEntity)_touchingEntity).getPosition2().y -= y;
					}
				}
				else {
					_touchingEntity.getPosition().x -= x;
					_touchingEntity.getPosition().y -= y;
				}
			}
	}

	@Override
	public void onSingleTapUp(float screenX, float screenY) {
		_isTouching = false;
	}

	@Override
	public void onTouchUp(float x, float y) {
		_isTouching = false;
	}
	
	@Override
	public void onScale(float newScaleFactor) {
			/*_zooming = true;
			_world.getCameraLocation().z = _world.getCameraLocation().z / newScaleFactor; 
			Vector3D currPos = _world.getCameraLocation();
				_world.setCameraLocation(new Vector3D(currPos.x,currPos.y, 1/_world.getCameraLocation().z ));
	*/
	}
	
	public void setController(GalacticPoolEditorController controller) {
		_controller = controller;
	}


}
