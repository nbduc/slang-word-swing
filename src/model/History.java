/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        Date time = new Date(epochSecond * 1000);
        return df.format(time) + " - " + word;
    }
    
    public String convertToCsv(){
        return epochSecond + "," + word;
    }
}
