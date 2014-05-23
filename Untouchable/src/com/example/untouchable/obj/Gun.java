package com.example.untouchable.obj;

import java.util.*;

import android.content.Context;
import android.graphics.*;
import android.media.SoundPool;

import com.example.untouchable.R;

public class Gun extends GameObject {
	private double bearing = 0., offset = 0.;
	private static int height, width;
	private short coolDown, spread;
	private int pattern, difficulty = 2;
	private Matrix matrix;
	private boolean init;
	
	//for debug
//	private Paint paint;
	
	/**
	 * Class constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param pattern the firing pattern of this Gun instance
	 * @param context the execution context
	 * @param sounds the sound effects
	 * @param soundLbls the sound effect IDs
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
		
		init();
	}

	/**
	 * Updates the position and bearing of the Gun.
	 * @param canvas the drawing surface
	 */
	public void update() {
		matrix.setRotate((float)(Math.toDegrees(bearing + offset)), width/2f, height/3.6f);
		matrix.postTranslate(x-width/2, y-(int)(height/3.6f));
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(sprite, matrix, null);
		
		/*
		{ 	//for debug
        	paint = new Paint();
        	paint.setColor(Color.WHITE);
		    canvas.drawText(""+pattern, x, y, paint);
		
			paint.setColor(Color.RED);
			canvas.drawCircle(x, y, 2, paint);
		}*/
	}
	
	/**
	 * Adjusts the aim of the Gun.
	 * @param targetX the x coordinate to aim for
	 * @param targetY the y coordinate to aim for
	 */
	public void setBearing(int targetX, int targetY) {
		bearing = Math.atan2(x - targetX, targetY - y);
	}
	
	/**
	 * Fires the Gun.
	 * @return an ArrayList containing the newly fired Shots
	 */
	public ArrayList<Shot> fireGun() {
		ArrayList<Shot> result = new ArrayList<Shot>();
		
		switch(pattern) {
		case 1:
			result.add(new Shot(x, y, bearing + Math.toRadians(5), 5+difficulty*5, context, sounds, soundLbls));
			result.add(new Shot(x, y, bearing - Math.toRadians(5), 5+difficulty*5, context, sounds, soundLbls));
            result.add(new Shot(x, y, bearing, 5+difficulty*5, context, sounds, soundLbls));
            
            break;
            
		case 0:
	        result.add(new Shot(x, y, bearing, 5+difficulty*5, context, sounds, soundLbls));
	        
	        spread = (short)(++spread % 5);
	        
	        if(spread == 0) {
	            coolDown = (short) (25/difficulty);
	        }

			break;
		    
		case 2:
		case 3:
		    offset = (pattern == 3 ? Math.toRadians(10*spread - 50) : Math.toRadians(50 - 10*spread));

            result.add(new Shot(x, y, bearing + offset, 5+difficulty*5, context, sounds, soundLbls));

            spread = (short) (++spread % 11);

            if(spread == 0) {
                coolDown = (short) (50/difficulty);
                
                offset = 0.;
            }
            
		    break;
	    
		}
			
		sounds.play(soundLbls.get("gunfire"), .01f, .01f, 1, 0, 1);

		return result;
	}
	
	/**
	 * Retrieves the center of the Gun.
	 * @return a Point containing the center x-y coordinates
	 */
	public Point getCenter() {
		return new Point(x, y);
	}

	/**
	 * Sets the difficulty level of the Gun.
	 * @param difficulty the difficulty level
	 */
	public void setDifficulty(short difficulty) {
		this.difficulty = difficulty;
	}
	
	/**
	 * Retrieves the cooldown value of the Gun.
	 * @return the frames left before the Gun fires again
	 */
	public short getFireTimer() {
	    if(pattern == 1) {
	        coolDown = (short) (++coolDown % (25f/difficulty));
		}
	    
	    else if(spread > 0 || init) {
    		switch(pattern) {
    		case 0:
    	        coolDown = (short)(++coolDown % 4);
    		
    		    break;
    		    
    		case 2:
    		case 3:
    			coolDown = 0;
    			
    			break;
    		}
    		
    		init = false;
		}
	    
		else {
		    coolDown--;
		}
	    
		return coolDown;
	}

	/**
	 * Sets the initial parameters of the Gun.
	 */
    public void init() {
        init = true;
        coolDown = 0;
        spread = 0;
    }
}
