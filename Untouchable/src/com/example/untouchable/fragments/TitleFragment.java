package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.app.*;
import android.os.Bundle;
import android.view.*;

public class TitleFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) 
	{
        // Inflate the layout for this fragment
		return inflater.inflate(R.layout.title, container, false);
	}
	
    /**
     * Callback method for a click event. 
     * @param v the View initiating this method 
     */
    public void onClick(View v) {
    	Fragment frag = null;
    	String tag = null;
    	
    	switch(v.getId()) {
		case R.id.startButton:
			tag = "GAME_FRAGMENT";
			frag = new GameFragment();
			((GameFragment)frag).setLevel(0);
			
			break;
			
		case R.id.scoresButton:
			tag = "SCORE_FRAGMENT";
			frag = new HiScoreFragment();
			
			break;
			
		case R.id.optionButton:
			tag = "PREFERENCE_FRAGMENT";
			frag = new PreferencesFragment();
			
			break;
    	}
    	
    	getFragmentManager()
			.beginTransaction()
			.replace(R.id.main_frame, frag, tag)
			.addToBackStack(tag)
			.show(frag)
			.commit();
    }
}
