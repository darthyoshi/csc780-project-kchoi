package com.example.untouchable.obj;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.untouchable.R;

public class Gun extends GameObject {
	private double bearing;
	private static int height, width;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param view
	 */
	public Gun(int x, int y, double bearing, Context context) {
		this.setX(x);
		this.setY(y);
		this.bearing = bearing;
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_gun);
		height = sprite.getHeight();
		width = sprite.getWidth();
	}

	public void draw(Canvas canvas) {
		Matrix matrix = new Matrix();
		matrix.setRotate((float)(bearing/Math.PI*180f), width/2, height/3.6f);
		
		canvas.drawBitmap(sprite, matrix, null);
	}
	
	public void setBearing(int targetX, int targetY) {
		bearing = Math.atan((1.*x - targetX)/(targetY - y));
	}
	
	public Shot fireGun() {
		return new Shot(x + WIDTH/2 , y + HEIGHT/2, bearing, context);
	}
}
