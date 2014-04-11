/**
 *  @file Player.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

import android.content.Context;
import android.graphics.*;
import android.view.*;

import com.example.untouchable.R;

public class Player extends GameObject {
	private static int frame = 0, width, height;
	private int xSpeed = 0, ySpeed = 0;
	private Rect src, dst;
	private boolean win = false, beginFire = true;
	private int beamWidth = 1, empR = 0;
	private RectF empBox, playerBox;
	
	private Paint paint;
	
	/**
	 * Class constructor.
	 * @param x
	 * @param y
	 * @param view
	 */
	public Player(Context context) {
		this.context = context;
		
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player);
		
		height = sprite.getHeight();
		width = sprite.getWidth() / 2;
		
		paint = new Paint();
		
		empBox = new RectF(0, 0, 0, 0);
		playerBox = new RectF(0, 0, 0, 0);
		
		init();
	}

	/**
	 * 
	 */
	public void init() {
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point outSize = new Point();
		
		display.getSize(outSize);
		
		x = outSize.x/2 - width;
		y = outSize.y - 2*height;
		
		playerBox.set(x + (int)(24f/57*width), y + (int)(48f*height/141), 
				x + (int)(34f*width/57), y + (int)(62f*height/141));
	}
	
	/**
	 * Updates the position of the Player and draws the sprite.
	 * @param canvas the canvas to draw on
	 * @param move whether or not the Player should be moving
	 */
	public void updateAndDraw (Canvas canvas, boolean move) {
		frame = ++frame % 4;

		int srcX = (frame/2) * width;
		
		if(move) {
			if(x < 0) {
				x = 0;
			}
			
			else if(x > canvas.getWidth() - width) {
				x = canvas.getWidth() - width;
			}
			
			else {
				x += xSpeed;
			}
			
			if(y < 0) {
				y = 0;
			}
			
			else if (y > canvas.getHeight() - height/2) {
				y = canvas.getHeight() - height/2;
			}
		
			else {
				y += ySpeed;
			}
		}
		
		src = new Rect(srcX, 0, srcX+width, height);
		dst = new Rect(x, y, x+width, y+height);
		
		if(win) {
			drawBeam(canvas);
		}
		
		canvas.drawBitmap(sprite, src, dst, null);

		drawDebugHitbox(canvas);
	}

	private void drawBeam(Canvas canvas) {
		Point center = getCenter();
		
		int tempWidth = (beamWidth < width/3 ? beamWidth : width/3);
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		paint.setColor(Color.RED);
		canvas.drawRect(center.x - tempWidth, 0, center.x + tempWidth, center.y, paint);
		
		paint.setColor(Color.YELLOW);
		canvas.drawRect(center.x - 2*tempWidth/3, 0, center.x + 2*tempWidth/3, center.y, paint);
		
		paint.setColor(Color.WHITE);
		canvas.drawRect(center.x - tempWidth/3, 0, center.x + tempWidth/3, center.y, paint);
		
		if(beginFire) {
			beamWidth++;
			
			if(beamWidth > 100) {
				beginFire = false;
			}
		}
		
		else {
			beamWidth--;
			
			if(beamWidth < 1) {
				win = false;
				
				beginFire = true;
			}
		}
	}
	
	/**
	 * 
	 * @param azimuth the angle of horizontal displacement, in radians 
	 * @param altitude the angle of vertical displacement, in radians
	 */
	public void setSpeed(double azimuth, double altitude) {
		double sin = Math.sin(altitude > Math.PI/12 ? Math.PI/12 : altitude);
		
		float mag = (float)(15f*sin/Math.sin(Math.PI/12));
		
		xSpeed = (int) (Math.sin(azimuth)*-mag);
		ySpeed = (int) (Math.cos(azimuth)*mag);
	}

	/**
	 * Draws the debug hitbox.
	 * @param canvas the canvas to draw on
	 */
	protected void drawDebugHitbox(Canvas canvas) {
		//debug
		RectF hitbox = getHitbox();
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.RED);
		
		canvas.drawArc(hitbox, 0, 360, true, paint);
	}
	
	public Point getCenter() {
		return new Point(x + width/2, y + (int)(height/2.5f));
	}

	/*
	 * Returns the current hitbox of the Player.
	 * @return a RectF that defines the bounds of the hitbox
	 */		 
	@Override
	public RectF getBounds() {
		return new RectF(x, y, x + width, y + height);
	}
	
	public RectF getHitbox() {
		playerBox.set(x + (int)(24f/57*width), y + (int)(48f*height/141), 
				x + (int)(34f*width/57), y + (int)(62f*height/141));

		return playerBox;
	}
	
	public boolean updateAndDrawEMP(Canvas canvas) {
		Point center = getCenter();
		boolean result;
		
		paint.setColor(Color.CYAN);
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);
		
		empBox.set(center.x - empR, center.y - empR, center.x + empR, center.y + empR);
		
		canvas.drawArc(empBox, 0, 360, false, paint);
		
		result = (empR += canvas.getHeight()/12) < canvas.getHeight();
		
		if(!result) {
			empR = 0;
		}
		
		return result;
	}
	
	public int getEMPR() {
		return empR;
	}
	
	public void setWinState(boolean state) {
		win = state;
	}
}
