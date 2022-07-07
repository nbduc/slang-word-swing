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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import model.SlangWord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Writer;

/**
 *
 * @author duc
 */
public class SlangWordUtils {
    private static final String SLANG_FILE_PATH = "slang.txt";
    
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
        if(searchString != null){
            searchString = searchString.toUpperCase();
            if(wordListTM.containsKey(searchString)){
                SlangWord result = new SlangWord(searchString, wordListTM.get(searchString));
                return result;
            }
        }
        return null;
    }
    
    public static TreeSet<SlangWord> searchByDef(String searchString){
        TreeSet<SlangWord> result = new TreeSet<>();
        String[] searchStringList = searchString.split("\\s+");
        for (var w : searchStringList){
            if(wordListHM.containsKey(w)){
                wordListHM.get(w).forEach((sw) -> {
                    result.add(sw);
                });
            }
        }
        return result;
    }
    
    private static void addNewSlangWordToHashMap(HashMap<String, LinkedList<SlangWord>> hm, SlangWord newSlangWord){
        String[] defs = newSlangWord.getDefinitionList();
        for (String def : defs){
            for (String w : def.split("\\s+")){
                w = w.toLowerCase();
                if(hm.containsKey(w)){
                    hm.get(w).add(newSlangWord);
                } else {
                    LinkedList<SlangWord> wList = new LinkedList<>();
                    wList.add(newSlangWord);
                    hm.put(w, wList);
                }
            }
        }
    }
    
    private static void loadAllWords(){
        TreeMap<String, String[]> resultWordListTM = new TreeMap<>();
        HashMap<String, LinkedList<SlangWord>> resultWordListHM = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader (new FileReader(SLANG_FILE_PATH))){
            String line;
            //get header
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
                    addNewSlangWordToHashMap(resultWordListHM, newSlangWord);
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
    
    private static boolean writeNewWordToFile(SlangWord newSlangWord){
        try {
            File slangWordFile = new File(SLANG_FILE_PATH);
            if(!slangWordFile.exists()){
                slangWordFile.createNewFile();
            }
            try (PrintWriter pw = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(slangWordFile, true)))){
                pw.println(newSlangWord.convertToCsv());
                pw.flush();
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public static boolean writeNewWord(SlangWord newSlangWord){
        if(wordListTM != null && wordListHM != null){
            String word = newSlangWord.getWord();
            String[] defs = newSlangWord.getDefinitionList();
            wordListTM.put(word, defs);
            addNewSlangWordToHashMap(wordListHM, newSlangWord);
            writeNewWordToFile(newSlangWord);
            
            return true;
        } else {
            return false;
        }
    }
    
    private static void removeWordFromTreeMap(String word){
        if(wordListTM.containsKey(word)){
            wordListTM.remove(word);
        }
    }
    
    private static void removeWordFromHashMap(SlangWord word){
        String[] defs = word.getFlatDefinitionList();
        for(String def : defs){
            if(wordListHM.containsKey(def)){
                wordListHM.get(def).remove(word);
            }
        }
        
    }
    
    public static boolean editWord(SlangWord oldSlangWord, SlangWord editedSlangWord){
        if(wordListTM != null && wordListHM != null){
            if(wordListTM.containsKey(editedSlangWord.getWord())){
                //user's changed the word key
                wordListTM.replace(editedSlangWord.getWord(), editedSlangWord.getDefinitionList());
            } else {
                //user's not changed the word key
                wordListTM.remove(oldSlangWord.getWord());
                wordListTM.put(editedSlangWord.getWord(), editedSlangWord.getDefinitionList());
            }
            
            //
            removeWordFromHashMap(oldSlangWord);
            addNewSlangWordToHashMap(wordListHM, editedSlangWord);
        }
        return false;
    }
    
    public static void saveWordListToFile(){
        File wordListTmFile = new File("slang_tm.json");
        File wordListHmFile = new File("slang_hm.json");
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        try (Writer writerTm = new FileWriter(wordListTmFile); 
                Writer writerHm = new FileWriter(wordListHmFile);) {
            if(!wordListTmFile.exists()){
                wordListTmFile.createNewFile();
            }
            if(!wordListHmFile.exists()){
                wordListHmFile.createNewFile();
            }
            gson.toJson(wordListTM, writerTm);
            gson.toJson(wordListHM, writerHm);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void loadWordList(){
        if(wordListTM == null || wordListHM == null){
            
        }
    }
}
