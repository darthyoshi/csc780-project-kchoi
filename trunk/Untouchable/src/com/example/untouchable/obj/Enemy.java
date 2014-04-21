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

//TODO create Enemy fields and methods
public class Enemy extends GameObject {
	private ArrayList<Gun> guns;
	private int type;
	private Bitmap exhaust;
	private static int height, width, frame = 0;
	private static Rect src, dst;
	private static Paint exhaustAlpha;
	private boolean explode = false;
	
	/**
	 * 
	 * @param context the parent Context for the Enemy
	 * @param type
	 */
	public Enemy(int type, Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		super(context, sounds, soundLbls);
		
		this.type = type;
	
		exhaustAlpha = new Paint();
		exhaustAlpha.setAlpha(192);
		
		init(type);
	}
	
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
		
		genGuns();
	}
	
	private void genGuns() {
		guns = new ArrayList<Gun>();
		
		short i;
		
		switch(type) {
		case 0:
			for(i = 1; i < 3; i++) {
				guns.add(new Gun(x + width/2 + i*width/6, y + (1+i)*height/6, /*(int)(Math.random()*5)*/(i-1)*2, context, sounds, soundLbls));
				guns.add(new Gun(x + width/2 - i*width/6, y + (1+i)*height/6, /*(int)(Math.random()*5)*/2*i-1, context, sounds, soundLbls));
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
	
	public void init() {
		int dy = ((Activity) context).findViewById(R.id.fg).getHeight() - y;
		
		y += dy;
		
		for(Gun gun : guns) {
			gun.setY(gun.getY()+dy);
		}
	}
	
	/**
	 * @return the guns
	 */
	public ArrayList<Gun> getGuns() {
		return guns;
	}
	
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
		frame = ++frame % 4;

		int srcX = width - (frame/2) * width;
		
		if(y > 0) {
			y -= canvasHeight/40;
			
			for(Gun gun : guns) {
				gun.setY(gun.getY() - canvasHeight/40);
			}
		}
				System.out.println(y);
		src = new Rect(srcX, 0, srcX + width, height);
		dst = new Rect(x, y, x + width, y + height);
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(exhaust, src, dst, exhaustAlpha);
		canvas.drawBitmap(sprite, dst.left, dst.top, null);
	}

	public void startExplosion() {
		explode = true;
	}
}
