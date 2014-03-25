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

		int view = v.getId();
    	switch(view) {
    		case R.id.doneButton:
    			getFragmentManager().popBackStackImmediate("TITLE_FRAGMENT", 0);
    			break;
    			
    		case R.id.easy:
    		case R.id.normal:
    		case R.id.hard:
    			((StartFragment)frag).setDifficulty(view);
    			break;
			
    		case R.id.startButton:
    			((TitleFragment)frag).onClickStart(v);
    			break;
    		
    		case R.id.instructButton:
    			((TitleFragment)frag).onClickInstruct(v);
    			break;
    		
    		case R.id.scoresButton:
    			((TitleFragment)frag).onClickScores(v);
        		break;
		}
    }

    private Fragment getActiveFragment() {
    	FragmentManager fragMan = getFragmentManager();
    	Fragment result = null;

        if (fragMan.getBackStackEntryCount() > 0) {
	        String tag = fragMan.getBackStackEntryAt(fragMan.getBackStackEntryCount()-1).getName();
	        result = fragMan.findFragmentByTag(tag);
	    }
        
    	return result;
    }
}
