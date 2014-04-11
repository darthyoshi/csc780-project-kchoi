/**
 *  @file Enemy.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.untouchable.R;

//TODO create Enemy fields and methods
public class Enemy extends GameObject {
	private ArrayList<Gun> guns;
	private int type;
	
	/**
	 * 
	 * @param context
	 * @param type
	 */
	public Enemy(Context context, int type) {
		this.type = type;
		this.sprite = BitmapFactory.decodeResource(context.getResources(), type);
		this.context = context;
		
		genGuns();
	}
	
	private void genGuns() {
		guns = new ArrayList<Gun>();
		
		short i = 0;
		int x, y;
		switch(type) {
		case R.drawable.sprite_enemy_1:
			while(i < 5) {
				guns.add(new Gun(0, 0, i, context));
			}
			break;
		
		case R.drawable.sprite_enemy_2:
			
			break;
		
		case R.drawable.sprite_enemy_3:
			
			break;
		
		case R.drawable.sprite_enemy_4:
			
			break;
		
		case R.drawable.sprite_enemy_5:
			
		}
	}
	
	public void init() {
		guns.clear();
	}
	
	/**
	 * @return the guns
	 */
	public ArrayList<Gun> getGuns() {
		return guns;
	}
	
	public void fireGuns() {
		for(Gun gun : guns) {
			gun.fireGun();
		}
	}
	
	public void updateAndDraw() {
		
	}
}
