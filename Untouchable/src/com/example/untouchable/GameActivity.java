/**
 *  @file GameActivity.java
 *  @author Kay Choi
 */

package com.example.untouchable;

import android.app.*;
import android.hardware.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class GameActivity extends Fragment implements SensorEventListener {
	private TextView lblX, lblX2;
	private TextView lblY, lblY2;
	private TextView lblZ, lblZ2;
	private Float xInit;
	private Float yInit;
	private Float zInit;
	private boolean init = false;

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
		init();
        // Inflate the layout for this fragment
		return inflater.inflate(R.layout.game, container, false);
	}
	
	
/*	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.game);
		*/public void init(){
		lblX = (TextView)getActivity().findViewById(R.id.lblX);
		lblY = (TextView)getActivity().findViewById(R.id.lblY);
		lblZ = (TextView)getActivity().findViewById(R.id.lblZ);
		
		lblX2 = (TextView)getActivity().findViewById(R.id.initX);
		lblY2 = (TextView)getActivity().findViewById(R.id.initY);
		lblZ2 = (TextView)getActivity().findViewById(R.id.initZ);
		
        /*
         * Retrieve the SensorManager.
         */
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
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
			lblX.setText(Float.toString(event.values[0] - xInit));
			lblY.setText(Float.toString(event.values[1] - yInit));
			lblZ.setText(Float.toString(event.values[2] - zInit));
		}

	}
	
	/*
	public void onClick(View v) {
		finish();
	}
*/
}
