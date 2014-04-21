/**
 *  @file HiScoreFragment.java
 *  @author Kay Choi
 */

package com.example.untouchable.fragments;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import android.app.*;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.example.untouchable.R;

public class HiScoreFragment extends Fragment {
    private TreeMap<Integer, String> scoreList = null;
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	if(scoreList == null) {
	    	scoreList = new TreeMap<Integer, String>();

	    	BufferedReader reader = null;
	    	try {
	    		reader = new BufferedReader(new InputStreamReader(getActivity().openFileInput("myscores.txt")));
    		}
	    	
	    	catch(Exception e) {
		    	try {
		    		reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("scores.txt")));
		    	}
		    	
	    		catch(Exception e1) {
	    			e1.printStackTrace();
	    		}
	    	}
	    	
	    	finally {
	    		if(reader != null) {
	    			try {
			    		String line;
				    	String[] words;
			    		while((line = reader.readLine()) != null) {
			    			words = line.split(",");
			    			
							scoreList.put(Integer.parseInt(words[0]), words[1]);
						}
		
			    		reader.close();
					}
	    			
	    			catch (IOException e) {
						e.printStackTrace();
					}
	    		}
	    	}
    	}

		TableLayout table = (TableLayout)(getActivity().findViewById(R.id.score_list));
		
		TableRow row;
		TextView num, score, name;
    	Iterator<Integer> iter = scoreList.descendingKeySet().iterator();
    	Activity parent = getActivity();
    	
    	for(int i = 1; i <= scoreList.size(); i++) {
    		int key = iter.next();
    		
    		row = new TableRow(parent);
    		row.setLayoutMode(TableLayout.LayoutParams.MATCH_PARENT);
    		
    		num = new TextView(parent);
    		num.setText(Integer.toString(i) + '.');
    		num.setTextColor(getResources().getColor(R.color.white));
    		num.setPadding(0, 0, 15, 0);
    		num.setTextSize(17);
    	
    		score = new TextView(parent);
    		score.setText(Integer.toString(key));
    		score.setTextColor(getResources().getColor(R.color.white));
    		score.setTextSize(17);
    		
    		name = new TextView(parent);
    		name.setText(scoreList.get(key));
    		name.setTextColor(getResources().getColor(R.color.white));
    		name.setTextSize(17);
    		
    		row.addView(num);
    		row.addView(score);
    		row.addView(name);
    		
    		table.addView(row);
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
