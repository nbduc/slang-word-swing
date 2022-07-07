/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.TreeSet;
import model.History;

/**
 *
 * @author duc
 */
public class HistoryUtils {
    private static final String HISTORY_FILE_PATH = "history.txt";
    private static TreeSet<History> historyList;
    
    private static void loadHistory(){
        historyList = new TreeSet<>();
        try {
            File historyFile = new File(HISTORY_FILE_PATH);
            if(!historyFile.exists()){
                historyFile.createNewFile();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
                String line = reader.readLine();
                while(line != null){
                    String[] parts = line.split(",");
                    historyList.add(new History(Long.parseLong(parts[0]), parts[1]));
                    line = reader.readLine();
                }
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public static TreeSet<History> getHistoryList(){
        if(historyList == null){
            loadHistory();
        }
        return historyList;
    }
    
    public static TreeSet<History> getLast10HistoryList(){
        if (historyList == null){
            loadHistory();
        }
        if(historyList.size() > 10){
                TreeSet<History> last10HistoryList = new TreeSet<>();
                Iterator<History> iter  = historyList.iterator();
                int i = 0;
                while(iter.hasNext() && i < 10){
                    History nextHistory = iter.next();
                    last10HistoryList.add(nextHistory);
                    i++;
                }
                return last10HistoryList;
            }
        return historyList;
    }
    
    public static boolean writeHistory(History newHistory){
        try {
            File historyFile = new File(HISTORY_FILE_PATH);
            if(!historyFile.exists()){
                historyFile.createNewFile();
            }
            try (PrintWriter pw = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(historyFile, true)))){
                pw.println(newHistory.convertToCsv());
                pw.flush();
            }
            historyList.add(newHistory);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
