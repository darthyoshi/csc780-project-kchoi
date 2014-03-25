package com.example.untouchable;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TitleFragment extends Fragment {
    private Intent intent;
    private AlertDialog.Builder instruct;
	
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
		return inflater.inflate(R.layout.title, container, false);
	}
	
    /**
     *  @param v
     */
    public void onClickStart(View v) {
    	Fragment frag = new StartFragment();

    	getFragmentManager()
    		.beginTransaction()
    		.replace(R.id.main_frame, frag, "START_FRAGMENT")
			.addToBackStack("START_FRAGMENT")
			.show(frag)
			.commit();
    }
    
    /**
     *  @param v
     */
    public void onClickInstruct(View v) {
    	instruct = new AlertDialog.Builder(/*this*/getActivity());
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
    	Fragment frag = new HiScoreFragment();

    	getFragmentManager()
    		.beginTransaction()
    		.replace(R.id.main_frame, frag, "SCORE_FRAGMENT")
			.addToBackStack("SCORE_FRAGMENT")
			.show(frag)
			.commit();
	}
}
