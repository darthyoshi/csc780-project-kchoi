package com.example.untouchable.canvas;

import java.util.*;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.media.*;
import android.util.AttributeSet;
import android.view.*;

import com.example.untouchable.R;
import com.example.untouchable.fragments.GameFragment;
import com.example.untouchable.obj.*;

@SuppressLint("WrongCall")
public class ForegroundView extends SurfaceView implements SurfaceHolder.Callback {
	private static Player player = null;
	private static Enemy enemy;
	private ArrayList<Shot> shots;
	private GameLoopThread gameLoopThread;
	private short difficulty = 2, frame = 0;
	private int level = -1, playTime = 0, score;
	private long startTime;
	private boolean init = true, isPaused = true, lvlClear = false, gameOver = false, useSensor, godMode;
	private static Rect blank = null;
	private static Paint erase = null;
	private Iterator<Shot> iter;
	private Shot shot;
	private Rect meter, pauseDest, pauseSrc;
	private Paint meterPaint = null, textPaint = null;
	private ArrayList<Gun> guns = null;
	private static Bitmap pauseButton;
	private RectF playerHitbox, shotHitbox;
	private Point playerCenter;
	private float charge = 0f, touchX, touchY, dX, dY;
	private Context context;
	private SoundPool sounds;
	private HashMap<String, Integer> soundLbls;
	
	private short touchCounter = 0; 
	
	//for debug
//	private Point gunCenter;
		
	/**
	 * Class constructor.
	 * @param context View execution context
	 * @param attrs View attributes
	 */
	public ForegroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
	
		gameLoopThread = new GameLoopThread(this);
		
		startTime = System.currentTimeMillis();
		
        setZOrderOnTop(true);
		
        SurfaceHolder holder = getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
		    public void surfaceDestroyed(SurfaceHolder holder) {
		        pauseThread();
	        }

		    @Override
		    public void surfaceCreated(SurfaceHolder holder) {
		    	resumeThread();
		    }
		    
		    @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
        });
				
		shots = new ArrayList<Shot>();
		
		pauseButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
		pauseSrc = new Rect(0, 0, pauseButton.getWidth(), pauseButton.getHeight());
	}
	
	/**
	 * Pauses the game execution thread.
	 */
	public void pauseThread() {
		boolean retry = true;
        gameLoopThread.setRunning(false);
        
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            }

            catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
        
        setPauseStatus(true);
	}
	
	/**
	 * Resumes the game execution thread.
	 */
	public void resumeThread() {
		if(enemy == null) {
			enemy = new Enemy(level, context, sounds, soundLbls);
		}
		
		else if(init) {
			enemy.init(level);
		}
		
		if(player == null) {
			player = new Player(context, sounds, soundLbls);
		}
		
		else if(init) {
			player.init();
		}
		
		touchX = player.getX();
		touchY = player.getY();
			
		if(guns == null) {
			guns = enemy.getGuns();

			for(Gun gun : guns) {
				gun.setDifficulty(difficulty);
			}
		}
		
		else if(init) {
		    for(Gun gun : guns) {
		        gun.init();
		    }
		}
		
		if(blank == null) {
			blank = new Rect(0, 0, getWidth(), getHeight());
		}
		
		if(erase == null) {
			erase = new Paint();
			erase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		}

		if(meterPaint == null) {
			meterPaint = new Paint();
		}
		
		if(textPaint == null) {
			textPaint = new Paint();
			textPaint.setColor(Color.WHITE);
		}

		pauseDest = new Rect(getWidth() - 10 - pauseButton.getWidth(), 10, getWidth() - 10, 10 + pauseButton.getHeight());
		
    	if(gameLoopThread.getState() == Thread.State.TERMINATED) {
    		gameLoopThread = new GameLoopThread(this);
    	}
    	
    	if(!gameLoopThread.isRunning()) {
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
    	}
		
		setPauseStatus(false);
	}

	/**
	 * Draws all objects to the canvas.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(canvas != null) {
			frame = (short) ((++frame) % (25 - 3*difficulty));

			canvas.drawRect(blank, erase);
			
			enemy.update(canvas.getHeight());
		
			if(!useSensor) {
			    player.setSpeed(Math.atan2(dX, dY), Math.sqrt(dX*dX+dY*dY));
			}
			
			synchronized(player) {
			    player.update(canvas.getWidth(), canvas.getHeight(), !isPaused && !init && !gameOver && !lvlClear);
			}
			
			if(!init && !isPaused && !lvlClear) {
				shots.addAll(enemy.fireGuns());
			}	
			
			playerCenter = player.getCenter();
			
			iter = shots.iterator();
			while(iter.hasNext()) {
				shot = iter.next();
				shot.update(!isPaused);

				if(!godMode && !gameOver && checkForHit(shot)) {
					//TODO: handle game over
					player.startExplosion();
					
					gameOver = true;
					
				      ((Activity)getContext()).runOnUiThread(new Runnable() {

				            @Override
				            public void run() {
    				            FragmentManager fragMan = ((Activity)context).getFragmentManager();
    				            ((GameFragment)fragMan.findFragmentByTag(
    				                fragMan.getBackStackEntryAt(fragMan.getBackStackEntryCount()-1)
    				                    .getName()))
				                    .saveScore();
				            }
				        });
				}
		
				if(shot.getX() > canvas.getWidth()+25 || shot.getX() < -25 ||
					shot.getY() < -25 || shot.getY() > canvas.getHeight()+25 ||
					checkEMPHit(shot)) 
				{
					iter.remove();
				}
			}		
			
			enemy.draw(canvas);
			
			for(Gun gun : guns) {
				gun.setBearing(playerCenter.x, playerCenter.y);
				gun.update();
				gun.draw(canvas);
				
/*				{	//for debug
					gunCenter = gun.getCenter();
					paint.setStrokeWidth(2);
					canvas.drawLine(gunCenter.x, gunCenter.y, playerCenter.x, playerCenter.y, paint);
				}
*/			}
			
			player.draw(canvas);
			
			for(Shot shot : shots) {
				shot.draw(canvas);
			}

		    updateAndDrawCharge(canvas);

			canvas.drawBitmap(pauseButton, pauseSrc, pauseDest, null);

			textPaint.setTextSize(32);
			textPaint.setTextAlign(Align.LEFT);
			
			canvas.drawText(context.getString(R.string.score) + score, canvas.getWidth()*.05f, canvas.getHeight()*.05f, textPaint);
			
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTextSize(50);
		
			if(init) {
				long curTime = System.currentTimeMillis();
				
				canvas.drawText(context.getString(R.string.level) + (level+1), canvas.getWidth()/2, (canvas.getHeight() - textPaint.ascent() - textPaint.descent())/2, textPaint);
				
				canvas.drawText(Long.toString(3 - (curTime - startTime)/1000), canvas.getWidth()/2, 0.6f*canvas.getHeight(), textPaint);
				
				if(curTime - startTime >= 3000) {
					startTime = System.currentTimeMillis();
					init = false;
				}
			}
			
			else if(isPaused) {
				canvas.drawText("PAUSED", canvas.getWidth()/2, (canvas.getHeight() - textPaint.ascent() - textPaint.descent())/2, textPaint);
			}
			
			else if(System.currentTimeMillis() - startTime < 1000) {
				canvas.drawText("GO!", canvas.getWidth()/2, (canvas.getHeight() - textPaint.ascent() - textPaint.descent())/2, textPaint);
			}
			
			else if(gameOver) {
				canvas.drawText("GAME OVER", canvas.getWidth()/2, (canvas.getHeight() - textPaint.ascent() - textPaint.descent())/2, textPaint);
			}
		}
	}
	
	/**
	 * Determines whether or not a Shot has crossed the EMP wave.
	 * @param shot the Shot to check
	 * @return true if the distance between the Shot and Player less than 100 pixels less than the EMP radius 
	 */
	private boolean checkEMPHit(Shot shot) {
		boolean result = false;
		
		int empR = player.getEMPR();
		
		if(empR > 0) {
			shotHitbox = shot.getHitbox();
			
			double dist = Math.sqrt(
				Math.pow(playerCenter.x - (shotHitbox.right + shotHitbox.left)/2, 2) +
				Math.pow(playerCenter.y - (shotHitbox.bottom + shotHitbox.top)/2, 2)
			);
			
			if(dist < empR && dist > empR - 100) {
				result = true;
			}
		}
		
		return result;
	}
	
	/**
	 * Increments the charge level and draws the charge meter.
	 * @param canvas the Canvas to draw upon
	 */
	private void updateAndDrawCharge(Canvas canvas) {
		int meterHeight;
		
		if(!init) {
			if(!isPaused && !gameOver && !lvlClear) {
				charge += 1f/665f;
			
    			if(charge > 1f) {
    				charge = 1f;
    
    				player.setBeamState(true);
    
    				enemy.startExplosion();
    			
    				//TODO: add delay
    				saveLvlScore();
    
    				dX = dY = 0;
    
    				lvlClear = true;
    				
    				guns.clear();
    			}
			}
		
			meterHeight = (int)(charge*(canvas.getHeight() - 5 - canvas.getHeight()/2));
		}
		
		else {
			meterHeight = 0;
		}
		
		meterPaint.setColor(Color.GRAY);
		
		meter = new Rect(canvas.getWidth() - 20, canvas.getHeight()/2 - 5, canvas.getWidth(), canvas.getHeight());
		canvas.drawRect(meter, meterPaint);
		
		meter.set(canvas.getWidth() - 15, canvas.getHeight()/2, canvas.getWidth() - 5, canvas.getHeight() - 5 - meterHeight);
		meterPaint.setColor(Color.RED);
		
		canvas.drawRect(meter, meterPaint);
		meterPaint.setColor((charge > difficulty*.1f + .2f ? Color.GREEN : 0xffff9d00));
		
		meter.set(meter.left, canvas.getHeight() - 5 - meterHeight, meter.right, canvas.getHeight() - 5);
		canvas.drawRect(meter, meterPaint);
	}
	
	/**
	 * Determines whether or not the Player has been hit by a Shot.
	 * @param shot the Shot to check
	 * @return true if the Shot sprite overlaps with the Player hitbox
	 */
	private boolean checkForHit(Shot shot) {
		playerHitbox = new RectF(player.getHitbox());
		
		Bitmap playerSprite = player.getSprite(), shotSprite = shot.getSprite();
		
		boolean result = false;
		
		if(playerHitbox.intersect(shot.getHitbox())) {
			for(int i = (int)playerHitbox.left; i < playerHitbox.right && !result; i++) {
				for(int j = (int)playerHitbox.top; j < playerHitbox.bottom && !result; j++) {
					if(playerSprite.getPixel(i - (int)playerHitbox.left, j - (int)playerHitbox.top) == 
						shotSprite.getPixel(i - (int)playerHitbox.left, j - (int)playerHitbox.top)) 
					{
						result = true;
					}
				}
			}
		}
		
		return result;
		
	}
	
	public void initParams(short difficulty, int level, int score, int[] enemyIds,
        SoundPool sounds, HashMap<String, Integer> soundLbls, boolean useSensor, boolean godMode)
	{
		this.difficulty = difficulty;
		this.level = level;
		this.score = score;
		this.soundLbls = soundLbls;
		this.sounds = sounds;
		this.useSensor = useSensor;
		this.godMode = godMode;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public class GameLoopThread extends Thread {
		private static final int FPS = 25;
	    private ForegroundView fg;
	    private boolean running = false;
	    private Canvas canvas;
	    
	    public GameLoopThread(ForegroundView fg) {
	        this.fg = fg;
	    }

		public boolean isRunning() {
			return running;
		}

		public void setRunning(boolean run) {
	        running = run;
	    }

	    @Override
	    public void run() {
	    	long ticksPS = 1000 / FPS, startTime, sleepTime;
	    	
	    	try {
	    		sleep(400);
	    	}
	    	
	    	catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
	    	while (running) {
	    	    canvas = null;
	    	    startTime = System.currentTimeMillis();

	    	    try {
	    	        canvas = fg.getHolder().lockCanvas();
	    	        synchronized (fg.getHolder()) {
	    	            fg.onDraw(canvas);
	    	        }
	    	    }

	    	    finally {
	    	        if (canvas != null) {
	    	            fg.getHolder().unlockCanvasAndPost(canvas);
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

	    	    catch (Exception e) {
	    	    	e.printStackTrace();
	    	    }

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
	
	/**
	 * Generates the score for the current level.
	 * @return the score for the level
	 */
	public void saveLvlScore() {
		/*guns.clear();
		shots.clear();
		
		player.init();*/
      ((Activity)getContext()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
      
    	    playTime += (int)(System.currentTimeMillis() - startTime);
    	    int baseScore = (1+level) * 500;
    	    int timeScore = playTime/1000;
    	    int lvlScore = difficulty * ((500*(level+1)) + (10000000/playTime));
            
    		FragmentManager fragMan = ((Activity)context).getFragmentManager();
            ((GameFragment)fragMan.findFragmentByTag(
                fragMan.getBackStackEntryAt(fragMan.getBackStackEntryCount()-1)
                    .getName()))
                .updateScore(baseScore, timeScore, lvlScore);
            }
        });
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
	  //  touchCounter = (short) ((++touchCounter) % 10);
	    
        if(/*touchCounter == 0 && */!init && !gameOver && !lvlClear) {
            switch(e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
    	    	if(pauseDest.contains((int)e.getX(), (int)e.getY())) {
    				setPauseStatus(!isPaused);
    				
    				if(!isPaused) {
    			    	FragmentManager fragMan = ((Activity)context).getFragmentManager();
    			    	((GameFragment)fragMan.findFragmentByTag(
    		    			fragMan.getBackStackEntryAt(fragMan.getBackStackEntryCount()-1)
    		    				.getName()))
    	    				.setInitState(isPaused);
    		    	}
    			}
    	    	
    	    	else if(!useSensor) {
    	    	    touchX = e.getX();
    	    	    touchY = e.getY();
    	    	}
                
                break;
        
            case MotionEvent.ACTION_POINTER_DOWN:
    			if(!isPaused && charge > difficulty*0.1f + 0.2f) {
    				player.startEMP();
    				
    				charge -= (difficulty*0.1f + 0.2f);
    			}
    			
    			break;
    			
            case MotionEvent.ACTION_MOVE:
                if(!useSensor && !lvlClear) {
                    dX = touchX - e.getX();
                    dY = e.getY() - touchY;
                }

                break;
                
            case MotionEvent.ACTION_UP:
                dX = dY = 0;
                
                break;
		    }
	    }
      
        synchronized(this) {
            try {
                wait(25);
            }
            
            catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

		return true;
	}
	
	private void setPauseStatus(boolean pause) {
		isPaused = pause;
		
		if(!isPaused) {
			playTime += (int)(System.currentTimeMillis() - startTime);
		}

		else {
			startTime = System.currentTimeMillis();
		}
	}

	/**
	 * Sets the game initialization flag.
	 * @param state whether or not to initialize the game state
	 */
    public void setInit(boolean state) {
        init = state;
    }
}
