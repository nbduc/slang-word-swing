/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author duc
 */
public class SlangWord {
    private String word;
    private String[] definitionList;

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the definitionList
     */
    public String[] getDefinitionList() {
        return definitionList;
    }

    /**
     * @param definitionList the definitionList to set
     */
    public void setDefinitionList(String[] definitionList) {
        this.definitionList = definitionList;
    }
    
    public String toString(){
        return word;
    }
}
