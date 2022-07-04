/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import model.SlangWord;
import utils.SlangWordUtils;

/**
 *
 * @author duc
 */
public class GuessTheWord extends JFrame{
    private ArrayList<SlangWord> randomSlangWordList;
    private int answeringIndex = 0;
    private JLabel titleLabel = new JLabel("Guess the Word Quiz");
    private JLabel conductLabel = new JLabel("Choose the word that matches the following meaning:");
    private JLabel definitionLabel = new JLabel();
    private JRadioButton answer0RadioButton = new JRadioButton();
    private JRadioButton answer1RadioButton = new JRadioButton();
    private JRadioButton answer2RadioButton = new JRadioButton();
    private JRadioButton answer3RadioButton = new JRadioButton();
    private ButtonGroup answerOptionButtonGroup = new ButtonGroup();
    private JButton checkButton = new JButton("Check");
    
    private void initAnswer(){
        Random random = new Random();
        answeringIndex = random.nextInt(4);
        SlangWord answeringWord = randomSlangWordList.get(answeringIndex);
        String definitionString = answeringWord.getDefinitionList()[random.nextInt(answeringWord.getDefinitionList().length)];
        definitionLabel.setText(definitionString);
        answer0RadioButton.setText(randomSlangWordList.get(0).getWord());
        answer1RadioButton.setText(randomSlangWordList.get(1).getWord());
        answer2RadioButton.setText(randomSlangWordList.get(2).getWord());
        answer3RadioButton.setText(randomSlangWordList.get(3).getWord());
        answerOptionButtonGroup.add(answer0RadioButton);
        answerOptionButtonGroup.add(answer1RadioButton);
        answerOptionButtonGroup.add(answer2RadioButton);
        answerOptionButtonGroup.add(answer3RadioButton);
    }
    
    private void createAndShowUI() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        titleLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
        getContentPane().add(titleLabel);
        getContentPane().add(conductLabel);
        getContentPane().add(definitionLabel);
        getContentPane().add(answer0RadioButton);
        getContentPane().add(answer1RadioButton);
        getContentPane().add(answer2RadioButton);
        getContentPane().add(answer3RadioButton);
        checkButton.setEnabled(false);
        getContentPane().add(checkButton);
        setTitle("Guess the Word");
        setSize(400, 400);
        setLocationRelativeTo(null);
    }
    
    public GuessTheWord() {
        randomSlangWordList = SlangWordUtils.random4SlangWord();
        initAnswer();
        createAndShowUI();
    }
}
