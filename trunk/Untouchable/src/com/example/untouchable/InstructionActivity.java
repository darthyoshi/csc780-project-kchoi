package com.example.untouchable;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class InstructionActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams params = getWindow().getAttributes();  
        params.x = -100;  
        params.height = 70;  
        params.width = 1000;  
        params.y = -50;  
  
        this.getWindow().setAttributes(params);  
		
	}

}
