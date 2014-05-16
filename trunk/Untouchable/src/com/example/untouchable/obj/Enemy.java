/**
 *  @file Enemy.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.media.SoundPool;

import com.example.untouchable.R;

public class Enemy extends GameObject {
	private ArrayList<Gun> guns;
	private int type;
	private Bitmap exhaust;
	private static int height, width, frame = 0;
	private static Rect src, dst;
	private static Paint exhaustAlpha;
	private static Bitmap explosion = null;
	private boolean explode = false;
	private ArrayList<Point> explosions = null;
	
	/**
	 * Class constructor.
	 * @param type specifies which Enemy to initialize
	 * @param context the execution context
	 * @param sounds the sound effects
	 * @param soundLbls the sound effect IDs
	 */
	public Enemy(int type, Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		super(context, sounds, soundLbls);
		
		this.type = type;
	
		exhaustAlpha = new Paint();
		exhaustAlpha.setAlpha(192);
		
		init(type);
	}
	
	/**
	 * Initializes the Enemy. The proper sprite is chosen, and the Guns are generated in the appropriate patterns.
	 * @param type specifies which Enemy to initialize
	 */
	public void init(int type) {
		TypedArray tmpArray = context.getResources().obtainTypedArray(R.array.enemy_sprites);
		int spriteIds = tmpArray.getResourceId(type, -1);
		tmpArray.recycle();
		
		tmpArray = context.getResources().obtainTypedArray(spriteIds);
		int tempId = tmpArray.getResourceId(0, -1);
		Bitmap tmpBmp = BitmapFactory.decodeResource(context.getResources(), tempId);
		int tmpWidth = tmpBmp.getWidth(), tmpHeight = tmpBmp.getHeight();

		sprite = Bitmap.createScaledBitmap(tmpBmp, 2*tmpWidth/3, 2*tmpHeight/3, true);

		tempId = tmpArray.getResourceId(1, -1);
		exhaust = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), tempId), 4*tmpWidth/3, 2*tmpHeight/3, true);

		tmpArray.recycle();
		
		height = 2*tmpHeight/3;
		width = 2*tmpWidth/3;
		
		Point outSize = new Point(((Activity) context).findViewById(R.id.fg).getWidth(), ((Activity) context).findViewById(R.id.fg).getHeight());

		x = (outSize.x - width)/2;
		y = outSize.y;
		
		if(explosion == null) {
		    explosion = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_explosion_2);
		}
		
		if(explosions == null) {
		    explosions = new ArrayList<Point>();
		}
		
		else {
		    explosions.clear();
		}
		
		genGuns();
	}
	
	/**
	 * Determines the Gun layout for the Enemy and generates the Gun objects.
	 */
	private void genGuns() {
		guns = new ArrayList<Gun>();
		
		short i;
		
		switch(type) {
		case 0:
			for(i = 1; i < 3; i++) {
				guns.add(new Gun(x + width/2 + i*width/6, y + (1+i)*height/6, (i-1)*2, context, sounds, soundLbls));
				guns.add(new Gun(x + width/2 - i*width/6, y + (1+i)*height/6, 2*i-1, context, sounds, soundLbls));
			}
			
			break;
		
		case 1:
			for(i = 0; i < 2; i++) {
				guns.add(new Gun(x + width/20 + i*width/5, y + (int)((1.5f+i*6f/6)*height/6), (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + 19*width/20 - i*width/5, y + (int)((1.5f+i*6f/6)*height/6), (int)(Math.random()*5), context, sounds, soundLbls));
			}
			
			guns.add(new Gun(x + width/2, y + height/2, i, context, sounds, soundLbls));
			
			break;
		
		case 2:
			guns.add(new Gun(x + width/2, y + 30*height/86, (int)(Math.random()*5), context, sounds, soundLbls));
			
			for(i = -1; i < 2; i += 2){
				guns.add(new Gun(x + width/2 + i*(int)(0.15*width), y + 26*height/86, (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + width/2 + i*4*width/10, 36*height/86, (int)(Math.random()*5), context, sounds, soundLbls));
			}
			
			break;
		
		case 3:
			for(i = 0; i < 2; i++) {
				guns.add(new Gun(x + 5*width/24, y + height/5 + i*height*3/10, (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + 19*width/24, y + height/5 + i*height*3/10, (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + width/2, y + (int)((.3f+i*.2f)*height), (int)(Math.random()*5), context, sounds, soundLbls));
			}
			
			break;
		
		case 4:
			for(i = -1; i < 2; i += 2) {
				guns.add(new Gun(x + width/2 + i*width/8, y + height/3, (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + width/2 + i*width/4, y + (int)(.445f*height), (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + width/2 + i*width/4, y + 13*height/24, (int)(Math.random()*5), context, sounds, soundLbls));
				guns.add(new Gun(x + width/2, y + 105*height/240 + 45*i*width/240, (int)(Math.random()*5), context, sounds, soundLbls));
			}
			
		}
	}
	
	/**
	 * Retrieves the Guns associated with the Enemy.
	 * @return an ArrayList containing the Guns
	 */
	public ArrayList<Gun> getGuns() {
		return guns;
	}
	
	/**
	 * Fires each of the Guns of the Enemy and returns the new Shots.
	 * @return an ArrayList containing the newly fired Shots
	 */
	public ArrayList<Shot> fireGuns() {
		ArrayList<Shot> result = new ArrayList<Shot>();
		
		for(Gun gun : guns) {
			if(gun.getFireTimer() == 0) {
				result.addAll(gun.fireGun());
			}
		}

		return result;
	}
	
	/**
	 * Updates the position of the Enemy.
	 * @param canvasHeight the height of the canvas
	 */
	public void update(int canvasHeight) {
		frame = ++frame % 2;
    		
		int srcX = width - frame * width;
		
		if(y > 0) {
    		y -= canvasHeight/40;
			
			for(Gun gun : guns) {
				gun.setY(gun.getY() - canvasHeight/40);
				
				if(y < 0) {
				    gun.setY(gun.getY() - y);
				}
			}
			
			if(y < 0) {
			    y = 0;
			}
		}

		src = new Rect(srcX, 0, srcX + width, height);
		dst = new Rect(x, y, x + width, y + height);
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(exhaust, src, dst, exhaustAlpha);
		canvas.drawBitmap(sprite, dst.left, dst.top, null);
		
		if(explode) {
			drawExplosion(canvas);
		}
	}
	
	private void drawExplosion(Canvas canvas) {
	    if(frame % 2 == 0) {
	        for(Point point : explosions) {
	            canvas.drawBitmap(explosion, point.x, point.y, null);
	        }
	    }
	}

	public void startExplosion() {
		explode = true;
		
        sounds.play(soundLbls.get("explosion_long"), .125f, .125f, 1, 0, 1);
        
        for(short i = 0; i < 10; i++) {
            explosions.add(new Point((int)(Math.random()*sprite.getWidth()/2), (int)(Math.random()*sprite.getHeight()/2)));
        }
        
        guns.clear();
	}
}
