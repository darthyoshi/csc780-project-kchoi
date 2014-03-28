/**
 *  @file InstructionFragment.java
 *  @author Kay Choi
 */
package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InstructionFragment extends Fragment {

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
		return inflater.inflate(R.layout.instruct, container, false);
	}

}
