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
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}
    
    /**
     *  
     *  @param v
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
    	
    	String tag = frag.getTag();
    	
    	if(tag.equals("GAME_FRAGMENT")) {
    		((GameFragment)frag).onBackPressed();
    	}
    	
    	else if(tag.equals("SCORE_FRAGMENT") || tag.equals("RESULT_FRAGMENT")) {
	        fragMan.popBackStack("TITLE_FRAGMENT", 0);
    	}
    	
    	else if(fragMan.getBackStackEntryCount() > 1) {
    		super.onBackPressed();
    	}
    	
    	else {
    		finish();
    	}
    }
}
