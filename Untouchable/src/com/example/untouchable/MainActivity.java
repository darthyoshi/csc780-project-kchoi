/**
 *  @file MainAcitivity.java
 *  @author Kay Choi
 */ 
 
package com.example.untouchable;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import com.example.untouchable.fragments.*;

public class MainActivity extends Activity {
	private Fragment frag;
	
    /**
     *  @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.activity_main);
        
        Fragment frag = new TitleFragment();
        getFragmentManager()
        	.beginTransaction()
        	.add(R.id.main_frame, frag, "TITLE_FRAGMENT")
        	.addToBackStack("TITLE_FRAGMENT")
        	.show(frag)
        	.commit();
	}
    
    /**
     *  
     *  @param v
     */
    public void onClick(View v) {
    	frag = getActiveFragment();

		int viewId = v.getId();
    	switch(viewId) {
/*    		case R.id.doneButton:
    			getFragmentManager().popBackStackImmediate("TITLE_FRAGMENT", 0);
    			break;*/
    			
    		case R.id.easy:
    			((StartFragment)frag).setDifficulty(1);
    			break;
    			
    		case R.id.normal:
    			((StartFragment)frag).setDifficulty(2);
    			break;
    			
    		case R.id.hard:
    			((StartFragment)frag).setDifficulty(3);
    			break;
			
    		case R.id.startButton:
    		case R.id.instructButton:
    		case R.id.scoresButton:
    			((TitleFragment)frag).onClick(v);
        		break;
		}
    }

    private Fragment getActiveFragment() {
    	FragmentManager fragMan = getFragmentManager();
    	Fragment result = null;
    	
    	int count = fragMan.getBackStackEntryCount();

        if (count > 0) {
	        String tag = fragMan.getBackStackEntryAt(count-1).getName();
	        result = fragMan.findFragmentByTag(tag);
	    }
        
    	return result;
    }
    
    @Override
    public void onBackPressed() {
    	frag = getActiveFragment();
    	
    	if(frag.getTag().equals("GAME_FRAGMENT")) {
    		((GameFragment)frag).onBackPressed();
    	}
    	
    	else if(getFragmentManager().getBackStackEntryCount() > 1) {
    		super.onBackPressed();
    	}
    	
    	else {
    		finish();
    	}
    }
}
