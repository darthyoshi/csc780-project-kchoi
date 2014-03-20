/**
 *  @file GameActivity.java
 *  @author Kay Choi
 */

 package com.example.untouchable;

import android.hardware.*;
import android.os.Bundle;

public class GameActivity extends android.app.Activity implements SensorEventListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

	}

}
