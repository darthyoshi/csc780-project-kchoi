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
	int xSpeed = 0, ySpeed = 0, dZ = 0;
	Rect src, dst;
	
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

		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point outSize = new Point();
		
		display.getSize(outSize);
		
		x = outSize.x/2 - width;
		y = outSize.y - 2*height;
	}

	private void update (Canvas canvas) {
		frame = ++frame % 4;

		int srcX = (frame/2) * width;
		
		src = new Rect(srcX, 0, srcX+width, height);
		dst = new Rect(x, y, x+width, y+height);
		
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
		
		else if (y > canvas.getHeight() - height) {
			y = canvas.getHeight() - height;
		}

		else {
			y += ySpeed;
		}
		
	}
	//TODO fix y update rate
	public void setSpeed(int dX, int dY, int dZ) {
		this.xSpeed = dX;
		this.ySpeed = dY;
	}
	
	public void draw(Canvas canvas) {
		update(canvas);
		canvas.drawBitmap(sprite, src, dst, null);
	}
	
	public Point getCenter() {
		return new Point(x + width, y + height);
	}
}
