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
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import model.SlangWord;

/**
 *
 * @author duc
 */
public class SlangWordUtils {
    private static final String FILE_PATH = "slang.txt";
    
    public static TreeMap<String, String[]> getAllWords(){
        TreeMap<String, String[]> wordList = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader (new FileReader(FILE_PATH))){
            //get header
            String line;
            reader.readLine();
            
            //get wordList
            line = reader.readLine();
            while (line != null) {
                SlangWord word = new SlangWord();
                
                String[] parts = line.split("`");
                word.setWord(parts[0]);
                word.setDefinitionList(parts[1].split("| "));
                
                if(!wordList.containsKey(word.getWord())){
                    wordList.put(word.getWord(), word.getDefinitionList());
                }
                
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordList;
    }
    
    public static String convertWordToHtml(String[] defs){
        StringBuilder htmlContent = new StringBuilder();
        for(String def: defs){
            htmlContent.append("<html>");
            htmlContent.append(def);
            htmlContent.append("");
            htmlContent.append("</html>");
        }
        return htmlContent.toString();
    }
    
    public static void printAllWords(TreeMap<String, String[]> wordList) {
        wordList.entrySet().forEach((entry) -> {
            String word = entry.getKey();
            String[] defs = entry.getValue();

            System.out.println(word + " => " + Arrays.toString(defs));
        });
    }
}
