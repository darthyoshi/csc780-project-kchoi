package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;

public class TitleFragment extends Fragment {
    /**
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @return
     */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) 
	{
        // Inflate the layout for this fragment
		return inflater.inflate(R.layout.title, container, false);
	}
	
    /**
     * 
     * @param v the View initiating this method 
     */
    public void onClick(View v) {
    	Fragment frag = null;
    	String tag = null;
    	
    	switch(v.getId()) {
    		case R.id.startButton:
				tag = "START_FRAGMENT";
				frag = new StartFragment();
				break;
				
    		case R.id.scoresButton:
				tag = "SCORE_FRAGMENT";
				frag = new HiScoreFragment();
				break;
				
    		case R.id.instructButton:
    			tag = "INSTRUCT_FRAGMENT";
    			frag = new InstructionFragment();
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
