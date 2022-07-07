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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.Collection;
import javax.swing.event.DocumentListener;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import model.History;
import model.SlangWord;
import utils.HistoryUtils;
import utils.SlangWordUtils;

/**
 *
 * @author duc
 */
public class SlangWordCRUD extends JFrame{
    private static boolean isSearchingByWord = true;
    
    private static JMenuBar menubar;
    private static JSplitPane mainPane;
    private static JMenu historyMenu;
    private static JPanel sidePane;
    private static JPanel leftPane;
    private static JPanel searchPane;
    private static JRadioButton searchByWordRadioButton;
    private static JRadioButton searchByDefinitionRadioButton;
    private static ButtonGroup searchOptionButtonGroup;
    private static JTextField searchTextField;
    private static JButton searchButton;
    private static JScrollPane wordListPane;
    private static JPanel definitionPane;
    private static JEditorPane definitionEditorPane;
    private static JList wordListContainer;
    private static Collection<SlangWord> originWordList;
    
    public SlangWordCRUD() {
        createAndShowGUI();
        initOriginWordList();
        createNewWordList(originWordList);
        createNewHistoryList();
    }
    
    private static void initOriginWordList(){
        TreeMap<String, String[]> wordListTM = SlangWordUtils.getWordListTM();
        Collection<SlangWord> wordList = new Vector<>();
        wordListTM.entrySet().stream().map((entry) -> {
            SlangWord word = new SlangWord();
            word.setWord(entry.getKey());
            word.setDefinitionList(entry.getValue());
            return word;
        }).forEachOrdered((word) -> {
            wordList.add(word);
        });
        originWordList = wordList;
    }
    
    private static void displayWordFromHistory(String word){
        isSearchingByWord = true;
        searchAndDisplayResult(word);
    }
    
    private static void createNewHistoryList(){
        historyMenu.removeAll();
        TreeSet<History> last10HistoryList = HistoryUtils.getLast10HistoryList();
        if(last10HistoryList.size() > 0){
            class HistoryMenuItemActionListener implements ActionListener {
                private final String word;
                public HistoryMenuItemActionListener(String word){
                    this.word = word;
                }
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayWordFromHistory(word);
                }
            }
            last10HistoryList.forEach(history -> {
                JMenuItem newHistoryMenuItem = new JMenuItem(history.getWord());
                newHistoryMenuItem.addActionListener(new HistoryMenuItemActionListener(history.getWord()));
                historyMenu.add(newHistoryMenuItem);
            });
            historyMenu.addSeparator();
        }
        JMenuItem historyListMenuItem = new JMenuItem("History...");
        historyListMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, 
                ActionEvent.CTRL_MASK));
        historyListMenuItem.addActionListener((ActionEvent e) -> {
            HistoryDialog historyDialog = new HistoryDialog(null, "History");
            displayWordFromHistory(historyDialog.getReturnedValue());
        });
        historyMenu.add(historyListMenuItem);
    }
    
    private static void createNewWordList(Collection<SlangWord> wordList){
        DefaultListModel model = new DefaultListModel();
        model.addAll(wordList);
        wordListContainer.setModel(model);
    }
    
    private static String NoDataHtmlContent(){
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html>")
                .append("<i style=\"color: red;\">")
                .append("Cannot find that word!")
                .append("</i>")
                .append("</html>");
        return htmlContent.toString();
    }
    
    private static void displayWordDetailAndWriteHistory(SlangWord word){
        definitionEditorPane.setText(SlangWordUtils.convertWordToHtml(word));
        History newHistory = new History(Instant.now().getEpochSecond(), word.getWord());
        HistoryUtils.writeHistory(newHistory);
        createNewHistoryList();
    }
    
    private static void searchAndDisplayResult(String searchString){
        if(!"".equals(searchString)){
            wordListContainer.clearSelection();
            
            if(isSearchingByWord){
                SlangWord searchResult = SlangWordUtils.searchByWord(searchString);
                if(searchResult != null){
                    displayWordDetailAndWriteHistory(searchResult);
                    
                    Vector<SlangWord> wordList = new Vector<>();
                    wordList.add(searchResult);
                    createNewWordList(wordList);
                } else {
                    definitionEditorPane.setText(NoDataHtmlContent());
                }
            } else {
                TreeSet<SlangWord> searchResult = SlangWordUtils.searchByDef(searchString);
                createNewWordList(searchResult);
            }
        }
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
        historyMenu = new JMenu("History");
        historyMenu.setMnemonic(KeyEvent.VK_H);
        
        //set quiz menu
        JMenu quizMenu = new JMenu("Quizzes");
        quizMenu.setMnemonic(KeyEvent.VK_Q);
        
        JMenuItem guessWordItem = new JMenuItem("Guess the Word!...");
        guessWordItem.addActionListener((ActionEvent e) -> {
            GuessQuiz guessTheWordFrame = new GuessQuiz(true);
            guessTheWordFrame.setVisible(true);
        });
        JMenuItem guessDefItem = new JMenuItem("Guess the Definition!...");
        guessDefItem.addActionListener((ActionEvent e) -> {
            GuessQuiz guessTheDefinitionFrame = new GuessQuiz(false);
            guessTheDefinitionFrame.setVisible(true);
        });
        
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
        searchTextField.addActionListener((ActionEvent e) -> 
                searchAndDisplayResult(searchTextField.getText()));
        searchTextField.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent e) {
                action();
            }
            public void removeUpdate(DocumentEvent e) {
                action();
            }
            public void insertUpdate(DocumentEvent e) {
                action();
            }

            public void action() {
                if ("".equals(searchTextField.getText())){
                    createNewWordList(originWordList);
                }
            }
        });
        
        //set search button
        searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent e) -> 
                searchAndDisplayResult(searchTextField.getText()));
        
        //set search option
        class ChooseSearchOptionActionListener implements ActionListener {
            public void actionPerformed(ActionEvent ex) {
                isSearchingByWord = Boolean.parseBoolean(searchOptionButtonGroup.getSelection().getActionCommand());
            }
        }
        ChooseSearchOptionActionListener csoal = new ChooseSearchOptionActionListener();
        searchByWordRadioButton = new JRadioButton("Search by word");
        searchByWordRadioButton.setSelected(true);
        searchByWordRadioButton.setActionCommand("true");
        searchByWordRadioButton.addActionListener(csoal);
        
        searchByDefinitionRadioButton = new JRadioButton("Search by definition");
        searchByDefinitionRadioButton.setActionCommand("false");
        searchByDefinitionRadioButton.addActionListener(csoal);
        
        searchOptionButtonGroup = new ButtonGroup();
        searchOptionButtonGroup.add(searchByWordRadioButton);
        searchOptionButtonGroup.add(searchByDefinitionRadioButton);
        
        //set search pane
        searchPane = new JPanel();
        searchPane.setLayout(new GridBagLayout());
        GridBagConstraints c;
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 0;
        c.gridx = 0;
        searchPane.add(searchByWordRadioButton, c);
        c.gridy = 1;
        searchPane.add(searchByDefinitionRadioButton, c);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 1.0; //take all extra space
        c.fill = GridBagConstraints.BOTH; //fill all the space
        searchPane.add(searchTextField, c);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 1;
        c.gridwidth = 1;
        searchPane.add(searchButton, c);
        
        //set word list container
        wordListContainer = new JList();
        wordListContainer.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        wordListContainer.setLayoutOrientation(JList.VERTICAL);
        wordListContainer.setVisibleRowCount(-1);
        wordListContainer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) 
            {
                SlangWord selectedWord = (SlangWord)wordListContainer.getSelectedValue();
                displayWordDetailAndWriteHistory(selectedWord);
            }
        });
        
        //set word list pane
        wordListPane = new JScrollPane(wordListContainer);
        
        //set the left pane
        leftPane = new JPanel();
        leftPane.setPreferredSize(new Dimension(250, -1));
        leftPane.setLayout(new BorderLayout());
        leftPane.add(searchPane, BorderLayout.NORTH);
        leftPane.add(wordListPane, BorderLayout.CENTER);
        
        //set definitionEditorPane
        definitionEditorPane = new JEditorPane();
        definitionEditorPane.setContentType("text/html");
        definitionEditorPane.setEditable(false);
        
        //set definition pane
        definitionPane = new JPanel(new BorderLayout());
        definitionPane.add(definitionEditorPane, BorderLayout.CENTER);
        
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
