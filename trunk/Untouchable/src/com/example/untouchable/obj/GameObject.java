/**
 * @file GameObject.java
 * @author Kay Choi
 */

package com.example.untouchable.obj;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceView;
import android.widget.ImageView;

public abstract class GameObject {
	protected int x;
	protected int y;
	protected Bitmap sprite;
	protected Context context;
	public int HEIGHT;
	public int WIDTH;
	
	/**
	 * @return the sprite
	 */
	public Bitmap getSprite() {
		return sprite;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
}
