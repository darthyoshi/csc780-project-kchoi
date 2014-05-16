/**
 * @file GameObject.java
 * @author Kay Choi
 */

package com.example.untouchable.obj;

import java.util.HashMap;

import android.content.Context;
import android.graphics.*;
import android.media.SoundPool;

public abstract class GameObject {
	protected int x;
	protected int y;
	protected Bitmap sprite;
	protected Context context;
	protected boolean isClear = false;
	protected SoundPool sounds;
	protected HashMap<String, Integer> soundLbls;
	
	/**
	 * Class constructor.
	 * @param context the execution context
	 * @param sounds a SoundPool containing the game sound effects
	 * @param soundLbls the labels needed sound playback
	 */
	public GameObject(Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		this.context = context;
		this.sounds = sounds;
		this.soundLbls = soundLbls;
	}

	/**
	 * Retrieves the sprite of the GameObject.
	 * @return a Bitmap containing the sprite
	 */
	public Bitmap getSprite() {
		return sprite;
	}

	/**
	 * Retrieves the y-coordinate of the GameObject.
	 * @return the y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate.
	 * @param y the new y-coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Retrieves the x-coordinate of the GameObject.
	 * @return the x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x-coordinate.
	 * @param x the new x-coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Retrieves the boundaries of the GameObject.
	 * @return 
	 */
	public RectF getBounds() {
		return new RectF(x, y, x + sprite.getWidth(), y + sprite.getWidth());
	}
	
	/**
	 * Draws the hitbox of the GameObject.
	 * @param canvas the Canvas to draw with
	 */
	protected void drawDebugHitbox(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.CYAN);
		canvas.drawRect(getBounds(), paint);
	}
	
	/**
	 * Responsible for rendering the GameObject on screen. Must be implemented in child classes. 
	 * @param canvas the Canvas to draw upon
	 */
	public abstract void draw(Canvas canvas);
}
