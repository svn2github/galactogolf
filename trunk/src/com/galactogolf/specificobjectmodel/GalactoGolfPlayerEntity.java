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

import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.util.Log;

import com.galactogolf.genericobjectmodel.GameEntity;
import com.galactogolf.genericobjectmodel.NonPlayerEntity;
import com.galactogolf.genericobjectmodel.PlayerEntity;
import com.galactogolf.genericobjectmodel.Vector2D;
import com.galactogolf.genericobjectmodel.GameWorld.CollisionModes;
import com.galactogolf.genericobjectmodel.levelloader.LevelLoadingException;
import com.galactogolf.views.RenderInstructionBuffer;

/**
 * The galacto golf player entity, e.g. the rocket
 * 
 */
public class GalactoGolfPlayerEntity extends PlayerEntity {
	private List<DebrisEntity> _debris;

	@Override
	protected void updateAnimation(long timeSinceLastFrame) {
		if (!_dead && !playerHitWormhole) {
			_currentFrameSet = 0;
			_currentFrame = 0;
		} else if(!_dead && playerHitWormhole)  {
			_currentFrameSet = 2;
			_currentFrame =  Math.min((int)Math
					.floor(((float) _timeSinceHitWormhole / 400.0f) * 2),2);
			if(_currentFrame==1) {
				Log.i("hello","world");
			}
		
		}	
		else {
		
			_currentFrameSet = 1;
			_currentFrame =  Math.min((int)Math
					.floor(((float) _timeSinceDeath / 1000.0f) * 5),3);
		}
	}

	protected Vector2D _velocity;
	private long _timeSinceDeath;
	private boolean playerHitWormhole = false;
	private WormholeEntity hitWormhole = null;
	private long _timeSinceHitWormhole;
	private static final int DEBRIS_AMOUNT = 10;

	public GalactoGolfPlayerEntity(GalactoGolfWorld world) {
		super(world);
		setScaleX(1.0f);
		setScaleY(1.0f);
		_velocity = new Vector2D(0.0f, 0.0f);
		_dead = false;
		playerHitWormhole = false;
		hitWormhole = null;
		_debris = new Vector<DebrisEntity>();
		for (int i = 0; i < DEBRIS_AMOUNT; i++) {
			if(i % 3 == 0) {
			_debris.add(new Debris1Entity("Debris", _world));
			}
			else if(i % 3 == 1) {
				_debris.add(new Debris2Entity("Debris", _world));
				
			}
			else if(i % 3 == 2) {
				_debris.add(new Debris3Entity("Debris", _world));
				
			}
		}

	}

	private void PlayerHasHitWormhole(WormholeEntity wormhole) {
		hitWormhole = wormhole;
		playerHitWormhole = true;
		((GalactoGolfWorld) _world).SetScore(((GalactoGolfWorld) _world)
				.GetScore() + 1);

	}

	public boolean HasPlayerHitWormhole() {
		return playerHitWormhole;
	}

	@Override
	public void RunPhysics(long timeSinceLastFrame) {
		if (_dead) {
			_velocity.x = 0.0f;
			_velocity.y = 0.0f;
			_timeSinceDeath += timeSinceLastFrame;
			if (_timeSinceDeath > 800) {
				_dead = false;
				((GalactoGolfWorld) _world).stopPhysics();
				// remove the debris
				List<NonPlayerEntity> debrisToRemove = _world
						.getObjectsOfType(DebrisEntity.class);
				for (NonPlayerEntity debris : debrisToRemove) {
					_world.removeNpc(debris);
				}
				this.die();
				_world.reset();

			}
		} else if (playerHitWormhole) {
			_timeSinceHitWormhole += timeSinceLastFrame;
			// move the player closer to the center of the black hole
			float distFromBlackHoleX = hitWormhole.getPosition().x
					- getPosition().x;
			float distFromBlackHoleY = hitWormhole.getPosition().y 
					- getPosition().y;
			float distFromBlackHole = (float) Math.sqrt(distFromBlackHoleX
					* distFromBlackHoleX + distFromBlackHoleY
					* distFromBlackHoleY);
			if (_timeSinceHitWormhole > 500 && distFromBlackHole < 10) {
				_timeSinceHitWormhole = 0;
				try {
					((GalactoGolfWorld) _world).OnLevelCompleted();
				} catch (LevelLoadingException e) {
					Log.e("GalactoGolf", e.getMessage());
				}
				hitWormhole = null;
			} else if (distFromBlackHole > 10) {
				_velocity.x = distFromBlackHoleX / distFromBlackHole
						* (((float) timeSinceLastFrame) / 100.0f);
				_velocity.y = distFromBlackHoleY / distFromBlackHole
						* (((float) timeSinceLastFrame) / 100.0f);
			}
			float scale = 1.0f;
			if (distFromBlackHole < 32) {
				scale = distFromBlackHole / 32.0f;
			}
			setScaleX(scale);
			setScaleY(scale);
		} else {
			NonPlayerEntity nearestEnt = null;
			float nearestZ = Float.MAX_VALUE;

			for (NonPlayerEntity ent : ((GalactoGolfWorld) _world)
					.GetGravityWells()) {
				float z = Vector2D.Sub(this.getPosition(), ent.getPosition())
						.magnitude();
				DetermineGravity(ent, timeSinceLastFrame);

				if (z < nearestZ) {
					nearestEnt = ent;
					nearestZ = z;

				}
			}

			if (nearestEnt != null) {
				// DetermineGravity(nearestEnt, timeSinceLastFrame);
			}

			float z = (float) Math.sqrt(this._velocity.x * this._velocity.x
					+ this._velocity.y * this._velocity.y);
			super.RunPhysics(timeSinceLastFrame);
		}
		this._position.x += this._velocity.x * (GameConstants.TIME_SCALE)
				* (double) timeSinceLastFrame;
		this._position.y += this._velocity.y * (GameConstants.TIME_SCALE)
				* (double) timeSinceLastFrame;

	}

	@Override
	public float getBoundingSphereRadius() {
		return 16.0f;
	}

	@Override
	public void render(RenderInstructionBuffer instructionList,
			long timeSinceLastFrame) {
		if (!_dead) {
			if (this._velocity.x > Float.MIN_VALUE
					|| this._velocity.x < -Float.MIN_VALUE) {
				this._angle = (float) (180.0d * this._velocity.GetAngle() / (Math.PI)) + 90;
			}
			super.render(instructionList, timeSinceLastFrame);
		} else {
			super.render(instructionList, timeSinceLastFrame);
		}

	}

	@Override
	public void reset() {
		super.reset();
		this.playerHitWormhole = false;
		this._timeSinceDeath = 0;
		this._timeSinceHitWormhole = 0;
		this.setScaleX(1.0f);
		this.setScaleY(1.0f);
	}

	float BIG_G = 0.41f;
	private boolean _dead;

	public void DetermineGravity(NonPlayerEntity ent, long timeSinceLastFrame) {
		double diffx = this._position.x - ent.getPosition().x;
		double diffy = this._position.y - ent.getPosition().y;
		double xsquared = diffx * diffx;
		double ysquared = diffy * diffy;
		double zsquared = xsquared + ysquared;
		double z = (double) Math.sqrt(zsquared);

		double normalisedx = diffx / z;
		double normalisedy = diffy / z;

		if (z > 10) {
			this._velocity.x -= (normalisedx) * BIG_G
					* ((GravitationalEntity) ent).GetMass()
					* (GameConstants.TIME_SCALE * (double) timeSinceLastFrame)
					/ (Math.sqrt(z * z));
			this._velocity.y -= (normalisedy) * BIG_G
					* ((GravitationalEntity) ent).GetMass()
					* (GameConstants.TIME_SCALE * (double) timeSinceLastFrame)
					/ (Math.sqrt(z * z));
		}

	}

	@Override
	public float getAngle() {
		if (this._velocity.x > Float.MIN_VALUE
				|| this._velocity.x < -Float.MIN_VALUE) {
			this._angle = (float) (180.0d * this._velocity.GetAngle() / (Math.PI)) + 90;
		}
		return super.getAngle();
	}

	public Vector2D getVelocity() {
		return _velocity;
	}

	@Override
	public void HandleCollision(GameEntity ent) {
		if (!_dead) {
			if (ent instanceof WormholeEntity) {
				if (!playerHitWormhole
						&& !((GalactoGolfWorld) _world).isLevelCompleted()) {
					PlayerHasHitWormhole((WormholeEntity) ent);
				}

			} else if (ent instanceof BonusStarEntity) {
				if (ent.visible()) {
					((BonusStarEntity) ent).HitByPlayer();
					((GalactoGolfWorld) _world)
							.setBonus(((GalactoGolfWorld) _world).getBonus() + 1);
				}
			} else if (ent instanceof BounceBarrierEntity) {
				// we must bounce the player's velocity

				// calculate normal to barrier
				Vector2D axis = Vector2D.Sub(
						((BounceBarrierEntity) ent).getPosition1(),
						((BounceBarrierEntity) ent).getPosition2()).normalize();
				Vector2D normal = new Vector2D(axis.y, -axis.x);
				Vector2D reflection = Vector2D.Sub(
						this.getVelocity(),
						Vector2D.mult(normal, (2 * Vector2D.dotProduct(
								this.getVelocity(), normal))));
				// move the player slightly away from the barrier,
				

				this._velocity = reflection;
				this._position.Add(this._velocity.mult(2));
			} else if (ent instanceof GravitationalEntity
					|| ent instanceof BarrierEntity) {
				this.explode(ent);

				// ((GalacticPoolWorld)_world).stopPhysics();
				// _world.reset();
			}
			super.HandleCollision(ent);
		}
	}

	public void explode(GameEntity entityPlayerHit) {
		// shower debris
		Vector2D reflection = new Vector2D(0.0f,0.0f);
		Vector2D normal;
		if(entityPlayerHit!=null) {
			if(entityPlayerHit instanceof BarrierEntity) {
			
				// calculate normal to barrier
				Vector2D axis = Vector2D.Sub(
						((BarrierEntity) entityPlayerHit).getPosition1(),
						((BarrierEntity) entityPlayerHit).getPosition2()).normalize();
				normal = new Vector2D(axis.y, -axis.x);
	
			}
			else {
		// work out vector from center of planet to player location, this gives us the normal
		normal = Vector2D.Sub(this.getPosition(), entityPlayerHit.getPosition()).normalize();
		
			}
			// now work out reflection vector for player's speed
			reflection = Vector2D.Sub(
					this.getVelocity(),
					Vector2D.mult(normal, (2 * Vector2D.dotProduct(
							this.getVelocity(), normal))));

		}
		double playerSpeed = this.getVelocity().magnitude();
		_dead = true;
		_timeSinceDeath = 0;
		Random rnd = new Random();
		for (DebrisEntity debris : _debris) {
			debris.getPosition().x = this.getPosition().x;
			debris.getPosition().y = this.getPosition().y;
			debris.getVelocity().x = (float) ((float) reflection.x / (playerSpeed * 3) + (0.4f * ((rnd.nextFloat() - 0.5) )));
			debris.getVelocity().y = (float) ((float) reflection.y / (playerSpeed * 3) + (0.4f * ((rnd.nextFloat() - 0.5) )));
			_world.addNpcNextFrame(debris);
		}

	}

	public void die() {
		_currentFrameSet = 0; // reset to the usual frameset
		_angle = 0;
		playerHitWormhole = false;
		((GalactoGolfWorld) _world).SetScore(((GalactoGolfWorld) _world)
				.GetScore() + 1);
		((GalactoGolfWorld) _world).setBonus(0);

	}

	@Override
	public CollisionModes getCollisionMode() {
		// TODO Auto-generated method stub
		return CollisionModes.BOUNDING_SPHERE;
	}

}
