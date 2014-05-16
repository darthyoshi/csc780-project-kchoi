package com.example.untouchable.fragments;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import android.content.Context;

public class ScoreManager {

    /**
     * Retrieves the score values from the file system.
     * @param context the execution context
     * @return an ArrayList containing the score values
     */
    public static ArrayList<Integer> readScores(Context context) {
        ArrayList<Integer> scores = new ArrayList<Integer>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput("myscores.csv")));
        }
        
        catch(Exception e) {
            try {
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
                    }
    
                    reader.close();
                }
                
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return scores;
    }
    
    /**
     * Retrieves the score names from the file system.
     * @param context the execution context
     * @return an ArrayList containing the score names
     */
    public static ArrayList<String> readNames(Context context) {
        ArrayList<String> names = new ArrayList<String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput("myscores.csv")));
        }
        
        catch(Exception e) {
            try {
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
                        
                        names.add(words[1]);
                    }
    
                    reader.close();
                }
                
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return names;
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
