/**
 * @file GameArea.java
 * @author Kay Choi 
 */
package com.example.untouchable;

import java.io.Serializable;
import java.util.*;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class Background extends View {
    private Paint p;
    private static List<Point> starField = null;
    private static List<Integer> scrollSpeeds = null;
    private static final int starCount = 25;
    private Point star;
    private Random r;

	/**
	 * @param context
	 * @param attrs
	 */
	public Background(Context context, AttributeSet attrs) {
		super(context, attrs);
		p = new Paint();
		r = new Random();
	}

    private void initializeStars(int maxX, int maxY) {
        starField = new ArrayList<Point>();
        scrollSpeeds = new ArrayList<Integer>();

        for (int i = 0; i < starCount; i++) {
            int x = r.nextInt(maxX - 5 + 1) + 5;
            int y = r.nextInt(maxY - 5 + 1) + 5;

            starField.add(new Point(x, y));
            
            scrollSpeeds.add(r.nextInt(10) + 5);
        }

    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        //create a black canvas
        p.setColor(Color.BLACK);
        p.setAlpha(255);
        p.setStrokeWidth(1);

        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        //initialize the starfield if needed
        if (starField == null) {
            initializeStars(canvas.getWidth(), canvas.getHeight());
        }

        //draw the stars
        p.setColor(Color.WHITE);

        p.setStrokeWidth(3);

        for (int i = 0; i < starCount; i++) {
        	star = starField.get(i);
        	
        	star.y += scrollSpeeds.get(i);
        	
        	if(star.y >= canvas.getHeight()) {
        		star.y = -4;
        		star.x = r.nextInt(canvas.getWidth() - 5 + 1) + 5;
        		
        		scrollSpeeds.set(i, r.nextInt(10) + 5);
        	}
        	
            canvas.drawPoint(starField.get(i).x, starField.get(i).y, p);
        }

    }
}
