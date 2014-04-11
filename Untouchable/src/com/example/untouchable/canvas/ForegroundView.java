package com.example.untouchable.canvas;

import java.util.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.*;
import android.widget.TextView;

import com.example.untouchable.R;
import com.example.untouchable.obj.*;

@SuppressLint("WrongCall")
public class ForegroundView extends SurfaceView implements SurfaceHolder.Callback {
	private static Player player;
	private Enemy enemy;
	private ArrayList<Shot> shots;
	private GameLoopThread gameLoopThread;
	private short difficulty = 2, frame = 0;
	private int level, playTime = 0;
	private long startTime;
	private boolean init = true, emp = false, isPaused = true, lvlClear = false;
	private static Rect blank = null;
	private static Paint paint = null;
	private Iterator<Shot> iter;
	private Shot shot;
	private Rect meter, pauseDest, pauseSrc;
	private Paint meterPaint = null;
	private ArrayList<Gun> guns;
	private Bitmap pause;
	private RectF playerHitbox, shotHitbox, empBox;
	private Point playerCenter;
	private float charge = 0f;
	private Context context;
	
	private Point gunCenter;
		
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
		        pause();
	        }

		    @Override
		    public void surfaceCreated(SurfaceHolder holder) {
		    	resume();
		    }
		    
		    @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
        });
		
		player = new Player(getContext());
		
		guns = new ArrayList<Gun>();
		for(short i = 0; i < 5; i++) {
			guns.add(new Gun((int)(Math.random()*400), (int)(Math.random()*1000), i, getContext()));
		}
		
		shots = new ArrayList<Shot>();
		
		pause = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
		pauseSrc = new Rect(0, 0, pause.getWidth(), pause.getHeight());
	}
	
	/**
	 * Pauses the game execution thread.
	 */
	public void pause() {
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
        
        togglePauseStatus();
	}
	
	/**
	 * Resumes the game execution thread.
	 */
	public void resume() {
		blank = new Rect(0, 0, getWidth(), getHeight());

		paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		meterPaint = new Paint();

		pauseDest = new Rect(getWidth() - 10 - pause.getWidth(), 10, getWidth() - 10, 10 + pause.getHeight());
		
    	if(gameLoopThread.getState() == Thread.State.TERMINATED) {
    		gameLoopThread = new GameLoopThread(this);
    	}
    	
    	if(!gameLoopThread.isRunning()) {
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
    	}
		
		togglePauseStatus();
	}

	/**
	 * Draws all objects to the canvas.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(canvas != null) {
			if(init && System.currentTimeMillis() - startTime > 3000) {
				startTime += 3000;
				init = false;
			}
			
			frame = (short) ((++frame) % (25 - 3*difficulty));

			canvas.drawRect(blank, paint);
			
			player.updateAndDraw(canvas, !isPaused && !init);
			
			if(frame == 0 && !init && !isPaused) {
				for(short i = 0; i < guns.size(); i++) {
					shots.addAll(guns.get(i).fireGun());
				}
			}	
			
			playerCenter = player.getCenter();
			
			
			iter = shots.iterator();
			while(iter.hasNext()) {
				shot = iter.next();
				shot.updateAndDraw(canvas, !isPaused);
				
				if(checkForHit(shot)) {
					//TODO: handle game over
				}
				
				else if(shot.getX() > canvas.getWidth()+25 || shot.getX() < -25 ||
					shot.getY() < -25 || shot.getY() > canvas.getHeight()+25 ||
					checkEMPHit(shot)) 
				{
					iter.remove();
				}
			}		
			
			for(short i = 0; i < guns.size(); i++) {
				guns.get(i).setBearing(playerCenter.x, playerCenter.y);
				guns.get(i).updateAndDraw(canvas);
				
				{	//for debug
					gunCenter = guns.get(i).getCenter();
					paint.setStrokeWidth(2);
					canvas.drawLine(gunCenter.x, gunCenter.y, playerCenter.x, playerCenter.y, paint);
				}
			}
			
			if(emp) {
				emp = player.updateAndDrawEMP(canvas);
			}
			
			updateAndDrawCharge(canvas);
			
			canvas.drawBitmap(pause, pauseSrc, pauseDest, null);
		}
	}
	
	private boolean checkEMPHit(Shot shot) {
		boolean result = false;
		
		if(emp) {
			shotHitbox = shot.getHitbox();
			int empR = player.getEMPR();
			
			double dist = Math.sqrt(
				Math.pow(playerCenter.x - (shotHitbox.right + shotHitbox.left)/2, 2) +
				Math.pow(playerCenter.y - (shotHitbox.bottom + shotHitbox.top)/2, 2)
			);
			
			if(dist < empR/* && dist > empR - 30*/) {
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
			if(!isPaused) {
				charge += 1f/(30f*25f);
			}
			
			if(charge > 1f) {
				charge = 1f;
				
				if(!lvlClear) {
					player.setWinState(true);
					
					lvlClear = true;
				}
				//TODO: handle level clear
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
		
		boolean result = false;
		
		if(playerHitbox.intersect(shot.getHitbox())) {
			for(int i = (int)playerHitbox.left; i < playerHitbox.right && !result; i++) {
				for(int j = (int)playerHitbox.top; j < playerHitbox.bottom && !result; j++) {
					if(player.getSprite().getPixel(i - (int)playerHitbox.left, j - (int)playerHitbox.top) == 
						shot.getSprite().getPixel(i - (int)playerHitbox.left, j - (int)playerHitbox.top)) 
					{
						result = true;
					}
				}
			}
		}
		
		return result;
		
	}
	
	public short getDifficulty() {
		return difficulty;
	}
	
	public void initParams(short difficulty, int level, Activity parent) {
		this.difficulty = difficulty;
		this.level = level;
		
		Gun.setDifficulty(difficulty);
		
		countdown(parent);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public class GameLoopThread extends Thread {
		private static final int FPS = 25;
	    private ForegroundView fg;
	    private boolean running = false;
	    private Canvas c;
	    
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
	
	private void countdown(final Activity parent) {
	    Thread th = new Thread(new Runnable() {
	    	
	        public void run() {
	        	final TextView timer = (TextView)parent.findViewById(R.id.start_timer);
	        	
	        	if(timer.isShown()) {
		            for(short i = 3; i > 0; i--) {
		                try {
		                    Thread.sleep(1000);
		                } 
		                
		                catch (InterruptedException e) {
		                    e.printStackTrace();
		                }           
		               
		                parent.runOnUiThread(new Runnable() {
		                	
		                    @Override
		                    public void run() {
		                    	try {
			                    	short i = (short) (Short.parseShort((String) timer.getText()) - 1);
			                    	timer.setText(Short.toString(i));
		                    	}
		                    	
		                    	catch (Exception e) {}
		                    }
		                });
		            }
		            
		            parent.runOnUiThread(new Runnable() {
	                	
	                    @Override
	                    public void run() {
	                    	parent.findViewById(R.id.lvl_label).setVisibility(View.INVISIBLE);
	                    	timer.setText("GO!");
	                    }
	                });
		            
		            try {
						Thread.sleep(1000);
					}
		            
		            catch (InterruptedException e) {
						e.printStackTrace();
					}
		            
		            parent.runOnUiThread(new Runnable() {
	                	
	                    @Override
	                    public void run() {
	                    	timer.setVisibility(View.GONE);
	                    }
	                });
	            
		        }
	        }
	    });
	    
	    th.start();
	}
	
	/**
	 * Called when the level is complete.
	 * @return the score for the level
	 */
	public int levelComplete() {
		guns.clear();
		shots.clear();
		
		player.init();

		return difficulty*1000*level*playTime;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if((e.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
			if(pauseDest.contains((int)e.getX(), (int)e.getY())) {
				togglePauseStatus();
			}
			
			else if(!isPaused && !init && charge > difficulty*0.1f + 0.2f) {
				emp = true;
				
				charge -= (difficulty*0.1f + 0.2f); 
			}
		}
		return true;
	}
	
	private void togglePauseStatus() {
		isPaused = !isPaused;
		
		if(!isPaused) {
			if(!init) {
				TextView msg = (TextView)((Activity)context).findViewById(R.id.start_timer);
				msg.setVisibility(GONE);
			}
			
			playTime += (int)(System.currentTimeMillis() - startTime);
		}

		else {
			if(!init) {
				TextView msg = (TextView)((Activity)context).findViewById(R.id.start_timer);
				msg.setText("PAUSED");
				msg.setVisibility(VISIBLE);
			}
		
			startTime = System.currentTimeMillis();
		}
	}
}
