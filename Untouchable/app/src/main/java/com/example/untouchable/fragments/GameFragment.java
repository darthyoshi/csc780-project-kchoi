/**
 *  @file GameActivity.java
 *  @author Kay Choi
 */

package com.example.untouchable.fragments;

import java.util.HashMap;

import com.example.untouchable.*;
import com.example.untouchable.canvas.ForegroundView;

import android.app.*;
import android.content.*;
import android.content.res.TypedArray;
import android.hardware.*;
import android.media.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;

public class GameFragment extends Fragment implements SensorEventListener {
	private float xInit, yInit;
	private float dX, dY, dZ;
	private boolean initSensor = false, useSensor, godMode, accelTime;
	private short difficulty, azimuth, altitude;
	private int score = 0, level;
	private ForegroundView fg;
	private static SoundPool sounds = null;
	private static HashMap<String, Integer> soundLbls = null;
	private static SensorManager sensorManager;
	
/*	//for debug
	private float zInit, initAz, initAl;
	private TextView lblX, lblX2, lblY, lblY2, lblZ, lblZ2, lblAz, lblAz2, lblAl, lblAl2;
*/

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) 
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.game, container, false);
	}
	
	/**
	 * Initializes the game.
	 */
	private void init() {
		initSensor = false;
		
		Activity parent = getActivity();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
		useSensor = prefs.getBoolean("useSensor", true);

		switch(prefs.getString("difficulty", "Normal")) {
		case "Easy":
		    difficulty = 1;
		    
		    break;
		
		case "Normal":
		    difficulty = 2;
		
		    break;
		
		case "Hard":
		    difficulty = 3;
		}
		
		godMode = prefs.getBoolean("godMode", false);
		
		accelTime = prefs.getBoolean("accelTime", false);
/*
		{	//for debug
			lblX = (TextView)parent.findViewById(R.id.lblX);
			lblY = (TextView)parent.findViewById(R.id.lblY);
			lblZ = (TextView)parent.findViewById(R.id.lblZ);
			
			lblX2 = (TextView)parent.findViewById(R.id.initX);
			lblY2 = (TextView)parent.findViewById(R.id.initY);
			lblZ2 = (TextView)parent.findViewById(R.id.initZ);
			
			lblAl = (TextView)parent.findViewById(R.id.lblAl);
			lblAz = (TextView)parent.findViewById(R.id.lblAz);
			
			lblAl2 = (TextView)parent.findViewById(R.id.initAl);
			lblAz2 = (TextView)parent.findViewById(R.id.initAz);
		}
*/
		if(useSensor) {
            /*
             * Retrieve the SensorManager.
             */
            sensorManager = (SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
            /*
             * Register this activity as the listener for accelerometer events.
             */
            sensorManager.registerListener(
            		this,
            		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            		SensorManager.SENSOR_DELAY_NORMAL);
    	}
		
        if(sounds == null) {
			sounds = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }
        
        if(soundLbls == null) {
			TypedArray soundIds = getResources().obtainTypedArray(R.array.sound_ids);
			
			soundLbls = new HashMap<String, Integer>();
			
			String[] tmp;
			
			for(short i = 0; i < soundIds.length(); i++) {
				tmp = getResources().getResourceName(soundIds.getResourceId(i, 0)).split("/");
				
				soundLbls.put(tmp[1], sounds.load(parent, soundIds.getResourceId(i, 0), 1));
			}
			
			soundIds.recycle();
        }

		fg = (ForegroundView)parent.findViewById(R.id.fg);
        fg.initParams(difficulty, level, score, sounds, soundLbls, useSensor, godMode, accelTime);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		init();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(!initSensor) {
			initSensor = true;
		
			xInit = event.values[0];
			yInit = event.values[1];

/*			{	//for debug
 				zInit = event.values[2];
	
				initAz = (float) Math.atan2(xInit, yInit);
				initAl = (float)(Math.atan2(zInit, Math.sqrt(xInit*xInit+yInit*yInit)) + Math.PI/2);
	
				lblX2.setText(Float.toString(-xInit));
				lblY2.setText(Float.toString(-yInit));
				lblZ2.setText(Float.toString(-zInit));
				
				lblAz2.setText(Float.toString(initAz));
				lblAl2.setText(Float.toString(initAl));
			}
*/		}
		
		else {
			dX = xInit - event.values[0];
			dY = yInit - event.values[1];
			dZ = event.values[2];
			
			azimuth = (short)Math.toDegrees(Math.atan2(dX, dY));
			altitude = (short)Math.toDegrees((Math.atan2(dZ, Math.sqrt(dX*dX+dY*dY)) - Math.PI/2));
/*
			{	//for debug
				lblX.setText(Float.toString(dX));
				lblY.setText(Float.toString(dY));
				lblZ.setText(Float.toString(dZ));
	
				lblAz.setText(Integer.toString(azimuth));
				lblAl.setText(Integer.toString(altitude));
			}
*/
			synchronized(fg.getPlayer()) {
			    fg.getPlayer().setSpeed(Math.toRadians(azimuth), Math.toRadians(altitude));
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		
		if(useSensor) {
		    sensorManager.unregisterListener(this);
		}
		
		fg.pauseThread();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(short difficulty) {
		this.difficulty = difficulty;
	}
	
	/**
	 * Callback method for the back button.
	 */
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		fg.pauseThread();
		
		initSensor = false;
		
		builder.setMessage("Do you really wish to quit?\n\nYour score will be lost!")
			.setNegativeButton(getActivity().getResources().getString(R.string.resume), new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					fg.resumeThread();
		
					dialog.dismiss();
				}
			})
			.setTitle(getActivity().getResources().getString(R.string.quit))
			.setPositiveButton(getActivity().getResources().getString(R.string.quit), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					fg.setInit(true);
					
					getFragmentManager().popBackStackImmediate();
				}
			})
			.setCancelable(false);
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Retrieves the current score.
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Increments the score for the current game session.
	 * @param baseScore the base score for the level
	 * @param timeBonus the time bonus multiplier
	 * @param lvlScore the total score for the level
	 */
	public void updateScore(int baseScore, float timeBonus, int lvlScore) {
		score += lvlScore;
		
  		String tag = "RESULT_FRAGMENT";
	    Fragment frag = new ResultFragment();

	    ((ResultFragment)frag).setParams(baseScore, timeBonus, difficulty, lvlScore, score);
	    
	    getFragmentManager()
	        .beginTransaction()
	        .replace(R.id.main_frame, frag, tag)
	        .addToBackStack(tag)
	        .show(frag)
	        .commit();
	}

	/**
	 * Specifies whether or not to reinitialize the accelerometer values.
	 * @param state the new accelerometer initialization state
	 */
	public void setInitState(boolean state) {
		initSensor = state;
	}

	/**
	 * Stores the score for the current session.
	 */
    public void saveScore() {
        String tag = "SCORE_FRAGMENT";
        Fragment frag = new HiScoreFragment();

        ((HiScoreFragment)frag).setScore(score);
        
        getFragmentManager()
            .beginTransaction()
            .replace(R.id.main_frame, frag, tag)
            .addToBackStack(tag)
            .show(frag)
            .commit();
        
    }
    
    /**
     * Increments the stage number.
     */
    public void nextLevel() {
        level++;
        
        if(fg != null) {
            fg.reset(level);
        }
    }
    
    /**
     * Sets the stage number.
     * @param level the new stage number
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
