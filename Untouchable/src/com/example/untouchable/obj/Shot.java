/**
 *  @file Shot.java
 *  @author Kay Choi
 */
package com.example.untouchable.obj;

public class Shot {
	private int x;
	private int y;
	private float bearing;
	private float velocity;
	
	/**
	 *  Class constructor.
	 *  @param x the initial x coordinate
	 *  @param y the initial y coordinate
	 *  @param bearing the initial bearing
	 *  @param velocity the initial velocity
	 */
	public Shot(int x, int y, float bearing, float velocity) {
		this.x = x;
		this.y = y;
		this.bearing = bearing;
		this.velocity = velocity;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the velocity
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the bearing
	 */
	public float getBearing() {
		return bearing;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

}
