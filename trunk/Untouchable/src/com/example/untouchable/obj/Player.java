/**
 *  @file Player.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

public class Player {
	private int x;
	private int y;

	/**
	 *  Class constructor.
	 * @param x
	 * @param y
	 */
	public Player(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 *  Returns the y-coordinate of the player craft.
	 *  @return the y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 *  Sets the y-coordinate of the player craft.
	 *  @param y the new y-coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 *  Returns the x-coordinate of the player craft.
	 *  @return the x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * 	Returns the x-coordinate of the player craft.
	 *  @param x the new x-coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	
}
