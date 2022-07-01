/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import model.SlangWord;

/**
 *
 * @author duc
 */
public class SlangWordUtils {
    private static final String FILE_PATH = "slang.txt";
    
    public static TreeMap<String, String> getAllWords(){
        TreeMap<String, String> wordList = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader (new FileReader(FILE_PATH))){
            //get header
            String line = reader.readLine();
            String[] headers = line.split("`");
            
            //get wordList
            line = reader.readLine();
            while (line != null) {
                SlangWord word = new SlangWord();
                String[] parts = line.split("`");
                word.setWord(parts[0]);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordList;
    }
}
