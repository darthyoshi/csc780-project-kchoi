package com.example.untouchable;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

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
}
