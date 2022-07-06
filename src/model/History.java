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
public class History implements Comparable<History>{
    private long epochSecond;
    private String word;
    
    public History(){}
    
    public History(long epochSecond, String word){
        this.epochSecond = epochSecond;
        this.word = word;
    }

    @Override
    public int compareTo(History o) {
        return Long.compare(o.getEpochSecond(), epochSecond);
    }

    /**
     * @return the epochSecond
     */
    public long getEpochSecond() {
        return epochSecond;
    }

    /**
     * @param epochSecond the epochSecond to set
     */
    public void setEpochSecond(long epochSecond) {
        this.epochSecond = epochSecond;
    }

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
    
    public String toString(){
        return epochSecond + "," + word;
    }
}
