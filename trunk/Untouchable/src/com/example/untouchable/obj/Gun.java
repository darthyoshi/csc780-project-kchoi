package com.example.untouchable.obj;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.*;

import com.example.untouchable.R;

public class Gun extends GameObject {
	private double bearing = 0.;
	private static int height, width, difficulty;
	private int pattern;
	private Matrix matrix;
	
	private Paint paint;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param view
	 */
	public Gun(int x, int y, int pattern, Context context) {
		this.x = x;
		this.y = y;
		this.context = context;
		this.pattern = pattern;

		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_gun);
		
		height = sprite.getHeight();
		width = sprite.getWidth();
		
		matrix = new Matrix();
	}

	public void updateAndDraw(Canvas canvas) {
		matrix.setRotate((float)(Math.toDegrees(bearing)), width/2f, height/3.6f);
		matrix.postTranslate(x/*-width/2f*/, y/*-height/3.6f*/);
		
		canvas.drawBitmap(sprite, matrix, null);
		
		//debug
		paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawCircle(x+width/2, y+(int)(height/3.6f), 2, paint);
	}
	
	/**
	 * 
	 * @param targetX the x coordinate to aim for
	 * @param targetY the y coordinate to aim for
	 */
	public void setBearing(int targetX, int targetY) {
		Point center = getCenter();

		bearing = Math.atan2(center.x - targetX, targetY - center.y);
	}
	
	public ArrayList<Shot> fireGun() {
		Point center = getCenter();
		ArrayList<Shot> result = new ArrayList<Shot>();
		
		result.add(new Shot(center.x, center.y, bearing, 5+difficulty*5, context));
		
		return result;
	}
	
	public Point getCenter() {
		return new Point(x+width/2, y+(int)(height/3.6f));
	}

	public static void setDifficulty(short difficulty) {
		Gun.difficulty = difficulty;
	}
}
