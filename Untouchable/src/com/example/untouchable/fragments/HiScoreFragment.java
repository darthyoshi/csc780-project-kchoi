/**
 *  @file HiScoreFragment.java
 *  @author Kay Choi
 */

package com.example.untouchable.fragments;

import java.util.*;

import android.app.*;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.example.untouchable.R;

public class HiScoreFragment extends Fragment {
    private ArrayList<Integer> scores = null;
    private ArrayList<String> names = null;
    private int score = -1;
    private EditText input = null;
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	Activity parent = getActivity();
    	
    	if(scores == null) {
	    	scores = ScoreManager.readScores(parent);
    	}
    	
    	if(names == null) {
    	    names = ScoreManager.readNames(parent);
    	}
    	
    	if(score >= scores.get(scores.size()-1)) {
    	    saveEntry(parent);
    	}
    	
    	populateTable(parent);
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
		
		if(scores != null && names != null) {
	        ScoreManager.saveScores(scores, names, getActivity());
		}
	}
	
    public void setScore(int score) {
        this.score = 2001/*score*/;
    }
    
    private void populateTable(Activity parent) {
        TableLayout table = (TableLayout)(parent.findViewById(R.id.score_list));
        
        TableRow row;
        TextView num, score, name;
        Resources resources = parent.getResources();
        
        short i;
        
        if(table.getChildCount() == 0) {
            for(i = 1; i <= scores.size(); i++) {
                row = new TableRow(parent);
                row.setLayoutMode(TableLayout.LayoutParams.MATCH_PARENT);
                
                num = new TextView(parent);
                num.setText(Integer.toString(i) + '.');
                num.setTextColor(resources.getColor(R.color.white));
                num.setPadding(0, 0, 15, 0);
                num.setTextSize(17);
            
                score = new TextView(parent);
                score.setText(Integer.toString(scores.get(i-1)));
                score.setTextColor(resources.getColor(R.color.white));
                score.setTextSize(17);
                
                name = new TextView(parent);
                name.setText(names.get(i-1));
                name.setTextColor(resources.getColor(R.color.white));
                name.setTextSize(17);
                
                row.addView(num);
                row.addView(score);
                row.addView(name);
                
                table.addView(row);
            }   
        }
        
        else {
            for(i = 0; i < scores.size(); i++) {
                row = (TableRow) table.getChildAt(i);
System.out.println("blah");
                ((TextView)row.getChildAt(1)).setText(Integer.toString(scores.get(i)));
                ((TextView)row.getChildAt(2)).setText(names.get(i));
            }
        }
    }
    
    private void saveEntry(Activity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        if(input == null) {
            input = new EditText(parent);
        }
        
        builder.setMessage("Congratulations, you have a high score!")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    short i = addScoreToList();
                    
                    names.add(i, "Faceless Monkey");
                    
                    names.remove(names.size() - 1);
                    
                    updateTable(getActivity());
                    
                    dialog.dismiss();
                }
            })
            .setTitle("High Score")
            .setPositiveButton(parent.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    short i = addScoreToList();
                    
                    names.add(i, input.getText().toString());
                    
                    names.remove(names.size() - 1);
                    
                    updateTable(getActivity());
                    
                    dialog.dismiss();
                }
            })
            .setView(input);
    
        AlertDialog dialog = builder.create();
        dialog.show();        
    }
    
    private short addScoreToList() {
        short i;
        for(i = 0; i < scores.size() && scores.get(i) > score; i++);
        
        scores.add(i, score);
        
        scores.remove(scores.size()-1);

        return i;
    }

    /**
     * Updates the score table.
     * @param parent the parent Activity
     */
    private void updateTable(Activity parent) {
        TableLayout table = (TableLayout)(parent.findViewById(R.id.score_list));
        
        TableRow row;
        
        short i;
   
        for(i = 0; i < scores.size(); i++) {
            row = (TableRow) table.getChildAt(i);

            ((TextView)row.getChildAt(1)).setText(Integer.toString(scores.get(i)));
            ((TextView)row.getChildAt(2)).setText(names.get(i));
        }
    }
}
