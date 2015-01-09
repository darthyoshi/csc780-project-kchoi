package com.example.untouchable.fragments;

import com.example.untouchable.R;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class ResultFragment extends Fragment {
    private int baseScore, lvlScore, totalScore;
    private float timeBonus;
    private short multiplier;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) 
    {   
        View view = inflater.inflate(R.layout.result, container, false);
        
        final Activity parent = getActivity();
        
        Button quitButton = (Button)view.findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showWarning(parent);
            }
            
        });
        
        Button continueButton = (Button)view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GameFragment frag = (GameFragment)parent.getFragmentManager().findFragmentByTag("GAME_FRAGMENT");
                
                frag.nextLevel();
                
                parent.getFragmentManager().popBackStackImmediate();
            }
            
        });
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        setText();
    }

    /**
     * Sets the score parameters.
     * @param baseScore base score for the level
     * @param timeBonus time bonus score
     * @param multiplier difficulty multiplier
     * @param lvlScore total score for the level 
     * @param totalScore total score for the current session
     */
    public void setParams(int baseScore, float timeBonus, short multiplier, int lvlScore, int totalScore) {
        this.baseScore = baseScore;
        this.timeBonus = timeBonus;
        this.multiplier = multiplier;
        this.lvlScore = lvlScore;
        this.totalScore = totalScore;
    }
    
    /**
     * Displays the score parameters.
     */
    private void setText() {
        Activity parent = getActivity();
        
        ((TextView)parent.findViewById(R.id.baseScore)).setText(Integer.toString(baseScore));
        ((TextView)parent.findViewById(R.id.timeScore)).setText("x"+(new java.text.DecimalFormat("#.##").format(timeBonus)));
        ((TextView)parent.findViewById(R.id.multiplier)).setText("x"+multiplier);
        ((TextView)parent.findViewById(R.id.lvlScore)).setText(Integer.toString(lvlScore));
        ((TextView)parent.findViewById(R.id.totalScore)).setText(Integer.toString(totalScore));
    }
    
    /**
     * Callback method for the back button.
     */
    public void onBackPressed() {
        showWarning(getActivity());
    }

    /**
     * Displays the quit warning.
     * @param parent the parent Activity of the fragment
     */
    private void showWarning(final Activity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        
        builder.setCancelable(false)
            .setPositiveButton(parent.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    parent.getFragmentManager().popBackStackImmediate("TITLE_FRAGMENT", 0);
                    
                    dialog.dismiss();
                }
            })
            .setNegativeButton(parent.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setMessage("Really quit? Your score will NOT be saved.")
            .setTitle(parent.getResources().getString(R.string.quit));
        
        AlertDialog dialog = builder.create();
        dialog.show();        
    }
}
