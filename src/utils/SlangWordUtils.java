/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;
import model.SlangWord;

/**
 *
 * @author duc
 */
public class SlangWordUtils {
    private static final String FILE_PATH = "slang.txt";
    private static TreeMap<String, String[]> wordListTM;
    
    public static TreeMap<String, String[]> getWordListTM(){
        if(wordListTM == null){
            loadAllWords();
        }
        return wordListTM;
    }
    
    public static SlangWord searchByWord(String searchString){
        searchString = searchString.toUpperCase();
        if(wordListTM.containsKey(searchString)){
            SlangWord result = new SlangWord();
            result.setWord(searchString);
            result.setDefinitionList(wordListTM.get(searchString));
            return result;
        }
        return null;
    }
    
    private static void loadAllWords(){
        TreeMap<String, String[]> wordList = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader (new FileReader(FILE_PATH))){
            //get header
            String line;
            reader.readLine();
            
            //get wordList
            line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("`");
                
                if(!wordList.containsKey(parts[0])){
                    if(parts.length > 1){
                        wordList.put(parts[0], parts[1].split("\\| "));
                    } else {
                        wordList.put(parts[0], null);
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordListTM = wordList;
    }
    
    public static String convertWordToHtml(SlangWord word){
        StringBuilder htmlContent = new StringBuilder();
        if(word != null){
            htmlContent.append("<html>")
                .append("<body>")
                .append("<h2>").append(word.getWord()).append("</h2>")
                .append("<ul>");
            for(String def: word.getDefinitionList()){
                htmlContent.append("<li>").append(def).append("</li>");
            }
            htmlContent.append("</ul>")
                .append("</body>")
                .append("</html>");
            return htmlContent.toString();
        }
        return null;
    }
    
    public static void printAllWords(TreeMap<String, String[]> wordList) {
        wordList.entrySet().forEach((entry) -> {
            String word = entry.getKey();
            String[] defs = entry.getValue();

            System.out.println(word + " => " + Arrays.toString(defs));
        });
    }
}
