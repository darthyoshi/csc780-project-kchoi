/**
 *  @file MainAcitivity.java
 *  @author Kay Choi
 */ 
 
package com.example.untouchable;

import android.app.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;

import com.example.untouchable.fragments.*;

public class MainActivity extends Activity {
	private Fragment frag;
	
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
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}
    
    /**
     *  Callback method for handling click events. 
     *  @param v the View initiating this method
     */
    public void onClick(View v) {
    	frag = getActiveFragment();

    	switch(v.getId()) {
    		case R.id.startButton:
    		case R.id.optionButton:
    		case R.id.scoresButton:
    			((TitleFragment)frag).onClick(v);
        		break;
		}
    }

    /**
     * Retrieves the active Fragment.
     * @return the Fragment at the top of the stack
     */
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
        FragmentManager fragMan = getFragmentManager();
    	frag = getActiveFragment();
    	
    	switch(frag.getTag()) {
    	case "GAME_FRAGMENT":
    	    ((GameFragment)frag).onBackPressed();
    	    
    	    break;
    	    
    	case "SCORE_FRAGMENT":
    	    fragMan.popBackStack("TITLE_FRAGMENT", 0);
    	    
    	    break;
    	    
    	case "RESULT_FRAGMENT":
    	    ((ResultFragment)frag).onBackPressed();
    	    
    	    break;
    	    
	    default:
	        if(fragMan.getBackStackEntryCount() > 1) {
        		super.onBackPressed();
        	}
        	
        	else {
        		finish();
        	}
    	}
	}
}
