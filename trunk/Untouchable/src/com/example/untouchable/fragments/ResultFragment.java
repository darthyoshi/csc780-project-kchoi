package com.example.untouchable.fragments;

import com.example.untouchable.R;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class ResultFragment extends Fragment {
    private int baseScore, timeScore, lvlScore, totalScore;
    private short multiplier;
    
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
        View view = inflater.inflate(R.layout.result, container, false);
        
        Button quitButton = (Button)view.findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().getFragmentManager().popBackStackImmediate("TITLE_FRAGMENT", 0);
            }
            
        });
        
        Button continueButton = (Button)view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().getFragmentManager().popBackStackImmediate();
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
     * 
     * @param baseScore
     * @param timeScore
     * @param multiplier
     * @param lvlScore
     * @param totalScore
     */
    public void setParams(int baseScore, int timeScore, short multiplier, int lvlScore, int totalScore) {
        this.baseScore = baseScore;
        this.timeScore = timeScore;
        this.multiplier = multiplier;
        this.lvlScore = lvlScore;
        this.totalScore = totalScore;
    }
    
    private void setText() {
        Activity parent = getActivity();
        
        ((TextView)parent.findViewById(R.id.baseScore)).setText(Integer.toString(baseScore));
        ((TextView)parent.findViewById(R.id.timeScore)).setText(Integer.toString(timeScore));
        ((TextView)parent.findViewById(R.id.multiplier)).setText("x"+multiplier);
        ((TextView)parent.findViewById(R.id.lvlScore)).setText(Integer.toString(lvlScore));
        ((TextView)parent.findViewById(R.id.totalScore)).setText(Integer.toString(totalScore));
    }
}
