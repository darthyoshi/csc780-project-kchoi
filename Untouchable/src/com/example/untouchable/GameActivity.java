/**
 *  @file GameActivity.java
 *  @author Kay Choi
 */

package com.example.untouchable;

import android.app.Activity;
import android.hardware.*;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

public class GameActivity extends Activity implements SensorEventListener {
	private TextView lblX, lblX2;
	private TextView lblY, lblY2;
	private TextView lblZ, lblZ2;
	private Float xInit;
	private Float yInit;
	private Float zInit;
	private boolean init = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.game);
		
		lblX = (TextView)findViewById(R.id.lblX);
		lblY = (TextView)findViewById(R.id.lblY);
		lblZ = (TextView)findViewById(R.id.lblZ);
		
		lblX2 = (TextView)findViewById(R.id.initX);
		lblY2 = (TextView)findViewById(R.id.initY);
		lblZ2 = (TextView)findViewById(R.id.initZ);
		
        /*
         * Retrieve the SensorManager.
         */
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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
			lblX.setText(Float.toString(event.values[0]));
			lblY.setText(Float.toString(event.values[1]));
			lblZ.setText(Float.toString(event.values[2]));
		}

	}
	
	
	public void onClick(View v) {
		finish();
	}

}
