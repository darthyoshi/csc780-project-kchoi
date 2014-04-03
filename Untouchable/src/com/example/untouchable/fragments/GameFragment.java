/**
 *  @file GameActivity.java
 *  @author Kay Choi
 */

package com.example.untouchable.fragments;

import com.example.untouchable.*;
import com.example.untouchable.canvas.Foreground;
import com.example.untouchable.obj.*;

import android.app.*;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class GameFragment extends Fragment implements SensorEventListener {
	private TextView lblX, lblX2;
	private TextView lblY, lblY2;
	private TextView lblZ, lblZ2;
	private Float xInit, yInit, zInit;
	int dX, dY, dZ;
	private boolean init = false;
	private int difficulty;
	
	private Foreground fg;
	
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
	
	
	public void init() {
		Activity parent = getActivity();
		
		fg = (Foreground)parent.findViewById(R.id.fg);
		
		lblX = (TextView)parent.findViewById(R.id.lblX);
		lblY = (TextView)parent.findViewById(R.id.lblY);
		lblZ = (TextView)parent.findViewById(R.id.lblZ);
		
		lblX2 = (TextView)parent.findViewById(R.id.initX);
		lblY2 = (TextView)parent.findViewById(R.id.initY);
		lblZ2 = (TextView)parent.findViewById(R.id.initZ);
		
        /*
         * Retrieve the SensorManager.
         */
        SensorManager sensorManager = (SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
        /*
         * Retrieve the default Sensor for the accelerometer.
         */
        Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        /*
         * Register this activity as the listener for accelerometer events.
         */
        sensorManager
                .registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		init();
		init = false;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(!init) {
			init = true;
			
			xInit = event.values[0];
			yInit = event.values[1];
			zInit = event.values[2];

			lblX2.setText(Float.toString(xInit));
			lblY2.setText(Float.toString(yInit));
			lblZ2.setText(Float.toString(zInit));
		}
		
		else {
			dX = (int) (xInit - event.values[0]);
			dY = (int) (event.values[1] - yInit);
			dZ = (int) (event.values[2] - zInit);
			
			lblX.setText(Float.toString(dX));
			lblY.setText(Float.toString(dY));
			lblZ.setText(Float.toString(dZ));
			
			fg.getPlayer().setSpeed(dX, dY, dZ);
		}

	}


	/**
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}


	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage("Do you really wish to quit?\n\nYour score will be lost!")
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			})
			.setTitle("Quit")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getActivity().getFragmentManager().popBackStackImmediate();
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
