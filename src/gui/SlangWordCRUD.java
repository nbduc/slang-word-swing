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
import java.awt.event.WindowAdapter;
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
    private static JLabel wordOfTheDayLabel;
    private static JButton showWordOfTheDayButton;
    private static JButton randomWordButton;
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
    private static SlangWord randomSlangWord;
    
    public SlangWordCRUD() {
        createAndShowGUI();
        initOriginWordList();
        createNewWordList(originWordList);
        createNewHistoryList();
        randomNextWord();
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
            String history = historyDialog.getReturnedValue();
            if(history != null){
                displayWordFromHistory(historyDialog.getReturnedValue());
            }
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
    
    private static String WelcomeHtmlContent(){
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html>")
                .append("<h1>")
                .append("Welcome to")
                .append("</h1>")
                .append("<i>")
                .append("Slang Word Dictionary")
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
    
    private void showCreateDialog(){
        TheWordDialog newDialog = new TheWordDialog(null, "Create a new Word");
        SlangWord newSlangWord = newDialog.getReturnedValue();
        if(newSlangWord != null){
            SlangWordUtils.writeNewWord(newSlangWord);
            initOriginWordList();
            createNewWordList(originWordList);
        }
    }
    
    private void showEditDialog(){
        SlangWord oldSlangWord = (SlangWord)wordListContainer.getSelectedValue();
        if(oldSlangWord != null){
            TheWordDialog newDialog = new TheWordDialog(null, "Edit", oldSlangWord);
            SlangWord editedSlangWord = newDialog.getReturnedValue();
            if(editedSlangWord != null){
                SlangWordUtils.editWord(oldSlangWord, editedSlangWord);
                initOriginWordList();
                createNewWordList(originWordList);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please choose a word!", "Warning", 
                        JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteWord(){
        SlangWord word = (SlangWord)wordListContainer.getSelectedValue();
        if(word != null){
            int option = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete \" " + word.getWord() + "\"?", "Close Window?", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION){
                SlangWordUtils.removeWord(word);
                initOriginWordList();
                createNewWordList(originWordList);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please choose a word!", "Warning", 
                        JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void resetWordList(){
        int option = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to reset the whole word list?", "Reset the Word List?", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION){
            SlangWordUtils.resetTheWordList();
            initOriginWordList();
            createNewWordList(originWordList);
            JOptionPane.showMessageDialog(null, "Successfully reset the word list!", "About", 
                            JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void randomNextWord(){
        randomSlangWord = SlangWordUtils.randomSlangWord();
        if(randomSlangWord != null){
            wordOfTheDayLabel.setText(randomSlangWord.getWord());
            showWordOfTheDayButton.setEnabled(true);
            randomWordButton.setEnabled(true);
        } else {
            wordOfTheDayLabel.setText("No data");
            showWordOfTheDayButton.setEnabled(false);
            randomWordButton.setEnabled(false);
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
        
        JMenuItem newItem = new JMenuItem("Create a new Word...");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                ActionEvent.CTRL_MASK));
        newItem.addActionListener((ActionEvent e) -> {
            showCreateDialog();
        });
        JMenuItem editItem = new JMenuItem("Edit the Word...");
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 
                ActionEvent.CTRL_MASK));
        editItem.addActionListener((ActionEvent e) -> {
            showEditDialog();
        });
        
        JMenuItem deleteItem = new JMenuItem("Delete the Word");
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
                ActionEvent.CTRL_MASK));
        deleteItem.addActionListener((ActionEvent e) -> {
            deleteWord();
        });
        JMenuItem resetItem = new JMenuItem("Reset the Word List");
        resetItem.addActionListener((ActionEvent e) -> {
            resetWordList();
        });
        
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
                if(SwingUtilities.isLeftMouseButton(e)){
                    SlangWord selectedWord = (SlangWord)wordListContainer.getSelectedValue();
                    displayWordDetailAndWriteHistory(selectedWord);
                }
            }
        });
        
        //set word list pane
        wordListPane = new JScrollPane(wordListContainer);
        
        //set function popup menu
        JPopupMenu funcPopupMenu = new JPopupMenu();
        JMenuItem editPopupMenuItem = new JMenuItem("Edit");
        editPopupMenuItem.addActionListener((ActionEvent e) -> {
            showEditDialog();
        });
        JMenuItem deletePopupMenuItem = new JMenuItem("Delete");
        deletePopupMenuItem.addActionListener((ActionEvent e) -> {
            deleteWord();
        });
        funcPopupMenu.add(editPopupMenuItem);
        funcPopupMenu.add(deletePopupMenuItem);
        wordListContainer.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    int row = wordListContainer.locationToIndex(e.getPoint());
                    wordListContainer.setSelectedIndex(row);
                    funcPopupMenu.show(wordListContainer, e.getX(), e.getY());
                }
            }
        });
        
        //set the left pane
        leftPane = new JPanel();
        leftPane.setPreferredSize(new Dimension(250, -1));
        leftPane.setLayout(new BorderLayout());
        leftPane.add(searchPane, BorderLayout.NORTH);
        leftPane.add(wordListPane, BorderLayout.CENTER);
        
        //set definitionEditorPane
        definitionEditorPane = new JEditorPane();
        definitionEditorPane.setContentType("text/html");
        definitionEditorPane.setText(WelcomeHtmlContent());
        definitionEditorPane.setEditable(false);
        
        //set definition pane
        definitionPane = new JPanel(new BorderLayout());
        definitionPane.add(definitionEditorPane, BorderLayout.CENTER);
        
        // set main pane
        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, definitionPane);
        
        //set the side pane
        sidePane = new JPanel();
        sidePane.setLayout(new GridBagLayout());
        JLabel wordOfTheDayTitleLabel = new JLabel("Word of the Day: ");
        wordOfTheDayLabel = new JLabel();
        wordOfTheDayLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
        showWordOfTheDayButton = new JButton("← About the word");
        showWordOfTheDayButton.addActionListener((ActionEvent e) -> {
            definitionEditorPane.setText(SlangWordUtils.convertWordToHtml(randomSlangWord));
        });
        randomWordButton = new JButton("Next to another word");
        randomWordButton.addActionListener((ActionEvent e) -> {
            randomNextWord();
        });
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridy = 0;
        c.gridx = 0;
        sidePane.add(wordOfTheDayTitleLabel, c);
        c.gridy = 1;
        sidePane.add(wordOfTheDayLabel, c);
        c.gridy = 2;
        c.insets = new Insets(3,0,3,0);
        sidePane.add(showWordOfTheDayButton, c);
        c.gridy = 3;
        sidePane.add(randomWordButton, c);
        sidePane.setPreferredSize(new Dimension(200, -1));
        
        //
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPane, BorderLayout.CENTER);
        getContentPane().add(sidePane, BorderLayout.EAST);
        setTitle("Slang Word Dictionary");
        setSize(750, 400);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to close Slang Word Dictionary?", "Close Window?", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION){
                    SlangWordUtils.saveWordListToJsonFile();
                    System.exit(0);
                }
            }
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SlangWordCRUD window = new SlangWordCRUD();
            window.setVisible(true);
        });
    }
}
