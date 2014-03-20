/**
 *  @file MainAcitivity.java
 *  @author Kay Choi
 */ 
 
package com.example.untouchable;
 
import android.app.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {
    private Intent intent;
    private Handler frame;
    
    public MainActivity() {
        super();
        frame = new Handler();
    }

     /**
     *  @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.activity_main);
    }
    
    /**
     *  @param v
     */
    public void onClickStart(View v) {
    	intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
    
    /**
     *  @param v
     */
    public void onClickInstruct(View v) {
    	intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);    
    }
    
    /**
     *  @param v
     */
    public void onClickScores(View v) {
    	intent = new Intent(this, HiScoresActivity.class);
        startActivity(intent);
    }
}
