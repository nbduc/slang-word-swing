/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    
    private void createNew() {
        randomSlangWordList = SlangWordUtils.random4SlangWord();
        Random random = new Random();
        answeringIndex = random.nextInt(4);
        SlangWord answeringWord = randomSlangWordList.get(answeringIndex);
        String definitionString = answeringWord.getDefinitionList()[random.nextInt(answeringWord.getDefinitionList().length)];
        definitionLabel.setText(definitionString);
        
        answer0RadioButton.setText(randomSlangWordList.get(0).getWord());
        answer1RadioButton.setText(randomSlangWordList.get(1).getWord());
        answer2RadioButton.setText(randomSlangWordList.get(2).getWord());
        answer3RadioButton.setText(randomSlangWordList.get(3).getWord());
        
        answerOptionButtonGroup.clearSelection();
        
        checkButton.setEnabled(false);
    }
    
    private void createAndShowGUI() {
        JPanel mainPane = new JPanel();
        mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
        titleLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
        mainPane.add(titleLabel);
        mainPane.add(conductLabel);
        
        definitionLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
        mainPane.add(definitionLabel);
        
        class ChooseWordActionListener implements ActionListener {
            public void actionPerformed(ActionEvent ex) {
                checkButton.setEnabled(true);
            }
        }
        ChooseWordActionListener cwal = new ChooseWordActionListener();
        
        answer0RadioButton.setActionCommand("0");
        answer0RadioButton.addActionListener(cwal);
        
        answer1RadioButton.setActionCommand("1");
        answer1RadioButton.addActionListener(cwal);
        
        answer2RadioButton.setActionCommand("2");
        answer2RadioButton.addActionListener(cwal);
        
        answer3RadioButton.setActionCommand("3");
        answer3RadioButton.addActionListener(cwal);
        
        answerOptionButtonGroup.add(answer0RadioButton);
        answerOptionButtonGroup.add(answer1RadioButton);
        answerOptionButtonGroup.add(answer2RadioButton);
        answerOptionButtonGroup.add(answer3RadioButton);
        
        
        mainPane.add(answer0RadioButton);
        mainPane.add(answer1RadioButton);
        mainPane.add(answer2RadioButton);
        mainPane.add(answer3RadioButton);
        checkButton.addActionListener((ActionEvent e) -> {
            String choice = answerOptionButtonGroup.getSelection().getActionCommand();
            if(Integer.parseInt(choice) == answeringIndex){
                JOptionPane.showMessageDialog(null, "That's right!", "Correct", 
                        JOptionPane.INFORMATION_MESSAGE);
                createNew();
            } else {
                JOptionPane.showMessageDialog(null, "Oh no, that's wrong! Choose again.", "Incorrect", 
                            JOptionPane.ERROR_MESSAGE);
            }
            
        });
        mainPane.add(checkButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPane, BorderLayout.CENTER);
        setTitle("Guess the Word");
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    
    public GuessTheWord() {
        createNew();
        createAndShowGUI();
    }
}
