/**
 *  @file Player.java
 *  @author Kay Choi
 */

package com.example.untouchable.obj;

import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.media.*;
import android.view.*;

import com.example.untouchable.R;

public class Player extends GameObject {
	private static int frame = 0, width, height;
	private int xSpeed = 0, ySpeed = 0;
	private Rect src, dst;
	private boolean fireBeam, expandBeam = true, emp = false, explode;
	private int beamWidth = 1, empR = 0, streamId = 0;
	private RectF empBox, playerBox;
	private static Bitmap exhaust = null, explosion = null;
	private static Paint paint = null;
	private static AudioTrack beamSound = null;
	
	/**
	 * Class constructor.
	 * @param context the execution context
	 * @param sounds the sound effects
	 * @param soundLbls the sound effect IDs
	 */
	public Player(Context context, SoundPool sounds, HashMap<String, Integer> soundLbls) {
		super(context, sounds, soundLbls);
		
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player_ship);
		
		if(exhaust == null) {
    		exhaust = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player_exhaust);
		}
		
		if(explosion == null) {
    		explosion = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_explosion_1);
		}
		
		height = sprite.getHeight();
		width = sprite.getWidth();
		
		if(paint == null) {
		    paint = new Paint();
		}
		
		empBox = new RectF(0, 0, 0, 0);
		playerBox = new RectF(0, 0, 0, 0);
		
        beamSound = new AudioTrack(
                AudioManager.STREAM_MUSIC, 
                22050, 
                AudioFormat.CHANNEL_OUT_DEFAULT, 
                AudioFormat.ENCODING_DEFAULT, 
                17000, 
                AudioTrack.MODE_STATIC
        );
        
        //TODO: load "beamfire", convert to PCM
        
        //beamSound.write(pcmData, 0, pcmData.length);

		init();
	}

	/**
	 * Initializes the Player fields.
	 */
	public void init() {
		View fg = ((Activity)context).findViewById(R.id.fg);
		x = fg.getWidth() / 2;
		y = fg.getHeight() - height/2;
		
		xSpeed = ySpeed = 0;
		
		playerBox.set(x - (int)(6f/57*width), y - (int)(10f*height/141), 
				x + (int)(6f*width/57), y + (int)(10f*height/141));
		
		src = new Rect(0, 0, width, height);
		dst = new Rect(x - width/2, y - (int)(height/2.6f), x + width/2, y + (int)(height*(16f/26f)));
		
		fireBeam = false;
		explode = false;
		emp = false;
		expandBeam = true;
	}
	
	/**
	 * Updates the position of the Player.
	 * @param canvasWidth the width of the canvas
	 * @param canvasHeight the height of the canvas
	 * @param move whether or not the Player can move
	 */
	public void update (int canvasWidth, int canvasHeight, boolean move) {
		frame = ++frame % 2;

		int srcX = frame * width;

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

	/**
	 * Draws the Player.
	 * @param canvas the drawing surface
	 */
	public void draw(Canvas canvas) {
		if(fireBeam) {
			drawBeam(canvas);
		}
		
		paint.setAlpha(192);
		
		if(!explode) {
			canvas.drawBitmap(exhaust, src, dst, paint);
			canvas.drawBitmap(sprite, dst.left, dst.top, null);
		}
		
		else {
			updateAndDrawExplosion(canvas);
		}

		if(emp) {
			updateAndDrawEMP(canvas);
		}
		
		//drawDebugHitbox(canvas);	
	}
	
	/**
	 * Draws the beam.
	 * @param canvas the drawing surface
	 */
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
		
		//sounds.resume(streamId);
		
		if(expandBeam) {
			beamWidth += 2;
			
			if(beamWidth > 100) {
				expandBeam = false;
			}
		}
		
		else {
			beamWidth -= 2;
			
			if(beamWidth < 1) {
				fireBeam = false;
				
	//			sounds.stop(streamId);
			}
		}
	}
	
	/**
	 * Sets the movement speed of the Player, based on the orientation of the device.
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
	 * @param canvas the drawing surface
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
	 
	/**
	 * Returns the current boundaries of the Player sprite.
	 * @return a RectF that defines the bounds of the sprite
	 */
	@Override
	public RectF getBounds() {
		return new RectF(x - width/2f, y - height/2.6f, x + width/2f, y + height*(16f/26f));
	}
	
	/**
	 * Returns the current hitbox of the Player.
	 * @return a RectF that defines the bounds of the hitbox
	 */	
	public RectF getHitbox() {
		playerBox.set(x - 6f/57f*width, y - 8f*height/141f, 
				x + 6f*width/57f, y + 8f*height/141f);

		return playerBox;
	}
	
	/**
	 * Updates the location and width of the EMP and draws it.
	 * @param canvas the drawing surface
	 */
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
	
	/**
	 * Returns the current radius of the EMP wave.
	 * @return one half of the length of the EMP boundary box
	 */
	public int getEMPR() {
		return empR;
	}
	
	/**
	 * Sets whether or not the beam should be firing.
	 * @param state the state of the beam
	 */
	public void setBeamState(boolean state) {
		fireBeam = state;
		
		/*if(fireBeam) {
			streamId = sounds.play(soundLbls.get("beamfire"), .5f, .5f, 1, -1, .5f);
		}*/
		
	//	beamSound.play();
	}
	
	/**
	 * Activates the EMP animation.
	 */
	public void startEMP() {
    	emp = true;
    		
		sounds.play(soundLbls.get("emp"), .125f, .125f, 1, 0, 2.f);
	}

	/**
	 * Activates the explosion animation.
	 */
	public void startExplosion() {
		explode = true;
		
		sounds.play(soundLbls.get("explosion_short"), .125f, .125f, 1, 0, 1);
	}
	
	/**
	 * 
	 * @param canvas the drawing surface
	 */
	private void updateAndDrawExplosion(Canvas canvas) {
		if(frame % 2 == 0) {
		    canvas.drawBitmap(explosion, x-explosion.getWidth()/2, y-explosion.getHeight()/2, null);
		}
	}

	public boolean beamFinished() {
	    return !(fireBeam && expandBeam);
	}
	
}
