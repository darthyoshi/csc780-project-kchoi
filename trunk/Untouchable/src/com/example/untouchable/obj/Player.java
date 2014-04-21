/**
 *  @file Player.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.media.SoundPool;
import android.view.*;

import com.example.untouchable.R;

public class Player extends GameObject {
	private static int frame = 0, width, height;
	private int xSpeed = 0, ySpeed = 0;
	private Rect src, dst;
	private boolean win = false, beginFire = true, emp = false, explode = false;
	private int beamWidth = 1, empR = 0;
	private RectF empBox, playerBox;
	private static Bitmap exhaust;
	private static Paint paint;
	
	/**
	 * Class constructor.
	 * @param context the parent context for the Player
	 */
	public Player(Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		super(context, sounds, soundLbls);
		
		exhaust = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player_exhaust);
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player_ship);
		
		height = sprite.getHeight();
		width = sprite.getWidth();
		
		paint = new Paint();
		
		empBox = new RectF(0, 0, 0, 0);
		playerBox = new RectF(0, 0, 0, 0);
		
		init();
	}

	/**
	 * Initializes the Player position.
	 */
	public void init() {
		View fg = ((Activity)context).findViewById(R.id.fg);
		x = fg.getWidth() / 2;
		y = fg.getHeight() - height/2;
		
		playerBox.set(x - (int)(6f/57*width), y - (int)(10f*height/141), 
				x + (int)(6f*width/57), y + (int)(10f*height/141));
		
		src = new Rect(0, 0, width, height);
		dst = new Rect(x - width/2, y - (int)(height/2.6f), x + width/2, y + (int)(height*(16f/26f)));
	}
	
	/**
	 * Updates the position of the Player.
	 * @param canvasWidth the width of the canvas
	 * @param canvasHeight the height of the canvas
	 * @param move whether or not the Player can move
	 */
	public void update (int canvasWidth, int canvasHeight, boolean move) {
		frame = ++frame % 4;

		int srcX = (frame/2) * width;

		if(move) {
			if(x < width/2) {
				x = width/2;
			}
			
			else if(x > canvasWidth - width/2) {
				x = canvasWidth - width/2;
			}
			
			else {
				x += xSpeed;
			}
			
			if(y < (int)(height * 1f/2.6f)) {
				y = (int)(height * 1f/2.6f);
			}
			
			else if (y > canvasHeight - height/5f) {
				y = (int)(canvasHeight - height/5f);
			}
		
			else {
				y += ySpeed;
			}
		}
		
		src.set(srcX, 0, srcX+width, height);
		dst.set(x - width/2, y - (int)(height/2.6f), x + width/2, y + (int)(height*(16f/26f)));
		
	}

	public void draw(Canvas canvas) {
		if(win) {
			drawBeam(canvas);
		}
		
		paint.setAlpha(192);
		
		if(!explode) {
			canvas.drawBitmap(exhaust, src, dst, paint);
			canvas.drawBitmap(sprite, dst.left, dst.top, null);
		}
		
		else {
			//TODO: draw explosion
		}

		if(emp) {
			updateAndDrawEMP(canvas);
		}
		
		//drawDebugHitbox(canvas);	
	}
	
	private void drawBeam(Canvas canvas) {
		Point center = getCenter();
		
		int tempWidth = (beamWidth < width/3 ? beamWidth : width/3);
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		paint.setColor(Color.RED);
		canvas.drawRect(center.x - tempWidth, 0, center.x + tempWidth, center.y - height/20, paint);
		
		paint.setColor(Color.YELLOW);
		canvas.drawRect(center.x - 2*tempWidth/3, 0, center.x + 2*tempWidth/3, center.y - height/20, paint);
		
		paint.setColor(Color.WHITE);
		canvas.drawRect(center.x - tempWidth/3, 0, center.x + tempWidth/3, center.y - height/20, paint);
		
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
				
				sounds.stop(soundLbls.get("beamfire"));
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
		
		float mag = (float)(16f*sin/Math.sin(Math.PI/12));
		
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
	
	/**
	 * Returns the origin coordinates of the Player.
	 * @return a Point containing the x and y coordinates of the Player
	 */
	public Point getCenter() {
		return new Point(x, y);
	}

	/*
	 * Returns the current hitbox of the Player.
	 * @return a RectF that defines the bounds of the hitbox
	 */		 
	@Override
	public RectF getBounds() {
		return new RectF(x - width/2f, y - height/2.6f, x + width/2f, y + height*(16f/26f));
	}
	
	public RectF getHitbox() {
		playerBox.set(x - 6f/57f*width, y - 8f*height/141f, 
				x + 6f*width/57f, y + 8f*height/141f);

		return playerBox;
	}
	
	private void updateAndDrawEMP(Canvas canvas) {
		Point center = getCenter();
		
		paint.setColor(Color.CYAN);
		paint.setStrokeWidth(5);
		paint.setAlpha(255);
		paint.setStyle(Paint.Style.STROKE);
		
		empBox.set(center.x - empR, center.y - empR, center.x + empR, center.y + empR);
		
		canvas.drawArc(empBox, 0, 360, false, paint);
		
		emp = (empR += canvas.getHeight()/12) < canvas.getHeight();
		
		if(!emp) {
			empR = 0;
		}
	}
	
	public int getEMPR() {
		return empR;
	}
	
	public void setWinState(boolean state) {
		win = state;
		
		if(win) {
			sounds.play(soundLbls.get("beamfire"), .5f, .5f, 1, -1, .5f);
		}
	}
	
	public Bitmap getSprite() {
		return sprite;
	}
	
	public void activateEMP() {
		emp = true;
		
		sounds.play(soundLbls.get("emp"), .25f, .25f, 1, 0, 1);
	}

	public void startExplosion() {
		explode = true;
		
		sounds.play(soundLbls.get("explosion_short"), .5f, .5f, 1, 0, 1);
	}
}
