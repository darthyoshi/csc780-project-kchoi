/**
 *  @file StartyFragment.java
 *  @author Kay Choi
 */

package com.example.untouchable;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
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
        // Inflate the layout for /*this*/getActivity() fragment
		return inflater.inflate(R.layout.start, container, false);
	}
	
	public void setDifficulty(int difficulty) {
    	Fragment frag = new GameFragment(difficulty);
    	FragmentManager fragMan = getFragmentManager();
    	
    	//fragMan.popBackStackImmediate("TITLE_FRAGMENT", 0);
    	
    	fragMan
    		.beginTransaction()
    		.replace(R.id.main_frame, frag, "GAME_FRAGMENT")
			.addToBackStack("GAME_FRAGMENT")
			.show(frag)
			.commit();
	}
}
