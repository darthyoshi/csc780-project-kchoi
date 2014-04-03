/**
 *  @file Enemy.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

import java.util.ArrayList;

import com.example.untouchable.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

//TODO create Enemy fields and methods
public class Enemy extends GameObject {
	private ArrayList<Gun> guns;
	/**
	 * 
	 * @param x
	 * @param y
	 * @param view
	 * @param type
	 */
	public Enemy(Context context, int type) {
		guns = new ArrayList<Gun>();
		//this.sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_enemy)
	}
	
	/**
	 * @return the guns
	 */
	public ArrayList<Gun> getGuns() {
		return guns;
	}
	
}
