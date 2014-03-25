/**
 *  @file HiScoreFragment.java
 *  @author Kay Choi
 */

package com.example.untouchable;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import android.app.*;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HiScoreFragment extends Fragment {
    private SortedMap<Integer, String> scoreList = null;
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	if(scoreList == null) {
	    	scoreList = new TreeMap<Integer, String>();
	    	
	    	BufferedReader reader = null;
	    	try {
	    		reader = new BufferedReader(new InputStreamReader(getActivity().openFileInput("myscores.txt")));
	    		
	    		String line;
		    	String[] words;
	    		while((line = reader.readLine()) != null) {
					words = line.split(",");
					
					scoreList.put(Integer.getInteger(words[0]), words[1]);
				}
			}
	    	
	    	catch(Exception e) {
	    		Resources res = getActivity().getResources();
	    		
	    		int scores[] = res.getIntArray(R.array.values);
	    		
	    		for(int i = 0; i < scores.length; i++) {
	    			scoreList.put(scores[i], res.getString(R.string.default_name));
	    		}
	    	}
	    	

	    	
	    	//TODO: create and populate TextViews
    	}
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
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.scores, container, false);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if(scoreList != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getActivity().openFileOutput("myscores.txt", Context.MODE_PRIVATE)));
				
				Iterator<Entry<Integer, String>> iter = scoreList.entrySet().iterator();
				Entry<Integer, String> entry;
				while(iter.hasNext()) {
					entry = iter.next();
					
					writer.write(entry.getKey().toString() + ',' + entry.getValue() + '\n');
				}
				
				writer.close();
			}
			
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
