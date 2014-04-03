/**
 *  @file Shot.java
 *  @author Kay Choi
 */
package com.example.untouchable.obj;

import com.example.untouchable.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

public class Shot extends GameObject {
	private double bearing;
	private double speed = 0.;
	
	/**
	 *  Class constructor.
	 *  @param x the initial x coordinate
	 *  @param y the initial y coordinate
	 *  @param bearing the initial bearing
	 *  @param velocity the initial velocity
	 */
	public Shot(int x, int y, double bearing, Context context) {
		this.x = x;
		this.y = y;
		this.bearing = bearing;
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_shot);
	}

	/**
	 * @return the velocity
	 */
	public double getVelocity() {
		return speed;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(double velocity) {
		this.speed = velocity;
	}

	/**
	 * @return the bearing
	 */
	public double getBearing() {
		return bearing;
	}
	
	/**
	 * @param bearing the bearing to set
	 */
	public void setBearing(double bearing) {
		this.bearing = bearing;
	}
}
