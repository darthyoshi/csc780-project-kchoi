/**
 *  @file MainAcitivity.java
 *  @author Kay Choi
 */ 
 
package com.example.untouchable;
 
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
    private Intent intent;
    private AlertDialog.Builder instruct;
 /*    private Handler handle;
    private int frameRate = 20;
    private Background area;
  */  
    /**
     *  @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.activity_main);
/*
        handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);
     */   
    }
    
    /**
     *  @param v
     */
    public void onClickStart(View v) {/*
    	intent = new Intent(this, GameActivity.class);
        startActivity(intent);*/
    	Fragment newFragment = new GameActivity();
    	FragmentTransaction transaction = getFragmentManager().beginTransaction();

    	// Replace whatever is in the fragment_container view with this fragment,
    	// and add the transaction to the back stack
    	transaction.replace(R.id.game_fragment, newFragment);
    	transaction.addToBackStack(null);

    	// Commit the transaction
    	transaction.commit();
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
 /*
    synchronized public void initGfx() {
        handle.removeCallbacks(frameUpdate);
        handle.postDelayed(frameUpdate, frameRate);
    }
    
    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            handle.removeCallbacks(frameUpdate);
            
            area.invalidate();
            handle.postDelayed(frameUpdate, frameRate);
        }
    };*/
}
