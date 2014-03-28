/**
 *  @file StartyFragment.java
 *  @author Kay Choi
 */

package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.app.*;
import android.os.Bundle;
import android.view.*;

public class StartFragment extends Fragment {
	
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
		return inflater.inflate(R.layout.start, container, false);
	}
	
	public void setDifficulty(int difficulty) {
    	GameFragment frag = new GameFragment();
    	
    	frag.setDifficulty(difficulty);
    	
    	FragmentManager fragMan = getFragmentManager();
    	
    	fragMan
    		.beginTransaction()
    		.replace(R.id.main_frame, frag, "GAME_FRAGMENT")
			.addToBackStack("GAME_FRAGMENT")
			.show(frag)
			.commit();
	}
}
