/**
 *  @file Shot.java
 *  @author Kay Choi
 */
package com.example.untouchable.obj;

import java.util.HashMap;

import com.example.untouchable.R;

import android.content.Context;
import android.graphics.*;
import android.media.SoundPool;

public class Shot extends GameObject {
	private double bearing, speed = 0.;
	private static int height, width;
	private float dx, dy;
	private Matrix matrix;
	private float[] values;
	private RectF bounds;
	
	/**
	 *  Class constructor.
	 *  @param x the initial x coordinate
	 *  @param y the initial y coordinate
	 *  @param bearing the bearing
	 *  @param speed the initial speed
	 *  @param context the execution context
	 *  @param sounds the sound effects
	 *  @param the sound effect IDs
	 */
	public Shot(int x, int y, double bearing, double speed, Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		super(context, sounds, soundLbls);
		
		this.x = x;
		this.y = y;
		this.bearing = bearing;
		this.speed = speed;

		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_shot);
		
		height = sprite.getHeight();
		width = sprite.getWidth();
		
		matrix = new Matrix();
		matrix.setRotate((float)(Math.toDegrees(bearing)), x, y);
		matrix.preTranslate(x-width/2, y+2f*height/3);
		
		bounds = new RectF(0, 0, 0, 0);
	}

	/**
	 * @return the velocity
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Updates the position of the Shot and draws the sprite. 
	 * @param canvas the drawing surface
	 * @param move whether or not the Shot should be moving
	 */
	public void update(boolean move) {
		dx = (float)(speed*Math.sin(bearing));
		dy = (float)(speed*Math.cos(bearing));

		if(move) {
			matrix.postTranslate(-dx, dy);
		}
		
		values = new float[9];
		matrix.getValues(values);
		x = (int)values[Matrix.MTRANS_X];
		y = (int)values[Matrix.MTRANS_Y];
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(sprite, matrix, null);
		
	//	drawDebugHitbox(canvas);
	}

	/**
	 * Draws the debug hitbox.
	 * @param canvas the drawing surface
	 */
	protected void drawDebugHitbox(Canvas canvas) {
		RectF hitbox = getHitbox();
		Paint paint = new Paint();
		paint.setColor(Color.argb(128, 255, 0, 255));
		canvas.drawRect(hitbox, paint);	
	}
	
	/**
	 * Returns the current hitbox of the Shot.
	 * @return a RectF that defines the bounds of the hitbox
	 */
	public RectF getHitbox() {
		bounds.set(0, height/2, width, height);
		
		matrix.mapRect(bounds);
		
		return bounds;
	}
}
