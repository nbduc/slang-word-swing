/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;
import model.SlangWord;

/**
 *
 * @author duc
 */
public class SlangWordUtils {
    private static final String FILE_PATH = "slang.txt";
    private static TreeMap<String, String[]> wordListTM;
    private static HashMap<String, LinkedList<SlangWord>> wordListHM;
    
    public static TreeMap<String, String[]> getWordListTM(){
        if(wordListTM == null){
            loadAllWords();
        }
        return wordListTM;
    }
    
    public static HashMap<String, LinkedList<SlangWord>> getWordListHM() {
        if(wordListHM == null){
            loadAllWords();
        }
        return wordListHM;
    }
    
    public static SlangWord searchByWord(String searchString){
        searchString = searchString.toUpperCase();
        if(wordListTM.containsKey(searchString)){
            SlangWord result = new SlangWord(searchString, wordListTM.get(searchString));
            return result;
        }
        return null;
    }
    
    public static HashSet<SlangWord> searchByDef(String searchString){
        HashSet<SlangWord> result = new HashSet<>();
        String[] searchStringList = searchString.split("\\s+");
        for (String w : searchStringList){
            if(wordListHM.containsKey(w)){
                for(SlangWord sw : wordListHM.get(w)){
                    result.add(sw);
                }
            }
        }
        return result;
    }
    
    private static void loadAllWords(){
        TreeMap<String, String[]> resultWordListTM = new TreeMap<>();
        HashMap<String, LinkedList<SlangWord>> resultWordListHM = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader (new FileReader(FILE_PATH))){
            //get header
            String line;
            reader.readLine();
            
            //get wordList
            line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("`");
                if(parts.length > 1){
                    String word = parts[0];
                    String[] defs = parts[1].split("\\| ");

                    //set wordListTM
                    if(!resultWordListTM.containsKey(word)){
                        resultWordListTM.put(word, defs);
                    }

                    //set wordListHM
                    SlangWord newSlangWord = new SlangWord(word, defs);
                    for (String def : defs){
                        for (String w : def.split("\\s+")){
                            w = w.toLowerCase();
                            if(resultWordListHM.containsKey(w)){
                                resultWordListHM.get(w).add(newSlangWord);
                            } else {
                                LinkedList<SlangWord> wList = new LinkedList<>();
                                wList.add(newSlangWord);
                                resultWordListHM.put(w, wList);
                            }
                        }
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordListTM = resultWordListTM;
        wordListHM = resultWordListHM;
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
    
    public static ArrayList<SlangWord> random4SlangWord() {
        ArrayList<SlangWord> wordListResult = new ArrayList<>();
        
        Random random = new Random();
        ArrayList<String> wordList = new ArrayList<>(wordListTM.keySet());
        for(int i = 0; i < 4; i++){
            String randomWord = wordList.get(random.nextInt(wordList.size()));
            wordListResult.add(new SlangWord(randomWord, wordListTM.get(randomWord)));
        }
        return wordListResult;
    }
}
