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
    private ArrayList<Integer> scores = new ArrayList<Integer>();
    private ArrayList<String> names = new ArrayList<String>();
    private int score = -1;
    private EditText input = null;
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	Activity parent = getActivity();
    	
    	ScoreManager.readScores(parent, scores, names);
    	
    	if(score >= scores.get(scores.size()-1)) {
    	    saveEntry(parent);
    	}
    	
    	populateTable(parent);
    }
    
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

	/**
	 * Sets the score for the current session.
	 * @param score the score to save
	 */
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * Populates and displays the high score list.
     * @param parent the parent Activity
     */
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

                ((TextView)row.getChildAt(1)).setText(Integer.toString(scores.get(i)));
                ((TextView)row.getChildAt(2)).setText(names.get(i));
            }
        }
    }
    
    /**
     * Gets the name for the new high score and saves the entry to memory.
     * @param parent the parent Activity
     */
    private void saveEntry(final Activity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        
        if(input == null) {
            input = new EditText(parent);
        }
        
        final short i = addScoreToList();
        
        builder.setMessage(parent.getResources().getString(R.string.hiScoreMsg))
            .setView(input)
            .setNegativeButton(parent.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    names.add(i, "Faceless Monkey");

                    dialog.dismiss();
                }
            })
            .setPositiveButton(parent.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    names.add(i, input.getText().toString());

                    dialog.dismiss();
                }
            })
            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                
                @Override
                public void onDismiss(DialogInterface dialog) {
                    names.remove(names.size() - 1);
                    
                    updateTable(parent);
                }
            });
    
        AlertDialog dialog = builder.create();
        dialog.show();        
    }
    
    /**
     * Saves the new high score values to memory.
     * @return the index of the new high score value 
     */
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
