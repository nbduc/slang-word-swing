/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author duc
 */
public class SlangWordCRUD extends JFrame{
    private static JMenuBar menubar;
    private static JSplitPane mainPane;
    private static JPanel sidePane;
    private static JPanel leftPane;
    private static JPanel searchPane;
    private static JTextField searchTextField;
    private static JButton searchButton;
    private static JScrollPane wordListPane;
    private static JPanel definitionPane;
    
    private static JList wordList;
    public SlangWordCRUD() {
        createAndShowGUI();
    }
    
    private void createAndShowGUI() {
        //set file menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 
                ActionEvent.ALT_MASK));
        exitItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
        fileMenu.add(exitItem);
        
        //set edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem newItem = new JMenuItem("Create a New Word...");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                ActionEvent.CTRL_MASK));
        JMenuItem editItem = new JMenuItem("Edit the Word...");
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 
                ActionEvent.CTRL_MASK));
        JMenuItem deleteItem = new JMenuItem("Delete the Word");
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
                ActionEvent.CTRL_MASK));
        JMenuItem resetItem = new JMenuItem("Reset the Word List");
        
        editMenu.add(newItem);
        editMenu.add(editItem);
        editMenu.add(deleteItem);
        editMenu.add(resetItem);
        
        //set history menu
        JMenu historyMenu = new JMenu("History");
        historyMenu.setMnemonic(KeyEvent.VK_H);
        
        //set quiz menu
        JMenu quizMenu = new JMenu("Quizzes");
        quizMenu.setMnemonic(KeyEvent.VK_Q);
        
        JMenuItem guessWordItem = new JMenuItem("Guess the Word!...");
        JMenuItem guessDefItem = new JMenuItem("Guess the Definition!...");
        
        quizMenu.add(guessWordItem);
        quizMenu.add(guessDefItem);
        
        //set menu bar
        menubar = new JMenuBar();
        menubar.add(fileMenu);
        menubar.add(editMenu);
        menubar.add(historyMenu);
        menubar.add(quizMenu);
        setJMenuBar(menubar);
        
        //set search text field
        searchTextField = new JTextField();
        
        //set search button
        searchButton = new JButton("Search");
        
        //set search pane
        searchPane = new JPanel();
        searchPane.setLayout(new GridBagLayout());
        GridBagConstraints c;
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0; //take all extra space
        c.fill = GridBagConstraints.BOTH; //fill all the space
        searchPane.add(searchTextField, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridwidth = 1;
        searchPane.add(searchButton, c);
        
        String[] choices = {"A", "long", "array", "of", "strings"};
        
        //set word list
        wordList = new JList(choices);
        wordList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        wordList.setLayoutOrientation(JList.VERTICAL);
        wordList.setVisibleRowCount(-1);
        
        //set word list pane
        wordListPane = new JScrollPane(wordList);
        
        //set the left pane
        leftPane = new JPanel();
        leftPane.setPreferredSize(new Dimension(200, -1));
        leftPane.setLayout(new BorderLayout());
        leftPane.add(searchPane, BorderLayout.NORTH);
        leftPane.add(wordListPane, BorderLayout.CENTER);
        
        
        //set definition pane
        definitionPane = new JPanel();
        
        // set main pane
        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, definitionPane);
        
        //set the side pane
        sidePane = new JPanel();
        sidePane.setPreferredSize(new Dimension(200, -1));
        
        //
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPane, BorderLayout.CENTER);
        getContentPane().add(sidePane, BorderLayout.EAST);
        setTitle("Slang Word");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SlangWordCRUD window = new SlangWordCRUD();
            window.setVisible(true);
        });
    }
}
