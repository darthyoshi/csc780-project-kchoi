/**
 *  @file HiScoreActivity.java
 *  @author Kay Choi
 */

package com.example.untouchable;

import java.util.SortedMap;

import android.app.Activity;
import android.os.Bundle;

public class HiScoreActivity extends Activity {
    private SortedMap<Integer, String> scoreList;
    
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
}
