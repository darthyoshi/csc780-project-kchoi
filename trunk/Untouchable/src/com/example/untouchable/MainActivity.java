/**
 *  @file MainAcitivity.java
 *  @author Kay Choi
 */ 
 
package com.example.untouchable;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
    private Intent intent;
    private Handler frame;
    private AlertDialog.Builder instruct;
    
     /**
     *  @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.activity_main);
        
        frame = new Handler();
    }
    
    /**
     *  @param v
     */
    public void onClickStart(View v) {
    	intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    
    /**
     *  @param v
     */
    public void onClickInstruct(View v) {
    	instruct = new AlertDialog.Builder(this);
        instruct.setMessage(R.string.instructions);
        instruct.setTitle(R.string.menu_instructions);
        instruct.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
    	
    	instruct.show();
    }
    
    /**
     *  @param v
     */
    public void onClickScores(View v) {
    	intent = new Intent(this, HiScoreActivity.class);
        startActivity(intent);
    }
}
