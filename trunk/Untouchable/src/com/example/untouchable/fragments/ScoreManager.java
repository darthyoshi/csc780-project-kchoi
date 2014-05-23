package com.example.untouchable.fragments;

import java.io.*;
import java.util.*;

import android.content.Context;

public class ScoreManager {

    /**
     * Retrieves the high scores from the file system.
     * @param context the execution context
     * @return an ArrayList containing the score values
     */
    public static void readScores(Context context, ArrayList<Integer> scores, ArrayList<String> names) {
        scores.clear();
        names.clear();
        
        BufferedReader reader = null;
        try {
            //read saved scores
            reader = new BufferedReader(new InputStreamReader(context.openFileInput("myscores.csv")));
        }
        
        catch(FileNotFoundException e) {
            try {
                //read default scores
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open("scores.csv")));
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
                        
                        scores.add(Integer.parseInt(words[0]));
                        
                        names.add(words[1]);
                    }
    
                    reader.close();
                }
                
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Saves the scores to the file system.
     * @param scores an ArrayList containing the score values
     * @param names an ArrayList containing the score names
     * @param context the execution context
     */
    public static void saveScores(ArrayList<Integer> scores, ArrayList<String> names, Context context) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput("myscores.csv", Context.MODE_PRIVATE)));
            
            for(short i = 0; i < scores.size(); i++) {
                writer.write(scores.get(i).toString() + ',' + names.get(i) + '\n');
            }
            
            writer.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetScores(Context context) {
        context.deleteFile("myscores.csv");
    }
}
