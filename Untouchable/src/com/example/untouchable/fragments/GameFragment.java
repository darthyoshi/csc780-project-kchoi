/**
 *  @file GameActivity.java
 *  @author Kay Choi
 */

package com.example.untouchable.fragments;

import com.example.untouchable.*;
import com.example.untouchable.canvas.ForegroundView;

import android.app.*;
import android.content.*;
import android.content.res.TypedArray;
import android.hardware.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class GameFragment extends Fragment implements SensorEventListener {
	private float xInit, yInit;
	private float dX, dY, dZ;
	private boolean init = false;
	private short difficulty, azimuth, altitude;
	private int score = 0, level;
	private ForegroundView fg;
	private TextView scoreBox;
	private TypedArray enemyIds;
	private static SensorManager sensorManager;
	
/*	//for debug
	private float zInit, initAz, initAl;
	private TextView lblX, lblX2, lblY, lblY2, lblZ, lblZ2, lblAz, lblAz2, lblAl, lblAl2;
*/
	/**
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @return
     */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) 
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.game, container, false);
	}
	
	
	private void init() {
		init = false;
		
		Activity parent = getActivity();
		
		enemyIds = parent.getResources().obtainTypedArray(R.array.enemy_sprites);
		
		fg = (ForegroundView)parent.findViewById(R.id.fg);
		
		scoreBox = (TextView)parent.findViewById(R.id.score);
		scoreBox.bringToFront();
		
		parent.findViewById(R.id.lvl_label).bringToFront();
		
		parent.findViewById(R.id.start_timer).bringToFront();
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
        
        fg.initParams(difficulty, level, getActivity());
		
		setScoreDisplay();
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
		if(!init) {
			init = true;
		
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
			fg.getPlayer().setSpeed(Math.toRadians(azimuth), Math.toRadians(altitude));
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		fg.pause();
	}

	/**
	 * @return the difficulty
	 */
	public short getDifficulty() {
		return difficulty;
	}


	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(short difficulty) {
		this.difficulty = difficulty;
	}
	
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		fg.pause();
		
		builder.setMessage("Do you really wish to quit?\n\nYour score will be lost!")
			.setNegativeButton("Resume", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					fg.resume();

					dialog.dismiss();
				}
			})
			.setTitle("Quit")
			.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getActivity().getFragmentManager().popBackStackImmediate();
				}
			})
			.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
	}


	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}


	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Updates the score display on the screen.
	 */
	private void setScoreDisplay() {
		scoreBox.setText(Integer.toString(score));
	}
}
