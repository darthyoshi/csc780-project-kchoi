package com.example.untouchable.canvas;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.untouchable.R;
import com.example.untouchable.R.color;
import com.example.untouchable.obj.*;

@SuppressLint("WrongCall")
public class Foreground extends SurfaceView implements SurfaceHolder.Callback {
	private static Player player;
	private static Enemy enemy;
	private static List<Shot> shots;
	private SurfaceHolder holder;
	private Context context;
	private GameLoopThread gameLoopThread;
	
	private Gun gun;
	
	public Foreground(Context context, AttributeSet attrs) {
		super(context, attrs);
		gameLoopThread = new GameLoopThread(this);

        setZOrderOnTop(true);
		
        holder = getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);
		holder.addCallback(new SurfaceHolder.Callback() {
		    
			@Override
		    public void surfaceDestroyed(SurfaceHolder holder) {
		        boolean retry = true;
		        gameLoopThread.setRunning(false);

		        while (retry) {
		            try {
		                gameLoopThread.join();
		                retry = false;
		            }

		            catch (InterruptedException e) { }
		        }
		    }

		    @Override
		    public void surfaceCreated(SurfaceHolder holder) {
		        gameLoopThread.setRunning(true);
		        gameLoopThread.start();
		    }
		    
		    @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
        });
		
		player = new Player(getContext());
		
		gun = new Gun(10,50,0.,getContext());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(canvas != null) {
			Paint paint = new Paint();
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			canvas.drawRect(new Rect(0, 0, canvas.getWidth(),canvas.getHeight()), paint);
	
			player.draw(canvas);
			
			Point point = player.getCenter();
			
			gun.setBearing(point.x, point.y);
			gun.draw(canvas);
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public class GameLoopThread extends Thread {
		private static final long FPS = 25;
	    private Foreground fg;
	    private boolean running = false;
	    private Canvas c;
	    
	    public GameLoopThread(Foreground fg) {
	        this.fg = fg;
	    }

	    public void setRunning(boolean run) {
	        running = run;
	    }

	    @Override
	    public void run() {
	    	long ticksPS = 1000 / FPS;
	    	long startTime;
	    	long sleepTime;
	    	
	    	while (running) {
	    	    c = null;
	    	    startTime = System.currentTimeMillis();

	    	    try {
	    	        c = fg.getHolder().lockCanvas();
	    	        synchronized (fg.getHolder()) {
	    	            fg.onDraw(c);
	    	        }
	    	    }

	    	    finally {
	    	        if (c != null) {
	    	            fg.getHolder().unlockCanvasAndPost(c);
	    	        }
	    	    }

	    	    sleepTime = ticksPS-(System.currentTimeMillis() - startTime);

	    	    try {
	    	        if (sleepTime > 0) {
	    	            sleep(sleepTime);
	    	        }

	    	        else {
	    	            sleep(10);
	    	        }

	    	    }

	    	    catch (Exception e) {}

	    	}
	    }
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}   
}
