package com.example.untouchable.obj;

import java.util.*;

import android.content.Context;
import android.graphics.*;
import android.media.SoundPool;

import com.example.untouchable.R;

public class Gun extends GameObject {
	private double bearing = 0.;
	private static int height, width;
	private short coolDown = 0, spread = 1;
	private int pattern, difficulty = 2;
	private Matrix matrix;
	
	private short interval[] = {3, 4, 5, 2, 1};
	
	//for debug
//	private Paint paint;
	
	/**
	 * 
	 * @param x the x coordinate of the Gun rotation point
	 * @param y the y coordinate of the Gun rotation point
	 * @param view
	 */
	public Gun(int x, int y, int pattern, Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		super(context, sounds, soundLbls);
		
		this.x = x;
		this.y = y;
		this.pattern = pattern;

		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_gun);
		
		height = sprite.getHeight();
		width = sprite.getWidth();
		
		matrix = new Matrix();
	}

	/**
	 * 
	 * @param canvas the canvas to draw on
	 */
	public void update() {
		matrix.setRotate((float)(Math.toDegrees(bearing)), width/2f, height/3.6f);
		matrix.postTranslate(x-width/2, y-(int)(height/3.6f));
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(sprite, matrix, null);
		/*
		{ 	//for debug
			paint = new Paint();
			paint.setColor(Color.RED);
			canvas.drawCircle(x, y, 2, paint);
		}*/
	}
	
	/**
	 * 
	 * @param targetX the x coordinate to aim for
	 * @param targetY the y coordinate to aim for
	 */
	public void setBearing(int targetX, int targetY) {
		bearing = Math.atan2(x - targetX, targetY - y);
	}
	
	public ArrayList<Shot> fireGun() {
		ArrayList<Shot> result = new ArrayList<Shot>();
		
		if(pattern == 3) {
			result.add(new Shot(x, y, bearing + Math.toRadians(5*spread - 55), 5+difficulty*5, context, sounds, soundLbls));
			
			spread = (short) (++spread % 21);
			
			if(spread == 0) {
				coolDown = 50;
			}
		}
		
		else if(pattern == 2) {
			result.add(new Shot(x, y, bearing + Math.toRadians(55 - 5*spread), 5+difficulty*5, context, sounds, soundLbls));
			
			spread = (short) (++spread % 21);
			
			if(spread == 0) {
				coolDown = 50;
			}
		}
		
		else {
			result.add(new Shot(x, y, bearing, 5+difficulty*5, context, sounds, soundLbls));
			
			if(pattern == 1) {
				result.add(new Shot(x, y, bearing + Math.toRadians(5), 5+difficulty*5, context, sounds, soundLbls));
				result.add(new Shot(x, y, bearing - Math.toRadians(5), 5+difficulty*5, context, sounds, soundLbls));
			}
			
		}
			
		sounds.play(soundLbls.get("gunfire"), .01f, .01f, 1, 0, 1);
		
		return result;
	}
	
	public Point getCenter() {
		return new Point(x, y);
	}

	public void setDifficulty(short difficulty) {
		this.difficulty = difficulty;
	}
	
	public short getFireTimer() {
		switch(pattern) {
		case 2:
		case 3:
			if(spread > 0) {
				coolDown = 0;
			}
			
			else {
				coolDown--;
			}
			
			break;
		
		default:
			coolDown = (short) (++coolDown % 25/*interval[pattern]*//difficulty);
		}
		
		return coolDown;
	}
}
